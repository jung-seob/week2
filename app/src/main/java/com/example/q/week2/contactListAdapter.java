package com.example.q.week2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.ArrayList;

public class contactListAdapter extends RecyclerView.Adapter<contactListAdapter.MyViewHolder> {
    ArrayList<contact_item> contactList;
    public static class MyViewHolder extends RecyclerView.ViewHolder
    {
        public TextView name_view;
        public TextView num_view;
        public ImageView photo_view;
        public MyViewHolder(View view)
        {
            super(view);
            name_view = view.findViewById(R.id.name);
            num_view = view.findViewById(R.id.number);
            photo_view = view.findViewById(R.id.photo);
        }
    }

    public contactListAdapter(ArrayList<contact_item> contactList){this.contactList = contactList;}
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item, parent, false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position)
    {
        Log.d("yelin","on Bind View Holder");
        holder.name_view.setText(contactList.get(position).getName());
        holder.num_view.setText(contactList.get(position).getNumber());
       if(contactList.get(position).getHasImage()==1) {
            final String imageString = contactList.get(position).getImage();
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        URL url = new URL("http://socrip4.kaist.ac.kr:2380/api/image/" + contactList.get(position).getNumber() + ".jpg");
                        Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                        holder.photo_view.setImageBitmap(image);
                    } catch (Exception e) {
                        Log.d("yelin bb", e.getMessage());
                    }
                }
            };
            thread.start();
       }
    }

    @Override
    public int getItemCount()
    {
        return contactList.size();
    }

    public void filterList(ArrayList<contact_item> filteredList) {
        contactList = filteredList;
        notifyDataSetChanged();
    }
}
