package Utility;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.file.Path;
import java.util.logging.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Split {
    private final Path Input_Path, Output_Path;
    private int countFiles = 0;
    private String[] array = createNamesArray();
    private boolean workingWithNumbers;
    private static Logger logger = Logger.getLogger(String.valueOf(Split.class));

    public Split(Path inputPath, Path outputPath, boolean workingWithNumbers) {
        this.Input_Path = inputPath;
        this.Output_Path = outputPath;
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
                "You didnt enter any of arguments or value of argument is 0");
        else if (sum < 0) throw new IllegalArgumentException(
                "Arguments cant be less than 0");
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
        String sep = File.separator;


        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(Input_Path + sep + inputName), UTF_8))) {


            if (countInChars != 0) {
                maxCount = countInChars;
                chr = reader.read();
            } else if (countOfFiles != 0) {
                maxCount = (int) Math.ceil(fileSize(Input_Path + sep + inputName) / countOfFiles);
                chr = reader.read();
                workingWithFiles = true;
            }
            if (chr == -1) {
                logger.info("Text is empty.Please choose different file and try again");
                return false;
            } else if (countInLines != 0) {
                maxCount = countInLines;
                line = reader.readLine();
                if (line == null) {
                    logger.info("Text is empty.Please choose different file and try again");
                    return false;
                }
                workingWithString = true;
            }
            while (checking(workingWithString, workingWithFiles, line, chr, countOfFiles)) {
                String outputName = setOutputName(basicOutputName, inputName);

                try (BufferedWriter writer =
                             new BufferedWriter(new OutputStreamWriter(
                                     new FileOutputStream(Output_Path + sep + outputName), UTF_8))) {

                    while (count != maxCount) {
                        if (workingWithString) {
                            writer.write(line);
                            line = reader.readLine();
                            if (line == null) break;
                            else writer.write(System.lineSeparator());
                        } else {
                            writer.write((char) chr);
                            chr = reader.read();
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