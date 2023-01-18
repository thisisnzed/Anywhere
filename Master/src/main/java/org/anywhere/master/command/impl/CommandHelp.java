package org.anywhere.master.command.impl;

import org.anywhere.master.command.Command;
import org.anywhere.master.command.CommandManager;
import org.anywhere.master.utils.Print;

public class CommandHelp extends Command {

    private final CommandManager commandManager;

    public CommandHelp(final CommandManager commandManager) {
        super("help", "?", "help", "Retrieve all commands to control agents", false);
        this.commandManager = commandManager;
    }

    @Override
    public void execute(final String[] args) {
        Print.println(" ");
        this.commandManager.getCommands().forEach(command -> Print.println(Print.LIGHT_CYAN + command.getSyntax() + Print.LIGHT_GREY + " - " + command.getDescription()));
        Print.println(" ");
    }
}
