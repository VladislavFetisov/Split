package Utility;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;


public class Split {
    private String Input_Path, Output_Path;
    private  int countFiles = 0;
    private String[] array = createNamesArray();
    private boolean workingWithNumbers;

    public Split(String Input_Path, String Output_Path, boolean workingWithNumbers) {
        this.Input_Path = Input_Path;
        this.Output_Path = Output_Path;
        this.workingWithNumbers = workingWithNumbers;
    }

    public boolean checking(boolean workingWithString, boolean workingWithFiles, String line, int chr, int countOfFiles) {
        if (workingWithString) return line != null;
        else if (workingWithFiles) return countFiles != countOfFiles;
        else return chr != -1;
    }

    public String[] createNamesArray() {
        if (workingWithNumbers) return new String[1];
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
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(inputName), UTF_8));

        double count = 0;
        int chr = reader.read();
        while (chr != -1) {
            if (chr == '\n') reader.read();
            count++;
            chr = reader.read();
        }
        reader.close();
        return count;
    }

    String setOutputName(String basicOutputName, String inputName) {
        if (basicOutputName == null) basicOutputName = "X";
        else if (basicOutputName.equals("-")) basicOutputName = inputName;
        if (workingWithNumbers) {
            ++countFiles;
            return basicOutputName + countFiles;
        } else {
            ++countFiles;
            return basicOutputName + array[countFiles - 1];
        }
    }

    public boolean cutFile (String inputName, int countInChars, int countInLines, int countOfFiles, String basicOutputName) {
        int chr = 0, count = 0, maxCount = 0;
        boolean workingWithString = false, workingWithFiles = false;
        String line = null;


        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(new FileInputStream(Input_Path + inputName), UTF_8));

            if (countInChars != 0) {
                maxCount = countInChars;
                chr = reader.read();
                if (chr == '\n') chr = reader.read();
            } else if (countOfFiles != 0) {
                maxCount = (int) Math.ceil(fileSize(Input_Path + inputName) / countOfFiles);
                chr = reader.read();
                if (chr == '\n') chr = reader.read();
                workingWithFiles = true;
            } else if (countInLines != 0) {
                maxCount = countInLines;
                line = reader.readLine();
                workingWithString = true;
            }
            while (checking(workingWithString, workingWithFiles, line, chr, countOfFiles)) {
                String outputName = setOutputName(basicOutputName, inputName);

                BufferedWriter writer =
                        new BufferedWriter(new OutputStreamWriter(
                                new FileOutputStream(Output_Path + outputName), UTF_8));

                while (count != maxCount) {
                    if (workingWithString) {
                        writer.write(line + System.lineSeparator());
                        line = reader.readLine();
                        if (line == null) break;
                    } else {
                        writer.write((char) chr);
                        chr = reader.read();
                        if (chr == '\n') chr = reader.read();
                        if (chr == -1) break;
                    }
                    ++count;
                }
                count = 0;
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}