package com.example.rishi.herbscout.Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.example.rishi.herbscout.Adapter.PlantListAdapter;
import com.example.rishi.herbscout.Adapter.StringAdapter;
import com.example.rishi.herbscout.Dialog.MapDialog;
import com.example.rishi.herbscout.Models.Plant;
import com.example.rishi.herbscout.Models.PlantDetail;
import com.example.rishi.herbscout.Models.URLS;
import com.example.rishi.herbscout.R;
import com.example.rishi.herbscout.VolleySingleton.AppController;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PlantDetailActivity extends AppCompatActivity {

//    RecyclerView rvProperties,rvPartsUsed,rvPlaces;
    List<Plant> propertiesList,partsUsedList,placesList;
    StringAdapter propertiesAdapter,partsUsedAdapter,placesAdapter;
//    LinearLayoutManager llm1,llm2,llm3;
    String plantName;
    PlantDetail plantDetail;
    TextView tvPlantName;
    ImageView ivPlant;
    Context context;
    Button btShowMap;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);
        context=this;
        ivPlant= (ImageView) findViewById(R.id.ivPlant);
        tvPlantName= (TextView) findViewById(R.id.tvPlantName);
        btShowMap=findViewById(R.id.btShowMap);
        context=this;

        btShowMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog=new MapDialog(PlantDetailActivity.this,plantDetail.lat,plantDetail.lng);
                dialog.show();
            }
        });
        plantName=getIntent().getExtras().getString("plantName");
        plantName=plantName.toLowerCase();
        plantName=plantName.replace(' ','-');
//        plantName="Azadirachta indica";

        tabLayout= (TabLayout) findViewById(R.id.tabs);
        mViewPager = (ViewPager) findViewById(R.id.container);




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
                                String newname=plantDetail.name.toLowerCase().replace(' ','-');
                                String url= URLS.BASE_URL+"PlantImages/"+newname+"/0.jpg";
                                ImageLoader imageLoader= AppController.getInstance().getImageLoader();
                                imageLoader.get(url,ImageLoader.getImageListener(ivPlant,R.mipmap.ic_launcher,R.drawable.arrow_34_48_blue));

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
//                                partsUsedAdapter=new StringAdapter(context,plantDetail.parts_used);
//                                rvPartsUsed.setAdapter(partsUsedAdapter);

                                plantDetail.properties=new ArrayList<>();
                                for(i=0;i<properties.length();i++){
                                    plantDetail.properties.add(properties.getString(i));
                                    Log.d("PROPERTIES",plantDetail.properties.get(i));
                                }
//                                propertiesAdapter=new StringAdapter(context,plantDetail.properties);
//                                rvProperties.setAdapter(propertiesAdapter);

                                plantDetail.places=new ArrayList<>();
                                for(i=0;i<places.length();i++){
                                    plantDetail.places.add(places.getString(i));
                                }
//                                placesAdapter=new StringAdapter(context,plantDetail.places);
//                                rvPlaces.setAdapter(placesAdapter);


                                mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                                mViewPager.setAdapter(mSectionsPagerAdapter);
                                tabLayout.setupWithViewPager(mViewPager);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("DETAIL",""+error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(request);
    }


    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        LinearLayoutManager linearLayoutManager;
        RecyclerView rvFragment;
        StringAdapter adapter;
        GridLayoutManager gridLayoutManager;
        Boolean TYPE;
        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(ArrayList<String> stringList, int TYPE) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putStringArrayList("list", stringList);
            args.putInt("TYPE",TYPE);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_tab, container, false);
            rvFragment=rootView.findViewById(R.id.rvFragment);
            linearLayoutManager=new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
            gridLayoutManager=new GridLayoutManager(getActivity(),2);
            rvFragment.setLayoutManager(linearLayoutManager);
            Log.d("TYPEFRAG",""+getArguments().getInt("TYPE"));
            adapter=new StringAdapter(getActivity(),getArguments().getStringArrayList("list"),getArguments().getInt("TYPE"));
            rvFragment.setAdapter(adapter);
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position){
                case 0:
                    return PlaceholderFragment.newInstance(plantDetail.properties,0);
                case 1:
                    return PlaceholderFragment.newInstance(plantDetail.parts_used,1);
                case 2:
                    return PlaceholderFragment.newInstance(plantDetail.places,2);
                case 3:
                    return PlaceholderFragment.newInstance(plantDetail.recommendations,3);
            }
            return PlaceholderFragment.newInstance(new ArrayList<String>(),5);
        }

        @Override
        public int getCount() {
            // Show 4 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Property";
                case 1:
                    return "Parts Used";
                case 2:
                    return "Places";
                case 3:
                    return "Similar Plants";
            }
            return null;
        }
    }

}
