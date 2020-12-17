package com.hilifecare.util.logging;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class LogWrapper {
    private static final String LOG_DIR_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/hyoil";
    private static final String LOG_FILE_NAME = "HyoilFileLog.txt";
    private static final String LOG_FILE_FULL_PATH = LOG_DIR_PATH + "/" + LOG_FILE_NAME;

    public static void v(String tag, String msg) {
        String logFileFullPath = LOG_FILE_FULL_PATH;
        File dir = new File(LOG_DIR_PATH);
        File file = new File(logFileFullPath);
        if (file.exists() == false) {
            try {
                dir.mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                Log.i("logwrapper","errincat");

            }
        } else {
            try {
                String log = String.format("V/%s: %s\n", tag, msg);

                BufferedWriter bfw = new BufferedWriter(new FileWriter(logFileFullPath, true));
                bfw.write(log);
                bfw.write("\n");
                bfw.flush();
                bfw.close();

                Log.v(tag, msg);
                Log.i("logwrapper","init");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Log.i("logwrapper","err");

            } catch (IOException e) {
                e.printStackTrace();
                Log.i("logwrapper","err");

            }
        }
    }

}
