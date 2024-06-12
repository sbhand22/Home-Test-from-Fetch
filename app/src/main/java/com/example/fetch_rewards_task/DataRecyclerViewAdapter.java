package com.example.fetch_rewards_task;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

/**
 * Adapter for a RecyclerView that displays a list of IDs each with a button.
 * When the button is clicked, a callback is triggered via OnIDRecyclerViewClickListener.
 */
public class DataRecyclerViewAdapter extends RecyclerView.Adapter<DataRecyclerViewAdapter.ViewHolder> {
    private ArrayList<String> listIDs;
    private Context context;
    private OnIDRecyclerViewClickListener onIDRecyclerViewClickListener;

    /**
     * Constructor for DataRecyclerViewAdapter.
     *
     * @param context The current context.
     * @param listIDs The list of IDs to display.
     * @param onIDRecyclerViewClickListener Listener for click events on the IDs.
     */
    public DataRecyclerViewAdapter(Context context, ArrayList<String> listIDs, OnIDRecyclerViewClickListener onIDRecyclerViewClickListener) {
        this.context = context;
        this.listIDs = listIDs;
        this.onIDRecyclerViewClickListener = onIDRecyclerViewClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.id_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(listIDs.get(position));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onIDRecyclerViewClickListener != null) {
                    onIDRecyclerViewClickListener.OnClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listIDs.size();
    }

    /**
     * ViewHolder for items in the RecyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        Button button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.list_id_name);
            button = itemView.findViewById(R.id.viewIDs);
        }
    }

    /**
     * Interface for handling click events on items in the RecyclerView.
     */
    public interface OnIDRecyclerViewClickListener {
        void OnClick(int position);
    }
}
