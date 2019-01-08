package com.example.q.week2;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONObject;

import java.util.ArrayList;

public class Tab3 extends Fragment {
    View rootView;
    private ArrayList<Recipe> recipeArrayList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    recipeAdapter recipeAdapter;
    JSONObject myInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.tab3,container,false);
        myInfo = new JSONObject();
        try {
            myInfo.put("id", Token.ID);
            myInfo.put("name",Token.Name);
            myInfo.put("email",Token.email);
        }
        catch (Exception e)
        {

        }
        GetList();
        buildRecyclerView();
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
        super.onResume();

    }
    private void GetList(){
        JsonSend jsonSend = new JsonSend("http://socrip4.kaist.ac.kr:2380/api/recipe",myInfo);
        recipeArrayList = jsonSend.getAllRecipe();
    }
    private void buildRecyclerView(){
        recyclerView=rootView.findViewById(R.id.recipeView);
        layoutManager = new LinearLayoutManager(getActivity());
        recipeAdapter = new recipeAdapter(recipeArrayList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);
        recyclerView.setAdapter(recipeAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }
    private void add()
    {
        Log.d("tab3","add");
        Intent intent = new Intent(getActivity(),addRecipe.class);
        getActivity().startActivity(intent);
    }
}
