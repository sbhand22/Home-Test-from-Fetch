package com.example.fetch_rewards_task;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HomeActivity extends AppCompatActivity implements DataRecyclerViewAdapter.OnIDRecyclerViewClickListener {
    private RecyclerView itemsRecyclerView;
    private ArrayList<String> uniqueListIDs; // Holds unique list IDs from JSON data
    private ArrayList<JSONArray> itemsGroupedByListID; // Stores arrays of items grouped by their list IDs

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        // Set up the RecyclerView to display items
        itemsRecyclerView = findViewById(R.id.recyclerView);
        itemsRecyclerView.setHasFixedSize(true);

        // URL from which to fetch data
        String dataUrl = "https://fetch-hiring.s3.amazonaws.com/hiring.json";
        downloadAndProcessData(dataUrl);
    }

    // Method to fetch and process data from the given URL
    private void downloadAndProcessData(String dataUrl) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        StringRequest requestData = new StringRequest(Request.Method.GET, dataUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray dataArray = new JSONArray(response);
                            extractAndSortListIDs(dataArray);
                            Collections.sort(uniqueListIDs);
                            JSONArray sortedByNameData = sortItemsByName(dataArray);
                            groupItemsByListID(sortedByNameData);
                            setupRecyclerViewWithItems();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(HomeActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(requestData);
    }

    // Groups items by their 'listId' and stores them in 'itemsGroupedByListID'
    private void groupItemsByListID(JSONArray sortedItems) throws JSONException {
        itemsGroupedByListID = new ArrayList<>();
        for (String listID : uniqueListIDs) {
            JSONArray itemsForListID = new JSONArray();
            for (int i = 0; i < sortedItems.length(); i++) {
                JSONObject item = sortedItems.getJSONObject(i);
                String itemName = item.isNull("name") ? null : item.getString("name");
                String itemID = item.getString("listId");

                // Add item to group if the name is not null or empty
                if (listID.equals(itemID) && itemName != null && !itemName.trim().isEmpty()) {
                    itemsForListID.put(item);
                }
            }
            itemsGroupedByListID.add(itemsForListID);
        }
    }

    // Extracts unique 'listId' from all items and sorts them
    private void extractAndSortListIDs(JSONArray items) throws JSONException {
        uniqueListIDs = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            JSONObject item = items.getJSONObject(i);
            String listID = item.getString("listId");
            if (!uniqueListIDs.contains(listID)) {
                uniqueListIDs.add(listID);
            }
        }
    }

    @Override
    public void OnClick(int position) {
        JSONArray selectedItems = itemsGroupedByListID.get(position);
        Intent displayItemsIntent = new Intent(this, ViewDataActivity.class);
        displayItemsIntent.putExtra("data", selectedItems.toString());
        startActivity(displayItemsIntent);
    }

    // Sorts the JSONArray of items by the 'name' field using an alphanumeric comparison
    private JSONArray sortItemsByName(JSONArray items) throws JSONException {
        List<JSONObject> jsonObjects = new ArrayList<>();
        for (int i = 0; i < items.length(); i++) {
            jsonObjects.add(items.getJSONObject(i));
        }
        Collections.sort(jsonObjects, new Comparator<JSONObject>() {
            @Override
            public int compare(JSONObject first, JSONObject second) {
                return compareItemsAlphanumerically(first, second, "name");
            }
        });
        JSONArray sortedItems = new JSONArray();
        for (JSONObject item : jsonObjects) {
            sortedItems.put(item);
        }
        return sortedItems;
    }

    // Compares two JSONObjects based on alphanumeric sorting of their 'name' fields
    private int compareItemsAlphanumerically(JSONObject first, JSONObject second, String key) {
        try {
            String firstValue = first.getString(key);
            String secondValue = second.getString(key);
            Matcher firstMatcher = PATTERN.matcher(firstValue);
            Matcher secondMatcher = PATTERN.matcher(secondValue);
            return alphanumericComparison(firstMatcher, secondMatcher);
        } catch (JSONException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private static final Pattern PATTERN = Pattern.compile("(\\D*)(\\d*)");

    // Performs alphanumeric comparison based on regex matches
    private int alphanumericComparison(Matcher firstMatcher, Matcher secondMatcher) {
        while (firstMatcher.find() && secondMatcher.find()) {
            int nonDigitCompare = firstMatcher.group(1).compareTo(secondMatcher.group(1));
            if (nonDigitCompare != 0) {
                return nonDigitCompare;
            }
            BigInteger n1 = new BigInteger(firstMatcher.group(2).isEmpty() ? "0" : firstMatcher.group(2));
            BigInteger n2 = new BigInteger(secondMatcher.group(2).isEmpty() ? "0" : secondMatcher.group(2));
            int numberCompare = n1.compareTo(n2);
            if (numberCompare != 0) {
                return numberCompare;
            }
        }
        return firstMatcher.hitEnd() && secondMatcher.hitEnd() ? 0 : firstMatcher.hitEnd() ? -1 : 1;
    }

    // Sets up the RecyclerView with the sorted and grouped items
    private void setupRecyclerViewWithItems() {
        DataRecyclerViewAdapter adapter = new DataRecyclerViewAdapter(this, uniqueListIDs, this);
        itemsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        itemsRecyclerView.setAdapter(adapter);
    }
}
