package com.example.q.week2;

import android.Manifest;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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

public class Tab1 extends Fragment {
    View rootView;
    EditText editText;
    private ArrayList<contact_item> arrayList;
    private ArrayList<contact_item> filteredList;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    contactListAdapter listAdapter;
    SwipeRefreshLayout mSwipeRefreshLayout;
    JSONObject myInfo;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.tab1, container, false);
        arrayList = new ArrayList<>();
        filteredList = new ArrayList<>();
        mSwipeRefreshLayout = rootView.findViewById(R.id.swipe);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
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
        recyclerView = rootView.findViewById(R.id.contactView);
        GetList();
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
        super.onResume();
        arrayList = new ArrayList<>();
        GetList();
    }

    private void GetList() {
        if(Token.ID==null) Token.ID="";
        requestQueue = Volley.newRequestQueue(getActivity().getApplicationContext());
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                "http://socrip4.kaist.ac.kr:2380/api/contact/" + Token.ID,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            arrayList = new ArrayList<>();
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject temp = response.getJSONObject(i);
                                String name = temp.getString("name");
                                String phone = temp.getString("phone");
                                String image = temp.getString("image");
                                int hasImage = temp.getInt("hasImage");
                                Log.d("RecyclerView", name);
                                arrayList.add(new contact_item(name, phone, image, hasImage));
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
    private void buildRecyclerView()
    {
        Log.d("RecyclerView","build recycler View");
        recyclerView = rootView.findViewById(R.id.contactView);
        layoutManager = new LinearLayoutManager(getActivity());
        listAdapter = new contactListAdapter(arrayList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);
        recyclerView.setAdapter(listAdapter);
    //    recyclerView.setItemAnimator(new DefaultItemAnimator());
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
                    people.put("contactOwner",Token.ID);
                Uri photo_uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactCursor.getLong(2));
                InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getContext().getContentResolver(),photo_uri);
                Bitmap image;
                if(input==null)
                {
                    people.put("image","0");
                    people.put("hasImage",0);
                }
                else
                {
                    image = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(input),50,50,true);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG,100,baos);
                    byte[] imageBytes = baos.toByteArray();
                    String imageString = Base64.encodeToString(imageBytes,Base64.DEFAULT);
                    people.put("image",imageString);
                    people.put("hasImage",1);
                }
                }
                catch(Exception e)
                {
                }
                JsonSend send = new JsonSend("http://socrip4.kaist.ac.kr:2380/api/contact",people);
                send.retriveContactByOwner();
            }while(contactCursor.moveToNext());
        }
        GetList();
    }
}



