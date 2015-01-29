package prodbyhato.npuzzle;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * Created by hato on 4-12-14.
 */
class MyAdapter extends BaseAdapter {

    private ArrayList<Cell> list;
    private Context context;

    MyAdapter(Context context, Bitmap[] bitmaps, int columns)
    {
        this.context = context;
        list = new ArrayList<Cell>();
        Resources res = context.getResources();
        for(int i = 0; i < columns * columns; i++)
        {
            Cell cell = new Cell(i, bitmaps[i], i);
            list.add(cell);
        }

        Cell cell = list.get((columns * columns)-1);
        cell.setEmptyTile();
    }

    // This function returns the size of the list.
    @Override
    public int getCount()
    {
        return list.size();
    }

    // This function returns the cell at location i in the list.
    @Override
    public Cell getItem(int i)
    {
        Cell cell = list.get(i);
        return cell;
    }

    // This function returns the id of the cell at location i in the list.
    @Override
    public long getItemId(int i)
    {
        Cell cell = list.get(i);
        int id = cell.getCellId();
        return id;
    }

    // This function returns the position of the empty tile.
    public int getEmptyTilePosition()
    {
        int position = 0;

        for(Cell cell : list)
        {
            if (cell.isEmpty())
            {
                position = cell.getCellPosition();
                return position;
            }
        }

        return -1;
    }

    // This function returns the list filled with cells.
    public ArrayList<Cell> getList()
    {
        return list;
    }

    // This function swaps two cells.
    public void swapCells(int emptyPosition, int clickedCellPosition)
    {
        Collections.swap(list, emptyPosition, clickedCellPosition);
        notifyDataSetChanged();
    }

    // This function returns a view. The views get placed in a ViewHolder if it is the first time
    // creating that item to avoid the constant view.findViewById(R.id.imageView calls. If you are
    // recycling, get a reference to the existing viewholder which you already stored.
    // https://www.youtube.com/watch?v=F9q0K1O8m20
    @Override
    public View getView(int i, View clickedView, ViewGroup viewGroup)
    {
        View view = clickedView;
        ViewHolder holder = null;

        if(view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.single_item, viewGroup,false);
            holder = new ViewHolder(view);
            view.setTag(holder);
        }

        else
        {
            holder = (ViewHolder) view.getTag();
        }

        Cell temp = list.get(i);
        holder.myCellView.setImageBitmap(temp.getCellBitmap());

        return view;
    }
}
