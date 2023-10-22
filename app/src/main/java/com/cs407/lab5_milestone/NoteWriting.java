package com.cs407.lab5_milestone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteWriting extends AppCompatActivity {

    //-1 bc its the default val. so when we go to this activity via menu/newNote, it auto knows
    //we will be doing a new blank note entry
    private int noteId = -1;

    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_note_writing);

        // 1. Get EditText view
        editText= (EditText) findViewById(R.id.noteEditBox);
        // 2. Get intent (coming here from screen 2)

        Intent intent= getIntent();

        // 3. Get the value of integer "noteid" from intent.

        //String str= intent.getStringExtra("noteId");
        int id= intent.getIntExtra("noteId", -1);


        // 4. Initialize class variable noteid with the value from intent
        noteId= id;

        if (noteId != -1){

            // Displaying content of the note by retrieving from ArrayList in SecondActivity
            Notes notes = notesPage.notes1.get(noteId);

            String noteContent = notes.getContent ();
            // 5. Use editText.setText) to display the contents of this note on the screen.

            editText.setText(noteContent);

        }
    }

    public void save(View view){

        // 1. Get editText view and the content that user entered
        String editedText= editText.getText().toString();


        // 2. Initialize SOLiteDatabase instance.
        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase ( "notes", Context. MODE_PRIVATE,null);

        // 3. Initialize DBHelper class.
        DBHelper dbHelper= new DBHelper(sqLiteDatabase);

        // 4. Set username in the following variable by fetching it from SharedPreferences
        SharedPreferences sharedPreferences =
                getSharedPreferences ("com.cs407.lab5_milestone", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        // Save data in Database
        String title;
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss", Locale.US);

        String date = dateFormat.format(new Date());
        Log.i("Info", "Printing noteId before using in condition"+noteId);
        if (noteId == -1) {
            //notes: heres how our note ID get marked at first
            title = "NOTES_"+(notesPage.notes1.size ()+1);
            Log. i ( "info", "printing content to be saved" + "replace with editTexts");

            dbHelper.saveNotes (username, title, date,editedText);

        }else {
            Log. i ("Info","Printing notes id from update condition "+noteId);
            title = "NOTES_"+(noteId+1);
            dbHelper. updateNotes (editedText, date, title, username);
        }

        //return
        Intent intent = new Intent (this, notesPage.class);
        intent.putExtra("valid", sharedPreferences.getString("username", ""));
        startActivity(intent);
    }

    public void delete(View view){
        // 1. Initialize the sql database.
        Context context = getApplicationContext();
        SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase ( "notes", Context. MODE_PRIVATE,null);

        DBHelper dbHelper= new DBHelper(sqLiteDatabase);

        // 2. Initialize shared Prefernces to get the logged in username
        SharedPreferences sharedPreferences =
                getSharedPreferences ("com.cs407.lab5_milestone", Context.MODE_PRIVATE);


        //TODO: what does step 3 mean "newContent"? put in my editText current text?

        // 3. Get the content of the notes using getText()
        String title = "NOTES_"+ (noteId+1);
        dbHelper.deleteNotes(editText.getText().toString(), title);


        Log.i("Info", "after delete: "+dbHelper.readNotes(sharedPreferences.getString("username", "")).toString());

        //return
        Intent intent = new Intent (this, notesPage.class);
        intent.putExtra("valid", sharedPreferences.getString("username", ""));
        startActivity(intent);
    }

}