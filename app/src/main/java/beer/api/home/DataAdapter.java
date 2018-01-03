package beer.api.home;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import beer.api.R;
import beer.api.model.Beer;
import beer.api.utils.ColorUtility;
import beer.api.widgets.MyBounceInterpolator;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {

    private static final int COLOR_NORMAL = android.R.color.transparent;
    private static final int COLOR_SELECTED = R.color.gray_light2;
    private int lastPosition = -1;
    Context context;
    private List<Beer> mList;
    private List<Beer> mBeerList;
    private List<FavoritePosition> mSelectFavorite = new ArrayList<FavoritePosition>();
    private OnBeerRequestClickedListener mListener;
    private FavoritePosition mInitialPos;


    public DataAdapter(List<Beer> posts, OnBeerRequestClickedListener listener, Context ctx) {
        setHasStableIds(true);
        this.mList = posts;
        mListener = listener;
        context = ctx;
    }

    public void setList(List<Beer> list) {
        int favor = 0;
        mList = list;
        if (mList != null) {
            mBeerList = new ArrayList<>(mList);

            mSelectFavorite.clear();
            for (int i = 0; i < mBeerList.size(); i++) {
                favor = mBeerList.get(i).getFavorite();
                mInitialPos = new FavoritePosition(i, favor);
                mSelectFavorite.add(mInitialPos);
            }
        }
    }

    @Override
    public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_beer, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Beer product = mList.get(position);

        holder.cardView.setBackgroundColor(ColorUtility.getColor((position % 2 == 0) ? COLOR_SELECTED : COLOR_NORMAL));

        holder.tvName.setText(product.getBeerName());
        holder.tvAbv.setText(context.getString(R.string.abv) + ": " + Double.toString(product.getAbv()));
        holder.tvIbu.setText(context.getString(R.string.ibu) + ": " + Double.toString(product.getIbu()));
        holder.tvEbc.setText(context.getString(R.string.ebc) + ": " + Double.toString(product.getEbc()));

        //to avoid lost position of item in the list
        holder.ivFavorite.setImageResource(R.drawable.favor0);
        for (int i = 0; i < mSelectFavorite.size(); i++) {
            mInitialPos = mSelectFavorite.get(i);
            if (mInitialPos.Position == position) {
                holder.ivFavorite.setImageResource(R.drawable.favor);
                break;
            }
        }

        if (product.getFavorite() == 0) {
            holder.ivFavorite.setImageResource(R.drawable.favor0);
        } else {
            holder.ivFavorite.setImageResource(R.drawable.favor);
        }

        holder.ivFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation anim;
                if (product.getFavorite() == 0) {
                    anim = AnimationUtils.loadAnimation(context, R.anim.rotate);
                } else {
                    anim = AnimationUtils.loadAnimation(context, R.anim.jump2);
                }
                holder.ivFavorite.startAnimation(anim);

                mListener.onFavoriteRequestClicked(position, product);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation myAnim = AnimationUtils.loadAnimation(context, R.anim.bounce_double);
                double animationDuration = 1500;
                myAnim.setDuration((long)animationDuration);

                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 10);
                myAnim.setInterpolator(interpolator);
                holder.tvName.startAnimation(myAnim);

                mListener.onProfileRequestClicked(position, product);
            }
        });

        setItemAnimation(holder.itemView, position);
    }

    private void setItemAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            anim.setDuration(new Random().nextInt(199));
            viewToAnimate.startAnimation(anim);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public long getItemId(int position) {
        return mList.get(position).getId();
    }

    public void myNotify(List<Beer> currentShows) {
        this.mList = currentShows;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView tvName, tvAbv, tvIbu, tvEbc;
        ImageButton ivFavorite;

        public ViewHolder(View view) {
            super(view);
            cardView = (CardView) view;
            tvName = (TextView) view.findViewById(R.id.listitem_tv_name);
            tvAbv = (TextView) view.findViewById(R.id.listitem_tv_abv);
            tvIbu = (TextView) view.findViewById(R.id.listitem_tv_ibu);
            tvEbc = (TextView) view.findViewById(R.id.listitem_tv_ebc);
            ivFavorite = (ImageButton) view.findViewById(R.id.listitem_iv_favorite);
        }
    }
}