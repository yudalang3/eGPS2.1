package utils.storage;

import java.io.File;

public class FinalFields {
    public static final String PROPERTIES_DIR;
    public static final String JSON_DIR;
    public static final String OBJECT_DIR;

    static {
        String userHomeDir = System.getProperty("user.home");
        File dir = new File(userHomeDir, ".egps2.yu.lab.utils");
        createIfNotExists( dir);
        File storage = new File(dir, "storage");
        PROPERTIES_DIR = storage.getAbsolutePath().replace("\\", "/");
        File jsonDir = new File(dir, "config/jsonData");
        createIfNotExists( jsonDir);
        JSON_DIR = jsonDir.getAbsolutePath().replace("\\", "/");
        File objectsDir = new File(dir, "config/objects");
        createIfNotExists( objectsDir);
        OBJECT_DIR = objectsDir.getAbsolutePath().replace("\\", "/");

    }

    private static void createIfNotExists(File file) {
        if (!file.exists()) {
            file.mkdirs();
        }
    }
}
