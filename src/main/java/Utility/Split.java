package Utility;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Split {
    private static int countFiles = 0;
    boolean OutputNameWithNum;
    String[] array = createNamesArray();


    public Split(boolean OutputNameWithNumbers) {
        this.OutputNameWithNum = OutputNameWithNumbers;
    }

    public boolean checking(boolean workingWithString, boolean workingWithFiles, String line, int chr, int countOfFiles) {
        if (workingWithString) return line != null;
        else if (workingWithFiles) return countFiles != countOfFiles;
        else return chr != -1;
    }

    public String[] createNamesArray() {
        if (OutputNameWithNum) return new String[1];
        //Если работаем с числами,то вместо большого массива букв возвращаем минимальный массив с null
        int alphabetCount = 0;
        int englishAlphabet = 26;
        String[] array = new String[englishAlphabet * englishAlphabet];

        for (int i = 0; i < englishAlphabet; i++) {
            for (int j = 0; j < englishAlphabet; j++) {
                array[i + j + alphabetCount] = Character.toString('a' + i) + Character.toString('a' + j);
            }
            alphabetCount += englishAlphabet - 1;
        }
        return array;
    }

    public double fileSize(String inputName) throws IOException {
        FileReader fileReader = new FileReader(inputName);
        BufferedReader reader = new BufferedReader(fileReader);

        double count = 0;
        int symbol = reader.read();
        while (symbol != -1) {
            ++count;
            symbol = reader.read();
        }
        return count;
    }

    public String setOutputName(String basicOutputName, String inputName) {
        if (basicOutputName == null) basicOutputName = "X";
        else if (basicOutputName.equals("-")) basicOutputName = inputName;
        if (OutputNameWithNum) {
            ++countFiles;
            return basicOutputName + countFiles;
        } else {
            ++countFiles;
            return basicOutputName + array[countFiles - 1];
        }
    }

    public String cutFile(String inputName, int countInChars, int countInLines, int countOfFiles, String basicOutputName) {
        int chr = 0, count = 0, maxCount = 0;
        boolean workingWithString = false, workingWithFiles = false;
        String line = null;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputName), UTF_8));

            if (countInChars != 0) {
                maxCount = countInChars;
                chr = reader.read();
            } else if (countOfFiles != 0) {
                maxCount = (int) Math.ceil(fileSize(inputName) / countOfFiles);
                chr = reader.read();
                workingWithFiles = true;
            } else if (countInLines != 0) {
                maxCount = countInLines;
                line = reader.readLine();
                workingWithString = true;
            }
            while (checking(workingWithString, workingWithFiles, line, chr, countOfFiles)) {
                String outputName = setOutputName(basicOutputName, inputName);

                BufferedWriter writer =
                        new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputName), UTF_8));

                while (count != maxCount) {
                    if (workingWithString) {
                        writer.write(line + System.lineSeparator());
                        line = reader.readLine();
                        if (line == null) break;
                    } else {
                        writer.write((char) chr);
                        chr = reader.read();
                        if (chr == -1) break;
                    }
                    ++count;
                }
                count = 0;
                writer.close();
                if (countFiles > 100) break;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Выполнено";
    }
}