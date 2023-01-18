package org.anywhere.master.command;

import org.anywhere.master.utils.Print;

import java.util.Scanner;

public class CommandParser {

    private final CommandManager commandManager;
    private final String name;

    public CommandParser(final CommandManager commandManager) {
        this.commandManager = commandManager;
        this.name = Print.LIGHT_YELLOW + System.getProperty("user.name") + Print.DARK_YELLOW + "@" + Print.LIGHT_YELLOW + "Anywhere" + Print.DARK_YELLOW + ":~# " + Print.RESET;
    }

    public void execute() {
        final Scanner scanner = new Scanner(System.in);
        Print.print(this.name);
        final String text = scanner.nextLine();
        final Command command = this.commandManager.getCommands().stream().filter(cmd -> (text + " ").toLowerCase().startsWith(cmd.getCommand().toLowerCase() + " ") || (cmd.getAlias() != null && (text + " ").toLowerCase().startsWith(cmd.getAlias().toLowerCase() + " "))).findFirst().orElse(null);
        if (command == null) {
            Print.println(Print.LIGHT_RED + "Unknown command, please try again");
            return;
        }
        command.execute(text.split(" "));
    }
}
