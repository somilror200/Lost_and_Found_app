package com.example.lostandfoundapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ItemDetailsActivity extends AppCompatActivity {

    private TextView nameTextView, phoneTextView, descriptionTextView, dateTextView, locationTextView;
    private Button deleteButton;
    private DatabaseHelper dbHelper;
    private long itemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        dbHelper = new DatabaseHelper(this);

        nameTextView = findViewById(R.id.name_text_view);
        phoneTextView = findViewById(R.id.phone_text_view);
        descriptionTextView = findViewById(R.id.description_text_view);
        dateTextView = findViewById(R.id.date_text_view);
        locationTextView = findViewById(R.id.location_text_view);
        deleteButton = findViewById(R.id.delete_button);

        // Get the ID of the item from intent extras
        itemId = getIntent().getLongExtra("ITEM_ID", -1);
        if (itemId == -1) {
            Toast.makeText(this, "Invalid item ID", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Populate item details
        populateItemDetails();

        // Setup delete button click listener
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem();
            }
        });
    }

    private void populateItemDetails() {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String[] projection = {
                DatabaseHelper.COLUMN_NAME,
                DatabaseHelper.COLUMN_PHONE,
                DatabaseHelper.COLUMN_DESCRIPTION,
                DatabaseHelper.COLUMN_DATE,
                DatabaseHelper.COLUMN_LOCATION
        };
        String selection = DatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(itemId)};
        Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, projection, selection, selectionArgs, null, null, null);
        if (cursor.moveToFirst()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PHONE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DATE));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_LOCATION));

            nameTextView.setText(name);
            phoneTextView.setText(phone);
            descriptionTextView.setText(description);
            dateTextView.setText(date);
            locationTextView.setText(location);
        }
        cursor.close();
        db.close();
    }

    private void deleteItem() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String selection = DatabaseHelper.COLUMN_ID + " = ?";
        String[] selectionArgs = {String.valueOf(itemId)};
        int deletedRows = db.delete(DatabaseHelper.TABLE_NAME, selection, selectionArgs);
        db.close();

        if (deletedRows > 0) {
            Toast.makeText(this, "Item deleted successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to delete item", Toast.LENGTH_SHORT).show();
        }
    }
}
