package com.example.concertscollector;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Registro extends AppCompatActivity {

    EditText et_email, et_password1, et_password2, et_nombre, et_apellidos, et_username, et_telefono;
    Button btn_terminar;
    String email, password1, password2, nombre, apellidos, username, telefono, imageProfile;
    ProgressDialog progressDialog;
    CircleImageView imagen;

    private FirebaseAuth auth; //Se pone cada que te vas a meter con autenticacion.
    private FirebaseDatabase database; //Se pone cada que se interactua con la base de datos
    private DatabaseReference reference; //Igual que lo de arriba
    private FirebaseUser firebaseUser;
    private StorageReference storageReference;
    private StorageTask<UploadTask.TaskSnapshot> uploadTask;
    Uri uri;

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
        et_username = findViewById(R.id.et_username);
        et_telefono = findViewById(R.id.et_telefono);
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
            uri = data.getData();
            imagen.setImageURI(uri);
        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getApplication().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImage(){
        storageReference = FirebaseStorage.getInstance().getReference();
        final StorageReference storage = storageReference.child("usuarios/"+firebaseUser.getUid()+"/perfil.jpg");
        if (uri != null){
            uploadTask = storage.putFile(uri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return storage.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        imageProfile = downloadUri.toString();
                        String userid = firebaseUser.getUid();
                        reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(userid);
                        storageReference = FirebaseStorage.getInstance().getReference(userid);

                        HashMap<String, String> hashMap = new HashMap<>();
                        hashMap.put("id", userid);
                        hashMap.put("nombre", nombre);
                        hashMap.put("apellidos", apellidos);
                        hashMap.put("correo", email);
                        hashMap.put("username", username);
                        hashMap.put("telefono", telefono);
                        hashMap.put("imageProfile", imageProfile);

                        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    Intent intent = new Intent(Registro.this, Prueba.class);
                                    progressDialog.dismiss();
                                    Toast.makeText(Registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                                    startActivity(intent);
                                } else{
                                    Toast.makeText(Registro.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else{

                    }
                }
            });

        } else {
            imageProfile = "default";

            String userid = firebaseUser.getUid();
            reference = FirebaseDatabase.getInstance().getReference("Usuarios").child(userid);
            storageReference = FirebaseStorage.getInstance().getReference(userid);

            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("id", userid);
            hashMap.put("nombre", nombre);
            hashMap.put("apellidos", apellidos);
            hashMap.put("correo", email);
            hashMap.put("username", username);
            hashMap.put("telefono", telefono);
            hashMap.put("imageProfile", imageProfile);

            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        Intent intent = new Intent(Registro.this, Prueba.class);
                        progressDialog.dismiss();
                        Toast.makeText(Registro.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    } else{
                        Toast.makeText(Registro.this, "Ha ocurrido un error", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void terminarRegistro() {
        nombre = et_nombre.getText().toString().trim();
        apellidos = et_apellidos.getText().toString().trim();
        username = et_username.getText().toString().trim();
        telefono = et_telefono.getText().toString().trim();
        email = et_email.getText().toString().trim();
        password1 = et_password1.getText().toString().trim();
        password2 = et_password2.getText().toString().trim();

        if (!email.equals("") && !password1.equals("") && !password2.equals("") && !nombre.equals("")
        && !apellidos.equals("") && !username.equals("") && !telefono.equals("")) {
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
                            uploadImage();


                        } else {
                            if (task.getException() instanceof FirebaseAuthUserCollisionException){ //Si se presenta una colision
                                Toast.makeText(Registro.this, "Este correo ya ha sido registrado", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(Registro.this, "Fallo", Toast.LENGTH_SHORT).show();
                            }
                        }

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
