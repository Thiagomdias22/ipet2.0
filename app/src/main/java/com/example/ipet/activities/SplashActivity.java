package com.example.ipet.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ipet.R;
import com.example.ipet.firebase.UserUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        /*
        * Acessa a quantidade de conexões já realizadas (quando o usuário entra em contato com a ong)
        * que está dentro do documento "counter" da coleção "conexoes", é acessado em específico o
        * atributo quantidade, que é onde esse valor está sendo guardado. Quando houver resposta,
        * abrirá as próximas telas, em específico a tela Main receberá o valor coletado.
        * */
        FirebaseFirestore.getInstance().collection("conexoes")
                .document("counter")
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(final DocumentSnapshot documentSnapshot) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                //Se a ong estiver logada, já inicia na tela de gerenciamento da ong
                                if (UserUtils.getUser() != null) {
                                    startActivity(new Intent(getBaseContext(), ListagemDeCasos.class));
                                    finish();
                                } else { //senão, inicia a tela main
                                    Integer qtdConexoes = documentSnapshot.get("quantidade", Integer.class);
                                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                    intent.putExtra("qtdConexoes", qtdConexoes);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        }, 2000);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getBaseContext(), "Ocorreu um erro :(",
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

}
