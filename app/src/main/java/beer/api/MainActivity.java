package beer.api;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
//import com.readystatesoftware.systembartint.SystemBarTintManager;
import beer.api.home.HomeFragment;


/**
 * Created by Aleksei Jegorov on 02/01/2018.
 */
public class MainActivity extends AppCompatActivity
        implements ConnectivityReceiver.ConnectivityReceiverListener {

    TextView tvOutput;
    Snackbar snackBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvOutput = (TextView) findViewById(R.id.tv_output);

        Bundle bundle = getIntent().getExtras();
        if (bundle !=null) {
            if (bundle.getString("key") != null) tvOutput.setText(bundle.getString("key"));
        }
        checkConnection();

        //if we change orientation
        if (savedInstanceState == null) {
        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .add(R.id.home_fragment_container, new HomeFragment(), HomeFragment.class.getName())
                //.addToBackStack(null)
                .commit();
        }
    }

    //Actually, this internet connection check is for testing only (to check offline mode).
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        showSnack(isConnected);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) checkConnection();

        BeerApiApplication.getApp().setConnectivityListener(this);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void showSnack(boolean isConnected) {
        if (!isConnected) {
            snackBar = Snackbar
                    .make(tvOutput, getString(R.string.no_connection), Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(getResources().getColor(R.color.yellow))
                    .setAction(getString(R.string.network_settings), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(android.provider.Settings.ACTION_SETTINGS);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });

            View sbView = snackBar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackBar.show();
        } else {
            if (snackBar != null) snackBar.dismiss();
        }
    }

}
