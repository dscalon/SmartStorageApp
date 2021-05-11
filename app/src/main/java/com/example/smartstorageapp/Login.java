package com.example.smartstorageapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class Login extends AppCompatActivity {

    EditText mEmail, mSenha;
    Button mBtnLogin, mCreateBtn;
    FirebaseAuth fAuth;
    TextView mForgotPassword;
    FirebaseFirestore fStore;
    static String userID;
    static String user;


    public static String getUserID() {
        return user;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmail = findViewById(R.id.email);
        mSenha = findViewById(R.id.senha);
        mBtnLogin = findViewById(R.id.btnLogin);
        fAuth = FirebaseAuth.getInstance();
        mCreateBtn = findViewById(R.id.btnNovaConta);
        mForgotPassword = findViewById(R.id.esqueciSenha);


        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString().trim();
                String senha = mSenha.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Insira um E-mail válido.");
                    return;
                }
                if(TextUtils.isEmpty(senha)){
                    mSenha.setError("Insira uma senha válida.");
                    return;
                }
                if(senha.length() < 6){
                    mSenha.setError("A senha deve conter no mínimo 6 caracteres.");
                    return;
                }


                // Autenticar o usuário

                fAuth.signInWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this, "Login realizado com sucesso!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            fStore = FirebaseFirestore.getInstance();
                            userID = fAuth.getCurrentUser().getUid();
                            getUser();
                        }else{
                            Toast.makeText(Login.this, "Erro! Tente novamente." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


            }
        });



        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Cadastro.class));
            }
        });

        mForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText resetEmail = new EditText(v.getContext());
                AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Criar Nova Senha?");
                passwordResetDialog.setMessage("Insira seu E-mail para receber o link e criar uma nova senha.");
                passwordResetDialog.setView(resetEmail);

                passwordResetDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Extrair o email e enviar o link para nova senha

                        String mail = resetEmail.getText().toString().trim();
                        fAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Login.this, "Link enviado para o E-mail cadastrado!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Login.this, "Erro! O link não pôde ser enviado." + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }
                });

                passwordResetDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Fecha a função
                    }
                });

                passwordResetDialog.create().show();
            }
        });

    }

    private void getUser(){
        user = "";
        DocumentReference documentReference = fStore.collection("usuarios").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                user = documentSnapshot.getString("nome");
            }
        });


    }

}