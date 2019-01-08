package com.example.q.week2;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Tab3 extends Fragment {
    View rootView;
    private ArrayList<Recipe> recipeArrayList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    recipeAdapter recipeAdapter;
    JSONObject myInfo;
    SwipeRefreshLayout mSwipeRefreshLayout;
    private RequestQueue requestQueue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.tab3,container,false);
        recipeArrayList = new ArrayList<>();
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe3);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d("Tab3","on Refresh");
                onResume();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        myInfo = new JSONObject();
        try {
            myInfo.put("id", Token.ID);
            myInfo.put("name",Token.Name);
            myInfo.put("email",Token.email);
        }
        catch (Exception e)
        {

        }
        recyclerView = rootView.findViewById(R.id.recipeView);
     //   GetList();
        FloatingActionButton add = rootView.findViewById(R.id.fab2);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();

            }
        });
        return rootView;
    }
    @Override
    public void onResume(){
        Log.d("tab3","onResume");
        super.onResume();
      //  recipeArrayList = new ArrayList<>();
        GetList();
    }
    private void GetList(){
        Log.d("tab3","get list");
        if(Token.ID==null) Token.ID="";
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://socrip4.kaist.ac.kr:2380/api/recipe/" + Token.ID,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject temp = response.getJSONObject(i);
                                Recipe r = new Recipe(temp.getString("name"),temp.getString("name")+".jpg",temp.getString("ingredient"),temp.getString("howToCook"),temp.getString("time"),temp.getString("user"));
                                recipeArrayList.add(r);
                            }
                            buildRecyclerView();
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
    private void buildRecyclerView(){
        Log.d("tab3","build recyclerview");
      //  recyclerView=rootView.findViewById(R.id.recipeView);
        layoutManager = new LinearLayoutManager(getActivity());
        recipeAdapter = new recipeAdapter(recipeArrayList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);
        recyclerView.setAdapter(recipeAdapter);
      //  recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
    private void add()
    {
        Log.d("tab3","add");
        Intent intent = new Intent(getActivity(),addRecipe.class);
        getActivity().startActivity(intent);
    }
}
