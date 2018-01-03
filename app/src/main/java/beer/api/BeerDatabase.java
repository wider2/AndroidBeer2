package beer.api;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import beer.api.dao.BeerDao;
import beer.api.model.Beer;

@Database(entities = {Beer.class}, version = 1, exportSchema = false)
public abstract class BeerDatabase extends RoomDatabase {

    private static volatile BeerDatabase INSTANCE;

    public abstract BeerDao beerDao();

    public static BeerDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (BeerDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            BeerDatabase.class, "Sample.db")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}