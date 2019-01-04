package com.example.q.week2;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("yelin","tab 1 view create start");
        rootView = inflater.inflate(R.layout.tab1, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Log.d("yelin","going to get permission");
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("yelin","contacts permission is not granted");
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 1);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
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
        FloatingActionButton add = rootView.findViewById(R.id.add);
//        add.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent addContact = new Intent(ContactsContract.Intents.Insert.ACTION);
//                addContact.setType(ContactsContract.RawContacts.CONTENT_TYPE);
//                startActivity(addContact);
//                Snackbar.make(v, "Add New Contact", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        Log.d("yelin","tab 1 created view successfully");
        return rootView;
    }
    @Override
    public void onResume() {
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

    private ArrayList<contact_item> GetList(){////여기부분 수정해야할듯
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

        String[] projection = new String[] {
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
        };

        String[] selectionArgs = null;

        //정렬
        String sortOrder = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " COLLATE LOCALIZED ASC";
        //조회해서 가져온다
        Cursor contactCursor = getContext().getContentResolver().query(uri,projection,null,selectionArgs,sortOrder);

        //정보를 담을 array 설정
        ArrayList <contact_item> persons = new ArrayList();

        if(contactCursor.moveToFirst()){
            do{
                contact_item temp = new contact_item(contactCursor.getString(1) , contactCursor.getString(0));
                Uri photo_uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactCursor.getLong(2));
                InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getContext().getContentResolver(),photo_uri);

                if(input==null)
                {
                    temp.setPhoto(BitmapFactory.decodeResource(getContext().getResources(),R.drawable.ic_action_name));
                }
                else
                {
                    temp.setPhoto(Bitmap.createScaledBitmap(BitmapFactory.decodeStream(input), 50, 50, true));
                }
                persons.add(temp);
            }while(contactCursor.moveToNext());
        }
        return persons;
    }
    private void buildRecyclerView()
    {
        recyclerView = rootView.findViewById(R.id.contactView);
        layoutManager = new LinearLayoutManager(getActivity());
        listAdapter = new contactListAdapter(arrayList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.scrollToPosition(0);
        recyclerView.setAdapter(listAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
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
}
