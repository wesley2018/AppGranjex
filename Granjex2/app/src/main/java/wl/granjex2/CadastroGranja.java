package wl.granjex2;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CadastroGranja extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    //EditText
    EditText CDGtxtIP;
    EditText CDGtxtNomeGranja;
    Button CDGbtnSalvar;
    Button CDGbtnExcluir;

    public static String granja_nome;
    public static String granja_ip;

    Spinner CDGspNome;

    public CadastroGranja() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_cadastro_granja, container, false);
        View view = inflater.inflate(R.layout.fragment_cadastro_granja, container, false);

        CDGtxtIP = (EditText) view.findViewById(R.id.CDGtxtIP);
        CDGtxtNomeGranja = (EditText) view.findViewById(R.id.CDGtxtNomeGranja);

        CDGbtnSalvar = (Button) view.findViewById(R.id.CDGbtnSalvar);
        CDGbtnSalvar.setOnClickListener(this);

        CDGbtnExcluir = (Button) view.findViewById(R.id.CDGbtnExcluir);
        CDGbtnExcluir.setOnClickListener(this);

        CDGspNome = (Spinner) view.findViewById(R.id.CDGspNome);
        CDGspNome.setOnItemSelectedListener(this);

        SpinnerGranja();

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            // Verifica o botão que está sendo clicado
            case R.id.CDGbtnSalvar:
                cadastrarGranja();
                break;

            case R.id.CDGbtnExcluir:
                excluirGranja();
                break;
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        granja_nome = parent.getItemAtPosition(position).toString();
        DataBaseHandler banco = new DataBaseHandler(getActivity().getApplicationContext());
        granja_ip  = banco.encontraGranja(granja_nome);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void SpinnerGranja()
    {

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
        CDGspNome.setAdapter(dataAdapter);
    }

    private void cadastrarGranja(){


        // Regra de negócio do botão cadastrar
        if(CDGtxtIP.getText().toString().trim().length()== 0 && CDGtxtNomeGranja.getText().toString().trim().length()==0){
            // Atributo da classe
            AlertDialog alerta;
            // Criar o gerador do AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Define a Mensagem
            builder.setMessage("Os campos IP e Nome da Granja são obrigatórios!");
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
            clearText();
            CDGtxtIP.requestFocus();
        }

        else {
            if (CDGtxtIP.getText().toString().trim().length() == 0 || CDGtxtNomeGranja.getText().toString().trim().length() == 0) {
                if (CDGtxtIP.getText().toString().trim().length() == 0) {
                    // Atributo da classe
                    AlertDialog alerta;
                    // Criar o gerador do AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // Define a Mensagem
                    builder.setMessage("O campo IP é obrigatório!");
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
                    CDGtxtIP.requestFocus();
                }

                if (CDGtxtNomeGranja.getText().toString().trim().length() == 0) {
                    //Toast.makeText(getApplication(), "O campo Senha é obrigatório!", Toast.LENGTH_LONG).show();
                    // Atributo da classe
                    AlertDialog alerta;
                    // Criar o gerador do AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // Define a Mensagem
                    builder.setMessage("O campo Nome da Granja é obrigatório!");
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
                    CDGtxtNomeGranja.requestFocus();
                }
            }
        }

        if(CDGtxtIP.getText().toString().trim().length() > 0 && CDGtxtNomeGranja.getText().toString().trim().length() > 0) {

            DataBaseHandler db = new DataBaseHandler(
                    getActivity().getApplicationContext());

            String granja_ip = CDGtxtIP.getText().toString();
            String granja_nome = CDGtxtNomeGranja.getText().toString();

            String granjaexiste = db.verificaExisteGranja(granja_nome);

            if(granjaexiste.equals("existe")){
                Toast.makeText(getActivity(), "Granja já existente, não pode ser gravado!", Toast.LENGTH_LONG).show();
                clearText();
                CDGtxtIP.requestFocus();
            }

            if(granjaexiste.equals("naoexiste")) {

                db.PopularTabelaGranja(granja_ip,granja_nome);

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(CDGtxtNomeGranja.getWindowToken(), 0);

                SpinnerGranja();

                Toast.makeText(getActivity(), "Granja cadastrada com sucesso!", Toast.LENGTH_LONG).show();
                clearText();

                CDGtxtIP.requestFocus();
            }
        }
    }

    private void excluirGranja(){

        // Atributo da classe
        AlertDialog alerta;
        // Criar o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Define o Titulo
        builder.setTitle("Apagar");
        // Define a Mensagem
        builder.setMessage("Deseja realmente apagar esta granja?");
        // Define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DataBaseHandler db = new DataBaseHandler(
                        getActivity().getApplicationContext());

                db.excluiGranja(granja_nome);
                db.excluiHistorico(granja_ip);

                Toast.makeText(getContext(), "Granja " + granja_nome + " apagado com sucesso! ",
                        Toast.LENGTH_LONG).show();

                SpinnerGranja();

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

    //clearText
    public void clearText(){
        CDGtxtIP.setText("");
        CDGtxtNomeGranja.setText("");
    }
}
