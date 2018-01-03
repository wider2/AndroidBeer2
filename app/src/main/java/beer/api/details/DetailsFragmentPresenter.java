package beer.api.details;

import android.content.Context;

import java.util.concurrent.Callable;

import beer.api.BeerDatabase;
import beer.api.model.Beer;
import beer.api.utils.Utilities;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;


public class DetailsFragmentPresenter {

    private static final String TAG = "BEERAPI";
    private IDetailsFragment mainView;
    private Context mContext;
    private BeerDatabase mDb;

    public DetailsFragmentPresenter(IDetailsFragment mainView, Context ctx) {
        this.mainView = mainView;
        this.mContext = ctx;
        mDb = BeerDatabase.getInstance(ctx);
    }

    public void getProductDetails(final int product_id) {
        try {
            Observable.fromCallable(new Callable<Beer>() {
                @Override
                public Beer call() {
                    Beer list = mDb.beerDao().loadById(product_id);
                    return list;
                }
            })
                    .filter(new Predicate<Beer>() {
                        @Override
                        public boolean test(@NonNull Beer beer) throws Exception {
                            return beer.getId() > 0;
                        }
                    })
                    .map(new Function<Beer, Beer>() {
                        @Override
                        public Beer apply(@NonNull Beer beer) throws Exception {
                            Utilities.truncate(beer.getDescription(), 500);
                            return beer;
                        }
                    })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Beer>() {
                        @Override
                        public void accept(Beer beerList) throws Exception {
                            mainView.refreshResult(beerList);
                        }
                    });
        } catch (Exception ex) {
            mainView.ExceptionOccurred(ex);
        }
    }


    public void setFavorite(final Beer beer, final int favor) {

        Observable.fromCallable(new Callable<Beer>() {
            @Override
            public Beer call() {
                mDb.beerDao().setFavorite(beer.getId(), favor);
                return beer;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Beer>() {
                    @Override
                    public void accept(Beer beer) throws Exception {
                        mainView.refreshFavorite(favor);
                    }
                });
    }

}