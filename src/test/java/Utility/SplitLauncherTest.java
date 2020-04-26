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
        char sep = File.separatorChar;
        String OutputPath = "src" + sep + "main" + sep + "resources" + sep + "OutputFiles" + sep;
        File inputFile = new File("src" + sep + "main" + sep + "resources" + sep + "InputFiles" + sep + "InputOnegin");
        String inputUntilReplace=readFileToString(inputFile, UTF_8);
        String inputInString = inputUntilReplace.replaceAll("[\r\n]","");


        String[] args = {"-d", "-c", "100", "-o", "OneginOutput", "InputOnegin"};
        Main.main(args);
        File o1 = new File(OutputPath + args[4] + "1");
        File o2 = new File(OutputPath + args[4] + "2");
        File o3 = new File(OutputPath + args[4] + "3");
        File o4 = new File(OutputPath + args[4] + "4");
        String output = readFileToString(o1, UTF_8) + readFileToString(o2, UTF_8) +
                readFileToString(o3, UTF_8) + readFileToString(o4, UTF_8);
        assertEquals(inputInString, output.replaceAll("[\r\n]",""));
        assertEquals(100, readFileToString(o1, UTF_8).length());
        assertEquals(100, readFileToString(o2, UTF_8).length());
        assertEquals(100, readFileToString(o3, UTF_8).length());
        assertEquals(inputUntilReplace.length()-3*100, readFileToString(o4, UTF_8).length());


        args = new String[]{"-l", "4", "-o", "Onegin", "InputOnegin"};
        Main.main(args);
        o1 = new File(OutputPath + args[3] + "aa");
        o2 = new File(OutputPath + args[3] + "ab");
        o3 = new File(OutputPath + args[3] + "ac");
        o4 = new File(OutputPath + args[3] + "ad");
        output = readFileToString(o1, UTF_8) + readFileToString(o2, UTF_8) +
                readFileToString(o3, UTF_8) + readFileToString(o4, UTF_8);
        assertEquals(inputInString, output.replaceAll("[\r\n]",""));
        assertEquals(4, countInLines(o1));
        assertEquals(4, countInLines(o2));
        assertEquals(4, countInLines(o3));
        assertEquals(countInLines(inputFile)-3*4, countInLines(o4));


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
        assertEquals(inputInString, output.replaceAll("[\r\n]",""));
        assertEquals(Math.ceil(Split.fileSize(
                "src" + sep + "main" + sep + "resources" + sep + "InputFiles" + sep + "InputOnegin") / 7),
                readFileToString(o1, UTF_8).length());


        args = new String[]{"-d", "-c", "120", "-o", "-", "InputOnegin"};
        Main.main(args);
        o1 = new File(OutputPath + args[5] + 1);
        o2 = new File(OutputPath + args[5] + 2);
        o3 = new File(OutputPath + args[5] + 3);
        o4 = new File(OutputPath + args[5] + 4);

        output = readFileToString(o1, UTF_8) + readFileToString(o2, UTF_8) +
                readFileToString(o3, UTF_8) + readFileToString(o4, UTF_8);
        assertEquals(inputInString, output.replaceAll("[\r\n]",""));
        assertEquals(120, readFileToString(o1, UTF_8).length());
        assertEquals(120, readFileToString(o2, UTF_8).length());
        assertEquals(120, readFileToString(o3, UTF_8).length());
        assertEquals(inputUntilReplace.length() - 120 * 3, readFileToString(o4, UTF_8).length());



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