package Utility;

import org.junit.jupiter.api.Test;
import org.kohsuke.args4j.CmdLineException;

import java.io.*;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.FileUtils.readFileToString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SplitLauncherTest {

    @Test
    void mainTests() throws IOException, CmdLineException {
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
        assertEquals(inputInString.replace("\r", "\r\n"), output);
        assertEquals(4, countInLines(o1));
        assertEquals(4, countInLines(o2));
        assertEquals(4, countInLines(o3));
        assertEquals(2, countInLines(o4));


        args = new String[]{"-n", "7", "InputOnegin"};
        Main.main(args);
        o1 = new File(OutputPath + "X" + "aa");
        o2 = new File(OutputPath + "X" + "ab");
        o3 = new File(OutputPath + "X" + "ac");
        o4 = new File(OutputPath + "X" + "ad");
        File o5 = new File(OutputPath + "X" + "ae");
        File o6 = new File(OutputPath + "X" + "af");
        File o7 = new File(OutputPath + "X" + "ag");
        output = readFileToString(o1, UTF_8) + readFileToString(o2, UTF_8) +
                readFileToString(o3, UTF_8) + readFileToString(o4, UTF_8) +
                readFileToString(o5, UTF_8) + readFileToString(o6, UTF_8) +
                readFileToString(o7, UTF_8);
        assertEquals(inputInString, output);
        assertEquals(Math.ceil(Split.fileSize("src/main/resources/InputFiles/inputOnegin") / 7),
                readFileToString(o1, UTF_8).length());

        args = new String[]{"-d", "-l", "-12", "InputOnegin"};
        String[] finalArgs = args;
        Exception exception = assertThrows(IllegalArgumentException.class, () -> Main.main(finalArgs));

        String expectedMessage = "Arguments cant be less than 0";
        String actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);


        args = new String[]{"-o", "Output", "InputOnegin"};
        String[] finalArgs1 = args;

        exception = assertThrows(IllegalArgumentException.class, () -> Main.main(finalArgs1));

        expectedMessage = "You didnt enter any of arguments or value of argument is 0";
        actualMessage = exception.getMessage();
        assertEquals(expectedMessage, actualMessage);


        args=new String[]{"-l","4","-o","Output","onputOnegin"};
        String[] finalArgs2 = args;
        assertThrows(IOException.class, () -> Main.main(finalArgs2));

        args=new String[]{"-c","50","asd"};
        Main.main(args);
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