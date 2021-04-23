package in.macrocodes.checkPermissions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AdpaterClass extends RecyclerView.Adapter<AdpaterClass.Viewholder> implements Filterable {
    List<String>apps = new ArrayList<>();
    Context context;
    String finAppNAme;
    Drawable icon;
    private int lastPosition = -1;
    List<String> packageName = new ArrayList<>();
    public AdpaterClass(AppCompatActivity activity, List<String> appName,List<String> packageName) {

        this.context = activity;
        this.apps = appName;
        this.packageName = packageName;

    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.textView.setText(apps.get(position));
        try {
            icon = context.getPackageManager().getApplicationIcon(apps.get(position));

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        final PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo( apps.get(position), 0);
        } catch (final PackageManager.NameNotFoundException e) {
            ai = null;
        }
        final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
        holder.textView.setText(applicationName);



        if(icon !=null){
            holder.appIcon.setImageDrawable(icon);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,PermissionsDispActivity.class);
                intent.putExtra("name",applicationName);
                intent.putExtra("package",apps.get(position));
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation((Activity) context, holder.appIcon, "profile");
                context.startActivity(intent,options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<String> filtered = new ArrayList<>();



            if (constraint.toString().isEmpty()){
                filtered.addAll(packageName);
            }else{

                for (String app : packageName){


                        final PackageManager pm = context.getPackageManager();
                        ApplicationInfo ai;
                        try {
                            ai = pm.getApplicationInfo( app, 0);
                        } catch (final PackageManager.NameNotFoundException e) {
                            ai = null;
                        }
                        final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");


                    if (applicationName.toLowerCase().contains(constraint.toString().toLowerCase())){
                        filtered.add(app);
                    }
                }

            }
            FilterResults filterResults = new FilterResults();
            filterResults.values=filtered;
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            apps.clear();
            apps.addAll((Collection<? extends String>) results.values);

            notifyDataSetChanged();
        }
    };
    public class Viewholder extends RecyclerView.ViewHolder{

        public TextView textView;
        public CircleImageView appIcon;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text1);
            appIcon = (CircleImageView) itemView.findViewById(R.id.appIcon);
        }
    }


}
