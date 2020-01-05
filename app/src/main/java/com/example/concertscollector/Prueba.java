package com.example.concertscollector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class Prueba extends AppCompatActivity {

    Button btn_sesion,btn_feed;
    TextView tv_mensaje;
    String nombre, imagenURL;
    CircleImageView imagen;

    private FirebaseAuth auth; //Se pone cada que te vas a meter con autenticacion.
    private FirebaseDatabase database; //Se pone cada que se interactua con la base de datos
    private DatabaseReference reference; //Igual que lo de arriba
    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prueba);
        btn_sesion = findViewById(R.id.btn_sesion);
        tv_mensaje = findViewById(R.id.tv_mensaje);
        btn_feed = findViewById(R.id.btn_feed);
        imagen = findViewById(R.id.imagen);
        database = FirebaseDatabase.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nombre = dataSnapshot.child("nombre").getValue().toString();
                imagenURL = dataSnapshot.child("imageProfile").getValue().toString();
                tv_mensaje.setText("Bienvenido:\n" + nombre);

                if (!imagenURL.equals("default")) {
                    Glide.with(getApplication()).load(imagenURL).into(imagen);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplication(), MainActivity.class);
                startActivity(intent);
            }
        });

        btn_feed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(),Feed.class);
                startActivity(intent);
            }
        });
        //hola Edson
    }
}

//Hola Felipe como estas?
