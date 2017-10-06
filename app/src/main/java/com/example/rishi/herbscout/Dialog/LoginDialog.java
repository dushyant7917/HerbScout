package com.example.rishi.herbscout.Dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.example.rishi.herbscout.Activity.HomeActivity;
import com.example.rishi.herbscout.Models.Constants;
import com.example.rishi.herbscout.Models.URLS;
import com.example.rishi.herbscout.R;
import com.example.rishi.herbscout.VolleySingleton.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Rishi on 06/10/17.
 */

public class LoginDialog extends Dialog implements View.OnClickListener{

    Button btLogin,btRegister,btCancel;
    TextView tvLogin,tvRegister;
    EditText etUsername,etPassword;
    ProgressDialog progressDialog;
    Context context;
    String username,password;

    public LoginDialog(@NonNull Context context) {
        super(context);
        this.context=context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_login);
        btLogin=findViewById(R.id.btLogin);
        btRegister=findViewById(R.id.btRegister);
        btCancel=findViewById(R.id.btCancel);
        tvLogin=findViewById(R.id.tvLogin);
        tvRegister=findViewById(R.id.tvRegister);
        etUsername=findViewById(R.id.etUsername);
        etPassword=findViewById(R.id.etPassword);
        btLogin.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        btRegister.setOnClickListener(this);
        tvRegister.setOnClickListener(this);
        tvLogin.setOnClickListener(this);
        showLogin();

        progressDialog=new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
    }

    void showRegister(){
        btLogin.setVisibility(View.GONE);
        tvLogin.setVisibility(View.GONE);

        btRegister.setVisibility(View.VISIBLE);
        tvRegister.setVisibility(View.VISIBLE);
    }

    void showLogin(){
        btLogin.setVisibility(View.VISIBLE);
        tvLogin.setVisibility(View.VISIBLE);

        btRegister.setVisibility(View.GONE);
        tvRegister.setVisibility(View.GONE);
    }

    void doLogin(){
        progressDialog.setMessage("Logging you in . . .");
        username=etUsername.getText().toString();
        password=etPassword.getText().toString();
        progressDialog.show();
        StringRequest request=new StringRequest(Request.Method.POST, URLS.BASE_URL + URLS.loginURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hide();
                        Log.d("LOGIN",response.toString());
                        try {
                            JSONObject json=new JSONObject(response);
                            String token=json.getString("token");
                            saveCredentials(token,username);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("LOGIN",""+error.getMessage());
                progressDialog.hide();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("password", password);
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(request);
    }

    void saveCredentials(String token, String username){
        SharedPreferences.Editor editor=context.getSharedPreferences(Constants.PREFS_NAME,Context.MODE_PRIVATE).edit();
        editor.putString(Constants.PREFS_NAME_TOKEN,token);
        editor.putString(Constants.PREFS_NAME_USERNAME,username);
        editor.commit();
    }

    void doRegister(){
        progressDialog.setMessage("Registering you . . .");
        username=etUsername.getText().toString();
        password=etPassword.getText().toString();
        progressDialog.show();
        StringRequest request=new StringRequest(Request.Method.POST, URLS.BASE_URL + URLS.registerURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hide();
                        Log.d("REGISTER",response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.hide();
            }
        }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("username", username);
                map.put("password", password);
                return map;
            }
        };
        AppController.getInstance().addToRequestQueue(request);
    }

    boolean validateForm(){
        if(etUsername.getText().toString().trim().length()==0){
            etUsername.setError("Please enter username");
            return false;
        }
        if(etPassword.getText().toString().trim().length()==0){
            etPassword.setError("Please enter password");
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tvLogin:
                showRegister();
                break;
            case R.id.tvRegister:
                showLogin();
                break;
            case R.id.btCancel:
                dismiss();
                break;
            case R.id.btLogin:
                if(validateForm()){
                    doLogin();
                }
                break;
            case R.id.btRegister:
                if(validateForm()){
                    doRegister();
                }
                break;
        }
    }
}
