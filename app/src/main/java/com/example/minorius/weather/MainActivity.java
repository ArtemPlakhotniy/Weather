package com.example.minorius.weather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);
        textView3 = (TextView) findViewById(R.id.textView3);
        textView4 = (TextView) findViewById(R.id.textView4);

        new GetData().execute();
    }

    class GetData extends AsyncTask {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String result = "";

        @Override
        protected Object doInBackground(Object[] params) {
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast?id=710791&appid=2de143494c0b295cca9337e1e96b00e0");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine()) != null){
                    buffer.append(line);
                }

                result = buffer.toString();

            }catch (Exception e){
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
            }

            return result;
        }

        @Override
        protected void onPostExecute(Object o){
            super.onPostExecute(o);
            JSONObject dataJsonObj = null;

            try{
                dataJsonObj = new JSONObject((String) o);
                JSONArray list = dataJsonObj.getJSONArray("list");
                for(int i = 0; i < list.length(); i++){
                    JSONObject element = list.getJSONObject(i);
                    JSONObject main = element.getJSONObject("main");
                    int humidity = main.getInt("humidity");
                    int temp_min = main.getInt("temp_min");
                    int temp_max = main.getInt("temp_max");
                    int temp = main.getInt("temp");

                    textView.setText(""+humidity);
                    textView2.setText(""+temp_min);
                    textView3.setText(""+temp_max);
                    textView4.setText(""+temp);
                }

            }
            catch (Exception e){
                Toast.makeText(getApplicationContext(), "error "+e, Toast.LENGTH_LONG).show();
            }

        }
    }
}
