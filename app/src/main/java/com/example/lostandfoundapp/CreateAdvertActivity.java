package com.example.lostandfoundapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CreateAdvertActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, descriptionEditText, dateEditText, locationEditText;
    private RadioGroup postTypeRadioGroup;
    private Button postButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_advert);

        dbHelper = new DatabaseHelper(this);

        nameEditText = findViewById(R.id.name_edit_text);
        phoneEditText = findViewById(R.id.phone_edit_text);
        descriptionEditText = findViewById(R.id.description_edit_text);
        dateEditText = findViewById(R.id.date_edit_text);
        locationEditText = findViewById(R.id.location_edit_text);

        postTypeRadioGroup = findViewById(R.id.post_type_radio_group);
        postButton = findViewById(R.id.post_button);

        postButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAdvert();
            }
        });
    }

    private void createAdvert() {
        String name = nameEditText.getText().toString().trim();
        String phone = phoneEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String date = dateEditText.getText().toString().trim();
        String location = locationEditText.getText().toString().trim();

        int selectedRadioButtonId = postTypeRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedRadioButton = findViewById(selectedRadioButtonId);
        String postType = selectedRadioButton.getText().toString();

        if (name.isEmpty() || phone.isEmpty() || description.isEmpty() || date.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_NAME, name);
        values.put(DatabaseHelper.COLUMN_PHONE, phone);
        values.put(DatabaseHelper.COLUMN_DESCRIPTION, description);
        values.put(DatabaseHelper.COLUMN_DATE, date);
        values.put(DatabaseHelper.COLUMN_LOCATION, location);
        values.put(DatabaseHelper.COLUMN_POST_TYPE, postType);

        long newRowId = db.insert(DatabaseHelper.TABLE_NAME, null, values);
        db.close();

        if (newRowId != -1) {
            Toast.makeText(this, "Advert created successfully", Toast.LENGTH_SHORT).show();
            clearFields();
        } else {
            Toast.makeText(this, "Error creating advert", Toast.LENGTH_SHORT).show();
        }
    }

    private void clearFields() {
        nameEditText.getText().clear();
        phoneEditText.getText().clear();
        descriptionEditText.getText().clear();
        dateEditText.getText().clear();
        locationEditText.getText().clear();
        postTypeRadioGroup.clearCheck();
    }
}
