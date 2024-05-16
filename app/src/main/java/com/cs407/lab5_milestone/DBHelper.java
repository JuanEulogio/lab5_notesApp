package com.cs407.lab5_milestone;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class DBHelper {
    static SQLiteDatabase sqLiteDatabase;
    public DBHelper(SQLiteDatabase sqLiteDatabase) { this.sqLiteDatabase = sqLiteDatabase; }

    public static void createTable() {
        //note how we make our table.
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS notes " +
                "(id INTEGER PRIMARY KEY, noteId INTEGER, username TEXT, date TEXT, content TEXT, title TEXT)");
    }


    public ArrayList<Notes> readNotes (String username) {
        //createTable
        createTable();

        //make a cursor
        //Cursor just holds our data so we can read from it

        Cursor c = sqLiteDatabase.rawQuery("SELECT * FROM notes WHERE username LIKE ?",
                new String[]{"%" + username + "%"});
        int dateIndex = c.getColumnIndex("date");
        int titleIndex = c.getColumnIndex("title");
        int contentIndex = c.getColumnIndex("content");

        //moveToFirst: Move the cursor to the first row.
        c.moveToFirst();
        ArrayList<Notes> notesList = new ArrayList<>();

        //isAfterLast: Returns whether the cursor is pointing to the position after the last row.
        while (!c.isAfterLast()) {
            //Returns the value of the requested column as a String.
            String title = c.getString(titleIndex);
            String date = c.getString(dateIndex);
            String content = c.getString(contentIndex);
            Notes notes = new Notes(date, username, title, content);
            notesList.add(notes);
            //moveToNext row
            c.moveToNext();
        }

        c.close();
        sqLiteDatabase.close();
        return notesList;
    }

    public void saveNotes (String username, String title, String date, String content) {
        //create table
        createTable();

        //INSERT INTO
        //makes a note that we will insert with the given info, then do ??? values, and to store it into a new row???
        sqLiteDatabase. execSQL( "INSERT INTO notes(username, date, title, content) VALUES (?, ?, ?, ?)",
                new String[]{username, date, title, content});
    }

    public void updateNotes (String content, String date, String title, String username) {
        //Note, create table
        createTable();

        Notes note = new Notes(date, username, title, content);
        sqLiteDatabase.execSQL("UPDATE notes set content=?, date=? where title=? and username=?",
                new String[]{content, date, title, username});
    }




    public void deleteNotes (String content, String title) {
        //make a table
        createTable();
        String date = "";

        //we make a cursor
        //Where= our content which is "?"
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT date FROM notes WHERE content =?",
                new String[]{content});
        if (cursor.moveToNext()) {
            //getString(0) gets the info of the first row
            date = cursor.getString(0);
        }

        //
        sqLiteDatabase.execSQL("DELETE FROM notes WHERE content = ? AND date = ?",
                new String[]{content, date});

        cursor.close();
    }
}