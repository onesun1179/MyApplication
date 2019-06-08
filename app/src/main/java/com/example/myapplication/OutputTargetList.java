package com.example.myapplication;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class OutputTargetList extends AsyncTask<String, Integer, String> {

    String Tables_in_cpbike;


    @Override
    protected String doInBackground(String... params) {
        StringBuilder jsonHtml = new StringBuilder();
        try {
            URL phpUrl = new URL(params[0]);
            HttpURLConnection conn = (HttpURLConnection)phpUrl.openConnection();

            if ( conn != null ) {
                conn.setConnectTimeout(10000);
                conn.setUseCaches(false);

                if ( conn.getResponseCode() == HttpURLConnection.HTTP_OK ) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    while ( true ) {
                        String line = br.readLine();
                        if ( line == null )
                            break;
                        jsonHtml.append(line + "\n");
                    }
                    br.close();
                }
                conn.disconnect();
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        return jsonHtml.toString();
    }

    @Override
    protected void onPostExecute(String str) {
        int count=0;
        try {
            // PHP에서 받아온 JSON 데이터를 JSON오브젝트로 변환
            JSONObject jObject = new JSONObject(str);
            // results라는 key는 JSON배열로 되어있다.
            JSONArray results = jObject.getJSONArray("results");

            for (int i=0; i < results.length(); i++) {

                JSONObject temp = results.getJSONObject(i);
                Tables_in_cpbike = "" + temp.get("Tables_in_cpbike");
                if(Tables_in_cpbike.contains("record"+CreateCourseActivity.selectedNum))
                    CreateCourseActivity.targetIDvector.add(Tables_in_cpbike);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}