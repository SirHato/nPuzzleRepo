package prodbyhato.npuzzle;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.graphics.Color;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Edit extends Activity {
    private static Bitmap bitmap, mutableBitmap, backupBitmap;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private Uri imageUri;
    private String logtag = "Edit";

    // This function is called when this activity starts.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        ContentResolver cr = getContentResolver();
        Bundle bundle = getIntent().getExtras();
        String imageUri = bundle.getString("imageUri");
        Uri myUri = Uri.parse(imageUri);

        //  Try to open the image using the uri. The bitmap is being copied in order to get a
        // mutable version, which can be edited. If it succeeds, crop the image.
        try
        {
            bitmap = MediaStore.Images.Media.getBitmap(cr, myUri);
            System.gc();
            mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

            if (mutableBitmap.getWidth() >= mutableBitmap.getHeight())
            {
                mutableBitmap = Bitmap.createBitmap(bitmap, bitmap.getWidth()/2 - bitmap.getHeight()/2,
                                0, bitmap.getHeight(), bitmap.getHeight());
                mutableBitmap = getResizedBitmap(mutableBitmap, 1000, 1000);
                backupBitmap = mutableBitmap.copy(Bitmap.Config.ARGB_8888, true);

            }

            else
            {
                mutableBitmap = Bitmap.createBitmap(bitmap, 0, bitmap.getHeight()/2 - bitmap.getWidth()/2,
                                bitmap.getWidth(), bitmap.getWidth());
                mutableBitmap = getResizedBitmap(mutableBitmap, 1000, 1000);
                backupBitmap = mutableBitmap.copy(Bitmap.Config.ARGB_8888, true);
            }

            ImageView imageView = (ImageView) findViewById(R.id.imageView_edit);
            imageView.setImageBitmap(mutableBitmap);

        }

        catch (Exception e)
        {
            Toast toast = Toast.makeText(getApplicationContext(),
                          "The image might be too large! Check your memory!", Toast.LENGTH_SHORT);
            toast.show();
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
        }
    }

    // This function returns a bitmap of a different size.
    // http://stackoverflow.com/questions/4837715/how-to-resize-a-bitmap-in-android
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth)
    {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
        return resizedBitmap;
    }


    // This function handles the clicks in this Activity
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.filter1Button:
                filter(v, 10);
                break;

            case R.id.filter2Button:
                //DO something
                filter(v, 200);
                break;

            case R.id.filter3Button:
                //DO something
                filter(v, 500);
                break;

            case R.id.resetButton:
                ImageView imageView = (ImageView) findViewById(R.id.imageView_edit);
                imageView.setImageBitmap(backupBitmap);
                mutableBitmap = backupBitmap.copy(Bitmap.Config.ARGB_8888, true);
                break;

            case R.id.continueButton_edit:
                storeImage(mutableBitmap);
                Intent continueIntent = new Intent(this, Difficulty.class);
                continueIntent.putExtra("imageUri", imageUri.toString());
                startActivity(continueIntent);
                break;

            case R.id.cancelButton:
                Intent cancelIntent = new Intent(this, HomeActivity.class);
                startActivity(cancelIntent);
                break;
        }
    }

    // This function handles the storing of a bitmap using the getOutputMediaFile function
    // https://www.pceworld.com/tags/android-image/63
    // http://developer.android.com/guide/topics/media/camera.html#saving-media
    private void storeImage(Bitmap image)
    {
        File pictureFile = getOutputMediaFile(1);

        if (pictureFile == null)
        {
            Log.e(logtag,"Error creating media file!");
            return;
        }

        try
        {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
        }

        catch (FileNotFoundException e)
        {
            Log.d(logtag, "File not found: " + e.getMessage());
        }

        catch (IOException e)
        {
            Log.d(logtag, "Error accessing file: " + e.getMessage());
        }

        imageUri = Uri.fromFile(pictureFile);
    }


    // Create a File for saving an image or video
    // http://developer.android.com/guide/topics/media/camera.html#saving-media
    private static File getOutputMediaFile(int type)
    {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists())
        {
            if (! mediaStorageDir.mkdirs())
            {
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE)
        {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_"+ timeStamp + ".jpg");
        }

        else
            return null;


        return mediaFile;
    }

    // This function mutates a bitmap based on the selected filter.
    public void filter(View view, int amount)
    {
        mutableBitmap = backupBitmap.copy(Bitmap.Config.ARGB_8888, true);
        int red, blue, green;
        int height = mutableBitmap.getHeight();
        int width = mutableBitmap.getWidth();
        int[] pixels = new int[width * height];

        mutableBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

        // Get and change RGB values.
        for (int i=0; i< pixels.length; i++)
        {
            red = Color.red(pixels[i]);
            blue = Color.blue(pixels[i]);
            green = Color.green(pixels[i]);

            pixels[i] = Color.rgb(blue*amount, blue%amount, green + amount);
        }

        // Create new bitmap with new pixels.
        mutableBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        ImageView imageView = (ImageView) findViewById(R.id.imageView_edit);
        imageView.setImageBitmap(mutableBitmap);
    }
}
