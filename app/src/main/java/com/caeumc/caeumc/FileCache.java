package com.caeumc.caeumc;

import android.content.Context;

import java.io.File;

/*
 * Created by EDNEI on 08/01/2016.
 */
class FileCache {

    private final File cacheDir;

    public FileCache (Context context) {
        //Find the dir to save cached images
        cacheDir = context.getCacheDir();
        if (!cacheDir.exists())
            //noinspection ResultOfMethodCallIgnored
            cacheDir.mkdirs();
    }

    public File getFile (String url) {
        String filename = String.valueOf(url.hashCode());
        return new File(cacheDir, filename);

    }

    public String getHash (String url) {
        String hash;
        hash = String.valueOf(url.hashCode());
        return hash;
    }

    public File getCacheDir () {
        return cacheDir;
    }

    public void clear () {
        File[] files = cacheDir.listFiles();
        if (files == null)
            return;
        for (File f : files)
            //noinspection ResultOfMethodCallIgnored
            f.delete();
    }

}