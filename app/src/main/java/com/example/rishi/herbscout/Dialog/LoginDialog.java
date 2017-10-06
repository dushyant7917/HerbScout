package com.example.rishi.herbscout.Dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.rishi.herbscout.Models.URLS;
import com.example.rishi.herbscout.R;

import org.json.JSONObject;

/**
 * Created by Rishi on 06/10/17.
 */

public class LoginDialog extends Dialog implements View.OnClickListener{

    Button btLogin,btRegister,btCancel;
    TextView tvLogin,tvRegister;
    EditText etUsername,etPassword;
    ProgressDialog progressDialog;
    Context context;

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

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.POST, URLS.BASE_URL + URLS.loginURL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "Error: "+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void doRegister(){
        progressDialog.setMessage("Registering you . . .");
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
                break;
            case R.id.btRegister:
                break;
        }
    }
}
