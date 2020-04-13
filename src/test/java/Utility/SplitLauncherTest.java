package Utility;

import org.junit.jupiter.api.Test;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SplitLauncherTest {

    @Test
    void mainTests() throws IOException {
        String OutputPath = "src/main/resources/OutputFiles/";
        File inputFile = new File("src/main/resources/InputFiles/inputOnegin");
        String inputInString = readFileToString(inputFile, UTF_8);


        String[] args = {"-d", "-c", "100", "-o", "OneginOutput", "InputOnegin"};
        Main.main(args);
        File o1 = new File(OutputPath + args[4] + "1");
        File o2 = new File(OutputPath + args[4] + "2");
        File o3 = new File(OutputPath + args[4] + "3");
        File o4 = new File(OutputPath + args[4] + "4");
        String output = readFileToString(o1, UTF_8) + readFileToString(o2, UTF_8) +
                readFileToString(o3, UTF_8) + readFileToString(o4, UTF_8);
        assertEquals(inputInString, output);
        assertEquals(100, readFileToString(o1, UTF_8).length());
        assertEquals(100, readFileToString(o2, UTF_8).length());
        assertEquals(100, readFileToString(o3, UTF_8).length());
        assertEquals(68, readFileToString(o4, UTF_8).length());


        args = new String[]{"-l", "4", "-o", "Onegin", "InputOnegin"};
        Main.main(args);
        o1 = new File(OutputPath + args[3] + "aa");
        o2 = new File(OutputPath + args[3] + "ab");
        o3 = new File(OutputPath + args[3] + "ac");
        o4 = new File(OutputPath + args[3] + "ad");
        output = readFileToString(o1, UTF_8) + readFileToString(o2, UTF_8) +
                readFileToString(o3, UTF_8) + readFileToString(o4, UTF_8);
        assertEquals(inputInString.replace("\n","\r\n"), output);
        assertEquals(4,countInLines(o1));
        assertEquals(4,countInLines(o2));
        assertEquals(4,countInLines(o3));
        assertEquals(2,countInLines(o4));
    }

    private int countInLines(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), UTF_8))) {
            int count = 0;
            String line = reader.readLine();
            while (line != null) {
                count++;
                line=reader.readLine();
            }
            return count;
        }
    }
}