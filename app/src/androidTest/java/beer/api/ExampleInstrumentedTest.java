package beer.api;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import beer.api.model.Beer;
import beer.api.utils.SharedStatesMap;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Instrumentation test, which will execute on an Android device.
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Test
    public void hasInternet() throws Exception {
        boolean isConnected = ConnectivityReceiver.isConnected();
        assertTrue(isConnected);
    }

    @Test
    public void checkSharedStates() throws Exception {
        int value=1;
        SharedStatesMap mSharedStates = SharedStatesMap.getInstance();
        mSharedStates.setKeyInt("detailsDisplay", value);
        int output = mSharedStates.getKeyInt("detailsDisplay");

        assertEquals(value, output);
    }

    @Test
    public void checkDatabase() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        BeerDatabase db = BeerDatabase.getInstance(appContext);
        int product_id = 1;
        Beer result = db.beerDao().loadById(product_id);

        assertNotNull(result);
    }

    @Test
    public void addToDatabase() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();

        BeerDatabase db = BeerDatabase.getInstance(appContext);
        int product_id = new Random().nextInt(9999999);
        Beer beer = new Beer();
        beer.setId(product_id);
        beer.setBeerName("Zumba Yumba Beer");
        db.beerDao().insertBeer(beer);

        Beer result = db.beerDao().loadById(product_id);

        assertNotEquals("Zumba Yumba", result.getBeerName());

        assertThat(true, is(result.getBeerName().contains("Yumba")));
    }

}
