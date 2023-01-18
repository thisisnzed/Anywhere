package org.anywhere.master.command.impl;

import org.anywhere.master.Master;
import org.anywhere.master.command.Command;
import org.anywhere.master.utils.Print;
import org.anywhere.master.utils.FileUtils;

public class CommandColors extends Command {

    private final FileUtils fileUtils;

    public CommandColors(final Master master) {
        super("color", "colors", "color <on/off>", "Toggle console colors", false);
        this.fileUtils = master.getFileUtils();
    }

    @Override
    public void execute(final String[] args) {
        if (args.length != 2) {
            Print.println(Print.LIGHT_RED + "Error : " + super.getSyntax());
            return;
        }
        if (!args[1].equalsIgnoreCase("on")) {
            this.fileUtils.setValue("colors", "false");
            Print.println(Print.LIGHT_RED + "Console colors will be disabled on next start!");
            return;
        }
        this.fileUtils.setValue("colors", "true");
        Print.println(Print.LIGHT_GREEN + "Console colors will be enabled on next start!");
    }
}
