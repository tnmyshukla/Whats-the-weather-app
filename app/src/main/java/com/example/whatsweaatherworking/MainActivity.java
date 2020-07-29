package com.example.whatsweaatherworking;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    EditText cityName;
    TextView resulttextView;
    public void checkWeather(View view){
//    Log.i("Cityname",cityName.getText().toString());
        InputMethodManager mgr=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(cityName.getWindowToken(),0);
        try {
            String encodedCityName= URLEncoder.encode( cityName.getText().toString(),"UTF-8");
            downloader task = new downloader();
            task.execute("http://api.openweathermap.org/data/2.5/weather?q=" +encodedCityName + "&appid=ab4dd5a2c2a2c1127e113f562e9e134b");
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    public class downloader extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            try{
                String result="";
                URL url=new URL(strings[0]);
                HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1){
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;
            }
            catch(Exception e){
                Toast toast1=Toast.makeText(getApplicationContext(),"Weather not found",Toast.LENGTH_LONG);
                toast1.show();
               // e.printStackTrace();

                return "FAILED";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try{
                JSONObject jsonObject=new JSONObject(result);
                String wi=jsonObject.getString("weather");
                JSONArray jsonArray=new JSONArray(wi);
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonpart=jsonArray.getJSONObject(i);
//                    Log.i("main",jsonpart.getString("main"));
//                    Log.i("description",jsonpart.getString("description"));
                    String main="";
                    String description="";
                    main=jsonpart.getString("main");
                    description=jsonpart.getString("description");
                    if(main!=""&&description!=""){
                        resulttextView.setText(main+": "+description+"\r\n");
                    }
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cityName=(EditText)findViewById(R.id.cityName);
        resulttextView=(TextView)findViewById(R.id.resulttextView);
    }
}
