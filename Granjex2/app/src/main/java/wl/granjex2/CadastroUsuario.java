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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import java.util.List;

public class CadastroUsuario extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{

    //Criação dos Objetos
    EditText CDUtxtNome;
    EditText CDUtxtSenha;
    CheckBox CDUcbAdministrador;
    Button CDUbtnSalvar;
    Button CDUbtnExcluir;

    public static String static_nome;
    Spinner CDUspNome;

    public CadastroUsuario() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cadastro_usuario, container, false);

        CDUcbAdministrador = (CheckBox) view.findViewById(R.id.CDUcbAdministrador);

        CDUtxtNome = (EditText) view.findViewById(R.id.CDUtxtNome);
        CDUtxtSenha = (EditText) view.findViewById(R.id.CDUtxtSenha);

        CDUbtnSalvar = (Button) view.findViewById(R.id.CDUbtnSalvar);
        CDUbtnSalvar.setOnClickListener(this);

        CDUbtnExcluir = (Button) view.findViewById(R.id.CDUbtnExcluir);
        CDUbtnExcluir.setOnClickListener(this);

        CDUspNome = (Spinner) view.findViewById(R.id.CDUspNome);
        CDUspNome.setOnItemSelectedListener(this);

        SpinnerUsuario();
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String nome = parent.getItemAtPosition(position).toString();

        /*
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Você selecionou: " + nome,
                Toast.LENGTH_LONG).show();
        */

        static_nome = nome;

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }

    private void SpinnerUsuario() {

        // database handler
        DataBaseHandler db = new DataBaseHandler(getActivity().getApplicationContext());

        // Spinner Drop down elements
        List<String> consultaUsuarios = db.ConsultaUsuarios();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, consultaUsuarios);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_checked);

        // attaching data adapter to spinner
        CDUspNome.setAdapter(dataAdapter);
    }

    //Metódo Click
    @Override
    public void onClick (View v){
        switch (v.getId()){
            // Verifica o botão que está sendo clicado
            case R.id.CDUbtnSalvar:
                cadastrarUsuario();
                break;

            case R.id.CDUbtnExcluir:
                excluirUsuario();
                break;
        }
    }

    private void mostrarMensagem(){
        Toast.makeText(getActivity(), "O campo Teste é obrigatório!", Toast.LENGTH_LONG).show();
    }

    private void cadastrarUsuario(){

        // Regra de negócio do botão acessar
        if(CDUtxtNome.getText().toString().trim().length()== 0 && CDUtxtSenha.getText().toString().trim().length()==0){
            // Atributo da classe
            AlertDialog alerta;
            // Criar o gerador do AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            // Define a Mensagem
            builder.setMessage("Os campos Usuário e Senha são obrigatórios!");
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
            CDUtxtNome.requestFocus();
        }

        else {
            if (CDUtxtNome.getText().toString().trim().length() == 0 || CDUtxtSenha.getText().toString().trim().length() == 0) {
                if (CDUtxtNome.getText().toString().trim().length() == 0) {
                    // Atributo da classe
                    AlertDialog alerta;
                    // Criar o gerador do AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // Define a Mensagem
                    builder.setMessage("O campo Usuário é obrigatório!");
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
                    CDUtxtNome.requestFocus();
                }

                if (CDUtxtSenha.getText().toString().trim().length() == 0) {
                    //Toast.makeText(getApplication(), "O campo Senha é obrigatório!", Toast.LENGTH_LONG).show();
                    // Atributo da classe
                    AlertDialog alerta;
                    // Criar o gerador do AlertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    // Define a Mensagem
                    builder.setMessage("O campo Senha é obrigatório!");
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
                    CDUtxtSenha.requestFocus();
                }
            }
        }

        if(CDUtxtNome.getText().toString().trim().length() > 0 && CDUtxtSenha.getText().toString().trim().length() > 0) {

            DataBaseHandler db = new DataBaseHandler(
                    getActivity().getApplicationContext());

            String usuario_nome = CDUtxtNome.getText().toString();
            String usuario_senha = CDUtxtSenha.getText().toString();

            String usuarioexiste = db.verificaExisteUsuario(usuario_nome);

            if(usuarioexiste.equals("existe")){
                Toast.makeText(getActivity(), "Usuário já existente, não pode ser gravado!", Toast.LENGTH_LONG).show();
                clearText();
                CDUtxtNome.requestFocus();
            }

            if(usuarioexiste.equals("naoexiste")){

                if(CDUcbAdministrador.isChecked() == true){
                    String usuario_administrador = "SIM";
                    db.PopularTabelaUsuario(usuario_nome,usuario_senha,usuario_administrador);
                }
                if(CDUcbAdministrador.isChecked()== false){
                    String usuario_administrador = "NAO";
                    db.PopularTabelaUsuario(usuario_nome,usuario_senha,usuario_administrador);
                }

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(CDUtxtNome.getWindowToken(), 0);

                SpinnerUsuario();

                //Toast.makeText(getApplication(), "Dados gravados com sucesso!", Toast.LENGTH_LONG).show();
                Toast.makeText(getActivity(), "Usuário salvo com sucesso!", Toast.LENGTH_LONG).show();
                clearText();
                CDUtxtNome.requestFocus();
            }
        }
    }

    private void excluirUsuario(){

        // Atributo da classe
        AlertDialog alerta;
        // Criar o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Define o Titulo
        builder.setTitle("Apagar");
        // Define a Mensagem
        builder.setMessage("Deseja realmente apagar este usuário?");
        // Define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DataBaseHandler db = new DataBaseHandler(
                        getActivity().getApplicationContext());

                db.excluiUsuario(static_nome);

                Toast.makeText(getContext(), " Usuário " + static_nome + " apagado com sucesso! ",
                        Toast.LENGTH_LONG).show();

                SpinnerUsuario();

                db.VerificaUsuarioPadrao();
                //db.close(); // Closing database connection
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
        CDUtxtNome.setText("");
        CDUtxtSenha.setText("");
        CDUcbAdministrador.setChecked(false);
    }


}
