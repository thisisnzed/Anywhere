package org.anywhere.master.command.impl;

import org.anywhere.master.command.Command;
import org.anywhere.master.utils.Print;

public class CommandExit extends Command {

    public CommandExit() {
        super("exit", null, "exit", "Close the master", false);
    }

    @Override
    public void execute(final String[] args) {
        Print.println(Print.LIGHT_RED + "Bye!");
        System.exit(-1);
    }
}
