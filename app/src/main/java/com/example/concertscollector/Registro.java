package com.example.concertscollector;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registro extends AppCompatActivity {

    EditText et_email, et_password1, et_password2, et_nombre, et_apellidos;
    Button btn_terminar;
    String email, password1, password2, nombre, apellidos;
    ProgressDialog progressDialog;
    CircleImageView imagen;

    private FirebaseAuth auth; //Se pone cada que te vas a meter con autenticacion.
    private FirebaseDatabase database; //Se pone cada que se interactua con la base de datos
    private DatabaseReference reference; //Igual que lo de arriba
    private FirebaseUser firebaseUser;

    private static final int GALLERY_INTENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //----------------Relaciones---------------------------
        et_email = findViewById(R.id.et_email);
        et_password1 = findViewById(R.id.et_password1);
        et_password2 = findViewById(R.id.et_password2);
        et_nombre = findViewById(R.id.et_nombre);
        et_apellidos = findViewById(R.id.et_apellido);
        btn_terminar = findViewById(R.id.btn_terminar);
        progressDialog = new ProgressDialog(this);
        imagen = findViewById(R.id.imagen);
        // Initialize Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        //-----------------------------------------------------

        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_INTENT);
            }
        });

        btn_terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terminarRegistro();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            Uri uri = data.getData();
            imagen.setImageURI(uri);
        }
    }

    private void terminarRegistro() {
        nombre = et_nombre.getText().toString().trim();
        apellidos = et_apellidos.getText().toString().trim();
        email = et_email.getText().toString().trim();
        password1 = et_password1.getText().toString().trim();
        password2 = et_password2.getText().toString().trim();

        if (!email.equals("") && !password1.equals("") && !password2.equals("") && !nombre.equals("")
        && !apellidos.equals("")) {
            if (validarPasswords()) {
                progressDialog.setMessage("Realizando registro...");
                progressDialog.show();
                registrar();
            } else {
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "Por favor, llene todos los campos", Toast.LENGTH_LONG).show();
        }


    }

    private void registrar() {
        auth.createUserWithEmailAndPassword(email, password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();
                            reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("nombre", nombre);
                            hashMap.put("apellidos", apellidos);
                            hashMap.put("correo", email);

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(Registro.this, Prueba.class);
                                        Toast.makeText(Registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                        startActivity(intent);
                                    } else{
                                        Toast.makeText(Registro.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException){ //Si se presenta una colision
                                Toast.makeText(Registro.this, "Este correo ya ha sido registrado", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Registro.this, "Fallo", Toast.LENGTH_SHORT).show();
                            }
                        }
                        progressDialog.dismiss();
                    }
                });
    }

    private boolean validarPasswords() {
        if (password1.equals(password2)) {
            return true;
        } else {
            return false;
        }
    }
}
