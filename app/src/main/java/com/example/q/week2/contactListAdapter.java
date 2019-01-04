package com.example.q.week2;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        holder.name_view.setText(contactList.get(position).getName());
        holder.num_view.setText(contactList.get(position).getNumber());
        holder.photo_view.setImageBitmap(contactList.get(position).getPhoto());
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
