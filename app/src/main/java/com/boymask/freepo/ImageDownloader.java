package com.boymask.freepo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

class ImageDownloader extends AsyncTask<String, String, Bitmap> {

    private final ImageView imageView;
    private final MainActivity main;
    private String turl;

    public ImageDownloader(String url, ImageView imageView, MainActivity mainActivity) {
        this.turl = url;
        this.imageView = imageView;
        this.main = mainActivity;
    }

    private Bitmap prev = null;

    @Override
    protected Bitmap doInBackground(String... params) {
        try {
            URL url = new URL(turl);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            if (prev != null) prev.recycle();
            prev = bmp;
            return bmp;
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return null;
    }

    private File saveToFile(String turl) throws Exception {
        URL url = new URL(turl);
        File tmp;
        try (InputStream fis = url.openStream()) {
            int av = fis.available();
            System.out.println("av: " + av);
            tmp = File.createTempFile("tmp", ".jpg");
            try (FileOutputStream fos = new FileOutputStream(tmp, true)) {


                byte buffer[] = new byte[1024];
                int size = 0;

                while ((size = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, size);
                    System.out.println("size: " + size);
                    System.out.println(new String(buffer));
                }

            }
        }
        System.out.println(tmp.length());
        return tmp;
    }


    @Override
    protected void onPostExecute(Bitmap result) {
        // execution of result of Long time consuming operation

        main.setImg(result);
    }


    @Override
    protected void onPreExecute() {

    }


    @Override
    protected void onProgressUpdate(String... text) {


    }

    private String readUrl(String url) throws Exception {
        String out = "";
        URL yahoo = new URL(url);
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        yahoo.openStream()));

        String inputLine;

        while ((inputLine = in.readLine()) != null)
            out += inputLine;

        in.close();
        return out;
    }
}