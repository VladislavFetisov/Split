package Utility;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Split {
    private static int countFiles = 0;
    String[] array = createNamesArray();

    public boolean checking(boolean workingWithString,
                            boolean workingWithFiles, String line, int chr, int countOfFiles) {
        if (workingWithString) return line != null;
        else if (workingWithFiles) return countFiles != countOfFiles;
        else return chr != -1;
    }

    public String[] createNamesArray() {
        int alphabetCount = 0;
        String[] array = new String[676];
        for (int i = 0; i < 26; i++) {
            for (int j = 0; j < 26; j++) {
                array[i + j + alphabetCount] = Character.toString('a' + i) + Character.toString('a' + j);
            }
            alphabetCount += 26 - 1;
        }
        return array;
    }

    public int fileSize(String inputName) throws IOException {
        FileReader fileReader = new FileReader(inputName);
        BufferedReader reader = new BufferedReader(fileReader);

        int count = 0;
        int symbol = reader.read();
        while (symbol != -1) {
            ++count;
            symbol = reader.read();
        }
        return count;
    }

    public String setOutputName(String basicOutputName, String inputName, boolean outputNameWithNumbers) {
        if (basicOutputName == null) basicOutputName = "X";
        else if(basicOutputName.equals("-")) basicOutputName = inputName;
        if (outputNameWithNumbers) {
            ++countFiles;
            return basicOutputName + countFiles;
        } else {
            ++countFiles;
            return basicOutputName + array[countFiles - 1];
        }
    }

    public String cutFile(String inputName, int countInChars, int countInLines, int countOfFiles,
                          String basicOutputName, boolean outputNameWithNumbers) {
        int maxCount = 0, chr = 0, count = 0;
        boolean workingWithString = false, workingWithFiles = false;
        String line = null;

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(inputName), UTF_8));

            if (countInChars != 0) {
                if (countInLines != 0 || countOfFiles != 0)
                    throw new IllegalArgumentException("Несколько аргументов для разделения не могут использоваться вместе!");
                maxCount = countInChars;
                chr = reader.read();
            } else if (countOfFiles != 0) {
                if (countInLines != 0)
                    throw new IllegalArgumentException("Несколько аргументов для разделения не могут использоваться вместе!");
                maxCount = fileSize(inputName) / countOfFiles;
                chr = reader.read();
                workingWithFiles = true;
            } else if (countInLines != 0) {
                maxCount = countInLines;
                line = reader.readLine();
                workingWithString = true;
            }
            while (checking(workingWithString, workingWithFiles, line, chr, countOfFiles)) {
                String outputName = setOutputName(basicOutputName, inputName, outputNameWithNumbers);

                //сделать переменную output path
                Writer writer = new OutputStreamWriter
                        (new FileOutputStream("C:\\Users\\Владислав\\Desktop\\output\\" + outputName), UTF_8);
                BufferedWriter bufferedWriter = new BufferedWriter(writer);

                while (count != maxCount) {//здесь происходит запись в файл,может по линиям,может по символам.
                    if (workingWithString) {
                        bufferedWriter.write(line + System.lineSeparator());
                        line = reader.readLine();
                        if (line == null) break;
                    } else {//то есть работаем либо с кол-вом файлом,либо с кол-вом символов
                        bufferedWriter.write((char) chr);
                        chr = reader.read();
                        if (chr == -1) break;
                    }
                    ++count;
                }
                count = 0;
                bufferedWriter.close();
                writer.close();
                if (countFiles > 100) break;//убрать
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Выполнено";
    }
}