package prodbyhato.npuzzle;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.InputStream;


public class HomeActivity extends Activity {
    private static final int TAKE_PICTURE = 1;
    private static final int SELECT_PHOTO = 100;
    private Uri imageUri;

    // This function is called when the activity is opened.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    // This function handles the click on the take photo button. Camera will be opened.
    public void takePhoto(View view)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "picture.jpg");
        imageUri = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    // This function handles the click on the choose photo button. Gallery will be opened.
    public void choosePhoto(View view)
    {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode)
        {
            case TAKE_PICTURE:
                if (resultCode == Activity.RESULT_OK)
                {
                    Intent intent2 = new Intent(this, Edit.class);
                    intent2.putExtra("imageUri", imageUri.toString());
                    startActivity(intent2);
                }

                break;

            case SELECT_PHOTO:
                if (resultCode == Activity.RESULT_OK)
                {
                    Uri selectedImage = intent.getData();
                    Intent intent2 = new Intent(this, Edit.class);
                    intent2.putExtra("imageUri", selectedImage.toString());
                    startActivity(intent2);
                }

                break;

        }
    }
}
