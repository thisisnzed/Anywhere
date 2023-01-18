package org.anywhere.agent.launch;

import org.anywhere.agent.Agent;

public class Launch {

    public static void main(final String[] args) {
        System.setProperty("file.encoding", "UTF-8");
        final String version = System.getProperty("os.name").toLowerCase();
        if (!version.contains("windows") && !version.contains("linux") && !version.contains("debian") && !version.contains("ubuntu") && !version.contains("centos") && !version.contains("unix")) {
            System.exit(-1);
            return;
        }
        new Agent(args).start();
    }
}