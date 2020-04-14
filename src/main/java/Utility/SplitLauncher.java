package Utility;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.IOException;


public class SplitLauncher {
    @Option(name = "-d", usage = "oName with numbers")
    private boolean oNameWithNum;

    @Option(name = "-l", usage = "sets size oFiles in lines", forbids = {"-c", "-n"})
    private int countInLines;

    @Option(name = "-c", usage = "sets size oFiles in chars", forbids = {"-n", "-l"})
    private int countInChars;

    @Option(name = "-n", usage = "sets count of oFiles", forbids = {"-c", "-l"})
    private int countOfFiles;

    @Option(name = "-o", usage = "output file name")
    private String basicOutputName;

    @Argument(required = true, usage = "input file name")
    private String inputFileName;

    public void launch(String[] args) throws IOException, CmdLineException {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
            Split spliterator = new Split("src/main/resources/InputFiles/",
                    "src/main/resources/OutputFiles/", oNameWithNum);
            System.out.println(spliterator.cutFile(inputFileName, countInChars, countInLines, countOfFiles, basicOutputName));
        } catch (CmdLineException | IOException | IllegalArgumentException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar split.jar [-d] [-l num|-c num|-n num] [-o oFile] iFile");
            parser.printUsage(System.err);
            throw e;
        }
    }
}