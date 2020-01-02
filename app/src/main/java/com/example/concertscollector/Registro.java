package com.example.concertscollector;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

public class Registro extends AppCompatActivity {

    EditText et_email, et_password1, et_password2;
    Button btn_terminar;
    String email, password1, password2;
    ProgressDialog progressDialog;

    private FirebaseAuth auth; //Se pone cada que te vas a meter con autenticacion.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //----------------Relaciones---------------------------
        et_email = findViewById(R.id.et_email);
        et_password1 = findViewById(R.id.et_password1);
        et_password2 = findViewById(R.id.et_password2);
        btn_terminar = findViewById(R.id.btn_terminar);
        progressDialog = new ProgressDialog(this);
        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance();
        //-----------------------------------------------------

        btn_terminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                terminarRegistro();
            }
        });


    }

    private void terminarRegistro() {
        email = et_email.getText().toString().trim();
        password1 = et_password1.getText().toString().trim();
        password2 = et_password2.getText().toString().trim();

        if (!email.equals("") && !password1.equals("") && !password2.equals("")) {
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
                            Toast.makeText(Registro.this, "Exito", Toast.LENGTH_SHORT).show();
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
