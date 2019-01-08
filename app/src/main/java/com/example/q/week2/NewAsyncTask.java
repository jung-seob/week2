package com.example.q.week2;

import android.os.AsyncTask;

import org.json.JSONObject;

import java.util.ArrayList;

public class NewAsyncTask extends AsyncTask<String, Void, String> {
    public String result;
    private String url_string;
    private JSONObject json;
    private int statusCode;
    private ArrayList<contact_item> contactList;
    private ArrayList<String> imageList;



    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... params) {
        return result;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

}
