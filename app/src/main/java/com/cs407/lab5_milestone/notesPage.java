package com.cs407.lab5_milestone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class notesPage extends AppCompatActivity {
    private ArrayList<String> displayNotes = new ArrayList<>();
    //we will get our info using our SQL Handler and add it here
    public static ArrayList<Notes> notes1= new ArrayList<>();
    ;
    TextView message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_page);

        Intent intent= getIntent();
        String str= intent.getStringExtra("valid");

        message= (TextView) findViewById(R.id.welcome);
        message.setText("Welcome " + str +" to notes app");


        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase ( "notes", Context. MODE_PRIVATE,null);
        DBHelper dbHelper= new DBHelper(sqLiteDatabase);

        SharedPreferences sharedPreferences =
                getSharedPreferences ("com.cs407.lab5_milestone", Context.MODE_PRIVATE);
        //make sure to update our notes array data
        notes1= dbHelper.readNotes(sharedPreferences.getString("username", ""));


        for (Notes notes: notes1) {
            displayNotes.add (String. format ("Title:%s\nDate:%s\n", notes.getTitle(), notes.getDate ()));
        }
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, displayNotes);
        ListView notesListView = (ListView) findViewById(R.id.notesList);
        notesListView.setAdapter (adapter);
        notesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //This is where and how we assigning note ID
            @Override
            public void onItemClick(AdapterView<?> adapterview, View view, int i, long L) {
                Intent intent = new Intent (getApplicationContext (), NoteWriting.class);
                intent.putExtra ("noteId", i);
                startActivity (intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.notes_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.addNoteItem) {
            Intent intent = new Intent (this, NoteWriting.class);
            startActivity(intent);
            return true;

        } else if (itemId == R.id.logoutItem) {
            Intent intent = new Intent (this, MainActivity.class);
            SharedPreferences sharedPreferences =
                    getSharedPreferences ("com.cs407.lab5_milestone", Context.MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}