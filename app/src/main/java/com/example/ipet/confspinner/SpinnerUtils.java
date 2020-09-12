package com.example.ipet.confspinner;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.ipet.apiufcity.DadosApi;
import com.example.ipet.R;

import java.util.Collections;
import java.util.List;

public class SpinnerUtils {

    /*
     * Método para inicializar, inserir dados e configurar os spinners de UF e Cidade.
     * */
    public static void confSpinners(final Context context, final Spinner spinnerUF, String titleSpUf,
                                    final Spinner spinnerCity, final String titleSpCity) {

        final String urlStatic = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/";

        DadosApi dadosApiUF = new DadosApi(urlStatic, "sigla");

        initSpinner(spinnerCity, titleSpCity, context);
        initSpinner(spinnerUF, titleSpUf, context);

        new ConfigureSpinner(context, spinnerUF, titleSpUf,
                dadosApiUF, true,
                new ConfigureSpinner.Action() { //Define a ação ao selecionar um item no spinner UF
                    @Override
                    public void onSelect(String itemSelected) {
                        setDataSpCity(context, spinnerCity, titleSpCity, urlStatic, itemSelected);
                    }
                }
        ).runConf();
    }

    /*
     * Método que irá inicializar um style_spinner, setando um conjunto de dados vazios, porém também
     * setará um titulo, adicionando um adaptador personalizado
     * */
    private static void initSpinner(Spinner spinner, String title, Context context) {
        //spinner_st é desativado, e só é ativado na classe ConfigureSpinner
        spinner.setEnabled(false);
        setDataSpinner(spinner, context, title, Collections.singletonList(""));
    }

    /*
     * Irá setar os dados no style_spinner com ajuda do método runConf da classe ConfigureSpinner
     * */
    private static void setDataSpCity(Context context, Spinner spinnerCity, String titleSpCity,
                                      String urlStatic, String itemSelected) {

        new ConfigureSpinner(context, spinnerCity, titleSpCity,
                new DadosApi(urlStatic + itemSelected + "/municipios", "nome"),
                false,
                new ConfigureSpinner.Action() {
                    @Override
                    public void onSelect(String itemSelected) {//Define a ação ao selecionar um item no spinner UF

                    }
                }
        ).runConf();
    }

    /*
     * Método para setar dados dentro de um spinner, além de configurar o layout
     * */
    public static void setDataSpinner(Spinner spinner, Context context, String title, List<String> dados) {
        spinner.setAdapter(new NothingSelectedSpinnerAdapter(new ArrayAdapter<>(context,
                R.layout.spinner_row, dados), title, R.layout.spinner_row, context)
        );
    }

}
