package in.macrocodes.checkPermissions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.pm.PackageInfo.REQUESTED_PERMISSION_GRANTED;
import static android.content.pm.PackageManager.GET_PERMISSIONS;
import static android.content.pm.PermissionInfo.PROTECTION_DANGEROUS;
import static android.content.pm.PermissionInfo.PROTECTION_SIGNATURE;

public class PermissionsDispActivity extends AppCompatActivity {
   String preName,packageName ;
   TextView textView,appName;
   CircleImageView appIcon;
   LinearLayout linearLayout;
   LinearLayout linearLayout2;
   LinearLayout lay1,lay2;
   TextView per1,da1;
   int dan=0,per=0;
   Button btn;

   PermissionAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions_disp);

        preName = getIntent().getStringExtra("name");
        packageName = getIntent().getStringExtra("package");
        lay1 = (LinearLayout) findViewById(R.id.lay1);
        lay2 = (LinearLayout) findViewById(R.id.lay2);

        per1 = (TextView) findViewById(R.id.per);
        da1 = (TextView) findViewById(R.id.dan);
        appIcon = (CircleImageView) findViewById(R.id.appIcon);
        appName = (TextView) findViewById(R.id.appName);
        appName.setText(preName);


        try {
            Drawable icon = getPackageManager().getApplicationIcon(packageName);
            appIcon.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        linearLayout = (LinearLayout) findViewById(R.id.view);
        linearLayout2 = (LinearLayout) findViewById(R.id.dangerous);
        textView = (TextView) findViewById(R.id.text2);
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package",packageName, null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        searchPermissions();
        startAnim();
    }

    public void searchPermissions(){

        PackageManager pm = getPackageManager();
        for (final PackageInfo pi : pm.getInstalledPackages(GET_PERMISSIONS)) {

            if ((pi.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                //it's a system app, not interested
            } else if ((pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                //Discard this one
                //in this case, it should be a user-installed app
            } else {

                final String[] requestedPermissions = pi.requestedPermissions;
                String appName = pi.applicationInfo.loadLabel(getPackageManager()).toString();
                if (preName.equalsIgnoreCase(appName)){
                    if (requestedPermissions == null) {
                        // No permissions defined in <manifest>
                        continue;

                    }
                    for (int i = 0, len = requestedPermissions.length; i < len; i++) {
                        final String requestedPerm = requestedPermissions[i];
                        Log.e("per", "Requested Permission : " + requestedPerm);



                       // textView.append(requestedPerm+"\n");




                        // textView.append(appName + "- "+requestedPerm+"\n");
                        // Retrieve the protection level for each requested permission
                        int protLevel;
                        try {
                            protLevel = pm.getPermissionInfo(requestedPerm, 0).protectionLevel;
                        } catch (PackageManager.NameNotFoundException e) {
                            Log.e("per", "Unknown permission: " + requestedPerm, e);
                            continue;
                        }
                        final boolean system = protLevel == PROTECTION_SIGNATURE;
                        final boolean dangerous = protLevel == PROTECTION_DANGEROUS;
                        final boolean granted = (pi.requestedPermissionsFlags[i]
                                & REQUESTED_PERMISSION_GRANTED) != 0;


                        if (dangerous){

                            TextView textView = new TextView(PermissionsDispActivity.this);
                            //String sub = requestedPerm.substring(0, requestedPerm.indexOf("."));

                            String s=null ;
                            if (requestedPerm.contains("android.permission.")){
                              s = requestedPerm.replace("android.permission.", "");
                            }else if (requestedPerm.contains("com.google.android.c2dm.permission.")){
                                s = requestedPerm.replace("com.google.android.c2dm.permission.", "");
                            }



                            textView.setText(s+"\n");
                            textView.setTextColor(Color.parseColor("#ffffff"));
                            textView.setTextSize(14);
                            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            Typeface face= Typeface.defaultFromStyle(Typeface.BOLD);
                            textView.setTypeface(face);
                            dan++;
                            // textView.setTextAppearance(PermissionsDispActivity.this,
                            //        R.style.fontForNotificationLandingPage);
                            linearLayout2.addView(textView);
                        }


                        if (granted){
                            TextView textView = new TextView(PermissionsDispActivity.this);
                            String s = requestedPerm.replace("android.permission.", "");
                            textView.setText(s+"\n");
                            textView.setTextColor(Color.parseColor("#ffffff"));

                            textView.setTextSize(14);
                            textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                            Typeface face= Typeface.defaultFromStyle(Typeface.BOLD);
                            textView.setTypeface(face);
                            per++;
                            // textView.setTextAppearance(PermissionsDispActivity.this,
                            //        R.style.fontForNotificationLandingPage);
                            linearLayout.addView(textView);
                        }
                    }
                }
            }


        }
        if (per == 0){
            per1.setVisibility(View.VISIBLE);
        }

        if (dan ==0){
            da1.setVisibility(View.VISIBLE);
        }


    }
//    public String NormalizePer(String req){
//
//
//    }

    public void startAnim(){
        lay1.setVisibility(View.VISIBLE);
        Animation animation   =    AnimationUtils.loadAnimation(PermissionsDispActivity.this, R.anim.rg3);
        animation.setDuration(500);
        lay1.setAnimation(animation);
        lay1.animate();
        animation.start();

        lay2.setVisibility(View.VISIBLE);
        Animation animation2   =    AnimationUtils.loadAnimation(PermissionsDispActivity.this, R.anim.rg3);
        animation2.setDuration(500);
        lay2.setAnimation(animation);
        lay2.animate();
        animation2.start();
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
        super.onBackPressed();

    }
}