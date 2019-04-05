package wl.granjex2;

import android.os.AsyncTask;
import android.util.Log;

import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import android.R.integer;
import android.os.AsyncTask;
import android.util.Log;


public class Conexao extends AsyncTask<String, android.R.integer, String> {

    private String vEntrada = "";
    private Scanner entrada;
    private PrintStream saida;

    //Comunicação com Arduino feita via Socket
    //Adicionado Ip nos parâmetros do método
    public String Inicia(String metodo,String pIp, int porta) throws UnknownHostException, Exception {
        try{
            Socket cliente = new Socket(); //Instanciado a classe 'Socket'
            cliente.connect(new InetSocketAddress(pIp, porta), 1000); //Usado o método construtor connect (possui configuração de timeout) e passado Ip do parâmetro do método

            entrada = new Scanner(cliente.getInputStream());
            saida = new PrintStream(cliente.getOutputStream());

            saida.println(metodo);
            vEntrada = entrada.nextLine();

            saida.close();
            entrada.close();
            cliente.close();

        } catch (Exception e) {
            Log.e("CONEXAO", e.toString()); //'Log', método nativo do android
            throw new Exception("|Falha na conexão, IP: [" + pIp + "]."); //Propagado erro para o método doInBackground, usado "|" (pipe) para gerenciar mensagem
        }
        return vEntrada;
    }

    @Override
    protected String doInBackground(String... params) {
        String metodo = params[0];
        String vIp = params[1];
        int porta = Integer.valueOf(params[2]);
        //String vIp = Configuracao.vIP; //Busca o IP informado na tela de configuração.

        try {
            vEntrada = Inicia(metodo, vIp, porta); //Adicionado Ip na chamada do método
        } catch (Exception e) {
            Log.e("Background", e.toString()); //Trocado para log, método nativo do android
            vEntrada = e.toString(); //Passado o erro como retorno
        }
        return vEntrada;
    }

    //Verifica se um valor passado é numero ou não
    public boolean F_eNumero(String valor) {
        try {
            Float.parseFloat(valor); //Troca para float caso precise
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}