package com.techcom.whats_the_weather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class MainActivity extends AppCompatActivity {
  Button btn_whats_the_weather;
  TextView tv_result;
  EditText et_enter_city_name;
    HttpsURLConnection httpsURLConnection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_enter_city_name=(EditText) findViewById(R.id.et_city_name);
        btn_whats_the_weather=(Button) findViewById(R.id.btn_get_weather);
        tv_result=(TextView) findViewById(R.id.tv_weather_result);
        btn_whats_the_weather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getWeather(v);
            }
        });

    }
    public  void getWeather(View v)
    {
        String city=et_enter_city_name.getText().toString();
        Download download=new Download();
        download.execute("https://openweathermap.org/data/2.5/weather?q="+city+"&appid=b6907d289e10d714a6e88b30761fae22");
    }
    public class Download extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {
          String result="";
           URL url;
           httpsURLConnection=null;
           try{
               url=new URL(urls[0]);
               httpsURLConnection=(HttpsURLConnection) url.openConnection();
               InputStream inputStream=httpsURLConnection.getInputStream();
               InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
               int data=inputStreamReader.read();
               while(data!=-1)
               {
                   char ch=(char)data;
                   result+=ch;
                   data=inputStreamReader.read();
               }
               return result;

           } catch (MalformedURLException e) {
               e.printStackTrace();
               return null;
           } catch (IOException e) {
               e.printStackTrace();
               return null;
           }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String message="";
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo= jsonObject.getString("weather");
                JSONArray jsonArray=new JSONArray(weatherInfo);
                for(int i=0;i<jsonArray.length();i++) {
                    JSONObject object = jsonArray.getJSONObject(i);
                    String res=object.getString("main");
                    String res1=object.getString("description");
                    if(!res.equals("") && !res1.equals(""))
                    {
                        message+=res+": "+res1+"\r\n";
                    }
                }
                if(!message.equals(""))
                {
                    tv_result.setText(message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }
}
