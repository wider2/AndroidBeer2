package beer.api;

import android.content.Intent;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.TextView;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by Alexei on 1/3/2018.
 */

@MediumTest
@RunWith(AndroidJUnit4.class)
public class ActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> rule  = new  ActivityTestRule<MainActivity>(MainActivity.class)
    {
        @Override
        protected Intent getActivityIntent() {
            InstrumentationRegistry.getTargetContext();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.putExtra("key", "Application to be crashed or not crashed?");
            return intent;
        }
    };

    @Test
    public void testDataIsDisplayed() throws Exception {
        MainActivity activity = rule.getActivity();

        View viewById = activity.findViewById(R.id.tv_output);
        TextView tv = (TextView) viewById;
        assertThat(tv.getText().toString(), containsString("crashed"));
    }

}
