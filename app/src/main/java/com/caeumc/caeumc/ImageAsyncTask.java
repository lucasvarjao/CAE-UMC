package com.caeumc.caeumc;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by EDNEI on 09/01/2016.
 */
public class ImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
    Bitmap bitmap=null;
    String mUrl;
    Context mContext;
    private NavDrawerActivity mActivity;
    CircleImageView mCircleImageView;

    ImageAsyncTask (NavDrawerActivity activity, Context context, String url, CircleImageView circleImageView) {
        this.mActivity = activity;
        this.mUrl = url;
        this.mContext = context;
        this.mCircleImageView = circleImageView;
    }

    @Override
    protected void onPreExecute()
    {
            /* Called before task execution; from UI thread, so you can access your widgets */

        // Optionally do some stuff like showing progress bar
    };

    @Override
    protected Bitmap doInBackground(String... urls)
    {
        Bitmap result;
            /* Called from background thread, so you're NOT allowed to interact with UI */

        // Perform heavy task to get YourObject by string
        // Stay clear & functional, just convert input to output and return it
        String url = mUrl;
        FileCache fileCache = new FileCache(mContext);
        File file=fileCache.getFile(url);
        Bitmap bm = decodeFile(file);
        if(bm!=null) {
            bitmap = bm;
            result = bm;
            return result;
        }
        try {
            URL ImageUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)ImageUrl.openConnection();
            conn.setConnectTimeout(50000);
            conn.setReadTimeout(50000);
            conn.setInstanceFollowRedirects(true);
            InputStream is = conn.getInputStream();
            String hash = fileCache.getHash(url);
            File file1 = fileCache.getCacheDir();
            OutputStream os = new FileOutputStream(file);
            Utils.CopyStream(is, os);
            os.close();
            bitmap = decodeFile(file);
        } catch (Exception ex){
            ex.printStackTrace();
        }
        NavDrawerActivity.imageProfile = bm;
        result = bitmap;
        return result;
    }

    @Override
    protected void onPostExecute(Bitmap result)
    {
        result = bitmap;
        super.onPostExecute(result);
        result = bitmap;
            /* Called once task is done; from UI thread, so you can access your widgets */
        NavDrawerActivity.imageProfile = result;
        // Process result as you like
    }

    private Bitmap decodeFile(File file){
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(file),null,opt);
            final int REQUIRED_SIZE=130;
            int width_tmp=opt.outWidth, height_tmp=opt.outHeight;
            int scale=1;
            while(true){
                if(width_tmp/2<REQUIRED_SIZE || height_tmp/2<REQUIRED_SIZE)
                    break;
                width_tmp/=2;
                height_tmp/=2;
                scale*=2;
            }
            BitmapFactory.Options opte = new BitmapFactory.Options();
            opte.inSampleSize=scale;
            return BitmapFactory.decodeStream(new FileInputStream(file), null, opte);
        } catch (FileNotFoundException e) {}
        return null;
    }
}
