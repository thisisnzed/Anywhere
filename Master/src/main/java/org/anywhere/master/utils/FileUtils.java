package org.anywhere.master.utils;

import lombok.Getter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    @Getter
    private final String path;

    public FileUtils() {
        this.path = System.getenv("LOCALAPPDATA") + "\\Anywhere";
    }

    public void create() {
        final File configFolder = new File(this.path);
        final File serversFolder = new File(this.path + "\\servers\\");
        configFolder.mkdirs();
        serversFolder.mkdirs();
        final File configFile = new File(configFolder.getAbsolutePath() + "\\config");
        try {
            if (configFile.createNewFile()) {
                final FileWriter fileWriter = new FileWriter(this.path + "\\config");
                fileWriter.write("colors=false\n");
                fileWriter.close();
            }
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    public String getValue(final String string) {
        try {
            final FileInputStream fileInputStream = new FileInputStream(this.path + "\\config");
            final BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
            String strLine;
            while ((strLine = br.readLine()) != null)
                if (strLine.startsWith(string + "="))
                    return strLine.replace(string + "=", "");
            fileInputStream.close();
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
        return "error";
    }

    public void setValue(final String string, final String value) {
        try {
            final Path path = Paths.get(this.path + "\\config");
            final List<String> fileContent = new ArrayList<>(Files.readAllLines(path));
            for (int i = 0; i < fileContent.size(); i++) {
                if (fileContent.get(i).startsWith(string + "=")) {
                    fileContent.set(i, string + "=" + value);
                    break;
                }
            }
            Files.write(path, fileContent, StandardCharsets.UTF_8);
        } catch (final IOException exception) {
            exception.printStackTrace();
        }
    }

    public static String read(final File file) {
        try {
            final BufferedReader reader = new BufferedReader(new FileReader(file));
            final StringBuilder inputBuffer = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) inputBuffer.append(line).append("\n");
            reader.close();
            return inputBuffer.toString();
        } catch (final Exception exception) {
            if (!exception.toString().toLowerCase().contains("filenotfound")) exception.printStackTrace();
        }
        return "";
    }
}
