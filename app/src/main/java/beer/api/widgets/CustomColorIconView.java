package beer.api.widgets;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.support.v7.widget.AppCompatImageView;
import beer.api.R;

public class CustomColorIconView extends ImageView {
    public CustomColorIconView(Context context) {
        super(context);
    }

    public CustomColorIconView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomColorIconView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomColorIconView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs);
    }
  
    private void init(Context context, AttributeSet attrs){
         
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomColorIconView);

        int color = typedArray.getColor(R.styleable.CustomColorIconView_dciv_color,0);
        setColorFilter(color, PorterDuff.Mode.SRC_ATOP);

        typedArray.recycle();
    }

    public void setImageFilterColor(int color)
    {
        if(color == -1)
        {
            setColorFilter(null);
        }
        else
        {
            setColorFilter(color,PorterDuff.Mode.SRC_ATOP);
        }
    }
}