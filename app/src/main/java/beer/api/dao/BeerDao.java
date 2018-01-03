package beer.api.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import beer.api.model.Beer;

@Dao
public interface BeerDao {

    @Query("SELECT * FROM beer WHERE id = :beerId")
    Beer loadById(int beerId);


    @Query("SELECT * FROM beer ORDER BY beer_name")
    List<Beer> loadAllByName();

    @Query("SELECT * FROM beer ORDER BY beer_name DESC")
    List<Beer> loadAllByNameDescending();


    @Query("SELECT * FROM beer WHERE favorite = :favor ORDER BY beer_name")
    List<Beer> loadAllFavoritesByName(int favor);

    @Query("SELECT * FROM beer WHERE favorite = :favor ORDER BY beer_name DESC")
    List<Beer> loadAllFavoritesByNameDescending(int favor);


    @Query("SELECT * FROM beer ORDER BY abv")
    List<Beer> loadAllByAbv();

    @Query("SELECT * FROM beer ORDER BY abv DESC")
    List<Beer> loadAllByAbvDescending();


    @Query("SELECT * FROM beer WHERE favorite = :favor ORDER BY abv")
    List<Beer> loadAllFavoritesByAbv(int favor);

    @Query("SELECT * FROM beer WHERE favorite = :favor ORDER BY abv DESC")
    List<Beer> loadAllFavoritesByAbvDescending(int favor);



    @Query("SELECT * FROM beer ORDER BY ibu")
    List<Beer> loadAllByIbu();

    @Query("SELECT * FROM beer ORDER BY ibu DESC")
    List<Beer> loadAllByIbuDescending();


    @Query("SELECT * FROM beer WHERE favorite = :favor ORDER BY ibu")
    List<Beer> loadAllFavoritesByIbu(int favor);

    @Query("SELECT * FROM beer WHERE favorite = :favor ORDER BY ibu DESC")
    List<Beer> loadAllFavoritesByIbuDescending(int favor);



    @Query("SELECT * FROM beer ORDER BY ebc")
    List<Beer> loadAllByEbc();

    @Query("SELECT * FROM beer ORDER BY ebc DESC")
    List<Beer> loadAllByEbcDescending();


    @Query("SELECT * FROM beer WHERE favorite = :favor ORDER BY ebc")
    List<Beer> loadAllFavoritesByEbc(int favor);

    @Query("SELECT * FROM beer WHERE favorite = :favor ORDER BY ebc DESC")
    List<Beer> loadAllFavoritesByEbcDescending(int favor);

/*
    @Insert
    void insertAll(List<Beer> beers);

    @Delete
    void delete(Beer user);

    @Delete
    void deleteAll(List<Beer> beers);
*/
    @Insert
    public abstract void insertBeer(Beer beer);

    //@Update
    @Query("UPDATE beer SET favorite = :favor WHERE id = :beerId")
    void setFavorite(int beerId, int favor);

}
