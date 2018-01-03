package beer.api.home;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import beer.api.R;
import beer.api.details.DetailsFragment;
import beer.api.model.Beer;
import beer.api.utils.SharedStatesMap;
import beer.api.widgets.DividerItemDecoration;
import beer.api.widgets.ToolbarHelper;
import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment implements IHomeFragment {

    private static final String TAG = "BEERAPI";
    SharedStatesMap mSharedStates;
    HomeFragmentPresenter mPresenter;
    ToolbarHelper mToolbarHelper;

    @BindView(R.id.products_srl)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.toolbar_ib_favorite)
    ImageButton ivFavorite;
    @BindView(R.id.tv_output)
    TextView tvOutput;
    @BindView(R.id.toolbar_title)
    TextView tvTitle;
    @BindView(R.id.caption_tv_name)
    TextView tvCaptionName;
    @BindView(R.id.caption_tv_abv)
    TextView tvCaptionAbv;
    @BindView(R.id.caption_tv_ibu)
    TextView tvCaptionIbu;
    @BindView(R.id.caption_tv_ebc)
    TextView tvCaptionEbc;
    @BindView(R.id.caption_layout_header)
    RelativeLayout rrHeader;
    @BindView(R.id.toolbar_progressbar)
    ProgressBar mProgressBar;
    @BindView(R.id.bt_reload)
    Button btReload;
    @BindView(R.id.rv_beer)
    RecyclerView mRecyclerView;

    DataAdapter adapterRv;
    LinearLayoutManager layoutManager;
    List<Beer> posts;
    private int offset = 80, favor = 0;
    private int favorFlag = 0, displayFlag = 0, sortFlag = 0;
    private Handler mPlHandler = new Handler();
    private int visibleThreshold = 25;
    private int lastVisibleItem, totalItemCount;
    private boolean offsetLoading;


    @AfterViews
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, v);
        try {
            mPresenter = new HomeFragmentPresenter(this, getContext());
            mSharedStates = SharedStatesMap.getInstance();
            favorFlag = mSharedStates.getKeyInt("favoriteDisplay");
            sortFlag = mSharedStates.getKeyInt("sortFlag");

            mToolbarHelper = ToolbarHelper.from(getActivity(), v.findViewById(R.id.toolbar))
                    .title(getString(R.string.app_name))
                    .favoriteListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              ToggleFavoriteDisplayMode();
                                          }
                                      }
                    )
                    .progressBarColorRes(R.color.yellow)
                    .insetsFrom(R.id.ll_root)
                    .colorRes(R.color.n_orange);


            tvOutput.setText(getString(R.string.please_wait_loading));

            tvCaptionName.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    favorFlag = mSharedStates.getKeyInt("favoriteDisplay");
                    int sortIndex = mSharedStates.getKeyInt("sortByName");
                    sortIndex = (sortIndex == 8) ? 9 : 8;
                    mSharedStates.setKeyInt("sortByName", sortIndex);
                    mSharedStates.setKeyInt("sortFlag", sortIndex);
                    setHeader(sortIndex, favorFlag);

                    mPresenter.retrieveBeerList(sortIndex, favorFlag);
                }
            });
            tvCaptionAbv.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    favorFlag = mSharedStates.getKeyInt("favoriteDisplay");
                    int sortIndex = mSharedStates.getKeyInt("sortByAbv");
                    sortIndex = (sortIndex == 2) ? 3 : 2;
                    mSharedStates.setKeyInt("sortByAbv", sortIndex);
                    mSharedStates.setKeyInt("sortFlag", sortIndex);
                    setHeader(sortIndex, favorFlag);

                    mPresenter.retrieveBeerList(sortIndex, favorFlag);
                }
            });
            tvCaptionIbu.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    favorFlag = mSharedStates.getKeyInt("favoriteDisplay");
                    int sortIndex = mSharedStates.getKeyInt("sortByIbu");
                    sortIndex = (sortIndex == 4) ? 5 : 4;
                    mSharedStates.setKeyInt("sortByIbu", sortIndex);
                    mSharedStates.setKeyInt("sortFlag", sortIndex);
                    setHeader(sortIndex, favorFlag);

                    mPresenter.retrieveBeerList(sortIndex, favorFlag);
                }
            });
            tvCaptionEbc.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    favorFlag = mSharedStates.getKeyInt("favoriteDisplay");
                    int sortIndex = mSharedStates.getKeyInt("sortByEbc");
                    sortIndex = (sortIndex == 6) ? 7 : 6;
                    mSharedStates.setKeyInt("sortByEbc", sortIndex);
                    mSharedStates.setKeyInt("sortFlag", sortIndex);
                    setHeader(sortIndex, favorFlag);

                    mPresenter.retrieveBeerList(sortIndex, favorFlag);
                }
            });


            posts = new ArrayList<>();
            layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            layoutManager.setItemPrefetchEnabled(true);
            layoutManager.setInitialPrefetchItemCount(25);
            mRecyclerView.setLayoutManager(layoutManager);

            adapterRv = new DataAdapter(posts, mProductListener, getContext());
            mRecyclerView.setAdapter(adapterRv);
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.setItemViewCacheSize(25);
            mRecyclerView.setDrawingCacheEnabled(true);
            mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
            mRecyclerView.setHasFixedSize(true); //is used to let the RecyclerView that its size will keep the same.
            //mRecyclerView.invalidateItemDecorations();
            mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());

            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                        mSwipeRefreshLayout.setEnabled(false);
                    } else {
                        mSwipeRefreshLayout.setEnabled(true);
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = layoutManager.getItemCount();
                    lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                    if (!offsetLoading && (lastVisibleItem >= totalItemCount - 1) && (totalItemCount >= visibleThreshold)) {
                        // End has been reached
                        //Toast.makeText(getContext(), getString(R.string.reach_end), Toast.LENGTH_SHORT).show();
                        //offset +=25;
                        //initData();
                        //offsetLoading = true;
                    }
                }
            });
            mSwipeRefreshLayout.setOnRefreshListener(mRefreshListener);

            SnapHelper startSnapHelper = new StartSnapHelper();
            startSnapHelper.attachToRecyclerView(mRecyclerView);


            adapterRv.notifyDataSetChanged();


            mProgressBar.setVisibility(View.VISIBLE);
            initData();

        } catch (Exception ex) {
            ex.printStackTrace();
            tvOutput.setText(ex.getMessage());
        }
        return v;
    }


    public void initData() {
        displayFlag = mSharedStates.getKeyInt("detailsDisplay");
        favorFlag = mSharedStates.getKeyInt("favoriteDisplay");
        showIconFavorite(favorFlag);
        if (favorFlag == 1) {
            mPresenter.retrieveBeerList(sortFlag, favorFlag);
        } else {
            mPresenter.getBeerList(displayFlag, sortFlag, offset);
        }
    }

    SwipeRefreshLayout.OnRefreshListener mRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            initData();
        }
    };


    public boolean setHeader(int sort, int favor) {
        boolean params = false;
        String result = ((favor == 1) ? getString(R.string.favorite) + ":\n" : "");
        if (sort == 0 || sort == 1) {
            result += ((sort == 0) ? getString(R.string.sort_name_asc) : getString(R.string.sort_name_desc));
            params = true;
        }
        if (sort == 8 || sort == 9) {
            result += ((sort == 8) ? getString(R.string.sort_name_asc) : getString(R.string.sort_name_desc));
            params = true;
        }
        if (sort == 2 || sort == 3) {
            result += ((sort == 2) ? getString(R.string.sort_abv_asc) : getString(R.string.sort_abv_desc));
            params = true;
        }
        if (sort == 4 || sort == 5) {
            result += ((sort == 4) ? getString(R.string.sort_ibu_asc) : getString(R.string.sort_ibu_desc));
            params = true;
        }
        if (sort == 6 || sort == 7) {
            result += ((sort == 6) ? getString(R.string.sort_ebc_asc) : getString(R.string.sort_ebc_desc));
            params = true;
        }
        tvTitle.setText(result);
        return params;
    }


    public void ToggleFavoriteDisplayMode() {
        sortFlag = mSharedStates.getKeyInt("sortFlag");
        int favor = mSharedStates.getKeyInt("favoriteDisplay");
        favor = (favor == 0) ? 1 : 0;

        mSharedStates.setKeyInt("favoriteDisplay", favor);
        setHeader(sortFlag, favor);
        showIconFavorite(favor);
        mProgressBar.setVisibility(View.VISIBLE);
        mPresenter.retrieveBeerList(favor);
    }

    public void showIconFavorite(int favor) {
        if (favor == 0) {
            ivFavorite.setImageResource(R.drawable.favor0);
        } else {
            ivFavorite.setImageResource(R.drawable.favor);
        }
    }


    OnBeerRequestClickedListener mProductListener = new OnBeerRequestClickedListener() {
        @Override
        public void onProfileRequestClicked(int position, Beer beer) {
            Log.wtf(TAG, "Profile clicked: " + position);
            mSharedStates.setKeyInt("productId", beer.getId());

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right)
                    .replace(R.id.home_fragment_container, new DetailsFragment(), DetailsFragment.class.getName())
                    .addToBackStack(DetailsFragment.class.getName())
                    .commit();
        }

        @Override
        public void onFavoriteRequestClicked(final int position, final Beer beer) {
            Log.wtf(TAG, "Favorite clicked: " + position);
            favor = beer.getFavorite();
            favor = (favor == 0) ? 1 : 0;

            //in case of you want to undo deleted item during 7 seconds
            if (favor == 0) {
                String name = posts.get(position).getBeerName();
                final String msg = "'" + name + "' " + getString(R.string.removed_favorite);

                final Snackbar snackbar = Snackbar.make(tvOutput, msg, Snackbar.LENGTH_LONG);
                snackbar.setAction(getString(R.string.undo), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mPresenter.setFavorite(beer, 1);
                    }
                });

                View snackbarView = snackbar.getView();
                snackbarView.setBackgroundColor(Color.DKGRAY);
                final TextView snackTv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                snackTv.setTextColor(Color.WHITE);

                snackbar.setDuration(7000);
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();
            }
            mPresenter.setFavorite(beer, favor);
        }
    };


    protected void postAndNotifyAdapter(final Handler handler, final RecyclerView recyclerView, final RecyclerView.Adapter adapter) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (recyclerView != null && !recyclerView.isComputingLayout()) {
                    adapter.notifyDataSetChanged();
                    //mRecyclerView.scrollToPosition(0); //it is a wrong approach for favorite list
                } else {
                    postAndNotifyAdapter(handler, recyclerView, adapter);
                }
            }
        }, 500);
    }


    public void updateRv() {
        mRecyclerView.invalidate();
        adapterRv.setList(posts);
        adapterRv.myNotify(posts);
        adapterRv.notifyItemRangeChanged(0, posts.size());
        postAndNotifyAdapter(mPlHandler, mRecyclerView, adapterRv);
    }


    @UiThread
    @Override
    public void refreshResult(List<Beer> resultList) {
        try {
            posts = resultList;

            sortFlag = mSharedStates.getKeyInt("sortFlag");
            favorFlag = mSharedStates.getKeyInt("favoriteDisplay");
            setHeader(sortFlag, favorFlag);
            mSharedStates.setKeyInt("sortFlag", sortFlag);

            if (resultList.size() <= 0) {
                tvOutput.setText(getString(R.string.not_found));
                rrHeader.setVisibility(View.INVISIBLE);
            } else {
                tvOutput.setText(getString(R.string.total_found) + ": " + Integer.toString(resultList.size()));
                rrHeader.setVisibility(View.VISIBLE);
            }
            updateRv();

            mSharedStates.setKeyInt("detailsDisplay", 0);
            mProgressBar.setVisibility(View.GONE);
            btReload.setVisibility(View.GONE);
            mSwipeRefreshLayout.setEnabled(false);

            offsetLoading = false;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @UiThread
    @Override
    public void showException(Exception ex) {
        tvOutput.setText(getString(R.string.server_response) + ": " + ex.getMessage() + "\n" + ex.getStackTrace().toString());
    }

    @UiThread
    @Override
    public void showErrorServerResponse(Throwable response) {
        mSharedStates.setKeyInt("favoriteDisplay", 0);
        mSharedStates.setKeyInt("detailsDisplay", 0);

        String msg = response.getMessage();
        tvOutput.setText(getString(R.string.server_response) + ": " + msg + "\n" + response.getStackTrace());
        if (msg.contains("Unable to resolve host") || msg.contains("No address associated with hostname") || msg.contains("403 Forbidden"))
            tvOutput.append("\n\n" + getString(R.string.no_response));

        mProgressBar.setVisibility(View.GONE);
        btReload.setVisibility(View.VISIBLE);
        btReload.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mProgressBar.setVisibility(View.VISIBLE);
                mPresenter.getBeerList(0, sortFlag, offset);
            }
        });
    }

}