package beer.api.home;

import android.content.Context;
import android.util.Log;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.reactivestreams.Subscription;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import beer.api.BeerDatabase;
import beer.api.model.Beer;
import beer.api.utils.Connectivity;
import beer.api.utils.RetrofitApi;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiConsumer;
import io.reactivex.functions.Consumer;
import io.reactivex.observables.ConnectableObservable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static beer.api.utils.GlobalConstants.SERVER_SSL_URL;


public class HomeFragmentPresenter {

    private static final String TAG = "BEERAPI";
    private int sortIndex = 0;
    private int favorFlag = 0;
    private IHomeFragment mainView;
    Context mContext;
    BeerDatabase mDb;

    public HomeFragmentPresenter(IHomeFragment mainView, Context ctx) {
        this.mainView = mainView;
        this.mContext = ctx;
        mDb = BeerDatabase.getInstance(ctx);
    }

    public void retrieveBeerList() {
        selectBeerList(mDb);
    }

    public void retrieveBeerList(int favor) {
        this.favorFlag = favor;
        selectBeerList(mDb);
    }

    public void retrieveBeerList(int sortOrder, int favor) {
        this.sortIndex = sortOrder;
        this.favorFlag = favor;
        selectBeerList(mDb);
    }

    private void getFromDatabase() {
        try {
            Observable.fromCallable(new Callable<List<Beer>>() {
                @Override
                public List<Beer> call() {
                    List<Beer> list = getBeerDatabase(mDb);
                    return list;
                }
            })
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<Beer>>() {
                        @Override
                        public void accept(List<Beer> beerList) throws Exception {
                            mainView.refreshResult(beerList);
                        }
                    });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getBeerList(int displayFlag, int sortOrder, int offset) {
        this.sortIndex = sortOrder;

        if (!Connectivity.isConnected(mContext)) {
            //Offline mode
            getFromDatabase();

        } else {

            if (displayFlag == 1) {
                getFromDatabase();
            } else {
                //Online mode
                try {
                    Retrofit retrofit = new Retrofit.Builder()
                            .validateEagerly(true)
                            .baseUrl(SERVER_SSL_URL)
                            .addConverterFactory(ScalarsConverterFactory.create())
                            //.addConverterFactory(JacksonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                    RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);


                    Single<String> obj = retrofitApi.getBeerList(1, offset)
                            .firstOrError()
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread());

                    obj.subscribe(new BiConsumer<String, Throwable>() {
                        @Override
                        public void accept(String jsonData, Throwable throwable) throws Exception {
                            if (throwable != null) {
                                Log.wtf(TAG, "Hunted for data: " + jsonData);
                                mainView.showErrorServerResponse(throwable);
                            } else {
                                try {
                                    Log.wtf(TAG, "Hunted for data: " + jsonData);

                                    Beer beer;
                                    List<Beer> beerList = new ArrayList<>();
                                    StringBuilder stringBuilder = new StringBuilder();

                                    ObjectMapper objectMapper = new ObjectMapper();
                                    JsonNode rootNode = objectMapper.readTree(jsonData);


                                    Iterator<JsonNode> elements = rootNode.elements();
                                    while (elements.hasNext()) {
                                        JsonNode node = elements.next();


                                        beer = new Beer();
                                        JsonNode idNode = node.path("id");
                                        beer.setId(idNode.asInt());

                                        JsonNode nameNode = node.path("name");
                                        if (nameNode != null)
                                            beer.setBeerName(nameNode.asText());

                                        JsonNode tagNode = node.path("tagline");
                                        if (tagNode != null)
                                            beer.setTagLine(tagNode.asText());

                                        JsonNode brewNode = node.path("first_brewed");
                                        if (brewNode != null)
                                            beer.setFirstBrewed(brewNode.asText());

                                        JsonNode descrNode = node.path("description");
                                        if (descrNode != null)
                                            beer.setDescription(descrNode.asText());

                                        JsonNode imgNode = node.path("image_url");
                                        if (imgNode != null)
                                            beer.setImageUrl(imgNode.asText());

                                        JsonNode abvNode = node.path("abv");
                                        if (abvNode != null)
                                            beer.setAbv(abvNode.asDouble());

                                        JsonNode ibuNode = node.path("ibu");
                                        if (ibuNode != null)
                                            beer.setIbu(ibuNode.asDouble());

                                        JsonNode fgNode = node.path("target_fg");
                                        if (fgNode != null)
                                            beer.setTargetFg(fgNode.asDouble());

                                        JsonNode ogNode = node.path("target_og");
                                        if (ogNode != null)
                                            beer.setTargetOg(ogNode.asDouble());

                                        JsonNode ebcNode = node.path("ebc");
                                        if (ebcNode != null)
                                            beer.setEbc(ebcNode.asDouble());

                                        JsonNode srmNode = node.path("srm");
                                        if (srmNode != null)
                                            beer.setSrm(srmNode.asDouble());

                                        JsonNode phNode = node.path("ph");
                                        if (phNode != null) beer.setPh(phNode.asDouble());

                                        JsonNode atNode = node.path("attenuation_level");
                                        if (atNode != null)
                                            beer.setAttenuationLevel(atNode.asDouble());


                                        JsonNode volumedNode = node.path("volume");
                                        JsonNode volValueNode = volumedNode.path("value");
                                        if (volValueNode != null)
                                            beer.setVolumeValue(volValueNode.asInt());
                                        JsonNode volUnitNode = volumedNode.path("unit");
                                        if (volUnitNode != null)
                                            beer.setVolumeUnit(volUnitNode.asText());


                                        JsonNode boilVolumedNode = node.path("boil_volume");
                                        JsonNode boilValueNode = boilVolumedNode.path("value");
                                        if (boilValueNode != null)
                                            beer.setBoilVolume(boilValueNode.asInt());
                                        JsonNode boilUnitNode = volumedNode.path("unit");
                                        if (boilUnitNode != null)
                                            beer.setBoilVolumeUnit(boilUnitNode.asText());


                                        JsonNode methodNode = node.path("method");
                                        JsonNode mashNode = methodNode.path("mash_temp");
                                        Iterator<JsonNode> itms = mashNode.elements();
                                        while (itms.hasNext()) {
                                            JsonNode item = itms.next();
                                            JsonNode tempNode = item.path("temp");
                                            JsonNode valueNode = tempNode.path("value");
                                            if (valueNode != null)
                                                beer.setMashTempValue(valueNode.asInt());

                                            JsonNode unitNode = tempNode.path("unit");
                                            if (unitNode != null)
                                                beer.setMashTempUnit(unitNode.asText());

                                            JsonNode durationNode = item.path("duration");
                                            if (durationNode != null)
                                                beer.setMashDuration(durationNode.asInt());
                                        }
                                        JsonNode fermNode = methodNode.path("fermentation");
                                        JsonNode fermTempNode = fermNode.path("temp");
                                        JsonNode fermTempValueNode = fermTempNode.path("value");
                                        beer.setFermentationTempValue(fermTempValueNode.asInt());

                                        JsonNode fermTempUnitNode = fermTempNode.path("unit");
                                        beer.setFermentationTempUnit(fermTempUnitNode.asText());


                                        JsonNode ingdNode = node.path("ingredients");
                                        JsonNode yeastNode = ingdNode.path("yeast");
                                        if (yeastNode != null)
                                            beer.setYeast(yeastNode.asText());


                                        stringBuilder.setLength(0);
                                        JsonNode foodNode = node.path("food_pairing");
                                        Iterator<JsonNode> items = foodNode.elements();
                                        while (items.hasNext()) {
                                            JsonNode item = items.next();
                                            if (item != null) {
                                                stringBuilder.append(item.asText() + "\n");
                                            }
                                        }
                                        beer.setFoodPairing(stringBuilder.toString());


                                        JsonNode tipNode = node.path("brewers_tips");
                                        if (tipNode != null)
                                            beer.setBrewersTips(tipNode.asText());

                                        JsonNode contrNode = node.path("contributed_by");
                                        if (contrNode != null)
                                            beer.setContributedBy(contrNode.asText());

                                        beerList.add(beer);
                                        addBeerToDatabase(mDb, beer);
                                    }
                                    selectBeerList(mDb);

                                } catch (Exception ex) {
                                    mainView.showException(ex);
                                }
                            }
                        }
                    });

                } catch (Exception ex) {
                    mainView.showException(ex);
                }
            }
        }
        //return true;
    }


