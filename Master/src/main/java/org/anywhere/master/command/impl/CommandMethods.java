package org.anywhere.master.command.impl;

import org.anywhere.master.command.Command;
import org.anywhere.master.command.CommandManager;
import org.anywhere.master.utils.Print;

public class CommandMethods extends Command {

    private final CommandManager commandManager;

    public CommandMethods(final CommandManager commandManager) {
        super("methods", "method", "methods", "Retrieve all attacks methods", false);
        this.commandManager = commandManager;
    }

    @Override
    public void execute(final String[] args) {
        if (this.commandManager.getCommands().stream().noneMatch(Command::isMethod)) {
            Print.println(Print.LIGHT_RED + "No method is currently available.");
            return;
        }
        Print.println(" ");
        Print.println("List of available methods :");
        this.commandManager.getCommands().stream().filter(Command::isMethod).forEach(command -> Print.println("  * " + Print.LIGHT_CYAN + command.getCommand() + Print.LIGHT_GREY + " - " + command.getDescription()));
        Print.println(" ");
    }
}
