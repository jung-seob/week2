package com.example.q.week2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.net.URL;
import java.util.ArrayList;

public class recipeAdapter extends RecyclerView.Adapter<recipeAdapter.MyViewHolder> {
    ArrayList<Recipe> recipeArrayList;

    public class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name;
        public TextView ingredient;
        public TextView howToCook;
        public TextView time;
        public TextView user;
        public ImageView photo;
        public MyViewHolder(View view)
        {
            super(view);
            name = view.findViewById(R.id.nameOfRecipe);
            ingredient = view.findViewById(R.id.ingredient);
            howToCook = view.findViewById(R.id.howToCook);
            time = view.findViewById(R.id.time);
            user=view.findViewById(R.id.user);
            photo = view.findViewById(R.id.imageOfRecipe);
        }
    }
   public recipeAdapter(ArrayList<Recipe> recipeArrayList){this.recipeArrayList = recipeArrayList;}

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe,parent,false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        holder.name.setText(recipeArrayList.get(position).getName());
        holder.ingredient.setText(recipeArrayList.get(position).getIngredient());
        holder.howToCook.setText(recipeArrayList.get(position).getHowToCook());
        holder.time.setText(recipeArrayList.get(position).getTime());
        holder.user.setText(recipeArrayList.get(position).getUser());
        final String imageString = recipeArrayList.get(position).getPhoto();
        Thread thread = new Thread(){
            @Override
            public void run(){
                try{
                    URL url = new URL("http://socrip4.kaist.ac.kr:2380/api/recipe/" + recipeArrayList.get(position).getPhoto());
                   Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                   holder.photo.setImageBitmap(image);
                }
                catch(Exception e)
                {

                }
            }
        };
        thread.start();
    }
    @Override
    public int getItemCount(){return recipeArrayList.size();}

}
