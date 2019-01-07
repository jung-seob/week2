package com.example.q.week2;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONObject;
import java.util.Arrays;

public class Tab3 extends Fragment {
    View rootView;
    CallbackManager callbackManager;
    LoginButton loginButton;
    LoginManager loginManager;
    AccessToken accessToken;
//    String graph = null;
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
//        rootView = inflater.inflate(R.layout.tab1,container,false);
//        callbackManager = CallbackManager.Factory.create();
//        loginButton = rootView.findViewById(R.id.login_button);
//        loginButton.setReadPermissions("public_profile","user_friends","email");
//        loginButton.setFragment(this);
//        accessToken = AccessToken.getCurrentAccessToken();
//        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
//        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("public_profile","user_friends","email"));
//        // Callback registration
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Thread thread = new Thread() {
//                    @Override
//                    public void run() {
//                        try {
//                            getFBGraph();
//                        }
//                        catch (Exception e) {
//                            Log.d("yelin", e.getMessage());
//                        }
//                    }
//                };
//                thread.start();
//            }
//
//            @Override
//            public void onCancel() {
//                // App code
//                Log.d("yelin","on Cancel");
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                // App code
//            }
//        });
//        return rootView;
//    }
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        callbackManager.onActivityResult(requestCode, resultCode, data);
//        super.onActivityResult(requestCode, resultCode, data);
//    }
//    public String getFBGraph()
//    {
//        Log.d("yelin","get fb graph");
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                try
//                {
//                    GraphRequest request = GraphRequest.newMeRequest(
//                            accessToken,
//                            new GraphRequest.GraphJSONObjectCallback() {
//                                @Override
//                                public void onCompleted(final JSONObject object, GraphResponse response) {
//                                    Log.d("yelin",object.toString());
//                                    JsonSend send = new JsonSend("http://socrip4.kaist.ac.kr:2380/api/user",object);
//                                    send.retriveUserById();
//                                }
//                            }
//                    );
//                    Bundle parameters = new Bundle();
//                    parameters.putString("fields","id,name,email");
//                    request.setParameters(parameters);
//                    request.executeAsync();
//                }
//                catch(Exception e)
//                {
//                    Log.d("yelin", e.getMessage());
//                }
//            }
//        };
//        thread.start();
//        return graph;
//    }
}
