package com.techexchange.mobileapps.lab8;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String SHARED_PREFS_FILE_NAME = "Contacts_SharedPrefs";

    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button saveButton = findViewById(R.id.save_button);
        saveButton.setOnClickListener(v -> onSaveButtonPressed());

        Button loadButton = findViewById(R.id.load_button);
        loadButton.setOnClickListener(v -> onLoadButtonPressed());

        database = new ContactSqliteHelper(this).getWritableDatabase();
    }

    private void onSaveButtonPressed(){

        EditText nameEditText = findViewById(R.id.name_edit_text);
        EditText numberEditText= findViewById(R.id.number_edit_text);

        String nameKey = nameEditText.getText().toString();
        String numberText = numberEditText.getText().toString();

        if (nameKey.equals("") || numberText.equals("")){
            Toast.makeText(this, "Invalid entry", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues cv = new ContentValues();
        cv.put(ContactDbSchema.Cols.NAME, nameKey);
        cv.put(ContactDbSchema.Cols.NUMBER, numberText);

        try (Cursor cursor = database.query(
                ContactDbSchema.TABLE_NAME,
                null,
                ContactDbSchema.Cols.NAME + " = ?",
                new String[]{nameKey},
                null, null, null)){
            //Make the cursor point to the first row in the result set.
            cursor.moveToFirst();
            if (cursor.isAfterLast()){
                // No records exist. Need to insert a new one.
                // insert
                database.insert(ContactDbSchema.TABLE_NAME, null, cv);
                Toast.makeText(this, "NEW CONTACT "+nameKey+": "+numberText, Toast.LENGTH_SHORT).show();
            } else {
                // A record already exists. Need to update it with a new number.
                // update
                database.update(ContactDbSchema.TABLE_NAME, cv, ContactDbSchema.Cols.NAME+" = ?", new String[]{nameKey});
                Toast.makeText(this, "CONTACT "+nameKey+" UPDATED TO: "+numberText, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void onLoadButtonPressed(){

        EditText nameEditText = findViewById(R.id.name_edit_text);
        EditText numberEditText = findViewById(R.id.number_edit_text);

        String nameKey = nameEditText.getText().toString();

        if (nameKey.equals("")){
            Toast.makeText(this, "Name field cannot be blank when trying to load a contact", Toast.LENGTH_SHORT).show();
            return;
        }

        try (Cursor cursor = database.query(ContactDbSchema.TABLE_NAME,
                null,
                ContactDbSchema.Cols.NAME + " =?",
                new String[]{nameKey},
                null, null, null)) {
            cursor.moveToFirst();
            if (cursor.isAfterLast()) {
                // The contact was not found.
                Toast.makeText(this, "Contact was not found", Toast.LENGTH_SHORT).show();
                return;
            }
            int colIndex = cursor.getColumnIndex(ContactDbSchema.Cols.NUMBER);
            if (colIndex < 0) {
                // The column was not found in the cursor.
                Toast.makeText(this, "Column was not found", Toast.LENGTH_SHORT).show();
                return;
            }
            String numberStr = cursor.getString(colIndex);
            numberEditText.setText(numberStr);
        }
    }
}
