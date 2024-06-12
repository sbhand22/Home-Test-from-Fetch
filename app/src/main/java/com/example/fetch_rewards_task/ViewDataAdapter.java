package com.example.fetch_rewards_task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Adapter for displaying data entities in a RecyclerView.
 * This adapter manages a list of DataEntity objects and binds them to the RecyclerView's rows.
 */
public class ViewDataAdapter extends RecyclerView.Adapter<ViewDataAdapter.ViewHolder> {

    private Context context;
    private ArrayList<DataEntity> idEntities;

    /**
     * Constructor for ViewDataAdapter.
     * @param context The current context.
     * @param idEntities The data entities to be displayed.
     */
    public ViewDataAdapter(Context context, ArrayList<DataEntity> idEntities) {
        this.context = context;
        this.idEntities = idEntities;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.row_list_ids, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Bind data to the views
        holder.id.setText(idEntities.get(position).getId());
        holder.name.setText(idEntities.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return idEntities.size();
    }

    /**
     * ViewHolder class for RecyclerView.
     * This class holds the views that will contain the data on the RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id, name;

        /**
         * Constructor for ViewHolder.
         * @param itemView The current item view.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.data_id);
            name = itemView.findViewById(R.id.data_name);
        }
    }
}
