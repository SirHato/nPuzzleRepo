package prodbyhato.npuzzle;

import android.graphics.Bitmap;

/**
 * Created by hato on 4-12-14.
 */
public class Cell {
    private boolean emptyTile;
    private Bitmap bitmap;
    private int position;
    private int id;


    Cell(int id, Bitmap bitmap, int position)
    {
        this.id = id;
        this.bitmap = bitmap;
        this.position = position;
        this.emptyTile = false;

    }


    // This function sets the emptyTile boolean for a Cell.
    public void setEmptyTile()
    {
        this.emptyTile = true;
    }

    // This function sets the position for a Cell.
    public void setPosition(int i)
    {
        this.position = i;
    }

    // This function returns the Cell's id.
    public int getCellId()
    {
        return this.id;
    }

    // This function returns the Cell's bitmap.
    public Bitmap getCellBitmap()
    {
        return this.bitmap;
    }

    // This function returns the Cell's position.
    public int getCellPosition()
    {
        return this.position;
    }

    // This function returns the Cell's emptyTile boolean.
    public boolean isEmpty()
    {
        return this.emptyTile;
    }

}
