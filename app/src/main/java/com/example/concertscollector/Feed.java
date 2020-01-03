package com.example.concertscollector;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

public class Feed extends AppCompatActivity {

    FloatingActionButton btn_messages,btn_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        //Relaciones------------------------
        btn_messages = findViewById(R.id.btn_messages);
        btn_search = findViewById(R.id.btn_search);

        btn_messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Ir a mensajes", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Ir a búsqueda", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
