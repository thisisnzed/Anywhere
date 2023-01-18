package org.anywhere.master.utils;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

public interface CLibrary extends Library {
    CLibrary INSTANCE = Native.loadLibrary((Platform.isWindows() ? "kernel32" : "c"), CLibrary.class);

    boolean SetConsoleTitleA(String title);
}