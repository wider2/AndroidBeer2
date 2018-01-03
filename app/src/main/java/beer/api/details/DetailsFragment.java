package beer.api.details;

import android.os.Bundle;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flaviofaria.kenburnsview.KenBurnsView;
import com.github.florent37.diagonallayout.DiagonalLayout;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.IgnoreWhen;

import beer.api.R;
import beer.api.model.Beer;
import beer.api.utils.SharedStatesMap;
import beer.api.widgets.ToolbarHelper;
import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailsFragment extends Fragment implements IDetailsFragment {

    SharedStatesMap mSharedStates;
    DetailsFragmentPresenter mPresenter;
    int productId;
    ToolbarHelper mToolbarHelper;
    Beer mBeer;

    @BindView(R.id.toolbar_ib_favorite)
    ImageButton ivFavorite;
    @BindView(R.id.tv_output)
    TextView tvOutput;
    @BindView(R.id.tv_main)
    TextView tvMain;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_abv)
    TextView tvAbv;
    @BindView(R.id.tv_ibu)
    TextView tvIbu;
    @BindView(R.id.tv_ebc)
    TextView tvEbc;
    @BindView(R.id.tv_description)
    TextView tvDescription;
    @BindView(R.id.tv_tagline)
    TextView tvTagline;
    @BindView(R.id.tv_brewed)
    TextView tvBrewed;
    @BindView(R.id.tv_srm)
    TextView tvSrm;
    @BindView(R.id.tv_ph)
    TextView tvPh;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.tv_volume)
    TextView tvVolume;
    @BindView(R.id.tv_boil_volume)
    TextView tvBoilVolume;
    @BindView(R.id.tv_mash_temp)
    TextView tvMashTemp;
    @BindView(R.id.tv_fermentation)
    TextView tvFermentation;
    @BindView(R.id.tv_food_pairing)
    TextView tvFoodPairing;
    @BindView(R.id.tv_brewers_tips)
    TextView tvBrewersTips;
    @BindView(R.id.tv_by)
    TextView tvBy;
    @BindView(R.id.sv_details)
    ScrollView svDetails;
    @BindView(R.id.toolbar_progressbar)
    ProgressBar mProgressBar;
    @BindView(R.id.diagonalLayout)
    DiagonalLayout dl;
    @BindView(R.id.kenBurnsView)
    KenBurnsView kenBurnsView;


    @AfterViews
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_details, container, false);
        ButterKnife.bind(this, v);
        try {
            mPresenter = new DetailsFragmentPresenter(this, getContext());
            mSharedStates = SharedStatesMap.getInstance();
            productId = mSharedStates.getKeyInt("productId");
            mSharedStates.setKeyInt("detailsDisplay", 1);

            //mProgressBar.setVisibility(View.VISIBLE);
            mToolbarHelper = ToolbarHelper.from(getActivity(), v.findViewById(R.id.toolbar))
                    .backButton()
                    .title(getString(R.string.description))
                    .progressBar(true)
                    .favoriteListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View view) {
                                              int favor = mBeer.getFavorite();
                                              favor = (favor == 0) ? 1 : 0;
                                              mPresenter.setFavorite(mBeer, favor);
                                          }
                                      }
                    )
                    .progressBarColorRes(R.color.yellow)
                    .insetsFrom(R.id.ll_details)
                    .colorRes(R.color.n_orange);

            tvOutput.setText(getString(R.string.please_wait_loading));
            kenBurnsView.setOnClickListener(null);

            mPresenter.getProductDetails(productId);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return v;
    }


    @UiThread
    @Override
    public void refreshFavorite(int favor) {
        if (favor == 0) {
            ivFavorite.setImageResource(R.drawable.favor0);
        } else {
            ivFavorite.setImageResource(R.drawable.favor);
        }
    }


    @UiThread
    @Override
    public void refreshResult(Beer result) {
        try {
            mBeer = result;
            if (result.getFavorite() == 0) {
                ivFavorite.setImageResource(R.drawable.favor0);
            } else {
                ivFavorite.setImageResource(R.drawable.favor);
            }

            if (result.getId() == 0) {
                dl.setVisibility(View.GONE);
                svDetails.setVisibility(View.GONE);
                tvOutput.setText(getString(R.string.info_not_found));
            } else {
                dl.setVisibility(View.VISIBLE);
                svDetails.setVisibility(View.VISIBLE);

                tvOutput.setText("");
                tvMain.setText(result.getBeerName());
                tvName.setText(result.getBeerName());
                tvAbv.setText("" + result.getAbv());
                tvIbu.setText("" + result.getIbu());
                tvEbc.setText("" + result.getEbc());

                tvTagline.setText(result.getTagLine());
                tvDescription.setText(result.getDescription());
                tvBrewed.setText(result.getFirstBrewed());
                tvSrm.setText("" + result.getSrm());
                tvPh.setText("" + result.getPh());
                tvLevel.setText("" + result.getAttenuationLevel());
                tvVolume.setText(result.getVolumeValue() + " " + result.getVolumeUnit());
                tvBoilVolume.setText(result.getBoilVolume() + " " + result.getBoilVolumeUnit());
                tvMashTemp.setText(result.getMashTempValue() + " " + result.getMashTempUnit() + ", " + getString(R.string.duration) + " " + result.getMashDuration());
                tvFermentation.setText(result.getFermentationTempValue() + " " + result.getFermentationTempUnit());
                tvFoodPairing.setText(result.getFoodPairing());
                tvBrewersTips.setText(result.getBrewersTips());
                tvBy.setText(result.getContributedBy());

                Picasso.with(getContext())
                        .load(result.getImageUrl())
                        .placeholder(R.drawable.loading)
                        .error(R.drawable.noimage)
                        .into(kenBurnsView);
            }
            mProgressBar.setVisibility(View.GONE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @UiThread
    @IgnoreWhen(IgnoreWhen.State.VIEW_DESTROYED)
    @Override
    public void ExceptionOccurred(Exception ex) {
        tvOutput.setText(ex.getMessage());
    }

}