package com.example.smartstorageapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MinhasInformacoes extends AppCompatActivity {
    TextView nome, email, celular, cpf, apto, bloco;
    Button inicio;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_informacoes);

        nome = findViewById(R.id.nomeInfo);
        email = findViewById(R.id.emailInfo);
        celular = findViewById(R.id.celularInfo);
        cpf = findViewById(R.id.cpfInfo);
        apto = findViewById(R.id.aptoInfo);
        bloco = findViewById(R.id.blocoInfo);
        inicio = findViewById(R.id.inicioInfo);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = fAuth.getCurrentUser().getUid();

        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


        DocumentReference documentReference = fStore.collection("usuarios").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                nome.setText(documentSnapshot.getString("nome"));
                email.setText(documentSnapshot.getString("email"));
                celular.setText(documentSnapshot.getString("celular"));
                cpf.setText(documentSnapshot.getString("cpf"));
                apto.setText(documentSnapshot.getString("apto"));
                bloco.setText(documentSnapshot.getString("bloco"));
            }
        });


    }
}