package com.example.q.week2;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class Tab2 extends Fragment {
    static final int REQUEST_PERMISSION_KEY = 2051;
    LoadAlbum loadAlbumTask;
    GridView galleryGridView;
    ArrayList<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("yelin","start create view tab2");
        rootView = inflater.inflate(R.layout.tab2, container, false);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            galleryGridView = rootView.findViewById(R.id.gridview);
            int iDisplayWidth = getResources().getDisplayMetrics().widthPixels ;
            Resources resources = getActivity().getApplicationContext().getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            float dp = iDisplayWidth / (metrics.densityDpi / 160f);
            if(dp < 360)
            {
                dp = (dp - 17) / 2;
                float px = Function.convertDpToPixel(dp, getActivity().getApplicationContext());
                galleryGridView.setColumnWidth(Math.round(px));
            }

            galleryGridView.setAdapter(new AlbumAdapter(getActivity(), albumList));
            galleryGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Log.d("yelin","clicked");
                }
            });
        }
        return rootView;
    }

    public static HashMap<String, String> mappingInbox(String path, String timestamp, String time)
    {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("path", path);
        map.put("date", time);
        return map;
    }

    public static String convertToTime(String timestamp)
    {
        long datetime = Long.parseLong(timestamp);
        Date date = new Date(datetime);
        DateFormat formatter = new SimpleDateFormat("dd/MM HH:mm");
        return formatter.format(date);
    }

    class LoadAlbum extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            albumList.clear();
        }

        protected String doInBackground(String... args) {
            Log.d("yelin", "do in Background");
            String xml = "";
            String path = null;
            String timestamp = null;
            String[] projection = {MediaStore.Images.Media._ID,
                    MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media.DISPLAY_NAME,
                    MediaStore.Images.Media.DATE_MODIFIED};

            Cursor imageCursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);

            if (imageCursor != null && imageCursor.moveToLast()) {
                do {
                    path = imageCursor.getString(imageCursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA));
                    timestamp = imageCursor.getString(imageCursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED));
                    albumList.add(mappingInbox(path, timestamp, convertToTime(timestamp)));
                } while (imageCursor.moveToPrevious());
                imageCursor.close();
            }
            return xml;
        }

    }

    @Override
    public void onResume() {
        Log.d("yelin","on resume");
        super.onResume();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            loadAlbumTask = new LoadAlbum();
            loadAlbumTask.execute();
        }
        else
        {
            String[] PERMISSIONS = {Manifest.permission.READ_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(getActivity(), PERMISSIONS, REQUEST_PERMISSION_KEY);
        }
    }
}

class AlbumAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<HashMap<String, String>> data;

    public AlbumAdapter(Activity a, ArrayList<HashMap<String, String>> d){
        activity = a;
        data = d;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AlbumViewHolder holder = null;
        if (convertView == null) {
            holder = new AlbumViewHolder();
            convertView = LayoutInflater.from(activity).inflate(R.layout.album_row, parent, false);
            holder.galleryImage = convertView.findViewById(R.id.galleryImage);
            convertView.setTag(holder);
        } else {
            holder = (AlbumViewHolder) convertView.getTag();
        }
        holder.galleryImage.setId(position);
        HashMap<String, String> song = new HashMap<>();
        song = data.get(position);
        try{
            Glide.with(activity).load(new File(song.get("path"))).into(holder.galleryImage);
        } catch (Exception e) {
            Log.d("yelin",e.getMessage());
        }
        return convertView;
    }
}

class AlbumViewHolder {
    ImageView galleryImage;
}
