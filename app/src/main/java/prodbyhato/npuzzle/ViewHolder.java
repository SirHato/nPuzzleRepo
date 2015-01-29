package prodbyhato.npuzzle;

import android.view.View;
import android.widget.ImageView;

/**
 * Created by hato on 4-12-14.
 */
public class ViewHolder {

    ImageView myCellView;

    ViewHolder(View view) {

        myCellView = (ImageView) view.findViewById(R.id.imageView);
    }
}
