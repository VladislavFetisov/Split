package Utility;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Split {
    private String Input_Path, Output_Path;
    public int countFiles = 0;
    private String[] array = createNamesArray();
    private boolean workingWithNumbers;
    private static Logger logger = Logger.getLogger(String.valueOf(Split.class));

    public Split(String Input_Path, String Output_Path, boolean workingWithNumbers) {
        this.Input_Path = Input_Path;
        this.Output_Path = Output_Path;
        this.workingWithNumbers = workingWithNumbers;
    }

    private boolean checking(boolean workingWithString, boolean workingWithFiles, String line, int chr, int countOfFiles) {
        if (workingWithString) return line != null;
        else if (workingWithFiles) return countFiles != countOfFiles;
        else return chr != -1;
    }

    private void throwExceptionIfNecessary(int countOfFiles, int countOfChars, int countOfLines) {
        int sum = countOfChars + countOfFiles + countOfLines;
        if (sum == 0) throw new IllegalArgumentException(
                "Вы не ввели значение ни одного аргумента");
        else if (sum < 0) throw new IllegalArgumentException(
                "Аргументы не могут быть меньше 0");
    }

    @NotNull
    private String[] createNamesArray() {
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

    public static double fileSize(String inputName) throws IOException {
        double count = 0;

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(inputName), UTF_8))) {

            int chr = reader.read();
            while (chr != -1) {
                if (chr == '\n') chr=reader.read();
                count++;
                chr = reader.read();
            }
        }
        return count;
    }

    @NotNull
    private String setOutputName(String basicOutputName, String inputName) {
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

    boolean cutFile(String inputName, int countInChars, int countInLines, int countOfFiles, String basicOutputName)
            throws IOException, IllegalArgumentException {

        throwExceptionIfNecessary(countOfFiles, countInChars, countInLines);

        int chr = 0, count = 0, maxCount = 0;
        boolean workingWithString = false, workingWithFiles = false;
        String line = null;


        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(Input_Path + inputName), UTF_8))) {


            if (countInChars != 0) {
                maxCount = countInChars;
                chr = reader.read();
                if (chr == '\n') chr = reader.read();
            } else if (countOfFiles != 0) {
                maxCount = (int) Math.ceil(fileSize(Input_Path + inputName) / countOfFiles);
                chr = reader.read();
                if (chr == '\n') chr = reader.read();
                workingWithFiles = true;
            }
            if (chr == -1) {
                logger.info("Текст пустой,выберете подходящий текст и повторите попытку");
                return false;
            } else if (countInLines != 0) {
                maxCount = countInLines;
                line = reader.readLine();
                if (line == null) {
                    logger.info("Текст пустой,выберете подходящий текст и повторите попытку");
                    return false;
                }
                workingWithString = true;
            }
            while (checking(workingWithString, workingWithFiles, line, chr, countOfFiles)) {
                String outputName = setOutputName(basicOutputName, inputName);

                try (BufferedWriter writer =
                             new BufferedWriter(new OutputStreamWriter(
                                     new FileOutputStream(Output_Path + outputName), UTF_8))) {

                    while (count != maxCount) {
                        if (workingWithString) {
                            writer.write(line);
                            line = reader.readLine();
                            if (line == null) break;
                            else writer.write(System.lineSeparator());
                        } else {
                            writer.write((char) chr);
                            chr = reader.read();
                            if (chr == '\n') chr = reader.read();
                            if (chr == -1) break;
                        }
                        ++count;
                    }
                    count = 0;
                }
            }
        }
        return true;
    }
}