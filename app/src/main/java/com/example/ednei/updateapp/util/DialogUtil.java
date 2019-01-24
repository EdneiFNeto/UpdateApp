package com.example.ednei.updateapp.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.example.ednei.updateapp.R;

public class DialogUtil {

    private static AlertDialog alerta;

    public static void showDialog(final Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //define o titulo
        builder.setTitle("ATUALIZAÇÃO");
        builder.setIcon(R.mipmap.ic_launcher);

        //define a mensagem
        builder.setMessage("Nova versão disponível, clique para atualizar o app.");
        //define um botão como positivo
        builder.setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //install App
                try {
                    new InstallAppUtil().openIntentInstall(context);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "Error install: "+e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }
}
