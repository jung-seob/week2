package com.example.q.week2;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class JsonSend {
    private String url_string;
    private JSONObject json;
    private int statusCode;
    private ArrayList<contact_item> contactList;
    private ArrayList<String> imageList;

    public JsonSend(String url, JSONObject json) {
        this.url_string = url;
        this.json = json;
    }
    public void create()
    {
        Log.d("yelin","start create");
        Thread newThread = new Thread()
        {
          @Override
          public void run()
          {
              try {
                  URL url = new URL(url_string);
                  HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                  connection.setRequestMethod("POST");
                  connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                  connection.setRequestProperty("Accept","application/json");
                  connection.setDoOutput(true);
                  OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
                  wr.write(json.toString());
                  Log.d("yelin json to string",json.toString());
                  wr.flush();
                  BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                  String line;
                  while((line=serverAnswer.readLine())!=null)
                  {
                      Log.d("yelin aa",line);
                  }
                  wr.close();
                  serverAnswer.close();
              } catch (Exception e) {
                  Log.d("yelin error",e.getMessage());
              }
          }
        };
        newThread.start();
    }
    public void retriveUserById()
    {
        Thread newThread = new Thread()
        {
            @Override
            public void run()
            {
                try {
                    URL url = new URL(url_string+ "/"+json.optString("id"));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    statusCode = connection.getResponseCode();
                    if(statusCode==200)
                    {
                        Log.d("yelin","already register");
                    }
                    else if(statusCode == 404)
                    {
                        create();
                    }
                } catch (Exception e) {
                    Log.d("yelin",e.getMessage());
                }
            }
        };
        newThread.start();
    }
    public void retriveContactByOwner()
    {
        Thread newThread = new Thread()
        {
            @Override
            public void run()
            {
                try {
                    URL url = new URL(url_string+ "/"+json.optString("contactOwner")+"/"+json.optString("phone"));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    statusCode = connection.getResponseCode();
                    Log.d("yelin",String .valueOf(statusCode));
                    if(statusCode==200)
                    {
                        Log.d("yelin","already has that number");
                    }
                    else if(statusCode == 404)
                    {
                       create();
                    }
                } catch (Exception e) {
                    Log.d("yelin",e.getMessage());
                }
            }
        };
        newThread.start();
    }

    public ArrayList<contact_item> getAllContact()
    {
        Log.d("yelin","getList in Json send");
        contactList = new ArrayList<contact_item>();
        Thread newThread = new Thread()
        {
            @Override
            public void run()
            {
                try {
                    URL url = new URL(url_string+ "/"+json.optString("id"));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    statusCode = connection.getResponseCode();
                    Log.d("yelin",String .valueOf(statusCode));
                    BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    JSONArray jsonArray = new JSONArray();
                    if((line=serverAnswer.readLine())!=null)
                    {
                        jsonArray = new JSONArray(line);
                    }
                    for(int i = 0 ; i <jsonArray.length();i++)
                    {
                        JSONObject temp = (JSONObject) jsonArray.get(i);
                        String name = temp.getString("name");
                        String phone = temp.getString("phone");
                        String image = temp.getString("image");
                        int hasImage = temp.getInt("hasImage");
                        contactList.add(new contact_item(name,phone,image,hasImage));
                    }
                    serverAnswer.close();
                } catch (Exception e) {
                    Log.d("yelin",e.getMessage());
                }
            }
        };
        newThread.start();
        try{
            newThread.join();
        }
        catch(Exception e)
        {

        }
        return contactList;
    }
    public ArrayList<String> getAllImage()
    {
        Log.d("yelin","getList in Json send");
        imageList = new ArrayList<String>();
        Thread newThread = new Thread()
        {
            @Override
            public void run()
            {
                try {
                    URL url = new URL(url_string+ "/"+json.optString("id"));
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    statusCode = connection.getResponseCode();
                    Log.d("yelin",String .valueOf(statusCode));
                    BufferedReader serverAnswer = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    JSONArray jsonArray = new JSONArray();
                    if((line=serverAnswer.readLine())!=null)
                    {
                        jsonArray = new JSONArray(line);
                    }
                    for(int i = 0 ; i <jsonArray.length();i++)
                    {
                        JSONObject temp = (JSONObject) jsonArray.get(i);
                       imageList.add(temp.getString("name"));
                       Log.d("yelin path",imageList.get(i));
                    }
                    serverAnswer.close();
                } catch (Exception e) {
                    Log.d("yelin",e.getMessage());
                }
            }
        };
        newThread.start();
        try{
            newThread.join();
        }
        catch(Exception e)
        {

        }
        return imageList;
    }
}
