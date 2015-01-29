package prodbyhato.npuzzle;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import java.io.IOException;


public class Difficulty extends Activity {
    public static String imageUri = null;

    // This function is called when the activity is opened.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);
        Bundle bundle = getIntent().getExtras();
        imageUri = bundle.getString("imageUri");
    }

    // This function is the on click handler for this Activity.
    public void onClick(View v) {
        Intent intent = new Intent(this, Game.class);
        intent.putExtra("imageUri", imageUri.toString());

        switch (v.getId()) {
            // Handles clicks on the easy button
            case R.id.easyButton:
                intent.putExtra("id", 3);
                startActivity(intent);
                break;
            // Handles clicks on the medium button
            case R.id.mediumButton:
                intent.putExtra("id", 4);
                startActivity(intent);
                break;
            // Handles clicks on the hard button
            case R.id.hardButton:
                intent.putExtra("id", 5);
                startActivity(intent);
                break;
        }
    }
}
