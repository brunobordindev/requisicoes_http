package br.com.requisicoeshttp;

import android.databinding.DataBindingUtil;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import br.com.requisicoeshttp.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.btn.setOnClickListener( view -> {
            MyTask task = new MyTask();

            //site de recuperar api de valores do bitcoin em moedas e recupera o cep
            String urlApi = "https://blockchain.info/ticker";
            String urlDollar = "https://blockchain.info/tobtc?currency=USD&value=500";
            String urlCepApi = "https://viacep.com.br/ws/01001000/json/";
            String urlBiticoin = "https://www.mercadobitcoin.net/api/BTC/ticker/";
            String urlMoedas = "https://economia.awesomeapi.com.br/json/last/USD-BRL";
            task.execute(urlMoedas);
        });
    }

    //consultar os dados da api
    // primeiro valor quero uma url - string
    // segundo parametr é o progresso
    //terceiro é o result o retorno
    class MyTask extends AsyncTask<String, Void, String >{

        @Override
        protected String doInBackground(String... strings) {

            //recupera o parametro do task.execute(urlApi), pode ter mias paramentros dentro dele.
            String stringUrl = strings[0];
            InputStream inputStream = null;
            InputStreamReader inputStreamReader = null;
            StringBuffer buffer = null;

            try {

                URL url = new URL(stringUrl);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();

                //recuoera os dados em  Bytes
                inputStream = conexao.getInputStream();

                //fazer a conversao do Bytes
                // inputStreamReader  lê os dados em Bytes e decodifica para caracteres
                inputStreamReader = new InputStreamReader(inputStream);

                //preciso convertes os caracteres para string
                // Objeto utilizado para leitura dos caracteres do inputStreamReader
                BufferedReader reader = new BufferedReader(inputStreamReader);

                buffer = new StringBuffer();
                String linha = "";

                //readLine() lê linha a linha
                while ((linha = reader.readLine()) != null){
                    buffer.append(linha);
                }



            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return buffer.toString();
        }

        @Override
        protected void onPostExecute(String resultado) {
            super.onPostExecute(resultado);

            /* CEP
            String cep = null;
            String rua = null;
            String complemento = null;
            String bairro = null;
            String cidade = null;
            String estado = null;
             */

            String objetoValor = null;
            String valorMoeda= null;
            String simbolo = null;

            try {
                /* Cep
                JSONObject jsonObject = new JSONObject(resultado);
                rua = jsonObject.getString("logradouro");
                cep = jsonObject.getString("cep");
                complemento = jsonObject.getString("complemento");
                bairro = jsonObject.getString("bairro");
                cidade = jsonObject.getString("localidade");
                estado = jsonObject.getString("uf");
                 */

                JSONObject jsonObject = new JSONObject(resultado);
                objetoValor = jsonObject.getString("BRL");

                JSONObject jsonObjectReal = new JSONObject(objetoValor);
                valorMoeda = jsonObjectReal.getString("last");
                simbolo = jsonObjectReal.getString("symbol");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            binding.textResult.setText(resultado);

//            binding.textResult.setText(rua);
//            binding.textResult.setText("Rua: " + rua + ", \n" +
//                    "Complemento: " + complemento + ", \n" +
//                    "CEP: " + cep + ", \n" +
//                    "Cidade: " + cidade + ", \n" +
//                    "Bairro: " + bairro + ", \n" +
//                    "Estado: " + estado + ", \n" );

//            binding.textResult.setText(objetoValor);
//            binding.textResult.setText(simbolo + ": " + valorMoeda);
        }
    }
}