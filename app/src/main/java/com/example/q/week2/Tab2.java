package com.example.q.week2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class Tab2 extends Fragment {
    GridView galleryGridView;
    ArrayList<String> imageList = new ArrayList<>();
    View rootView;
    JSONObject myInfo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab2, container, false);
        myInfo = new JSONObject();
        try {
            myInfo.put("id", "45645654654");
            myInfo.put("name","정예린");
            myInfo.put("email","jyl7464@naver.com");
        }
        catch (Exception e)
        {

        }
        galleryGridView = rootView.findViewById(R.id.gridview);
        JsonSend jsonSend = new JsonSend("http://socrip4.kaist.ac.kr:2380/api/gallery",myInfo);
        imageList = jsonSend.getAllImage();
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
                Snackbar.make(v, "Add All Contact to Server", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        galleryGridView = rootView.findViewById(R.id.gridview);
        JsonSend jsonSend = new JsonSend("http://socrip4.kaist.ac.kr:2380/api/gallery",myInfo);
        imageList = jsonSend.getAllImage();
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
                //Display an error
                return;
            }
            try {
                JSONObject object = new JSONObject();
                InputStream inputStream = getContext().getContentResolver().openInputStream(data.getData());
                Bitmap image = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(inputStream),50,50,true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG,100,baos);
                byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes,Base64.DEFAULT);
                object.put("imageOwner",myInfo.getString("id"));
                object.put("name","a");
                object.put("image",imageString);
                JsonSend jsonSend = new JsonSend("http://socrip4.kaist.ac.kr:2380/api/gallery",object);
                jsonSend.create();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}

