package com.example.fetch_rewards_task;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

/**
 * ViewDataActivity handles displaying data entities in a RecyclerView.
 * The data is received via intent as a JSON array, parsed, and displayed.
 */
public class ViewDataActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_activity);
        // Setup RecyclerView
        RecyclerView recyclerView = findViewById(R.id.list_recycler_view);
        recyclerView.setHasFixedSize(true);
        // Retrieve the JSON array passed from the previous activity
        Intent intent = getIntent();
        String jsonArrayString = intent.getStringExtra("data");

        try {
            ArrayList<DataEntity> entities = new ArrayList<>();
            JSONArray jsonArray = new JSONArray(jsonArrayString);

            // Parse each JSON object in the array and convert to DataEntity objects
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                DataEntity entity = new DataEntity(json.getString("id"), json.getString("name"));
                entities.add(entity);
            }
            // Set up the RecyclerView with the data
            ViewDataAdapter adapter = new ViewDataAdapter(this, entities);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
