package com.example.ipet.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ipet.R;
import com.example.ipet.entities.DadosFiltro;
import com.example.ipet.utils.GeralUtils;
import com.example.ipet.utils.SpinnerUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Filter extends AppCompatActivity {

    public interface DataFilter {
        void executeFilter();
    }

    ImageView ivEspCao, ivEspGato, ivEspCoelho;
    EditText etValorMin, etValorMax;
    Spinner spFilterUf, spFilterCidade;
    Button bCadastrar;
    TextView tvSeparar;
    HashMap<Integer, Boolean> especiesEscolhidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        initViews();
        initValues();

        setarInformacoes();
    }

    public int heightTela(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return (int) displayMetrics.heightPixels;
    }

    public void setarInformacoes() {
        if(heightTela() < 1400){
            setMargins(bCadastrar,0, -30, 0, 0);
            setMargins(etValorMax, 285, 0, 0, 0);
            setMargins(etValorMin, 0, 0, 285, 0);
            setMargins(spFilterUf, 45, 0, 10, 10);
            setMargins(spFilterCidade, 10, 4, 5, 10);
        }
    }

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }

    /*
    * Inicializa as views desta activity
    * */
    public void initViews(){
        ivEspCao = findViewById(R.id.ivEspCao);
        ivEspGato = findViewById(R.id.ivEspGato);
        ivEspCoelho = findViewById(R.id.ivEspCoelho);

        etValorMin = findViewById(R.id.etValorMin);
        etValorMax = findViewById(R.id.etValorMax);

        spFilterUf = findViewById(R.id.spFilterUf);
        spFilterCidade = findViewById(R.id.spFilterCidade);

        bCadastrar = findViewById(R.id.bCadastrar);
        tvSeparar = findViewById(R.id.tvSeparacao);
    }

    /*
     * Insere valores nas variaveis
     * */
    public void initValues(){

        DadosFiltro dados = QueroAjudarOng.dadosDoFiltro;

        //Inicia os spinners com valores de ufs, cidades.
        SpinnerUtils.confSpinnersUfCity(getApplicationContext(),
                spFilterUf, "UF",
                spFilterCidade, "Cidade",
                dados.getIdUf(), dados.getIdCity()
        );

        etValorMin.setText((dados.getMinValue() == 0.0 ? "" : String.valueOf(dados.getMinValue())));
        etValorMax.setText((dados.getMaxValue() == 0.0 ? "" : String.valueOf(dados.getMaxValue())));

        //Inicia os valores das espécies selecionadas com false(não selecionada)
        especiesEscolhidas = new HashMap<>();
        especiesEscolhidas.put(R.id.ivEspCao, false);
        especiesEscolhidas.put(R.id.ivEspGato, false);
        especiesEscolhidas.put(R.id.ivEspCoelho, false);

        for(int i=0; i<dados.getEspecies().length; i++){
            switch (dados.getEspecies()[i]){
                case "Cachorro": especiesEscolhidas.put(R.id.ivEspCao, true); break;
                case "Gato": especiesEscolhidas.put(R.id.ivEspGato, true); break;
                case "Coelho": especiesEscolhidas.put(R.id.ivEspCoelho, true); break;
            }
        }

        changeImgEspecie(R.id.ivEspCao, especiesEscolhidas.get(R.id.ivEspCao));
        changeImgEspecie(R.id.ivEspGato, especiesEscolhidas.get(R.id.ivEspGato));
        changeImgEspecie(R.id.ivEspCoelho, especiesEscolhidas.get(R.id.ivEspCoelho));
    }

    /*
    * Método chamado no onclick do botão filtrar
    * */
    public void filtrar(View view){
        saveDadosFiltro();
        if(!QueroAjudarOng.dadosDoFiltro.isClear()) {
            QueroAjudarOng.dataFilter.executeFilter();
        }
        onBackPressed();
    }

    /*
    * Extrai os dados dos inputs do filtro e salva na variável global
    * */
    public void saveDadosFiltro(){

        String[] especies = especiesSelected();
        Double minValue = getDouble(etValorMin.getText().toString());
        Double maxValue = getDouble(etValorMax.getText().toString());
        String uf = getDataOfSp(spFilterUf);
        Integer idUf = getIndexOfSp(spFilterUf);
        String cidade = getDataOfSp(spFilterCidade);
        Integer idCidade = getIndexOfSp(spFilterCidade);

        QueroAjudarOng.dadosDoFiltro = new DadosFiltro(especies, minValue, maxValue, uf, cidade,
                idUf, idCidade);
    }

    /*
    * Converte string para double, caso for vazio, vira 0
    * */
    public Double getDouble(String value) {
        if(value.equals("")){
            return 0.0;
        }

        return Double.parseDouble(value);
    }

    /*
    * Retorna quais espécies foram selecionadas
    * */
    public String[] especiesSelected(){

        List<String> especies = new ArrayList<>();

        Boolean espCao = especiesEscolhidas.get(R.id.ivEspCao);
        Boolean espGato = especiesEscolhidas.get(R.id.ivEspGato);
        Boolean espCoelho = especiesEscolhidas.get(R.id.ivEspCoelho);

        if(espCao) especies.add("Cachorro");
        if(espGato) especies.add("Gato");
        if(espCoelho) especies.add("Coelho");

        return especies.toArray(new String[0]);
    }

    /*
    * Controla quais espécies estão selecionadas
    * */
    public void selectEspecie(View view){
        Integer id = view.getId();
        Boolean isSelected =  !especiesEscolhidas.get(id);
        especiesEscolhidas.put(id, isSelected);
        changeImgEspecie(id, isSelected);
    }

    /*
    * Acessa uma imageview e dependendo do valor de selected, vai ser setado uma img diferente
    * */
    public void changeImgEspecie(Integer id, Boolean selected){

        switch (id){

            case R.id.ivEspCao: ivEspCao.setBackgroundResource(
                    selected ? R.drawable.icone_dog2 : R.drawable.icone_dog1); break;

            case R.id.ivEspGato: ivEspGato.setBackgroundResource(
                    selected ? R.drawable.icone_cat2 : R.drawable.icone_cat1); break;

            case R.id.ivEspCoelho: ivEspCoelho.setBackgroundResource(
                    selected ? R.drawable.icone_bunny2 : R.drawable.icone_bunny1); break;

        }
    }

    /*
    * Força salvar os dados do filtro caso o usuário não deseje mais ter filtro,
    * e depois manda atualizar na classe queroajudarong
    * */
    @Override
    public void onBackPressed(){
        super.onBackPressed();

        if(QueroAjudarOng.dadosDoFiltro.isClear()){
            QueroAjudarOng.dataFilter.executeFilter();
        }
    }

    /*
     * Simula a ação de clicar em voltar
     * */
    public void voltar(View view){
        onBackPressed();
    }

    /*
     * Método que recebe um Spinner e pega o conteudo selecionado
     * */
    private String getDataOfSp(Spinner sp){

        Object selected = sp.getSelectedItem();

        if(selected == null || selected.toString().equals("")){
            switch (sp.getId()){
                case R.id.spFilterUf: return QueroAjudarOng.dadosDoFiltro.getUf();
                case R.id.spFilterCidade: return QueroAjudarOng.dadosDoFiltro.getCidade();
            }
        }

        return selected.toString();
    }

    /*
     * Método que recebe um Spinner e pega o index do conteudo selecionado
     * */
    private Integer getIndexOfSp(Spinner sp){
        int index = sp.getSelectedItemPosition();
        if(index == 0){
            switch (sp.getId()){
                case R.id.spFilterUf: index = QueroAjudarOng.dadosDoFiltro.getIdUf(); break;
                case R.id.spFilterCidade: index = QueroAjudarOng.dadosDoFiltro.getIdCity(); break;
            }
        }
        return index;
    }

    /*
    * Zera os campos de filtro
    * */
    public void limpar(View view){
        QueroAjudarOng.dadosDoFiltro = new DadosFiltro(new String[0], 0.0, 0.0,
                "","", -1, -1);
        initValues();
        GeralUtils.toast(getApplicationContext(), "Filtro(s) Removido(s).");
    }

}