package org.anywhere.master.utils;

import org.anywhere.master.Master;

public class Print {

    public static final String RESET = Master.COLORS ? "\033[0m" : "";
    public static final String BLACK = Master.COLORS ? "\033[0;30m" : "";
    public static final String GREY = Master.COLORS ? "\033[0;90m" : "";
    public static final String LIGHT_RED = Master.COLORS ? "\033[0;91m" : "";
    public static final String LIGHT_GREEN = Master.COLORS ? "\033[0;92m" : "";
    public static final String LIGHT_YELLOW = Master.COLORS ? "\033[0;93m" : "";
    public static final String DARK_BLUE = Master.COLORS ? "\033[0;94m" : "";
    public static final String LIGHT_MAGENTA = Master.COLORS ? "\033[0;95m" : "";
    public static final String LIGHT_CYAN = Master.COLORS ? "\033[0;96m" : "";
    public static final String WHITE = Master.COLORS ? "\033[0;97m" : "";
    public static final String DARK_RED = Master.COLORS ? "\033[0;31m" : "";
    public static final String DARK_GREEN = Master.COLORS ? "\033[0;32m" : "";
    public static final String DARK_YELLOW = Master.COLORS ? "\033[0;33m" : "";
    public static final String VERY_DARK_BLUE = Master.COLORS ? "\033[0;34m" : "";
    public static final String DARK_MAGENTA = Master.COLORS ? "\033[0;35m" : "";
    public static final String DARK_CYAN = Master.COLORS ? "\033[0;36m" : "";
    public static final String LIGHT_GREY = Master.COLORS ? "\033[0;37m" : "";
    public static final String CONSOLE_CLEAR = Master.COLORS ? "\033[H\033[2J" : "";

    public static void println(final String message) {
        System.out.println(message + RESET);
    }

    public static void print(final String message) {
        System.out.print(message + RESET);
    }

}
// reg add HKEY_CURRENT_USER\Console /v VirtualTerminalLevel /t REG_DWORD /d 0x00000001 /f