package wl.granjex2;

import java.util.List;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v4.util.SparseArrayCompat;
import android.widget.Toast;

public class DataBaseHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Banco de Dados
    private static final String BANCO_DE_DADOS = "GRANJEX";

    // Tabelas
    private static final String TABELA_USUARIO = "USUARIO";
    private static final String TABELA_GRANJA = "GRANJA";
    private static final String TABELA_HISTORICO = "HISTORICO";

    // Tabela USUARIO
    private static final String USUARIO_ID = "USUARIO_ID";
    private static final String USUARIO_NOME = "USUARIO_NOME";
    private static final String USUARIO_SENHA = "USUARIO_SENHA";
    private static final String USUARIO_ADMINISTRADOR = "USUARIO_ADMINISTRADOR";

    //Tabela GRANJA
    private static final String GRANJA_ID = "GRANJA_ID";
    private static final String GRANJA_IP = "GRANJA_IP";
    private static final String GRANJA_NOME = "GRANJA_NOME";

    //Tabela Histórico
    private static final String HISTORICO_ID = "HISTORICO_ID";
    private static final String HISTORICO_GRANJA = "HISTORICO_GRANJA";//valor do IP
    private static final String HISTORICO_DATA = "HISTORICO_DATA";
    private static final String HISTORICO_UMIDADE = "HISTORICO_UMIDADE";
    private static final String HISTORICO_TEMPERATURA = "HISTORICO_TEMPERATURA";


    public DataBaseHandler(Context context) {
        super(context, BANCO_DE_DADOS, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Category table create query
        String CRIA_TABELA_USUARIO = "CREATE TABLE "
                + TABELA_USUARIO + "("
                + USUARIO_ID + " INTEGER PRIMARY KEY, "
                + USUARIO_NOME + " TEXT, "
                + USUARIO_SENHA + " TEXT, "
                + USUARIO_ADMINISTRADOR + " TEXT )";

        String CRIA_TABELA_GRANJA = "CREATE TABLE "
                + TABELA_GRANJA + "("
                + GRANJA_ID + " INTEGER PRIMARY KEY, "
                + GRANJA_IP + " TEXT, "
                + GRANJA_NOME + " TEXT )";

        String CRIA_TABELA_HISTORICO = "CREATE TABLE "
                + TABELA_HISTORICO + "("
                + HISTORICO_ID + " INTEGER PRIMARY KEY, "
                + HISTORICO_GRANJA + " TEXT, "
                + HISTORICO_DATA + " TEXT, "
                + HISTORICO_UMIDADE+ " TEXT, "
                + HISTORICO_TEMPERATURA + " TEXT )";

        db.execSQL(CRIA_TABELA_USUARIO);
        db.execSQL(CRIA_TABELA_GRANJA);
        db.execSQL(CRIA_TABELA_HISTORICO);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_USUARIO);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_GRANJA);
        db.execSQL("DROP TABLE IF EXISTS " + TABELA_HISTORICO);

        // Create tables again
        onCreate(db);
    }

    public void PopularTabelaUsuario(String usuario_nome, String usuario_senha, String usuario_administrador){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(USUARIO_NOME, usuario_nome);
        values.put(USUARIO_SENHA, usuario_senha);
        values.put(USUARIO_ADMINISTRADOR, usuario_administrador);

        // Inserting Row
        db.insert(TABELA_USUARIO, null, values);
        db.close(); // Closing database connection
    }

    public void PopularTabelaGranja(String granja_ip, String granja_nome){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(GRANJA_IP, granja_ip);
        values.put(GRANJA_NOME, granja_nome);

        // Inserting Row
        db.insert(TABELA_GRANJA, null, values);

        db.close(); // Closing database connection
    }

    public void PopularTabelaHistorico(String historico_granja, String historico_data, String historico_umidade, String historico_temperatura ){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HISTORICO_GRANJA, historico_granja); //Sendo IP
        values.put(HISTORICO_DATA, historico_data);
        values.put(HISTORICO_UMIDADE, historico_umidade);
        values.put(HISTORICO_TEMPERATURA, historico_temperatura);


        // Inserting Row
        db.insert(TABELA_HISTORICO, null, values);

        db.close(); // Closing database connection
    }

    public void VerificaUsuarioPadrao(){

        SQLiteDatabase db = this.getWritableDatabase();

        //Realiza a ação de verificar no banco de dados se o usuário Granjex existe
        Cursor c = db.rawQuery("SELECT * FROM " + TABELA_USUARIO + " WHERE "+ USUARIO_NOME + " = 'Granjex' ",null);

        //Verifica se na base de dados o usuário padrão já existe, se sim, não faz nada
        if(c.moveToFirst()) {

        }
        else {
            //Insere na base de dados o usuário Padrão quando ele não existe já no momento do Login!
            db.execSQL("INSERT INTO " + TABELA_USUARIO + " VALUES('1','Granjex','123','SIM');");
        }
    }
    // Validando Usuario e Senha
    public String validaUsuarioSenha(String usuario_nome, String usuario_senha) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABELA_USUARIO + " WHERE " + USUARIO_NOME + " ='" +
                usuario_nome + "' and " + USUARIO_SENHA + " = '" + usuario_senha + "';", null);

        if (c.moveToFirst()) {

             //validaUsuarioSenhaADM();
            return "valido";

        } else {
            return "invalido";
        }
    }

    public String verificaExisteUsuario(String usuario_nome){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABELA_USUARIO + " WHERE " + USUARIO_NOME + " ='" +
                usuario_nome + "';", null);

        if (c.moveToFirst()) {
            return "existe";
        } else {
            return "naoexiste";
        }
    }

    public String verificaExisteGranja(String granja_nome){
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABELA_GRANJA + " WHERE " + GRANJA_NOME + " ='" +
                granja_nome + "';", null);

        if (c.moveToFirst()) {
            return "existe";

        } else {
            return "naoexiste";
        }
    }
    //Valida Usuario e Senha
    public String validaUsuarioSenhaADM(String usuario_nome, String usuario_senha) {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABELA_USUARIO + " WHERE " + USUARIO_NOME + " ='" +
                usuario_nome + "' and " + USUARIO_SENHA + " = '" + usuario_senha + "' and " + USUARIO_ADMINISTRADOR + " = 'SIM';", null);

        if (c.moveToFirst()) {

        return "administradorsim";

        }
        else{
            return "administradornao";
        }
    }

    public List<String> ConsultaUsuarios(){
        List<String> usuario = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABELA_USUARIO + " ORDER BY "+ USUARIO_NOME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                usuario.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return usuario;
    }

    public List<String> ConsultaGranjas(){
        List<String> granja = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABELA_GRANJA + " ORDER BY "+ GRANJA_NOME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                granja.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return granja;
    }

    public List<String> ConsultaHistorico(String granja){
        List<String> historico = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM "
                + TABELA_HISTORICO
                + " WHERE "
                + HISTORICO_GRANJA // valor do IP
                + "= '"
                + granja
                + "' "
                + " ORDER BY "+ HISTORICO_ID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                historico.add("IP: " + cursor.getString(1));
                historico.add("Data: " + cursor.getString(2));
                historico.add("Umidade: " + cursor.getString(3));
                historico.add("Temperatura: " + cursor.getString(4));
                historico.add("");

            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return historico;
    }

    public void excluiUsuario(String usuario_nome){

        SQLiteDatabase db = this.getWritableDatabase();

        // Inserting Row
        db.delete(TABELA_USUARIO, USUARIO_NOME + " =?" ,new String[] {usuario_nome});
        db.close(); // Closing database connection
    }

    public void excluiGranja(String granja_nome){

        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABELA_GRANJA, GRANJA_NOME + "=?", new String[]{granja_nome});

        db.close(); // Closing database connection
    }

    public void excluiHistorico(String granja_ip){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABELA_HISTORICO, HISTORICO_GRANJA + "=?", new String[]{granja_ip});

        db.close(); // Closing database connection
    }

    public String encontraGranja(String granja){

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor c = db.rawQuery("SELECT * FROM " + TABELA_GRANJA + " WHERE " + GRANJA_NOME + " ='" +
                granja + "';", null);

        if (c.moveToFirst()) {
            return c.getString(1);
        } else{
            return null;
        }
    }

}
