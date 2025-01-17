package com.yuvdroid.studentdatabase;



import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextName, editTextId;
    private Button buttonAdd, buttonUpdate, buttonDelete, buttonRead;
    private ListView listViewStudents;
    private DatabaseHelper databaseHelper;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> studentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        editTextId = findViewById(R.id.editTextId);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonRead = findViewById(R.id.buttonRead);
        listViewStudents = findViewById(R.id.listViewStudents);

        databaseHelper = new DatabaseHelper(this);
        studentList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, studentList);
        listViewStudents.setAdapter(adapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStudent();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteStudent();
            }
        });

        buttonRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readStudents();
            }
        });

        readStudents(); // Load students when the app starts
    }

    private void addStudent() {
        String name = editTextName.getText().toString();
        if (!name.isEmpty()) {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name);
            db.insert("students", null, values);
            db.close();
            Toast.makeText(this, "Student Added", Toast.LENGTH_SHORT).show();
            editTextName.setText(""); // Clear input field
            editTextId.setText("");
            readStudents(); // Refresh the list
        } else {
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateStudent() {
        String id = editTextId.getText().toString();
        String name = editTextName.getText().toString();
        if (!id.isEmpty() && !name.isEmpty()) {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("name", name);
            int rowsAffected = db.update("students", values, "id=?", new String[]{id});
            db.close();
            if (rowsAffected > 0) {
                Toast.makeText(this, "Student Updated", Toast.LENGTH_SHORT).show();
                editTextName.setText("");
                editTextId.setText("");
                readStudents(); // Refresh the list
            } else {
                Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter both ID and name", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteStudent() {
        String id = editTextId.getText().toString();
        if (!id.isEmpty()) {
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("isDeleted", 1); // Mark as deleted
            int rowsUpdated = db.update("students", values, "id=?", new String[]{id});
            if (rowsUpdated > 0) {
                Toast.makeText(this, "Student Deleted", Toast.LENGTH_SHORT).show();
                editTextId.setText("");
                readStudents(); // Refresh the list
            } else {
                Toast.makeText(this, "Student not found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please enter an ID", Toast.LENGTH_SHORT).show();
        }
    }

    private void readStudents() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM students WHERE isDeleted = 0", null);
        studentList.clear(); // Clear the current list
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            studentList.add("ID: " + id + ", Name: " + name);
        }
        cursor.close();
        adapter.notifyDataSetChanged(); // Notify the adapter to refresh the ListView
    }
}
