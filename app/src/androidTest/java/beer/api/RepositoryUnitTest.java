package beer.api;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import beer.api.home.HomeFragment;
import beer.api.home.HomeFragmentPresenter;
import beer.api.home.IHomeFragment;
import beer.api.model.Beer;
import beer.api.utils.RetrofitApi;
import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by Alexei on 1/3/2018.
 */

public class RepositoryUnitTest {

    BeerDatabase mDb;
    @Mock
    Context mMockContext;

    @Mock
    private IHomeFragment view;
    @Mock
    private RetrofitApi retrofitApi;
    @Mock
    private Call<List<Beer>> mockCall;

    private List<Beer> beerList;
    Observable<String> mockResponse;

    @Captor
    private ArgumentCaptor<Callback<List<Beer>>> captor;

    private HomeFragmentPresenter presenter;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mMockContext = InstrumentationRegistry.getTargetContext();
        mDb = BeerDatabase.getInstance(mMockContext);

        presenter = new HomeFragmentPresenter(view, mMockContext);
        beerList = new ArrayList<Beer>();
        mockResponse = Observable.just("test");
    }


    @Test
    public void withStrictStubsTest() {
        mMockContext = InstrumentationRegistry.getTargetContext();

        HomeFragment view = mock(HomeFragment.class);
        HomeFragmentPresenter presenter = new HomeFragmentPresenter(view, mMockContext);
        presenter.getBeerDatabase(mDb);
        verify(view, Mockito.times(0)).refreshResult(new ArrayList<Beer>());
        verifyNoMoreInteractions(view);
    }


}
