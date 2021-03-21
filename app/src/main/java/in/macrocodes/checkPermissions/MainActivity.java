package in.macrocodes.checkPermissions;

import android.app.ActivityOptions;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.codemybrainsout.ratingdialog.RatingDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.pm.PackageManager.GET_PERMISSIONS;

public class MainActivity extends AppCompatActivity {


    SearchView searchView;
    RecyclerView recyclerView;
    AdpaterClass mAdapter;
    RelativeLayout splash;
    ImageView myappIcon,settings;
    TextView textView;
    List<String> apps = new ArrayList<>();
    List<String> packageName = new ArrayList<>();
    List<HashMap<String, Object>> permissions = new ArrayList<HashMap<String, Object>>();

    private Handler mHandler;

    private HandlerThread mHandlerThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // TextView textView = (TextView) findViewById(R.id.tx);
        final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                .icon(getApplicationInfo().loadIcon(getPackageManager()))
                .session(2)
                .threshold(1)
                .title("How was your experience with us?")
                .titleTextColor(R.color.black)
                .positiveButtonText("Not Now")
                .negativeButtonText("Never")
                .positiveButtonTextColor(R.color.grey_500)
                .negativeButtonTextColor(R.color.grey_500)

                .ratingBarColor(R.color.yellow)
                .playstoreUrl("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)

                .onRatingChanged(new RatingDialog.Builder.RatingDialogListener() {
                    @Override
                    public void onRatingSelected(float rating, boolean thresholdCleared) {
                        try{
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                        }
                        catch (ActivityNotFoundException e){
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
                        }
                    }
                })
                .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                    @Override
                    public void onFormSubmitted(String feedback) {
                        try{
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id="+getPackageName())));
                        }
                        catch (ActivityNotFoundException e){
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="+getPackageName())));
                        }
                    }
                }).build();

        ratingDialog.show();

        splash = (RelativeLayout) findViewById(R.id.splash);
        myappIcon = (ImageView) findViewById(R.id.myappicon);
        settings = (ImageView) findViewById(R.id.settings);

        searchView = (SearchView) findViewById(R.id.search);
        myappIcon.setVisibility(View.VISIBLE);
        textView = (TextView) findViewById(R.id.scanning);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
                ActivityOptions activityOptions= ActivityOptions.makeCustomAnimation(MainActivity.this,R.anim.zoom_in,R.anim.nothing);
                startActivity(intent,activityOptions.toBundle());
            }
        });


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                splash.setVisibility(View.GONE);
                Animation animation   =    AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out);
                animation.setDuration(400);
                splash.setAnimation(animation);
                splash.animate();
                animation.start();
            }
        },1500);


        recyclerView = findViewById(R.id.allApps);
        LinearLayoutManager mLinearLayout = new LinearLayoutManager(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(mLinearLayout);
        mAdapter = new AdpaterClass(MainActivity.this, apps, packageName);
        recyclerView.setAdapter(mAdapter);

        Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                getApp();

            }
        }, 100);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                mAdapter.getFilter().filter(newText);
                return false;
            }
        });

    }

    public void getApp() {
        PackageManager pm = getPackageManager();

        // Loop each package requesting <manifest> permissions
        for (final PackageInfo pi : pm.getInstalledPackages(GET_PERMISSIONS)) {

            if ((pi.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                //it's a system app, not interested
            } else if ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                //Discard this one
                //in this case, it should be a user-installed app
            } else {
                final String[] requestedPermissions = pi.requestedPermissions;
                String as = pi.packageName;
                String appName = pi.applicationInfo.loadLabel(getPackageManager()).toString();

                packageName.add(as);
                apps.add(as);
                mAdapter.notifyDataSetChanged();


            }


        }
    }
}