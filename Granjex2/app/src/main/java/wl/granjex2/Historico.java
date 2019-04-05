package wl.granjex2;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Historico extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    Spinner HistoricoSpGranja;
    public static String granja_nome;
    public static String ip_granja = null;
    public static String vTemp = null;
    public static String umidade = null;
    public static String temperatura = null;

    Button Historico_btnConsultar, Historico_btnExcluir;
    ListView listaHistorico;

    public Historico() {
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_historico, container, false);

        HistoricoSpGranja = (Spinner) view.findViewById(R.id.HistoricoSpGranja);
        HistoricoSpGranja.setOnItemSelectedListener(this);

        Historico_btnConsultar = (Button) view.findViewById(R.id.Historico_btnConsultar);
        Historico_btnConsultar.setOnClickListener(this);

        Historico_btnExcluir = (Button) view.findViewById(R.id.Historico_btnExcluir);
        Historico_btnExcluir.setOnClickListener(this);

        listaHistorico = (ListView) view.findViewById(R.id.listaHistorico);

        //insereHistorico();
        //populaListViewHistorico();

        SpinnerGranja();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // Verifica o botão que está sendo clicado
            case R.id.Historico_btnConsultar:
                insereHistorico();
                populaListViewHistorico();
                break;

            case R.id.Historico_btnExcluir:
                excluiHistorico();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        granja_nome = parent.getItemAtPosition(position).toString();
        //Toast.makeText(getActivity(), "Granja " + static_granja, Toast.LENGTH_LONG).show();
        DataBaseHandler banco = new DataBaseHandler(getActivity().getApplicationContext());
        ip_granja  = banco.encontraGranja(granja_nome);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
        HistoricoSpGranja.setAdapter(dataAdapter);
    }

    public void insereHistorico(){
            String umidade = umidade();
            String temperatura = temperatura();

            if((umidade != null) && (temperatura != null)){

                DataBaseHandler db = new DataBaseHandler(
                        getActivity().getApplicationContext());

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
                ///SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");
                Date data = new Date();
                Calendar cal = Calendar.getInstance();

                cal.setTime(data);
                Date data_atual = cal.getTime();
                String data_completa = dateFormat.format(data_atual);
                //String hora_atual = dateFormat_hora.format(data_atual);
                //txtHora.setText(data_completa);

                //db.PopularTabelaHistorico(ip_granja,data_completa,umidade,temperatura);

                db.PopularTabelaHistorico(ip_granja,data_completa,umidade,temperatura);
            }else{

                // Atributo da classe
                AlertDialog alerta;
                // Criar o gerador do AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Define a Mensagem
                builder.setMessage("Falha de Conexão com o IP: " + ip_granja);
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
            }
    }

    public String umidade(){
        try {
            Conexao conexao_tmp = new Conexao();
            vTemp = conexao_tmp.execute("umidade",ip_granja, "80").get();

            if (vTemp != ""){

                //Válida se houve retorno, caso não seja numero, estoura o erro e o exibe
                if (conexao_tmp.F_eNumero(vTemp.substring(0, 5)) == false) {
                    umidade = null;
                    //throw new Exception(vTemp);

                }else{
                    //Toast.makeText(getActivity(), "Umidade Consultada!", Toast.LENGTH_LONG).show();

                    umidade = vTemp+" %";

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
        return umidade;

    }

    public String temperatura(){

        try {
            Conexao conexao_tmp = new Conexao();
            vTemp = conexao_tmp.execute("temperatura",ip_granja, "80").get();

            if (vTemp != ""){

                //Válida se houve retorno, caso não seja numero, estoura o erro e o exibe
                if (conexao_tmp.F_eNumero(vTemp.substring(0, 5)) == false) {
                    temperatura = null;
                    //throw new Exception(vTemp);

                }else{
                    //Toast.makeText(getActivity(), "Temperatura consultada!", Toast.LENGTH_LONG).show();
                    temperatura = vTemp + " C°";
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

        return temperatura;
    }

    public void populaListViewHistorico(){

        // database handler
        DataBaseHandler db = new DataBaseHandler(getActivity().getApplicationContext());

        // Spinner Drop down elements
        List<String> consultaHistorico = db.ConsultaHistorico(ip_granja);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, consultaHistorico);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);

        // attaching data adapter to spinner
        listaHistorico.setAdapter(dataAdapter);
    }

    public void excluiHistorico(){

        // Atributo da classe
        AlertDialog alerta;
        // Criar o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Define o Titulo
        builder.setTitle("Apagar");
        // Define a Mensagem
        builder.setMessage("Deseja apagar o histórico inteiro da granja "+ granja_nome +"?");
        // Define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DataBaseHandler db = new DataBaseHandler(
                        getActivity().getApplicationContext());

                db.excluiHistorico(ip_granja);

                Toast.makeText(getContext(), "Histórico da granja " + granja_nome + " apagado com sucesso! ",
                        Toast.LENGTH_LONG).show();

                //SpinnerGranja();
                populaListViewHistorico();

            }
        });

        builder.setNegativeButton("Não", new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialog, int which){
                dialog.dismiss();
            }
        });
        // Cria AlertDialog
        alerta = builder.create();
        // Exibe
        alerta.show();
    }
}
