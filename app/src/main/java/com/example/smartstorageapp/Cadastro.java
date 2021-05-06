package com.example.smartstorageapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Cadastro extends AppCompatActivity {

    Button finalizaCadastro, jaPossui;
    EditText mSenha, mConfSenha, mNome, mEmail, mCelular, mCPF, mApto, mBloco;
    String userID;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    ProgressBar progressBar;

    public static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        mNome = findViewById(R.id.nome);
        mEmail = findViewById(R.id.email);
        mSenha = findViewById(R.id.senha);
        mConfSenha = findViewById(R.id.confSenha);
        mCelular = findViewById(R.id.celular);
        mCPF = findViewById(R.id.cpf);
        mApto = findViewById(R.id.apto);
        mBloco = findViewById(R.id.bloco);
        finalizaCadastro = findViewById(R.id.finalCadastro);
        jaPossui = findViewById(R.id.btnJapossui);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);



        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        jaPossui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Login.class));
            }
        });

        finalizaCadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = mEmail.getText().toString().trim();
                String senha = mSenha.getText().toString().trim();
                String nome = mNome.getText().toString();
                String celular = mCelular.getText().toString();
                String cpf = mCPF.getText().toString();
                String apto = mApto.getText().toString();
                String bloco = mBloco.getText().toString();

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
                if(TextUtils.isEmpty(nome)){
                    mNome.setError("Informe um Nome.");
                    return;
                }
                if(TextUtils.isEmpty(celular)){
                    mCelular.setError("Informe o celular corretamente.");
                    return;
                }
                if(celular.length() < 12){
                    mCelular.setError("Insira o celular no formato: (DDD)X XXXX-XXXX");
                    return;
                }
                if(TextUtils.isEmpty(cpf)){
                    mCPF.setError("Informe o CPF.");
                    return;
                }
                if(cpf.length() < 11){
                    mCPF.setError("Insira apenas os números do CPF");
                    return;
                }
                if(TextUtils.isEmpty(apto)){
                    mApto.setError("Insira o número do apartamento.");
                    return;
                }
                if(TextUtils.isEmpty(bloco)){
                    mBloco.setError("Informe o bloco.");
                }

                progressBar.setVisibility(View.VISIBLE);

                if(fAuth.getCurrentUser() != null ){
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    finish();
                }

                // Registra o usuário no Firebase

                fAuth.createUserWithEmailAndPassword(email,senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Cadastro.this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("usuarios").document(userID);
                            Map<String,Object> usuario = new HashMap<>();
                            usuario.put("nome", nome);
                            usuario.put("email", email);
                            usuario.put("celular", celular);
                            usuario.put("cpf", cpf);
                            usuario.put("apto", apto);
                            usuario.put("bloco", bloco);
                            documentReference.set(usuario).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "on Success: Perfil de usuário criado para" + userID);
                                    sendToServer();
                                }
                            });
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        }else{
                            Toast.makeText(Cadastro.this, "Erro! Tente novamente." + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });



            }
        });



    }

    public void sendToServer(){
        /** Implementação da conexão com o servidor usando a biblioteca Volley **/
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this); //Criar nova  fila de requisições
            String URL = "http://10.0.2.2:5000/dados"; //URL do servidor

            /**Criação do JSON que será enviado na requisição**/
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("nome", mNome.getText().toString());
            jsonBody.put("endereco", "Rua tal");
            jsonBody.put("bloco", mBloco.getText().toString());
            jsonBody.put("ap", mApto.getText().toString());
            //final String requestBody = jsonBody.toString();

            /**Criaçao e definição do tipo de Request*/
            JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {
                /**
                 * Definição dos handlers para resposta e erro
                 **/
                @Override
                public void onResponse(JSONObject response) {

                    //for (int i = 0; i < jsonArray.length(); i++) {
                    //JSONObject user = jsonArray.getJSONObject(i);
                    System.out.println(response.toString());

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                /**
                 * Definição do tipo de resposta
                 **/
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
            };
            /**Adicionar a requisição na fila do Volley**/

            myRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

            requestQueue.add(myRequest);

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}