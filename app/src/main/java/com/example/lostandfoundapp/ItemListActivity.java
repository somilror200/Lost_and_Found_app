package com.example.lostandfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {

    private ListView itemList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        itemList = findViewById(R.id.item_list);
        dbHelper = new DatabaseHelper(this);

        // Fetch data from SQLite and populate the list view
        populateItemList();

        // Set up item click listener
        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, android.view.View view, int position, long id) {
                // Get the item ID from the itemNames ArrayList
                long itemId = dbHelper.getItemId(position);

                // Open ItemDetailsActivity with the ID
                Intent intent = new Intent(ItemListActivity.this, ItemDetailsActivity.class);
                intent.putExtra("ITEM_ID", itemId);
                startActivity(intent);
            }
        });
    }

    private void populateItemList() {
        ArrayList<String> itemNames = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {String.valueOf(DatabaseHelper.COLUMN_ID), DatabaseHelper.COLUMN_NAME, DatabaseHelper.COLUMN_POST_TYPE};
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, projection, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID));
                String itemName = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
                String postType = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_POST_TYPE));
                itemNames.add(postType + ": " + itemName);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemNames);
        itemList.setAdapter(adapter);
    }
}
