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

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Cell getItem(int i)
    {
        Cell cell = list.get(i);
        return cell;
    }

    @Override
    public long getItemId(int i)
    {
        Cell cell = list.get(i);
        int id = cell.getCellId();
        return id;
    }

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

    public ArrayList<Cell> getList()
    {
        return list;
    }

    public void swapCells(int emptyPosition, int clickedCellPosition)
    {
        Collections.swap(list, emptyPosition, clickedCellPosition);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        View row = view;
        ViewHolder holder = null;

        if(row == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.single_item, viewGroup,false);
            holder = new ViewHolder(row);
            row.setTag(holder);
        }

        else
        {
            holder = (ViewHolder) row.getTag();
        }

        Cell temp = list.get(i);
        holder.myCellView.setImageBitmap(temp.getCellBitmap());

        return row;
    }
}
