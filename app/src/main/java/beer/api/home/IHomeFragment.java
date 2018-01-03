package beer.api.home;

import java.util.List;
import beer.api.model.Beer;

public interface IHomeFragment {

    void refreshResult(List<Beer> list);

    void showException(Exception ex);

    void showErrorServerResponse(Throwable response);

}
