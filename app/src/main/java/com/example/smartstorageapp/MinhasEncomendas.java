package com.example.smartstorageapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class MinhasEncomendas extends AppCompatActivity {

    Button inicio;
    Button btn[] = new Button[5];
    TextView box[] = new TextView[5];

    String[] Posicoes = new String[5]; //Vetor de strings para vc colocar na tela
    int[] intPosicoes = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_minhas_encomendas);
        inicio = findViewById(R.id.inicioInfo);
        btn[0] = findViewById(R.id.btnBox1);
        btn[1] = findViewById(R.id.btnBox2);
        btn[2] = findViewById(R.id.btnBox3);
        btn[3] = findViewById(R.id.btnBox4);
        btn[4] = findViewById(R.id.btnBox5);
        box[0] = findViewById(R.id.box1);
        box[1] = findViewById(R.id.box2);
        box[2] = findViewById(R.id.box3);
        box[3] = findViewById(R.id.box4);
        box[4] = findViewById(R.id.box5);
        pegarEncomendas();


        btn[0].setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                retirarEncomendas(intPosicoes[0]);
            }
        });

        btn[1].setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                retirarEncomendas(intPosicoes[1]);
            }
        });

        btn[2].setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                retirarEncomendas(intPosicoes[2]);
            }
        });

        btn[3].setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                retirarEncomendas(intPosicoes[3]);
            }
        });


        btn[4].setOnClickListener(new Button.OnClickListener()
        {
            public void onClick(View v)
            {
                retirarEncomendas(intPosicoes[4]);
            }
        });


        inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


    }



    private void pegarEncomendas() {
        /** Implementação da conexão com o servidor usando a biblioteca Volley **/
        try {
            for (int i=0; i<4; i++) {
                box[i].setVisibility(View.INVISIBLE);
                btn[i].setVisibility(View.INVISIBLE);

            }
            RequestQueue requestQueue = Volley.newRequestQueue(this); //Criar nova  fila de requisições
            String URL = "http://10.0.2.2:5000/compras"; //URL do servidor - ver qual rota Carlos vai dar pra gente

            /**Criação do JSON que será enviado na requisição**/
            JSONObject jsonBody;

            if(Login.getUserID() != null){
                jsonBody = new JSONObject();
                jsonBody.put("nome", Login.getUserID()); //vai precisar ser o nome do usuário
            } else if (Cadastro.getmNome() != null) {
                jsonBody = new JSONObject();
                jsonBody.put("nome", Cadastro.getmNome()); //vai precisar ser o nome do usuário
            } else{
                jsonBody = new JSONObject();
                jsonBody.put("nome", MinhasInformacoes.getNome()); //vai precisar ser o nome do usuário
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
                        String posicao = response.getString("Slots");
                        if (encomendas == 0){
                            box[0].setText("Nenhuma encomenda a sua espera :)");
                            box[0].setVisibility(View.VISIBLE);


                        } else if(posicao != null) {
                            posicao = posicao.substring(1, posicao.lastIndexOf("]"));
                            Posicoes = posicao.split(",");

                            int i = 0;
                            if (Posicoes[0] != "") {
                                for (String str : Posicoes) {
                                    if (str == "") {
                                        break;
                                    }
                                    intPosicoes[i] = Integer.parseInt(str);
                                    box[i].setText(str);
                                    box[i].setVisibility(View.VISIBLE);
                                    btn[i].setVisibility(View.VISIBLE);
                                    i++;
                                    if (i >= 4) {
                                        break;
                                    }
                                }
                            }
                        }

                        System.out.println(encomendas);
                        System.out.println(posicao);
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

    private void retirarEncomendas(Integer id) {
        /** Implementação da conexão com o servidor usando a biblioteca Volley **/
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this); //Criar nova  fila de requisições
            String URL = "http://10.0.2.2:5000/excluir"; //URL do servidor - ver qual rota Carlos vai dar pra gente

            /**Criação do JSON que será enviado na requisição**/
            JSONObject jsonBody;

                jsonBody = new JSONObject();
                jsonBody.put("id", id); //vai precisar ser o nome do usuário

            /**Criaçao e definição do tipo de Request*/
            JsonObjectRequest myRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonBody, new Response.Listener<JSONObject>() {

                /**
                 * Definição dos handlers para resposta e erro
                 **/
                @Override
                public void onResponse(JSONObject response) {

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
            pegarEncomendas();

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

}


