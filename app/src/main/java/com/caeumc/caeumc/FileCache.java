package com.caeumc.caeumc;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by EDNEI on 08/01/2016.
 */
public class FileCache {

    private File cacheDir;

    public FileCache(Context context){
        //Find the dir to save cached images
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }

    public File getFile(String url){
        String filename=String.valueOf(url.hashCode());
        File f = new File(cacheDir, filename);
        return f;

    }

    public String getHash (String url) {
        String hash="";
        hash = String.valueOf(url.hashCode());
        return hash;
    }

    public File getCacheDir () {
        return cacheDir;
    }

    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }

}