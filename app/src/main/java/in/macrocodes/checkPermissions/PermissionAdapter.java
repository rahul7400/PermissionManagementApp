package in.macrocodes.checkPermissions;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PermissionAdapter extends RecyclerView.Adapter<PermissionAdapter.Viewholder> {
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class Viewholder extends RecyclerView.ViewHolder{

        public Viewholder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
