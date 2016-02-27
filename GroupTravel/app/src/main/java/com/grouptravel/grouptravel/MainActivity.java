package com.grouptravel.grouptravel;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.PersistableBundle;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    private TextView textView;
    private LoginButton loginButton;
    private Button showButton;
    private CallbackManager callbackManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());


        /*
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.grouptravel.grouptravel",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
        */


        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
        loginButton = (LoginButton)findViewById(R.id.loginButton);
        showButton = (Button)findViewById(R.id.button);
        loginButton.setReadPermissions("user_friends");

        callbackManager = CallbackManager.Factory.create();


        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException e) {

            }
        });

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("Debug", "success");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(Profile.getCurrentProfile().getId());
                    }
                });
            }

            @Override
            public void onCancel() {
                Log.d("Debug", "cancel");
            }

            @Override
            public void onError(FacebookException e) {
                Log.d("Debug", "error");
            }
        });

        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFriends();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Logs 'install' and 'app activate' App Events.
        AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        AppEventsLogger.deactivateApp(this);
    }

    private void showFriends(){

        //final List<Friend> friends = new ArrayList<>();
        final ArrayList<String> friends_names = new ArrayList<>();
        final ArrayList<String> friends_ids = new ArrayList<>();

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(GraphResponse response) {
                        try{
                            JSONArray arr = response.getJSONObject().getJSONArray("data");
                            for(int i=0; i<arr.length(); i++) {
                                JSONObject obj = arr.getJSONObject(i);
                                Log.d("friend", obj.toString());
                                String name = obj.get("name").toString();
                                String id = obj.get("id").toString();
                                Log.d("friend added", name + " - - " + id);
                                //Friend friend = new Friend(name, id);
                                //friends.add(friend);
                                friends_names.add(name);
                                friends_ids.add(id);
                            }

                        } catch(JSONException e){

                        }

                        Intent i = new Intent(getApplicationContext(), Next.class);
                        i.putStringArrayListExtra("friends_names", friends_names);
                        i.putStringArrayListExtra("friends_ids", friends_ids);
                        startActivity(i);

                    }
                }
        ).executeAsync();


    }

}