    private void addBeerToDatabase(final BeerDatabase db, final Beer beer) {
        Observable.fromCallable(new Callable<Beer>() {
            @Override
            public Beer call() {
                try {
                    Beer model = db.beerDao().loadById(beer.getId());
                    if (model == null) db.beerDao().insertBeer(beer);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return beer;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }


    private void selectBeerList(final BeerDatabase db) {

        Observable.fromCallable(new Callable<List<Beer>>() {
            @Override
            public List<Beer> call() {
                List<Beer> list = getBeerDatabase(db);
                return list;
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<Beer>>() {
                    @Override
                    public void accept(List<Beer> beerList) throws Exception {
                        mainView.refreshResult(beerList);
                    }
                });
    }


    public List<Beer> getBeerDatabase(BeerDatabase db) {
        List<Beer> list = null;
        if (favorFlag == 1) {
            if (sortIndex == 0 || sortIndex == 8) {
                list = db.beerDao().loadAllFavoritesByName(favorFlag);
            } else if (sortIndex == 1 || sortIndex == 9) {
                list = db.beerDao().loadAllFavoritesByNameDescending(favorFlag);
            } else if (sortIndex == 2) {
                list = db.beerDao().loadAllFavoritesByAbv(favorFlag);
            } else if (sortIndex == 3) {
                list = db.beerDao().loadAllFavoritesByAbvDescending(favorFlag);
            } else if (sortIndex == 4) {
                list = db.beerDao().loadAllFavoritesByIbu(favorFlag);
            } else if (sortIndex == 5) {
                list = db.beerDao().loadAllFavoritesByIbuDescending(favorFlag);
            } else if (sortIndex == 6) {
                list = db.beerDao().loadAllFavoritesByEbc(favorFlag);
            } else if (sortIndex == 7) {
                list = db.beerDao().loadAllFavoritesByEbcDescending(favorFlag);
            }
        } else {
            if (sortIndex == 0 || sortIndex == 8) {
                list = db.beerDao().loadAllByName();
            } else if (sortIndex == 1 || sortIndex == 9) {
                list = db.beerDao().loadAllByNameDescending();
            } else if (sortIndex == 2) {
                list = db.beerDao().loadAllByAbv();
            } else if (sortIndex == 3) {
                list = db.beerDao().loadAllByAbvDescending();
            } else if (sortIndex == 4) {
                list = db.beerDao().loadAllByIbu();
            } else if (sortIndex == 5) {
                list = db.beerDao().loadAllByIbuDescending();
            } else if (sortIndex == 6) {
                list = db.beerDao().loadAllByEbc();
            } else if (sortIndex == 7) {
                list = db.beerDao().loadAllByEbcDescending();
            }
        }
        return list;
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
                        retrieveBeerList();
                    }
                });
    }


}