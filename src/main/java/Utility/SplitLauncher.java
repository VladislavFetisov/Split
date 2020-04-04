package Utility;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;


public class SplitLauncher {
    @Option(name = "-d", usage = "oName with numbers")
    private boolean oNameWithNum;

    @Option(name = "-l", usage = "sets size oFiles in lines")
    private int countInLines;

    @Option(name = "-c", usage = "sets size oFiles in chars")
    private int countInChars;

    @Option(name = "-n", usage = "sets count of oFiles")
    private int countOfFiles;

    @Option(name = "-o", usage = "output file name")
    private String basicOutputName;

    @Argument(required = true, usage = "input file name")
    private String inputFileName;

    public void launch(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("java -jar split.jar [-d] [-l num|-c num|-n num] [-o oFile] file");
            parser.printUsage(System.err);
        }
        Split spliterator = new Split();
        System.out.println(spliterator.cutFile(inputFileName, countInChars, countInLines, countOfFiles, basicOutputName, oNameWithNum));
    }
}