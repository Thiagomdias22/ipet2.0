package com.example.ipet.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ipet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static android.util.Patterns.EMAIL_ADDRESS;

public class SouUmaOngActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    EditText etEmail, etSenha;
    Button bLogin;
    TextView tvNaoTenhoCadastro, tvEsqueciSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sou_uma_ong);

        etEmail = findViewById(R.id.etEmail);
        etSenha = findViewById(R.id.etSenha);
        bLogin = findViewById(R.id.bLogin);
        tvNaoTenhoCadastro = findViewById(R.id.tvNaoTenhoCadastro);
        tvEsqueciSenha = findViewById(R.id.tvEsqueciSenha);

        mAuth = FirebaseAuth.getInstance();
    }


    /*
    * Habilida/Desabilida as views desta tela por meio da variável op
    * */
    public void enableViews(boolean op){
        etEmail.setEnabled(op);
        etSenha.setEnabled(op);
        bLogin.setEnabled(op);
        tvNaoTenhoCadastro.setEnabled(op);
        tvEsqueciSenha.setEnabled(op);
    }

    /*
    * Método executado dentro do onClick do botão Login
    * Irá extrair as informações dos campos email e senha, repassando para o método realizarLogin
    * */
    public void login(View view){

        String email = etEmail.getText().toString();

        if(!validateEmailFormat(email)){
            toast("Insira um email válido!");
            return;
        }

        String senha = etSenha.getText().toString();

        if(senha.equals("")){
            toast("Insira uma senha!");
            return;
        }

        realizarLogin(email, senha);
    }

    /*
    * Utilizando a autenticação do Firebase, o método recebe email e senha
    * Com os dados ele verificará se as credenciais estão corretas
    * Caso sim, o método listagemDeCasos será chamado para ir na tela de gerenciamento da ong
    * */
    public void realizarLogin(String email, String senha){

        enableViews(false); //desativa as views enquanto aguarda a requisição, para evitar bug

        mAuth.signInWithEmailAndPassword(email, senha)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            listagemDeCasos();
                        } else {
                            enableViews(true); //se deu falha, ativa as views para tentar dnv
                            toast("Credenciais Inválidas!");
                        }
                    }
                });
    }

    /*
    * Caso o usuário esquecer a senha, esse método sera acionado, sendo necessário apenas o valor
    * do input email, caso o email exista no bd, será mandado um email com um link para alterar
    * a mesma.
    * */
    public void esqueciSenha(View view){

        final String emailAtual = etEmail.getText().toString();

        if(!validateEmailFormat(emailAtual)){
            toast("Insira um email válido no campo E-Email para recuperar sua senha!");
            return;
        }

        mAuth.sendPasswordResetEmail(emailAtual)
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        toast("Email não encontrado no nosso sistema!");
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            toast("Email para recuperação de senha enviado para: " + emailAtual);
                        }
                    }
                });
    }

    /*
     * Método chamado assim que as credenciais forem validadas
     * Serve para chamar a activity de listagem de casos passando a informação do email da ong
     * */
    public void listagemDeCasos(){
        Intent intent = new Intent(this, ListagemDeCasos.class);
        startActivity(intent);
    }

    /*
    * Método do onClick do TextView não tenho cadastro
    * Serve para chamar a activity de cadastro
    * */
    public void naoTenhoCadastro(View view){
        Intent intent = new Intent(this, CadastroOng.class);
        startActivity(intent);
    }

    /*
    * Método chamado quando clicado na setinha de voltar do proprio layout, simula o pressionamento
    * do botão de voltar do telefone
    * */
    public void voltar(View view){
        onBackPressed();
    }

    /*
    * Lança um simples toast, criado apenas para agilizar o uso do toast, pois aqui já estará
    * configurado.
    * */
    public void toast(String msg){
        Toast.makeText(getApplicationContext(),
                msg, Toast.LENGTH_LONG).show();
    }

    /*
    * Método utilizando uma lógica pronta para verificar se o email é válido ou não
    * */
    private boolean validateEmailFormat(final String email) {
        return EMAIL_ADDRESS.matcher(email).matches();
    }
}