package com.example.smartstorageapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class AdicionarEncomenda extends AppCompatActivity {

    Button addEncomenda;
    EditText codigoRastreio, nomeEncomenda;
    String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;



    public static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_encomenda);

        codigoRastreio = findViewById(R.id.codigoRastreio);
        nomeEncomenda = findViewById(R.id.nomeEncomenda);
        addEncomenda = findViewById(R.id.addEncomenda);


        fStore = FirebaseFirestore.getInstance();
        fAuth = FirebaseAuth.getInstance();




/*
        // Tentativa de pegar as infos inseridas e salvar dentro do usuário uma coleção para os cod de rastreio
        addEncomenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String codigo = codigoRastreio.getText().toString();

                if(TextUtils.isEmpty(codigo)){
                    codigoRastreio.setError("Insira um código de rastreio válido");
                    return;
                }
                if(codigoRastreio.length() < 11){
                    codigoRastreio.setError("O código de rastrio deve ter no mínimo 11 caracteres");
                    return;
                }


                    userID = fAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = fStore.collection("encomendas").document(userID);
                    Map<String, Object> encomenda = new HashMap<>();
                    encomenda.put("encomenda", nomeEncomenda);
                    encomenda.put("codigo", codigoRastreio);
                    documentReference.set(encomenda).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "on Success: Encomenda adicionada para" + userID);
                        }
                    });




            }


        });
*/


    }

    }
