package com.example.rishi.herbscout.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.rishi.herbscout.Dialog.LoginDialog;
import com.example.rishi.herbscout.Models.Constants;
import com.example.rishi.herbscout.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button btClassification,btLogin,btLogout;
    ImageView ivSearch;
    EditText etSearch;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar= (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);


        btClassification= (Button) findViewById(R.id.btClassification);
        ivSearch= (ImageView) findViewById(R.id.ivSearch);
        etSearch= (EditText) findViewById(R.id.etSearch);
        btLogin= (Button) findViewById(R.id.btLogin);
        btLogout= (Button) findViewById(R.id.btLogout);
        btClassification.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        etSearch.setOnClickListener(this);
        btLogin.setOnClickListener(this);
        btLogout.setOnClickListener(this);

        if(isLogin()){
            showLogoutLayout();
        }else{
            showLoginLayout();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btClassification:
                startActivity(new Intent(HomeActivity.this,ClassificationActivity.class));
                break;
            case R.id.ivSearch:
                if(validateForm()){
                    String textSearch=etSearch.getText().toString();
                    Intent intent=new Intent(HomeActivity.this,SearchActivity.class);
                    intent.putExtra("searchQuery",textSearch);
                    startActivity(intent);
                }
                break;
            case R.id.btLogin:
                showLogin();
                break;
            case R.id.btLogout:
                doLogout();
                break;

        }
    }

    private boolean validateForm() {
        if(etSearch.getText().toString().trim().length()==0){
            etSearch.setError("Please enter some text!");
            return false;
        }
        return true;
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

}
