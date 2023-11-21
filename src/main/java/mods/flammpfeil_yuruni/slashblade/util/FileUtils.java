package mods.flammpfeil_yuruni.slashblade.util;

import java.io.File;

//CREDIT: Eric Golde (modified)
public class FileUtils {

    public static File getFolder(String folderName) {
        File folder = new File(FileManager.getDataDirectory(), folderName);
        folder.mkdirs();
        return folder;
    }

    public static void saveDataToFile(String folderName, Object obj) {
        String realObj = String.valueOf(obj);
        FileManager.writeJsonToFile(new File(getFolder(folderName), "value.json"), realObj);
    }

    public static String loadDataFromFile(String folderName, String defaultString) {
        String dataString = FileManager.readFromJson(new File(getFolder(folderName), "value.json"));
        if (dataString == null) {
            saveDataToFile(folderName, defaultString);
            return defaultString;
        }
        return dataString;
    }

}
