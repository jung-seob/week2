package com.example.q.week2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import com.facebook.CallbackManager;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class addRecipe extends Activity {
    CallbackManager callbackManager;
    Button camera;
    EditText nameInput;
    EditText ingredientInput;
    EditText howToCookInput;
    Button save;
    JSONObject jsonObject;
    Boolean hasPhoto;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Log.d("tab3", "onCreate");
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.add_recipe);
        hasPhoto=false;
        callbackManager = CallbackManager.Factory.create();
        camera = findViewById(R.id.camera);
        nameInput = findViewById(R.id.nameInput);
        ingredientInput = findViewById(R.id.ingredientInput);
        howToCookInput = findViewById(R.id.howToCookInput);
        save = findViewById(R.id.save);
        jsonObject = new JSONObject();
        camera.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                pickImage();
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nameInput.getText().toString().equals("")|| ingredientInput.getText().toString().equals("") || howToCookInput.getText().toString().equals("")||!hasPhoto)
                {
                    Log.d("yelin","fill the conetnt");

                    Snackbar.make(v, "Please fill the content.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else
                {
                    try {
                        jsonObject.put("name", nameInput.getText().toString());
                        jsonObject.put("ingredient",ingredientInput.getText().toString());
                        jsonObject.put("howToCook",howToCookInput.getText().toString());
                        jsonObject.put("time",new java.text.SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date()));
                        jsonObject.put("user",Token.Name);
                        JsonSend jsonSend = new JsonSend("http://socrip4.kaist.ac.kr:2380/api/recipe",jsonObject);
                        jsonSend.create();
                        Snackbar.make(v, "Add new Recipe", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                    catch(Exception e)
                    {

                    }
                    finish();
                }
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
                return;
            }
            try {
                Log.d("tab3","on activity result try");
                JSONObject object = new JSONObject();
                InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
                Bitmap image = Bitmap.createScaledBitmap(BitmapFactory.decodeStream(inputStream),100,100,true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG,100,baos);
                byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes,Base64.DEFAULT);
                jsonObject.put("image",imageString);
                hasPhoto = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
