package com.example.rishi.herbscout.Activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.rishi.herbscout.Adapter.PlantListAdapter;
import com.example.rishi.herbscout.Models.Plant;
import com.example.rishi.herbscout.Models.URLS;
import com.example.rishi.herbscout.R;
import com.example.rishi.herbscout.VolleySingleton.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    String searchQuery;
    List<Plant> plantList;
    PlantListAdapter plantListAdapter;
    GridLayoutManager gridLayoutManager;
    RecyclerView rvPlant;
    TextView tvNoResults;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchQuery=getIntent().getExtras().getString("searchQuery");
        plantList=new ArrayList<>();
        rvPlant= (RecyclerView) findViewById(R.id.rvPlant);
        tvNoResults= (TextView) findViewById(R.id.tvNoResults);
        gridLayoutManager=new GridLayoutManager(this,2);
        rvPlant.setLayoutManager(gridLayoutManager);
        progressDialog=new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please Wait . . .");
        progressDialog.setIndeterminate(false);
        doSearch();
    }

    private void doSearch() {
        progressDialog.show();
        StringRequest request=new StringRequest(Request.Method.GET, URLS.BASE_URL + URLS.searchNameURL+"?query="+searchQuery,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.hide();
                        Log.d("NAME",""+response);
                        plantList=new ArrayList<>();
                        try {
                            JSONObject jsonObject= new JSONObject(response.toString());
                            JSONArray jsonArray=jsonObject.getJSONArray("results");
                            if(jsonArray.length()!=0){
                                for(int i=0;i<jsonArray.length();i++){
                                    Plant plant=new Plant();
                                    plant.name=jsonArray.getJSONObject(i).getString("botanical_name");
                                    plant.image=jsonArray.getJSONObject(i).getString("image");
                                    plantList.add(plant);
                                }
                                plantListAdapter=new PlantListAdapter(SearchActivity.this,plantList);
                                rvPlant.setAdapter(plantListAdapter);
                                tvNoResults.setVisibility(View.GONE);
                            }else {
                                rvPlant.setVisibility(View.GONE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("NAME",""+error.getMessage());
                progressDialog.hide();
            }
        });

        AppController.getInstance().addToRequestQueue(request);
    }


}
