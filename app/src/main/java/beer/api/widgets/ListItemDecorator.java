package beer.api.widgets;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class ListItemDecorator extends RecyclerView.ItemDecoration {

    private static final int DIVIDER_HEIGHT = 1;
    private Paint paint;

    public ListItemDecorator() {
        paint = new Paint();
        paint.setColor(Color.BLACK);
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        int count = parent.getChildCount();
        int width = parent.getWidth();
        for (int i = 0; i < count; i++) {
            View child = parent.getChildAt(i);
            int bottom = child.getBottom();
            c.drawRect(0, bottom, width, bottom + DIVIDER_HEIGHT, paint);
        }
    }
}