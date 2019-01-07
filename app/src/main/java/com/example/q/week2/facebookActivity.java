package com.example.q.week2;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class facebookActivity extends AppCompatActivity {


    CallbackManager callbackManager;
    LoginButton loginButton;
    LoginManager loginManager;
    AccessToken accessToken;
    String graph = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    Log.d("hmm","lalalal");

//        // 페북  SDK정보 초기화
//        FacebookSdk.sdkInitialize(getApplicationContext());
//        AppEventsLogger.activateApp(this);
//        //</페북 SDK정보 초기화>

        setContentView(R.layout.facebook_main);



        callbackManager = CallbackManager.Factory.create();

        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("public_profile", "user_friends", "email");

        accessToken = AccessToken.getCurrentAccessToken();



        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile", "user_friends", "email"));
        // Callback registration


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("hmm","lalalal2");
                Log.d("TAG", "페이스북 토큰 = " + loginResult.getAccessToken().getToken());
                Log.d("TAG", "페이스북 UserID = " + loginResult.getAccessToken().getUserId());
                Log.d("TAG", "페이스북 Application ID = " + loginResult.getAccessToken().getApplicationId());

                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            getFBGraph();
                        } catch (Exception e) {
                            Log.d("yelin", e.getMessage());
                        }
                    }
                };
                thread.start();
                Log.d("hmm","lalalal3");
//                Intent intent = new Intent(facebookActivity.this, MainActivity.class);
//                facebookActivity.this.startActivity(intent);
                finish();
            }

            @Override
            public void onCancel() {
                // App code
                Log.d("yelin", "on Cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

////        LoginManager.getInstance().logInWithReadPermissions();
//        // return rootView;
//

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public String getFBGraph()
    {
        Log.d("yelin","get fb graph");
        Thread thread = new Thread() {
            @Override
            public void run() {
                try
                {
                    GraphRequest request = GraphRequest.newMeRequest(
                            accessToken,
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(final JSONObject object, GraphResponse response) {
                                    Log.d("yelin",object.toString());
                                    JsonSend send = new JsonSend("http://socrip4.kaist.ac.kr:2380/api/user",object);
                                    send.retriveUserById();
                                }
                            }
                    );
                    Bundle parameters = new Bundle();
                    parameters.putString("fields","id,name,email");
                    request.setParameters(parameters);
                    request.executeAsync();
                }
                catch(Exception e)
                {
                    Log.d("yelin", e.getMessage());
                }
            }
        };
        thread.start();
        return graph;
    }

}