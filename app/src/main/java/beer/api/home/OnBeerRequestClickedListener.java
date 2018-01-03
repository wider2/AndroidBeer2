package beer.api.home;

import beer.api.model.Beer;

public interface OnBeerRequestClickedListener {

    void onProfileRequestClicked(int position, Beer beer);

    void onFavoriteRequestClicked(int position, Beer beer);

}
