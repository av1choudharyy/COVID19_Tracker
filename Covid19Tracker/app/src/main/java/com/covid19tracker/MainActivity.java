package com.covid19tracker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView stateActiveCases;
    private TextView stateConfirmed;
    private TextView stateDeceased;
    private TextView stateRecovered;
    private TextView cChanges;
    private TextView rChanges;
    private TextView dChanges;
    private String State;
    private Spinner stateView;
    private int check = 0;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter<DistrictAdapter.ViewHolder> mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayAdapter<String> arrayAdapter;

    private List<Districts> districtsList;
    private ArrayList<String> States;


    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        districtsList = new ArrayList<>();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        final TextView Total = findViewById(R.id.textView4);
        final TextView Recovered = findViewById(R.id.textView5);
        final TextView Death = findViewById(R.id.textView6);
        final TextView TodayCases = findViewById(R.id.textView7);
        final TextView TodayDeath = findViewById(R.id.textView8);
        final TextView ActiveCases = findViewById(R.id.textView10);
        stateActiveCases = findViewById(R.id.textView29);
        stateView = findViewById(R.id.spinner);
        stateConfirmed = findViewById(R.id.textView26);
        stateRecovered = findViewById(R.id.textView27);
        stateDeceased = findViewById(R.id.textView28);
        cChanges = findViewById(R.id.textView14);
        rChanges = findViewById(R.id.textView15);
        dChanges = findViewById(R.id.textView16);
        stateView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                ((TextView) parent.getChildAt(0)).setTextSize(16);
                if (check++ > 0) {
                    State = String.valueOf(stateView.getSelectedItem());
                    districtsList.clear();
                    fun1(State);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        States = new ArrayList<>();

        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, "https://coronavirus-19-api.herokuapp.com/countries/india", null, objectResponse -> {
            try {
                int cases = objectResponse.getInt("cases");
                int recovered = objectResponse.getInt("recovered");
                int deaths = objectResponse.getInt("deaths");
                int todayCases = objectResponse.getInt("todayCases");
                int todayDeaths = objectResponse.getInt("todayDeaths");
                int activeCases = objectResponse.getInt("active");
                Total.setText(String.valueOf(cases));
                Death.setText(String.valueOf(deaths));
                Recovered.setText(String.valueOf(recovered));
                String tc = ("+" + todayCases);
                String td = ("+" + todayDeaths);
                TodayCases.setText(tc);
                TodayDeath.setText(td);
                ActiveCases.setText(String.valueOf(activeCases));
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }, Throwable::printStackTrace);
        requestQueue.add(jsonObjectRequest);
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 2199);
        } else {
            getLocation();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2199) {
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(task -> {
            Location location = task.getResult();
            if (location != null) {
                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                try {
                    List<Address> addresses = geocoder.getFromLocation(
                            location.getLatitude(), location.getLongitude(), 1
                    );
                    stateData(addresses.get(0).getAdminArea(), addresses.get(0).getLocality());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void stateData(final String stateName, final String cityName) {
        final RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://api.covid19india.org/v4/min/data.min.json", null, arrayResponse -> {
            try {
                for (int i = 0; i < arrayResponse.length(); i++) {
                    JSONObject obj = arrayResponse.getJSONObject(i);
                    if (obj.getString("state").equals(stateName)) {
                        stateActiveCases.setText(String.valueOf(obj.getInt("active")));
                        stateConfirmed.setText(String.valueOf(obj.getInt("confirmed")));
                        stateRecovered.setText(String.valueOf(obj.getInt("recovered")));
                        stateDeceased.setText((String.valueOf(obj.getInt("deaths"))));
                        String stateCChanges, stateRChanges, stateDChanges;
                        stateCChanges = "+" + obj.getInt("cChanges");
                        stateRChanges = "+" + obj.getInt("rChanges");
                        stateDChanges = "+" + obj.getInt("dChanges");
                        cChanges.setText(stateCChanges);
                        rChanges.setText(stateRChanges);
                        dChanges.setText(stateDChanges);
                        JSONArray array = obj.getJSONArray("districtData");
                        for (int j = 0; j < array.length(); j++) {
                            Districts districts = new Districts();
                            JSONObject obj1 = array.getJSONObject(j);
                            if (obj1.getString("name").equals(cityName)) {
                                districts.setName(obj1.getString("name"));
                                districts.setConfirmed((obj1.getInt("confirmed")));
                                districtsList.add(districts);
                            }
                        }
                        for (int k = 0; k < array.length(); k++) {
                            Districts districts = new Districts();
                            JSONObject obj1 = array.getJSONObject(k);
                            if (obj1.getString("name").equals(cityName)) {
                                continue;
                            }
                            districts.setName(obj1.getString("name"));
                            districts.setConfirmed(obj1.getInt("confirmed"));
                            districtsList.add(districts);

                        }
                    }
                    String state=obj.getString("state");
                    States.add(state);
                }
                arrayAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, States);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                stateView.setAdapter(arrayAdapter);
                int spinner = arrayAdapter.getPosition(stateName);
                stateView.setSelection(spinner);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            mAdapter = new DistrictAdapter(districtsList);
            recyclerView.setAdapter(mAdapter);
        }, Throwable::printStackTrace);
        requestQueue.add(jsonArrayRequest);
    }

    private void fun1(String state) {
        final RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, "https://api.covid19india.org/v4/min/data.min.json", null, arrayResponse -> {
            try {
                for (int i = 0; i < arrayResponse.length(); i++) {
                    JSONObject obj = arrayResponse.getJSONObject(i);
                    if (obj.getString("state").equals(state)) {
                        stateActiveCases.setText(String.valueOf(obj.getInt("active")));
                        stateConfirmed.setText(String.valueOf(obj.getInt("confirmed")));
                        stateRecovered.setText(String.valueOf(obj.getInt("recovered")));
                        stateDeceased.setText((String.valueOf(obj.getInt("deaths"))));
                        String stateCChanges, stateRChanges, stateDChanges;
                        stateCChanges = "+" + obj.getInt("cChanges");
                        stateRChanges = "+" + obj.getInt("rChanges");
                        stateDChanges = "+" + obj.getInt("dChanges");
                        cChanges.setText(stateCChanges);
                        rChanges.setText(stateRChanges);
                        dChanges.setText(stateDChanges);
                        JSONArray array = obj.getJSONArray("districtData");
                        for (int k = 0; k < array.length(); k++) {
                            Districts districts = new Districts();
                            JSONObject obj1 = array.getJSONObject(k);
                            districts.setName(obj1.getString("name"));
                            districts.setConfirmed(obj1.getInt("confirmed"));
                            districtsList.add(districts);
                        }
                    }
                }
                mAdapter = new DistrictAdapter(districtsList);
                recyclerView.setAdapter(mAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);
        requestQueue.add(jsonArrayRequest);
    }

    public void about(View view) {
        startActivity(new Intent(this, About.class));
    }
}
