package com.example.rishi.herbscout.Activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.rishi.herbscout.Dialog.LoginDialog;
import com.example.rishi.herbscout.Models.Constants;
import com.example.rishi.herbscout.Models.URLS;
import com.example.rishi.herbscout.R;
import com.example.rishi.herbscout.VolleySingleton.AppController;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class ClassificationActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int READ_REQUEST_CODE = 42;
    private Toolbar toolbar;
    private Button btLogin,btLogout,btUpload,btChoose;
    ImageView ivUpload;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        btLogin= (Button) findViewById(R.id.btLogin);
        btLogout= (Button) findViewById(R.id.btLogout);
        btUpload= (Button) findViewById(R.id.btUpload);
        btChoose= (Button) findViewById(R.id.btChoose);
        ivUpload= (ImageView) findViewById(R.id.ivUpload);
        btLogin.setOnClickListener(this);
        btLogout.setOnClickListener(this);
        btUpload.setOnClickListener(this);
        btChoose.setOnClickListener(this);

        setSupportActionBar(toolbar);

        if(isLogin()){
            showLogoutLayout();
        }else{
            showLoginLayout();
        }
    }

    private void doLogout() {
        SharedPreferences.Editor editor=getSharedPreferences(Constants.PREFS_NAME,MODE_PRIVATE).edit();
        editor.putString(Constants.PREFS_NAME_TOKEN,Constants.NULL);
        editor.commit();
        showLoginLayout();

    }

    private void showLogin() {
        LoginDialog loginDialog=new LoginDialog(this);
        loginDialog.setCancelable(false);
        loginDialog.show();
    }

    private void showLoginLayout() {
        btLogin.setVisibility(View.VISIBLE);

        btLogout.setVisibility(View.GONE);
    }

    private void showLogoutLayout() {
        btLogout.setVisibility(View.VISIBLE);

        btLogin.setVisibility(View.GONE);
    }

    public boolean isLogin() {
        SharedPreferences prefs=getSharedPreferences(Constants.PREFS_NAME,MODE_PRIVATE);
        if(prefs.getString(Constants.PREFS_NAME_TOKEN,Constants.NULL).contentEquals("NULL")){
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btLogin:
                showLogin();
                break;
            case R.id.btLogout:
                doLogout();
                break;
            case R.id.btUpload:
                doUpload();
                break;
            case R.id.btChoose:
                performFileSearch();
                break;
        }
    }

    private void doUpload() {
        ivUpload.buildDrawingCache();
        Bitmap bitmap = ivUpload.getDrawingCache();
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
        byte[] image=stream.toByteArray();
        final String base64 = Base64.encodeToString(image, 0);
        //System.out.print(base64);
        StringRequest request=new StringRequest(Request.Method.POST, URLS.BASE_URL + URLS.searchImageURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("IMAGE",response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                    Log.d("IMAGE error",""+error.getMessage());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("base64Content",base64);
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(request);
    }

    public void performFileSearch() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");

        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            if (resultData != null) {
                uri = resultData.getData();
                showImage(uri);
            }
        }
    }

    private void showImage(Uri uri) {
        Toast.makeText(this, "fileUri: "+uri.toString(), Toast.LENGTH_SHORT).show();
        this.uri=uri;
        ivUpload.setImageURI(uri);
    }


}
