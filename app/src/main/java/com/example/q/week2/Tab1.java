package com.example.q.week2;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Tab1 extends Fragment {
    View rootView;
    EditText editText;
    private ArrayList<contact_item> arrayList;
    private ArrayList<contact_item> filteredList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    contactListAdapter listAdapter;
    JSONObject myInfo;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab1, container, false);
        myInfo = new JSONObject();
        try {
            myInfo.put("id", "45645654654");
            myInfo.put("name","정예린");
            myInfo.put("email","jyl7464@naver.com");
        }
        catch (Exception e)
        {

        }
        //내정보 페북 token이용해서 전체로 가져오기!!
        //임시로 바로 지정해주었음
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_DENIED) {
            arrayList = GetList();
            buildRecyclerView();
            editText = rootView.findViewById(R.id.search);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    filter(s.toString());
                }
            });
            rootView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(v);
                    return false;
                }
            });

            editText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(v);
                    return false;
                }
            });

            recyclerView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideKeyboard(v);
                    return false;
                }
            });
        }

        FloatingActionButton add = rootView.findViewById(R.id.fab);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addContactToServer();
                Snackbar.make(v, "Add All Contact to Server", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        return rootView;
    }
    @Override
    public void onResume() {
        Log.d("yelin","onResume");
        super.onResume();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_DENIED) {
            ArrayList<contact_item> arrayList;
            arrayList = GetList();
            RecyclerView recyclerView = rootView.findViewById(R.id.contactView);
            recyclerView.setHasFixedSize(true);
            contactListAdapter listAdapter = new contactListAdapter(arrayList);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.scrollToPosition(0);
            recyclerView.setAdapter(listAdapter);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    private ArrayList<contact_item> GetList() {
        Log.d("yelin","GetList");
        ArrayList<contact_item> persons = new ArrayList();
        JsonSend jsonSend = new JsonSend("http://socrip4.kaist.ac.kr:2380/api/contact",myInfo);
        persons= jsonSend.getAllContact();
        return persons;
    }
    private void buildRecyclerView()
    {
        Log.d("yelin","build Recycler View");
        recyclerView = rootView.findViewById(R.id.contactView);
        layoutManager = new LinearLayoutManager(getActivity());
        listAdapter = new contactListAdapter(arrayList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        Log.d("yelin","end build Recycler View");
    }
    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    private void filter(String text)
    {
        filteredList = new ArrayList<>();
        for(contact_item i : arrayList)
        {
            if(i.getName().toLowerCase().contains(text.toLowerCase()) || i.getNumber().toLowerCase().contains(text.toLowerCase()) || check(i.getNumber()).contains(check(text.toLowerCase())))
            {
                filteredList.add(i);
            }
        }
        listAdapter.filterList(filteredList);
        recyclerView.setAdapter(listAdapter);
    }

    private String check(String s)
    {
        s = s.toLowerCase();
        if(s.contains("-"))
        {
            s = s.replaceAll("-","");
        }
        return s;
    }
    public void addContactToServer()
    {
        Log.d("yelin","add start");
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        };
        String[] selectionArgs = null;
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        Cursor contactCursor = getContext().getContentResolver().query(uri,projection,null,selectionArgs,sortOrder);
        if(contactCursor.moveToFirst()){
            do{
                JSONObject people = new JSONObject();
                try {
                    people.put("name", contactCursor.getString(1));
                    people.put("phone", contactCursor.getString(0));
                    people.put("contactOwner","45645654654");
                Uri photo_uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactCursor.getLong(2));
                InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getContext().getContentResolver(),photo_uri);
                Bitmap image;
                Bitmap resized;
                if(input==null)
                {
                    Log.d("yelin","null");
                    people.put("image","0");
                    people.put("hasImage",0);
                }
                else
                {
                    Log.d("yelin","not null");
                    image = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(input),50,50,true);
                    resized = Bitmap.createScaledBitmap(image, 10, 10, true);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte[] imageBytes = baos.toByteArray();
                    String imageString = Base64.encodeToString(imageBytes,Base64.DEFAULT);
                    people.put("image",imageString);
                    Log.d("yelin","sibal");
                    people.put("hasImage",1);
                }
                }
                catch(Exception e)
                {
                    Log.d("yelinnnn",e.getMessage());
                }
                JsonSend send = new JsonSend("http://socrip4.kaist.ac.kr:2380/api/contact",people);
                send.retriveContactByOwner();
            }while(contactCursor.moveToNext());
        }
    }
}