package com.example.rishi.herbscout.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.rishi.herbscout.Adapter.PlantListAdapter;
import com.example.rishi.herbscout.Adapter.StringAdapter;
import com.example.rishi.herbscout.Models.Plant;
import com.example.rishi.herbscout.Models.PlantDetail;
import com.example.rishi.herbscout.Models.URLS;
import com.example.rishi.herbscout.R;
import com.example.rishi.herbscout.VolleySingleton.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlantDetailActivity extends AppCompatActivity {

    RecyclerView rvProperties,rvPartsUsed,rvPlaces;
    List<Plant> propertiesList,partsUsedList,placesList;
    StringAdapter propertiesAdapter,partsUsedAdapter,placesAdapter;
    LinearLayoutManager llm1,llm2,llm3;
    String plantName;
    PlantDetail plantDetail;
    TextView tvPlantName;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);
        context=this;
        tvPlantName= (TextView) findViewById(R.id.tvPlantName);
        rvProperties= (RecyclerView) findViewById(R.id.rvProperties);
        rvPartsUsed= (RecyclerView) findViewById(R.id.rvPartsUsed);
        rvPlaces= (RecyclerView) findViewById(R.id.rvPlaces);
        llm1=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        llm2=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        llm3=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rvProperties.setLayoutManager(llm1);
        rvPartsUsed.setLayoutManager(llm2);
        rvPlaces.setLayoutManager(llm3);
        plantName=getIntent().getExtras().getString("plantName");
        plantName=plantName.toLowerCase();
        plantName=plantName.replace(' ','-');
//        plantName="Azadirachta indica";
        plantDetail=new PlantDetail();
        tvPlantName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PlantDetailActivity.this,MapActivity.class));
            }
        });
        getPlantName();

    }

    public void getPlantName() {
        Toast.makeText(this, ""+plantName, Toast.LENGTH_SHORT).show();
        StringRequest request=new StringRequest(Request.Method.GET, URLS.BASE_URL + URLS.getSpecificPlantURL + "?name=" + plantName,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DETAIL",response);
                        try {
                            JSONObject json=new JSONObject(response);
                            if(json.getString("success").contentEquals("true")){
                                JSONObject jsonObject=json.getJSONObject("data");
                                JSONArray recommendations=jsonObject.getJSONArray("recommendations");
                                JSONArray map_info=jsonObject.getJSONArray("map_info");
                                JSONObject herb_data=jsonObject.getJSONObject("herb_data");
                                JSONArray parts_used=herb_data.getJSONArray("parts_used");
                                JSONArray properties=herb_data.getJSONArray("properties");
                                JSONArray places=herb_data.getJSONArray("places");
                                plantDetail.name=herb_data.getString("botanical_name");
                                tvPlantName.setText(plantDetail.name);
                                int i;
                                plantDetail.recommendations=new ArrayList<>();
                                for(i=0;i<recommendations.length();i++){
                                    plantDetail.recommendations.add(recommendations.getJSONObject(i).getString("botanical_name"));
                                }
                                plantDetail.lat=new ArrayList<>();
                                plantDetail.lng=new ArrayList<>();
                                for(i=0;i<map_info.length();i++){
                                    plantDetail.lat.add(map_info.getJSONObject(i).getDouble("latitude"));
                                    plantDetail.lng.add(map_info.getJSONObject(i).getDouble("longitude"));
                                }

                                plantDetail.parts_used=new ArrayList<>();
                                for(i=0;i<parts_used.length();i++){
                                    plantDetail.parts_used.add(parts_used.getString(i));
                                }
                                partsUsedAdapter=new StringAdapter(context,plantDetail.parts_used);
                                rvPartsUsed.setAdapter(partsUsedAdapter);

                                plantDetail.properties=new ArrayList<>();
                                for(i=0;i<properties.length();i++){
                                    plantDetail.properties.add(properties.getString(i));
                                    Log.d("PROPERTIES",plantDetail.properties.get(i));
                                }
                                propertiesAdapter=new StringAdapter(context,plantDetail.properties);
                                rvProperties.setAdapter(propertiesAdapter);

                                plantDetail.places=new ArrayList<>();
                                for(i=0;i<places.length();i++){
                                    plantDetail.places.add(places.getString(i));
                                }
                                placesAdapter=new StringAdapter(context,plantDetail.places);
                                rvPlaces.setAdapter(placesAdapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DETAIL",error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }
}
