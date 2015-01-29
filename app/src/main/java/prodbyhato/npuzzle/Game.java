package prodbyhato.npuzzle;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class Game extends Activity implements AdapterView.OnItemClickListener{
    private static Bitmap bitmap;
    private GridView grid;
    private Bitmap[] bitmaps;
    private int columns;
    private MyAdapter myAdapter;
    private int totalTaps = 0;
    private String imageUri;
//    private final String mySharedPreferences = "mySavedState";
//
//    @Override
//    protected void onPause()
//    {
//        super.onPause();
//
//        // Enable shared preferences
//        final SharedPreferences settings = getSharedPreferences(mySharedPreferences, MODE_PRIVATE);
//        final SharedPreferences.Editor editor = settings.edit();
//
//        // Call and clear editor
//        editor.clear();
//
//        ...
//
//
//        editor.commit();
//    }

    // This function is called when this activity is started.
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        final Toast mTextField = Toast.makeText(getApplicationContext(), "Go..!!", Toast.LENGTH_SHORT);
        ContentResolver cr = getContentResolver();
        setContentView(R.layout.activity_game);

        // Get data from previous activity
        Bundle bundle = getIntent().getExtras();
        imageUri = bundle.getString("imageUri");
        columns = bundle.getInt("id");
        Uri myUri = Uri.parse(imageUri);

        // Try to open the image with myUri
        try
        {
            bitmap = MediaStore.Images.Media.getBitmap(cr, myUri);
        }

        catch (IOException e)
        {
            e.toString();
        }

        // First copy the bitmap to get a mutable version. Then we send it over to the slicer to
        // get a bitmap array.
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        bitmaps = slicer(bitmap, columns);

        // Set the grid
        grid = (GridView) findViewById(R.id.gridView);
        myAdapter = new MyAdapter(this, bitmaps, columns);
        grid.setAdapter(myAdapter);
        grid.setNumColumns(columns);

        // Wait 3 seconds, then shuffle the board and let the game begin.
        new CountDownTimer(3000, 1000)
        {
            public void onTick(long millisUntilFinished) {}

            public void onFinish()
            {
                Collections.shuffle(myAdapter.getList());
                mTextField.show();
                updateCellPositions();
                grid.setOnItemClickListener(Game.this);
                myAdapter.notifyDataSetChanged();
            }
        }.start();


    }

    // This function disables the use of the back button.
    @Override
    public void onBackPressed(){}


    // This function updates the position of all the cells.
    public void updateCellPositions()
    {
        ArrayList<Cell> lijst = myAdapter.getList();
        int i = 0;

        for (Cell cell : lijst)
        {
            cell.setPosition(i);
            i = i + 1;
        }
    }

    // This function takes a bitmap and slices it into columns^2 even pieces.
    // http://androidattop.blogspot.nl/2012/05/splitting-image-into-smaller-chunks-in.html
    public Bitmap[] slicer(Bitmap bitmap, int columns)
    {
        Bitmap[] bitmaps = new Bitmap[columns * columns];
        int screenWidth = this.getResources().getDisplayMetrics().widthPixels;
        int screenHeight = this.getResources().getDisplayMetrics().heightPixels;
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int tileWidth = bitmapWidth / columns;

        for(int x = 0; x < columns; x++)
        {
            for(int y = 0; y < columns; y++)
            {
                int xCoord = x * tileWidth;
                int yCoord = y * tileWidth;
                Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, xCoord, yCoord, tileWidth, tileWidth);
                int position = y * columns + x;
                bitmaps[position] = croppedBitmap;
            }
        }

        // Create the blank tile.
        bitmaps[(columns * columns) - 1].eraseColor(Color.WHITE);

        return bitmaps;
    }

    // This function opens the menu.
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    // This function handles clicks on the menu.
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        // Shuffle the board.
        if (id == R.id.action_shuffle)
        {
            Collections.shuffle(myAdapter.getList());
            updateCellPositions();
            myAdapter.notifyDataSetChanged();
        }
        // Set new difficulty and start a new game.
        if (id == R.id.action_difficulty)
        {
            Intent difficultyIntent = new Intent(this, Difficulty.class);
            difficultyIntent.putExtra("imageUri", imageUri.toString());
            startActivity(difficultyIntent);
        }
        // End the game
        if (id == R.id.action_quit)
        {
            Intent quitIntent = new Intent(this, HomeActivity.class);
            startActivity(quitIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    // This function checks if the players click is legal.
    public boolean legalMove(int emptyPosition, int clickedPosition)
    {
        if(((emptyPosition - columns) == clickedPosition)
            || ((emptyPosition + columns) == clickedPosition)
            || (((clickedPosition + 1) == emptyPosition) && (emptyPosition % columns != 0))
            || (((clickedPosition - 1) == emptyPosition) && (emptyPosition % columns != (columns - 1))))
        {
            return true;
        }

        else
        {
            return false;
        }
    }

    // This function checks if the puzzle has been solved by checking if the array is sorted.
    // http://stackoverflow.com/questions/18111231/how-to-check-if-array-is-already-sorted
    public boolean checkWin()
    {
        boolean sorted = true;

        for (int i = 0; i < myAdapter.getList().size() - 1; i++)
        {
            if (myAdapter.getItem(i).getCellId() > myAdapter.getItem(i+1).getCellId())
            {
                sorted = false;
                break;
            }
        }

        return  sorted;
    }

    // This function handles on item clicks.
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {

        int emptyTilePosition = myAdapter.getEmptyTilePosition();

        if(legalMove(emptyTilePosition, i))
        {
            // Swap blank tile with the tile that was clicked on
            Cell emptyCell = myAdapter.getItem(emptyTilePosition);
            emptyCell.setPosition(i);
            myAdapter.swapCells(emptyTilePosition, i);

            // Check if puzzle is solved.
            if(checkWin())
            {
                Intent wonIntent = new Intent(this, Won.class);
                wonIntent.putExtra("id", totalTaps);
                startActivity(wonIntent);
            }

            totalTaps++;
        }
    }
}

