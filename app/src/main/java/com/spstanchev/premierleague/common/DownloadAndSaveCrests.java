package com.spstanchev.premierleague.common;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * Created by Stefan on 1/23/2015.
 */
public class DownloadAndSaveCrests extends AsyncTask <String, Void, Void> {

    @Override
    protected Void doInBackground(String... params) {
        String crestUrl = params[0];
        String crestFileName = params[1];
        String crestFileDir = params[2];
        Log.d("DOWNLOAD CRESTS", String.format("crestUrl=%s, crestFileName=%s, crestFileDir=%s",crestUrl, crestFileName, crestFileDir));

        File crestFilePath = new File(crestFileDir);
        if (!crestFilePath.exists()) {
            crestFilePath.mkdirs();
        }
        try {
            URL url = new URL(crestUrl);
            InputStream inputStream = url.openStream();
            File crestFile = new File(crestFileDir, crestFileName);
            OutputStream outputStream = new FileOutputStream(crestFile);
            byte[] buffer = new byte[1024];
            int bytesRead = 0;
            while ((bytesRead = inputStream.read(buffer, 0, buffer.length)) >= 0){
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.close();
            inputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

}
