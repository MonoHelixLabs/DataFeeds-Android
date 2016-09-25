package com.monohelixlabs.datafeeds;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends ListActivity {

    public MainActivity(){}

    ListView lv;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setListAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, this.fetchTwitterPublicTimeline()));

        lv = (ListView)findViewById(android.R.id.list);

        new RESTApiManager().execute();
    }


    private class RESTApiManager extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            String result = "";
            try {
                URL restAPI = new URL("https://io.adafruit.com/api/feeds?x-aio-key="); //Add AIO key here
                URLConnection tc = restAPI.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(tc.getInputStream()));
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = in.readLine()) != null) {
                    //JSONArray ja = new JSONArray(line);

                    //for (int i = 0; i < ja.length(); i++) {
                    //    JSONObject jo = (JSONObject) ja.get(i);
                    //    listItems.add(jo.getString("name"));
                    //}

                    sb.append(line + "\n");
                }

                result = sb.toString();


            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return result;
        }

        protected void onPostExecute(String result) {

            ArrayList<String> listItems = new ArrayList<String>();
            try {
                JSONArray json = new JSONArray(result);

                for (int i = 0; i < json.length(); i++) {
                    JSONObject feed = json.getJSONObject(i);
                    listItems.add(feed.getString("name") + ": " + feed.get("last_value"));
                }

            } catch (JSONException e) {
                // manage exceptions
            }

            lv.setAdapter(new ArrayAdapter(MainActivity.this, android.R.layout.simple_list_item_1, listItems));

        }

    }


}