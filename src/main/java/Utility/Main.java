package Utility;

import org.kohsuke.args4j.CmdLineException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, CmdLineException {
        SplitLauncher launcher = new SplitLauncher();
        launcher.launch(args);
    }
}