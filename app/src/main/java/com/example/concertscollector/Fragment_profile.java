package com.example.concertscollector;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import de.hdodenhof.circleimageview.CircleImageView;

public class Fragment_profile extends Fragment {

    View view;
    Button btn_sesion;
    TextView tv_nombre,tv_apellido,tv_usuario;
    String nombre, apellidos, username, imagenURL;
    CircleImageView imagen;

    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private FirebaseUser firebaseUser;

    public Fragment_profile() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_profile,container,false);

        //Referencias----------
        btn_sesion = (Button) view.findViewById(R.id.btn_sesion);
        tv_nombre = view.findViewById(R.id.tv_nombre);
        tv_apellido = view.findViewById(R.id.tv_apellido);
        tv_usuario = view.findViewById(R.id.tv_usuauario);
        imagen = view.findViewById(R.id.imagen);
        database = FirebaseDatabase.getInstance();

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Obtiene datos del usuario----------
                nombre = dataSnapshot.child("nombre").getValue().toString();
                apellidos = dataSnapshot.child("apellidos").getValue().toString();
                username = dataSnapshot.child("username").getValue().toString();
                imagenURL = dataSnapshot.child("imageProfile").getValue().toString();

                //Cambia etiquetas-------------------
                tv_nombre.setText(nombre);
                tv_apellido.setText(apellidos);
                tv_usuario.setText("Username: " + username);

                if (!imagenURL.equals("default")) {
                    Glide.with(getContext()).load(imagenURL).into(imagen);
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
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
