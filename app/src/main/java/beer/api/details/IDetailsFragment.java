package beer.api.details;

import beer.api.model.Beer;

public interface IDetailsFragment {

    void refreshResult(Beer beer);

    void ExceptionOccurred(Exception ex);

    void refreshFavorite(int favor);

}
