package com.example.ipet.utils;

import android.content.Context;
import android.widget.Toast;

public class GeralUtils {

    /*
     * Lança um simples toast, criado apenas para agilizar o uso do toast, pois aqui já estará
     * configurado.
     * */
    public static void toast(Context context, String msg){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }
}
