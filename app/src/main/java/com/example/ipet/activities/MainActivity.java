package com.example.ipet.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.ipet.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getQtdConxoes();
    }

    /*
     * Método do onClick do Botão Sou Uma Ong
     * Serve para chamar a activity que irá credenciar uma Ong
     * */
    public void openSouUmaOng(View view){
        Intent intent = new Intent(this, SouUmaOngActivity.class);
        startActivity(intent);
    }

    /*
     * Método do onClick do Botão Quero Ajudar Uma Ong
     * Serve para chamar a activity que irá listar os casos de todas ongs
     * */
    public void openQueroAjudarOng(View view){
        Intent intent = new Intent(this, QueroAjudarOng.class);
        startActivity(intent);
    }

    /*
     * Acessa a quantidade de conexões já realizadas (quando o usuário entra em contato com a ong)
     * que está dentro do documento "counter" da coleção "conexoes", é acessado em específico o
     * atributo quantidade, que é onde esse valor está sendo guardado. Quando houver resposta,
     * setará na textview de conexões desta tela.
     * */
    public void getQtdConxoes(){

        FirebaseFirestore.getInstance().collection("conexoes")
                .document("counter")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(final DocumentSnapshot documentSnapshot) {
                        Integer qtdConexoes = documentSnapshot.get("quantidade", Integer.class);
                        setTextQtdConexoes(qtdConexoes);
                    }
                });
    }

    /*
    * Recebe a quantidade de conexões e atualiza o texto da textview de informação sobre conexões.
    * */
    public void setTextQtdConexoes(Integer qtd){

        TextView tvApresentacao = findViewById(R.id.tvApresentacao);

        String msg = "Total de " + qtd + " conexões já realizadas";

        tvApresentacao.setText(msg);
    }
}