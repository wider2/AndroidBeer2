package beer.api;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.TextView;

import com.android21buttons.fragmenttestrule.FragmentTestRule;

import org.junit.Rule;
import org.junit.Test;

import beer.api.home.HomeFragment;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by Alexei on 1/3/2018.
 */
public class FragmentTest {

    @Rule
    public FragmentTestRule<?, HomeFragment> fragmentTestRule =
            FragmentTestRule.create(HomeFragment.class);

    @Test
    public void clickButton() throws Exception {
        onView(withText(R.string.reload)).perform(click());

        onView(withText("Beer")).check(matches(isDisplayed()));
    }


}
