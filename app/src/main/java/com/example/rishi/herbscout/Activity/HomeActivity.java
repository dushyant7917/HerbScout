package com.example.rishi.herbscout.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.rishi.herbscout.Dialog.LoginDialog;
import com.example.rishi.herbscout.Models.Constants;
import com.example.rishi.herbscout.Models.URLS;
import com.example.rishi.herbscout.R;
import com.example.rishi.herbscout.VolleySingleton.AppController;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button btClassification;
    ImageView ivSearch;
    EditText etSearch;
    private static final int READ_REQUEST_CODE = 42;
    private Button btLogin,btLogout,btUpload,btChoose;
    ImageView ivUpload;
    Uri uri;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        btClassification= (Button) findViewById(R.id.btClassification);
        ivSearch= (ImageView) findViewById(R.id.ivSearch);
        etSearch= (EditText) findViewById(R.id.etSearch);
        btClassification.setOnClickListener(this);
        ivSearch.setOnClickListener(this);
        etSearch.setOnClickListener(this);
        btUpload= (Button) findViewById(R.id.btUpload);
        btChoose= (Button) findViewById(R.id.btChoose);
        ivUpload= (ImageView) findViewById(R.id.ivUpload);
        btUpload.setOnClickListener(this);
        btChoose.setOnClickListener(this);

        progressDialog=new ProgressDialog(this);
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(false);

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
            case R.id.btUpload:
                doUpload();
                break;
            case R.id.btChoose:
                performFileSearch();
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

    private void doUpload() {
        progressDialog.show();
        ivUpload.buildDrawingCache();
        Log.d("PATH:",getPath(uri));
        Bitmap bitmap = BitmapFactory.decodeFile(getPath(uri));
        ByteArrayOutputStream stream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image=stream.toByteArray();
        final String base64= Base64.encodeToString(image, Base64.DEFAULT);
        StringRequest request=new StringRequest(Request.Method.POST, URLS.BASE_URL + URLS.searchImageURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("IMAGE",response);
                        progressDialog.hide();
                        try {

                            JSONObject jsonObject=new JSONObject(response);
                            if(jsonObject.getString("success").contentEquals("true")){
                                Log.d("DATA",jsonObject.getJSONObject("data").toString());
                                String name=jsonObject.getJSONObject("data").getJSONObject("result").getJSONObject("herb_data").getString("botanical_name");
                                Intent intent=new Intent(HomeActivity.this, PlantDetailActivity.class);
                                intent.putExtra("plantName",name);
                                startActivity(intent);
                            }else {
                                Toast.makeText(HomeActivity.this, ""+jsonObject.getJSONObject("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("IMAGE error",""+error.getMessage());
                progressDialog.hide();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> map=new HashMap<>();
                map.put("base64Content",base64);
                return map;
            }
        };
        request.setRetryPolicy(new DefaultRetryPolicy(40000,0,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
                showUploadButton();
            }
        }
    }

    private void showUploadButton() {
        btUpload.setVisibility(View.VISIBLE);
    }

    private void showImage(Uri uri) {
        this.uri=uri;
        ivUpload.setImageURI(uri);
    }

    public String getPath(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }


}
