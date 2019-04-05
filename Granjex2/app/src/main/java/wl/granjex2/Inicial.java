package wl.granjex2;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Inicial extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    Spinner InicialSpGranja;
    public static String static_granja;
    public static String ip_granja;

    public static String vTemp = null;
    Button btnConexao;
    TextView Inicial_txtResultado;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
    SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");
    Date data = new Date();
    Calendar  cal = Calendar.getInstance();


    public Inicial() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_inicial, container, false);

        btnConexao = (Button) view.findViewById(R.id.btnConexao);
        btnConexao.setOnClickListener(this);

        InicialSpGranja = (Spinner) view.findViewById(R.id.InicialSpGranja);
        InicialSpGranja.setOnItemSelectedListener(this);

        Inicial_txtResultado = (TextView) view.findViewById(R.id.Inicial_txtResultado);
        //txtHora = (TextView) view.findViewById(R.id.txtHora);

        cal.setTime(data);
        Date data_atual = cal.getTime();
        String data_completa = dateFormat.format(data_atual);
        String hora_atual = dateFormat_hora.format(data_atual);

        //txtHora.setText(data_completa);

        SpinnerGranja();

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
        InicialSpGranja.setAdapter(dataAdapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnConexao:
                verificaConexao();
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        static_granja = parent.getItemAtPosition(position).toString();
        //Toast.makeText(getActivity(), "Granja " + static_granja, Toast.LENGTH_LONG).show();
        DataBaseHandler banco = new DataBaseHandler(getActivity().getApplicationContext());
        ip_granja  = banco.encontraGranja(static_granja);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void verificaConexao(){
        try {
            Conexao conexao_tmp = new Conexao();

            // comentado dia 12/10/2016
            //vTemp = conexao_tmp.execute("conex", "80").get();

            String porta = "80";
            
            vTemp = conexao_tmp.execute("conex", ip_granja, porta).get();

            if (vTemp != ""){

                //Válida se houve retorno, caso não seja numero, estoura o erro e o exibe
                if (conexao_tmp.F_eNumero(vTemp.substring(0, 5)) == false) {
                    Inicial_txtResultado.setText("Falha de Conexão!");

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
                    //throw new Exception(vTemp);

                }else{
                    if(vTemp.equals("55555")){

                        //Toast.makeText(getActivity(), "Conectado com sucesso!", Toast.LENGTH_LONG).show();
                        Inicial_txtResultado.setText("Conectado com sucesso!");

                        // Atributo da classe
                        AlertDialog alerta;
                        // Criar o gerador do AlertDialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        // Define a Mensagem
                        builder.setMessage("Conectado com sucesso no IP: " + ip_granja);
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

                    else{
                        //Toast.makeText(getActivity(), "Falha de Conexão:" + vTemp, Toast.LENGTH_LONG).show();
                        Inicial_txtResultado.setText("Falha de Conexão!");

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
