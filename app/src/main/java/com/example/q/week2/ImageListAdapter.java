package com.example.q.week2;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class ImageListAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<String> imageList;

    public ImageListAdapter(Context context,ArrayList<String> imageList)
    {
        this.context = context;
        this.imageList = imageList;
    }
    @Override
    public int getCount() {
        return imageList.size();
    }

    @Override
    public Object getItem(int position) {
        return imageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imageView;
        if(convertView==null)
        {
            imageView=new ImageView(context);
        }
        else
        {
            imageView = (ImageView)convertView;
        }
        try {
            Log.d("imageList",imageList.get(position));
            Glide.with(context).load("http://socrip4.kaist.ac.kr:2380/api/gallery/" + imageList.get(position)).into(imageView);
        }
        catch(Exception e)
        {
            Log.d("yelin 123",e.getMessage());
        }
        return imageView;
    }
}
