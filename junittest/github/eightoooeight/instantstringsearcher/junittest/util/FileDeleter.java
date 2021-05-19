package github.eightoooeight.instantstringsearcher.junittest.util;

import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;

public class FileDeleter {
    public static boolean deleteFileIfExists(String filepath) {
        try {
            return Files.deleteIfExists(Paths.get(filepath));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteDirIfExists(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirIfExists(file);
            }
        }
        return directoryToBeDeleted.delete();
    }
}
