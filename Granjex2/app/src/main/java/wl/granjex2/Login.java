package wl.granjex2;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements View.OnClickListener {

    //Criação dos objetos
    EditText txtLoginUsuario;
    EditText txtLoginSenha;
    Button btnLoginAcessar;
    Button btnLoginSair;

    //Declaração da variável do Tipo Banco de Dados
    SQLiteDatabase db;

    //Criação do Objeto de inicializa a atividade
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR);
        setContentView(R.layout.activity_login);

        //Vinculando os objetos aos seus IDs
        txtLoginUsuario = (EditText) findViewById(R.id.txtLoginUsuario);
        txtLoginSenha = (EditText) findViewById(R.id.txtLoginSenha);
        btnLoginAcessar = (Button) findViewById(R.id.btnLoginAcessar);
        btnLoginSair = (Button) findViewById(R.id.btnLoginSair);

        // Programando o botão, atribuindo ao setOnClickListener
        btnLoginAcessar.setOnClickListener(this);
        btnLoginSair.setOnClickListener(this);

        txtLoginUsuario.requestFocus();

        //Criando o Banco de Dados
        DataBaseHandler db = new DataBaseHandler(getApplicationContext());
        db.VerificaUsuarioPadrao();
    }

    //Método que trata o evento dos botões
    @Override
    public void onClick (View v){
        switch (v.getId()){
            // Verifica se é o botão Acessar que está sendo clicado
            case R.id.btnLoginAcessar:
                validaUsuarioLogin();
                txtLoginUsuario.requestFocus();
                break;
            case R.id.btnLoginSair:
                onBackPressed();
                txtLoginUsuario.requestFocus();
                break;
        }
    }

    //Evento ao tentar sair da tela de Login
    @Override
    public void onBackPressed() {
        // Atributo da classe
        AlertDialog alerta;
        // Criar o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Define o Titulo
        builder.setTitle("Sair");
        // Define a Mensagem
        builder.setMessage("Deseja realmente sair?");
        // Define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                //deletaUsuarioPadrao();
                finish();
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

    //Se uma atividade filha parar, a atividade Login também para.
    @Override
    public void onResume(){
        super.onResume();
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
    }

    //Validação de Login
    public void validaUsuarioLogin(){

        //Regra de negócio do botão acessar
        if(txtLoginUsuario.getText().length()== 0 && txtLoginSenha.getText().length()==0){
            Toast.makeText(getApplication(),"Os campos Usuário e Senha são obrigatórios!",Toast.LENGTH_LONG).show();
        }

        else {
            if (txtLoginUsuario.getText().length() == 0 || txtLoginSenha.getText().length() == 0) {
                if (txtLoginUsuario.getText().length() == 0) {
                    Toast.makeText(getApplication(), "O campo Usuário é obrigatório!", Toast.LENGTH_LONG).show();
                    txtLoginUsuario.requestFocus();
                }
                if (txtLoginSenha.getText().length() == 0) {
                    Toast.makeText(getApplication(), "O campo Senha é obrigatório!", Toast.LENGTH_LONG).show();
                    txtLoginSenha.requestFocus();
                }
            }
        }
        if(txtLoginUsuario.getText().length() > 0 && txtLoginSenha.getText().length() > 0){

            DataBaseHandler db = new DataBaseHandler(getApplicationContext());
            String usuarioexiste =   db.validaUsuarioSenha(txtLoginUsuario.getText().toString(),txtLoginSenha.getText().toString());

            if(usuarioexiste.equals("invalido"))
            {
                // Atributo da classe
                AlertDialog alerta;
                // Criar o gerador do AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                // Define o Titulo
                builder.setTitle("Erro");
                // Define a Mensagem
                builder.setMessage("Usuário e Senha incorretos!");
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

            if(usuarioexiste.equals("valido")){

                String administrador = db.validaUsuarioSenhaADM(txtLoginUsuario.getText().toString(),txtLoginSenha.getText().toString());

                if(administrador.equals("administradorsim")){

                    Toast.makeText(getApplication(),"Seja Bem Vindo " + txtLoginUsuario.getText().toString()+"!",Toast.LENGTH_LONG).show();

                    clearText();

                    Intent Principal = new Intent(getApplicationContext(), Main.class);

                    Principal.putExtra("administrador", "SIM");
                    startActivity(Principal);

                }
                if(administrador.equals("administradornao")){

                    Toast.makeText(getApplication(), "Seja Bem Vindo " + txtLoginUsuario.getText().toString() + "!", Toast.LENGTH_LONG).show();
                    // Limpando os dados digitados
                    clearText();

                    Intent Principal = new Intent(getApplicationContext(), Main.class);

                    Principal.putExtra("administrador", "NAO");
                    startActivity(Principal);

                }
            }
        }
    }

    //Deletar Usuario Padrão
    /*
    public void deletaUsuarioPadrao(){

        Cursor c = db.rawQuery("SELECT * FROM tabela_usuario WHERE Usuario ='Granjex'",null);

        if(c.moveToFirst()){
            db.execSQL("DELETE FROM tabela_usuario WHERE Usuario = 'Granjex';");
            //showMessage("Sucesso","Tabela Deletada");
            db.close(); // Closing database connection
        }
        clearText();
    }
    */

    //clearText
    public void clearText(){
        txtLoginUsuario.setText("");
        txtLoginSenha.setText("");
    }
}
