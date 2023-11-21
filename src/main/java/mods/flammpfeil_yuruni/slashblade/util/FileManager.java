package mods.flammpfeil_yuruni.slashblade.util;

import com.google.gson.Gson;

import java.io.*;

//CREDIT: Eric Golde
public class FileManager {

    private static Gson gson = new Gson();
    private static final File ROOT_DIR = new File("SlashBlade");
    private static final File DATA_DIR = new File(ROOT_DIR, "LocalPlayerData");

    public static void init() {
        if (!ROOT_DIR.exists()) ROOT_DIR.mkdirs();
        if (!DATA_DIR.exists()) DATA_DIR.mkdirs();
    }

    public static Gson getGson() {
        return gson;
    }

    public static File getDataDirectory() {
        return DATA_DIR;
    }

    public static boolean writeJsonToFile(File file, Object obj) {

        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(gson.toJson(obj).getBytes());
            outputStream.flush();
            outputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String readFromJson(File file) {
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            StringBuilder builder = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            bufferedReader.close();
            inputStreamReader.close();
            fileInputStream.close();

            return builder.toString();
        } catch (IOException e) {
            return null;
        }
    }

}
