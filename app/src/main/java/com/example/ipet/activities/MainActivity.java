package com.example.ipet.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.example.ipet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getQtdConexoes();
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
    public void getQtdConexoes(){
        FirebaseFirestore.getInstance()
                .collection("conexoes")
                .document("counter")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            Integer qtd = document.get("quantidade", Integer.class);
                            if (document.exists()) {
                                Log.d("oi","DocumentSnapshot data: " + document.getData());
                                setTextQtdConexoes(qtd);
                            } else {
                                Log.d("oi", "No such document");
                                setTextQtdConexoes(0);
                            }
                        } else {
                            Log.d("oi", "get failed with ", task.getException());
                        }
                    }
                });
    }

    /*
     * Ciclo de Vida para atualizar as informações de conexões quando o usuario voltar
     * a tela principal
     * */
    @Override
    protected void onResume() {
        super.onResume();
        getQtdConexoes();
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