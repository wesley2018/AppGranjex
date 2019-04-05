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

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class Monitoramento extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener  {

    Spinner MonitoramentoSpGranja;
    Button M_btnTemperatura, M_btnUmidade;

    TextView M_txtUmidade, M_txtTemperatura;

    public static String static_granja;
    public static String ip_granja;

    public static String vTemp = null;

    public Monitoramento() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_monitoramento, container, false);

        MonitoramentoSpGranja = (Spinner) view.findViewById(R.id.MonitoramentoSpGranja);
        MonitoramentoSpGranja.setOnItemSelectedListener(this);
        SpinnerGranja();

        M_btnTemperatura = (Button) view.findViewById(R.id.M_btnTemperatura);
        M_btnTemperatura.setOnClickListener(this);

        M_btnUmidade = (Button) view.findViewById(R.id.M_btnUmidade);
        M_btnUmidade.setOnClickListener(this);

        M_txtTemperatura = (TextView) view.findViewById(R.id.M_txtTemperatura);

        M_txtUmidade = (TextView) view.findViewById(R.id.M_txtUmidade);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            // Verifica o botão que está sendo clicado
            case R.id.M_btnTemperatura:
                pegaTemperatura();
                break;

            case R.id.M_btnUmidade:
                pegaUmidade();
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        static_granja = parent.getItemAtPosition(position).toString();
        DataBaseHandler banco = new DataBaseHandler(getActivity().getApplicationContext());
        ip_granja = banco.encontraGranja(static_granja);

        //pegaTemperatura();
        //pegaUmidade();
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
        MonitoramentoSpGranja.setAdapter(dataAdapter);
    }

    public void pegaTemperatura(){

        try {
            Conexao conexao_tmp = new Conexao();
            vTemp = conexao_tmp.execute("temperatura",ip_granja, "80").get();

            if (vTemp != ""){

                //Válida se houve retorno, caso não seja numero, estoura o erro e o exibe
                if (conexao_tmp.F_eNumero(vTemp.substring(0, 5)) == false) {

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
                    //Toast.makeText(getActivity(), "Temperatura consultada!", Toast.LENGTH_SHORT).show();

                    // Atributo da classe
                    AlertDialog alerta;
                    // Criar o gerador do AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // Define a Mensagem
                    builder.setMessage("Temperatura consultada do IP: " + ip_granja);
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

                    //Habilita o campo "Temperatura na tela e mostra o valor"
                    M_txtTemperatura.setText(vTemp + " C°");

                    //Instância da classe 'HistoricoTemp' para chamar o método de inserção no banco de dados.
                    //Historicodb dbTemp = new Historicodb(getActivity().getApplicationContext());

                    //Chamada do método de inserção no banco de dados e passa os valores por parâmetros
                    //dbTemp.inserir("TMP", vTemp.substring(0, 4));

                    //text_Temperatura.setVisibility(View.INVISIBLE);
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

    public void pegaUmidade(){

        try {
            Conexao conexao_tmp = new Conexao();
            vTemp = conexao_tmp.execute("umidade",ip_granja, "80").get();

            if (vTemp != ""){

                //Válida se houve retorno, caso não seja numero, estoura o erro e o exibe
                if (conexao_tmp.F_eNumero(vTemp.substring(0, 5)) == false) {

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
                    //Toast.makeText(getActivity(), "Umidade Consultada!", Toast.LENGTH_LONG).show();
                    // Atributo da classe
                    AlertDialog alerta;
                    // Criar o gerador do AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // Define a Mensagem
                    builder.setMessage("Umidade Consultada do IP: " + ip_granja);
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

                    //Habilita o campo "Temperatura na tela e mostra o valor"
                    M_txtUmidade.setText(vTemp + " %");

                    //Instância da classe 'HistoricoTemp' para chamar o método de inserção no banco de dados.
                    //Historicodb dbTemp = new Historicodb(getActivity().getApplicationContext());

                    //Chamada do método de inserção no banco de dados e passa os valores por parâmetros
                    //dbTemp.inserir("TMP", vTemp.substring(0, 4));

                    //text_Temperatura.setVisibility(View.INVISIBLE);
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
