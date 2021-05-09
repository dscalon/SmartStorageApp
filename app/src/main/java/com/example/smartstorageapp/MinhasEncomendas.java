package com.example.smartstorageapp;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class MinhasEncomendas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_encomendas);
        pegarEncomendas();
    }

    private void pegarEncomendas() {
        /** Implementação da conexão com o servidor usando a biblioteca Volley **/
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this); //Criar nova  fila de requisições
            String URL = "http://10.0.2.2:5000/compras"; //URL do servidor - ver qual rota Carlos vai dar pra gente

            /**Criação do JSON que será enviado na requisição**/
            JSONObject jsonBody;
            if(Login.getUserID() != null){
                jsonBody = new JSONObject();
                jsonBody.put("nome", Login.getUserID()); //vai precisar ser o nome do usuário
            } else {
                jsonBody = new JSONObject();
                jsonBody.put("nome", Cadastro.getmNome()); //vai precisar ser o nome do usuário
            }



            /**Criaçao e definição do tipo de Request*/
            JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {

                /**
                 * Definição dos handlers para resposta e erro
                 **/
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        System.out.println(response.toString());
                        int encomendas = response.getInt("Quantidade de produtos"); //Descobrir nome do json
                        ///String posicao = response.getString("posicao");
                        System.out.println(encomendas);
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                    }

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


