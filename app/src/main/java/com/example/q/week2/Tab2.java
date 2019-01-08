package com.example.q.week2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class Tab2 extends Fragment {
    GridView galleryGridView;
    ArrayList<String> imageList = new ArrayList<>();
    View rootView;
    JSONObject myInfo;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private RequestQueue requestQueue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab2, container, false);
        imageList = new ArrayList<>();
        myInfo = new JSONObject();
        try {
            myInfo.put("id", Token.ID);
            myInfo.put("name",Token.Name);
            myInfo.put("email",Token.email);
        }
        catch (Exception e)
        {

        }
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe2);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onResume();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        galleryGridView = rootView.findViewById(R.id.gridview);
        GetList();
        galleryGridView.setAdapter(new ImageListAdapter(getContext(),imageList));
        galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("yelin","clicked");
            }
        });
        FloatingActionButton addImage = rootView.findViewById(R.id.fab2);
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();
                Snackbar.make(v, "Add a Photo to Server", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        galleryGridView = rootView.findViewById(R.id.gridview);
        GetList();
        galleryGridView.setAdapter(new ImageListAdapter(getContext(),imageList));
        galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("yelin","clicked");
            }
        });
    }
    public void pickImage() {
        Log.d("yelin","pick Image");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,2);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("yelin","on Activity Result");
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                JSONObject object = new JSONObject();
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                Bitmap image = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(inputStream),100,100,true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG,100,baos);
                byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes,Base64.DEFAULT);
                object.put("imageOwner",Token.ID);
                Random rnd = new Random();
                String name = String.valueOf(rnd.nextInt(5000));
                Log.d("naming",name);
                object.put("name",name);
                object.put("image",imageString);
                JsonSend jsonSend = new JsonSend("http://socrip4.kaist.ac.kr:2380/api/gallery",object);
                jsonSend.create();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private void GetList() {
        if(Token.ID==null) Token.ID="";
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://socrip4.kaist.ac.kr:2380/api/gallery/" + Token.ID,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            imageList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject temp = response.getJSONObject(i);
                                imageList.add(temp.getString("name"));
                            }
                        } catch (Exception e) {
                            Log.d("yelin", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }
}

