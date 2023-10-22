package com.cs407.lab5_milestone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences sharedPreferences =
                getSharedPreferences ("com.cs407.lab5_milestone", Context.MODE_PRIVATE);

        if (sharedPreferences.getString ( "username", "") != "") {

            // Implies that the username exists in SharedPreferences. The user did not log out when closing
            // the application
            // Get the value of the logged in username from SharedPreferences using the
            // sharedPreferences.getString(username,"").
            // Using Intents start the second screen, Figure 2 activity
            goToActivity( sharedPreferences.getString("username", "") );


        }else {

            setContentView(R.layout.activity_main);
        }
        Intent intent= getIntent();

    }

    public void login(View view){
        EditText usernameText = (EditText) findViewById(R.id.Username);

        SharedPreferences sharedPreferences = getSharedPreferences ("com.cs407.lab5_milestone", MODE_PRIVATE);
        sharedPreferences.edit() .putString("username", usernameText.getText().toString()).apply();

        goToActivity(usernameText.getText().toString());
    }

    public void goToActivity(String s){
        Intent intent = new Intent (this, notesPage.class);
        intent.putExtra("valid", s);
        startActivity(intent);
    }


}