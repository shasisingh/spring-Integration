package nl.shashi.playground.jms.util;



import java.io.File;
import java.nio.file.Files;

public final class FilePollerUtility {

    public static boolean fileExists(final File file) {
        return Files.exists(file.toPath());
    }

    public static boolean onlyFiles(final File file) {
        return !file.isDirectory() && !file.isHidden() && file.canRead();
    }

    public static boolean onlyTxt(final File file) {
        return file.getName().endsWith(".txt");
    }
}
