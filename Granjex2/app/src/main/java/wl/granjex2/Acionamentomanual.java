package wl.granjex2;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Scanner;


public class Acionamentomanual extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener  {

    Spinner AcionamentoManualSpGranja;
    public static String granja_nome;
    public static String granja_ip;
    public static String vTemp = null;

    private Socket socket;
    private static final int SERVERPORT = 80;
    private static final String SERVER_IP = "192.168.0.105";

    Button lampadasOn, lampadasOff, ventiladoresOn, ventiladoresOff;
    Handler handler = new Handler();
    Intent NovoLayout;

    public Acionamentomanual() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_acionamentomanual, container, false);

        lampadasOn = (Button) view.findViewById(R.id.btnLampadasOn);
        lampadasOff = (Button) view.findViewById(R.id.btnLampadasOff);
        ventiladoresOn = (Button) view.findViewById(R.id.btnVentiladoresOn);
        ventiladoresOff = (Button) view.findViewById(R.id.btnVentiladoresOff);

        lampadasOn.setOnClickListener(this);
        lampadasOff.setOnClickListener(this);
        ventiladoresOn.setOnClickListener(this);
        ventiladoresOff.setOnClickListener(this);

        AcionamentoManualSpGranja = (Spinner) view.findViewById(R.id.AcionamentoManualSpGranja);
        AcionamentoManualSpGranja.setOnItemSelectedListener(this);

        SpinnerGranja();
        //consulta_lampada();
        //consulta_ventilador();

        // Inflate the layout for this fragment
        return view;
    }

    public void SpinnerGranja() {

        // database handler
        DataBaseHandler db = new DataBaseHandler(getActivity().getApplicationContext());

        // Spinner Drop down elements
        List<String> consultaGranjas = db.ConsultaGranjas();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, consultaGranjas);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);

        // attaching data adapter to spinner
        AcionamentoManualSpGranja.setAdapter(dataAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        case R.id.btnLampadasOff:
            //LigarLampadas();
            ligar_lampadas();
        break;

        case R.id.btnLampadasOn:
            //DesligarLampadas();
            desligar_lampadas();
        break;

        case R.id.btnVentiladoresOff:
            //LigarVentiladores();
            ligar_ventiladores();
        break;

        case R.id.btnVentiladoresOn:
            //DesligarVentiladores();
            desligar_ventiladores();
        break;

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        granja_nome = parent.getItemAtPosition(position).toString();
        //Toast.makeText(getActivity(), "Granja " + static_granja, Toast.LENGTH_LONG).show();
        DataBaseHandler banco = new DataBaseHandler(getActivity().getApplicationContext());
        granja_ip  = banco.encontraGranja(granja_nome);
        consulta_lampada();
        consulta_ventilador();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    class ClientThread implements Runnable {

        @Override
        public void run() {
            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);
                socket = new Socket(serverAddr, SERVERPORT);

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }  //Não está em uso


    private void LigarLampadas(){

        NovoLayout = new Intent(getContext(),Carregamentoespera.class);
        startActivity(NovoLayout);

        new Thread(new ClientThread()).start();
        try {
            //EditText et = (EditText) findViewById(R.id.EditText01);
            String et = "lampadaacesa";
            //String str = et.getText().toString();
            String str = et.toString();

            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);

            //Envia a mensagem para o Servidor Arduino
            out.println(str);




        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lampadasOn.setPressed(true);
                lampadasOn.setEnabled(true);
                lampadasOff.setPressed(false);
                lampadasOff.setEnabled(false);
            }
        },3000);

        //Mensagem para ver o que está passando
        //Toast.makeText(getActivity(),"Testando! "+abc,Toast.LENGTH_LONG).show();


    } //Não está em uso

    private void DesligarLampadas(){

        NovoLayout = new Intent(getContext(),Carregamentoespera.class);
        startActivity(NovoLayout);

        new Thread(new ClientThread()).start();
        try {
            //EditText et = (EditText) findViewById(R.id.EditText01);
            String et = "lampadaapagada";
            //String str = et.getText().toString();
            String str = et.toString();

            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);

            //Envia a mensagem para o Servidor Arduino
            out.println(str);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }


        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                lampadasOff.setPressed(true);
                lampadasOff.setEnabled(true);
                lampadasOn.setPressed(false);
                lampadasOn.setEnabled(false);
            }
        },3000);
    } //Não está em uso

    private void LigarVentiladores(){

        NovoLayout = new Intent(getContext(),Carregamentoespera.class);
        startActivity(NovoLayout);

        new Thread(new ClientThread()).start();
        try {
            //EditText et = (EditText) findViewById(R.id.EditText01);
            String et = "ventiladorligado";
            //String str = et.getText().toString();
            String str = et.toString();

            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);

            //Envia a mensagem para o Servidor Arduino
            out.println(str);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ventiladoresOn.setPressed(true);
                ventiladoresOn.setEnabled(true);
                ventiladoresOff.setPressed(false);
                ventiladoresOff.setEnabled(false);
            }
        },3000);
    } //Não está em uso

    private void DesligarVentiladores(){

        NovoLayout = new Intent(getContext(),Carregamentoespera.class);
        startActivity(NovoLayout);

        new Thread(new ClientThread()).start();
        try {
            //EditText et = (EditText) findViewById(R.id.EditText01);
            String et = "ventiladordesligado";
            //String str = et.getText().toString();
            String str = et.toString();

            PrintWriter out = new PrintWriter(new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream())),
                    true);

            //Envia a mensagem para o Servidor Arduino
            out.println(str);

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ventiladoresOff.setPressed(true);
                ventiladoresOff.setEnabled(true);
                ventiladoresOn.setPressed(false);
                ventiladoresOn.setEnabled(false);
            }
        },3000);
    } //Não está em uso


    public void ligar_lampadas(){
        try {
            Conexao conexao_tmp = new Conexao();
            vTemp = conexao_tmp.execute("lampadaacesa", granja_ip, "80").get();

            if (vTemp != ""){

                //Válida se houve retorno, caso não seja numero, estoura o erro e o exibe
                if (conexao_tmp.F_eNumero(vTemp.substring(0, 5)) == false) {

                    // Atributo da classe
                    AlertDialog alerta;
                    // Criar o gerador do AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // Define a Mensagem
                    builder.setMessage("Falha de Conexão!");
                    // Define um botão como positivo
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    // Cria AlertDialog
                    alerta = builder.create();
                    // Exibe
                    alerta.show();

                    //throw new Exception(vTemp);

                }else{
                    //Toast.makeText(getActivity(), "Lâmpadas acesas!", Toast.LENGTH_LONG).show();

                    // Atributo da classe
                    AlertDialog alerta;
                    // Criar o gerador do AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // Define a Mensagem
                    builder.setMessage("Lâmpadas acesas!");
                    // Define um botão como positivo
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    // Cria AlertDialog
                    alerta = builder.create();
                    // Exibe
                    alerta.show();

                    lampadasOn.setPressed(true);
                    lampadasOn.setEnabled(true);
                    lampadasOff.setPressed(false);
                    lampadasOff.setEnabled(false);

                }
            }

            //Exception criado para tratar o erro caso o Care Fish não esteja conectado a internet
        } catch (Exception e) {
            Log.e("OnclickTemp", e.toString()); //Log, método nativo do Android
            if (e.toString().indexOf("|")>=0){
                Toast.makeText(getContext(), e.toString().substring(e.toString().indexOf("|")+1), Toast.LENGTH_LONG).show(); //Toast para exibir as mensagens de erro
            }else{
                Toast.makeText(getContext(), e.toString().substring(0), Toast.LENGTH_LONG).show(); //Toast para exibir as mensagens de erro
            }
        }
    }
    public void desligar_lampadas(){

        try {
            Conexao conexao_tmp = new Conexao();
            vTemp = conexao_tmp.execute("lampadaapagada", granja_ip, "80").get();

            if (vTemp != ""){

                //Válida se houve retorno, caso não seja numero, estoura o erro e o exibe
                if (conexao_tmp.F_eNumero(vTemp.substring(0, 5)) == false) {

                    // Atributo da classe
                    AlertDialog alerta;
                    // Criar o gerador do AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // Define a Mensagem
                    builder.setMessage("Falha de Conexão!");
                    // Define um botão como positivo
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    // Cria AlertDialog
                    alerta = builder.create();
                    // Exibe
                    alerta.show();
                    //throw new Exception(vTemp);

                }else{
                    //Toast.makeText(getActivity(), "Lâmpadas apagadas!", Toast.LENGTH_LONG).show();

                    // Atributo da classe
                    AlertDialog alerta;
                    // Criar o gerador do AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // Define a Mensagem
                    builder.setMessage("Lâmpadas apagadas!");
                    // Define um botão como positivo
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    // Cria AlertDialog
                    alerta = builder.create();
                    // Exibe
                    alerta.show();

                    lampadasOff.setPressed(true);
                    lampadasOff.setEnabled(true);
                    lampadasOn.setPressed(false);
                    lampadasOn.setEnabled(false);

                }
            }

            //Exception criado para tratar o erro caso o Care Fish não esteja conectado a internet
        } catch (Exception e) {
            Log.e("OnclickTemp", e.toString()); //Log, método nativo do Android
            if (e.toString().indexOf("|")>=0){
                Toast.makeText(getContext(), e.toString().substring(e.toString().indexOf("|")+1), Toast.LENGTH_LONG).show(); //Toast para exibir as mensagens de erro
            }else{
                Toast.makeText(getContext(), e.toString().substring(0), Toast.LENGTH_LONG).show(); //Toast para exibir as mensagens de erro
            }
        }

    }

    public void ligar_ventiladores(){
        try {
            Conexao conexao_tmp = new Conexao();
            vTemp = conexao_tmp.execute("ventiladorligado", granja_ip, "80").get();

            if (vTemp != ""){

                //Válida se houve retorno, caso não seja numero, estoura o erro e o exibe
                if (conexao_tmp.F_eNumero(vTemp.substring(0, 5)) == false) {

                    // Atributo da classe
                    AlertDialog alerta;
                    // Criar o gerador do AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // Define a Mensagem
                    builder.setMessage("Falha de Conexão!");
                    // Define um botão como positivo
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    // Cria AlertDialog
                    alerta = builder.create();
                    // Exibe
                    alerta.show();

                    //throw new Exception(vTemp);

                }else{

                    //Toast.makeText(getActivity(), "Ventiladores acionados!", Toast.LENGTH_LONG).show();

                    // Atributo da classe
                    AlertDialog alerta;
                    // Criar o gerador do AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // Define a Mensagem
                    builder.setMessage("Ventiladores acionados!");
                    // Define um botão como positivo
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    // Cria AlertDialog
                    alerta = builder.create();
                    // Exibe
                    alerta.show();

                    ventiladoresOn.setPressed(true);
                    ventiladoresOn.setEnabled(true);
                    ventiladoresOff.setPressed(false);
                    ventiladoresOff.setEnabled(false);

                }
            }

            //Exception criado para tratar o erro caso o Care Fish não esteja conectado a internet
        } catch (Exception e) {
            Log.e("OnclickTemp", e.toString()); //Log, método nativo do Android
            if (e.toString().indexOf("|")>=0){
                Toast.makeText(getContext(), e.toString().substring(e.toString().indexOf("|")+1), Toast.LENGTH_LONG).show(); //Toast para exibir as mensagens de erro
            }else{
                Toast.makeText(getContext(), e.toString().substring(0), Toast.LENGTH_LONG).show(); //Toast para exibir as mensagens de erro
            }
        }
    }
    public void desligar_ventiladores(){

        try {
            Conexao conexao_tmp = new Conexao();
            vTemp = conexao_tmp.execute("ventiladordesligado", granja_ip, "80").get();

            if (vTemp != ""){

                //Válida se houve retorno, caso não seja numero, estoura o erro e o exibe
                if (conexao_tmp.F_eNumero(vTemp.substring(0, 5)) == false) {

                    // Atributo da classe
                    AlertDialog alerta;
                    // Criar o gerador do AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // Define a Mensagem
                    builder.setMessage("Falha de Conexão!");
                    // Define um botão como positivo
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    // Cria AlertDialog
                    alerta = builder.create();
                    // Exibe
                    alerta.show();

                    //throw new Exception(vTemp);

                }else{

                    //Toast.makeText(getActivity(), "Ventiladores desligados!", Toast.LENGTH_LONG).show();

                    // Atributo da classe
                    AlertDialog alerta;
                    // Criar o gerador do AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // Define a Mensagem
                    builder.setMessage("Ventiladores desligados!");
                    // Define um botão como positivo
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    // Cria AlertDialog
                    alerta = builder.create();
                    // Exibe
                    alerta.show();

                    ventiladoresOff.setPressed(true);
                    ventiladoresOff.setEnabled(true);
                    ventiladoresOn.setPressed(false);
                    ventiladoresOn.setEnabled(false);

                }
            }

            //Exception criado para tratar o erro caso o Care Fish não esteja conectado a internet
        } catch (Exception e) {
            Log.e("OnclickTemp", e.toString()); //Log, método nativo do Android
            if (e.toString().indexOf("|")>=0){
                Toast.makeText(getContext(), e.toString().substring(e.toString().indexOf("|")+1), Toast.LENGTH_LONG).show(); //Toast para exibir as mensagens de erro
            }else{
                Toast.makeText(getContext(), e.toString().substring(0), Toast.LENGTH_LONG).show(); //Toast para exibir as mensagens de erro
            }
        }
    }

    public void consulta_lampada(){
        try {
            Conexao conexao_tmp = new Conexao();
            vTemp = conexao_tmp.execute("lam",granja_ip, "80").get();

            if (vTemp != ""){

                //Válida se houve retorno, caso não seja numero, estoura o erro e o exibe
                if (conexao_tmp.F_eNumero(vTemp.substring(0, 5)) == false) {

                    //throw new Exception(vTemp);

                }else{
                    if(vTemp.equals("11111")){
                        //Toast.makeText(getActivity(), "A lampada está acesa!", Toast.LENGTH_LONG).show();
                        lampadasOff.setPressed(false);
                        lampadasOff.setEnabled(false);
                        lampadasOn.setPressed(true);
                        lampadasOn.setEnabled(true);
                    }
                    if(vTemp.equals("22222")){
                        //Toast.makeText(getActivity(), "A lampada está apagada!", Toast.LENGTH_LONG).show();
                        lampadasOn.setPressed(false);
                        lampadasOn.setEnabled(false);
                        lampadasOff.setPressed(true);
                        lampadasOff.setEnabled(true);
                    }
                    else{
                        //Toast.makeText(getActivity(), "Valor recebido da lâmpada:" + vTemp, Toast.LENGTH_LONG).show();
                    }
                }
            }

            //Exception criado para tratar o erro caso o Care Fish não esteja conectado a internet
        } catch (Exception e) {
            Log.e("OnclickTemp", e.toString()); //Log, método nativo do Android
            if (e.toString().indexOf("|")>=0){
                Toast.makeText(getContext(), e.toString().substring(e.toString().indexOf("|")+1), Toast.LENGTH_LONG).show(); //Toast para exibir as mensagens de erro
            }else{
                Toast.makeText(getContext(), e.toString().substring(0), Toast.LENGTH_LONG).show(); //Toast para exibir as mensagens de erro
            }
        }
    }
    public void consulta_ventilador(){
        try {
            Conexao conexao_tmp = new Conexao();
            vTemp = conexao_tmp.execute("ven", granja_ip, "80").get();

            if (vTemp != ""){

                //Válida se houve retorno, caso não seja numero, estoura o erro e o exibe
                if (conexao_tmp.F_eNumero(vTemp.substring(0, 5)) == false) {

                    //throw new Exception(vTemp);

                }else{
                    if(vTemp.equals("33333")){
                        //Toast.makeText(getActivity(), "ventilador ligado!", Toast.LENGTH_LONG).show();
                        ventiladoresOff.setPressed(false);
                        ventiladoresOff.setEnabled(false);
                        ventiladoresOn.setPressed(true);
                        ventiladoresOn.setEnabled(true);
                    }
                    if(vTemp.equals("44444")){
                        //Toast.makeText(getActivity(), "ventilador desligado!", Toast.LENGTH_LONG).show();
                        ventiladoresOn.setPressed(false);
                        ventiladoresOn.setEnabled(false);
                        ventiladoresOff.setPressed(true);
                        ventiladoresOff.setEnabled(true);
                    }
                    else{
                        //Toast.makeText(getActivity(), "Valor recebido do ventilador:" + vTemp, Toast.LENGTH_LONG).show();
                    }
                }
            }

            //Exception criado para tratar o erro caso o Care Fish não esteja conectado a internet
        } catch (Exception e) {
            Log.e("OnclickTemp", e.toString()); //Log, método nativo do Android
            if (e.toString().indexOf("|")>=0){
                Toast.makeText(getContext(), e.toString().substring(e.toString().indexOf("|")+1), Toast.LENGTH_LONG).show(); //Toast para exibir as mensagens de erro
            }else{
                Toast.makeText(getContext(), e.toString().substring(0), Toast.LENGTH_LONG).show(); //Toast para exibir as mensagens de erro
            }
        }
    }

}
