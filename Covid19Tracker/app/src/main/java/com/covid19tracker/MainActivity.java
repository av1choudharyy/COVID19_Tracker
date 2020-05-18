package com.covid19tracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView Total = findViewById(R.id.textView2);
        final TextView Cured = findViewById(R.id.textView3);
        final TextView Death = findViewById(R.id.textView4);
        final TextView Today = findViewById(R.id.textView28);
        final TextView Active = findViewById(R.id.textView31);
        final TextView Tests = findViewById(R.id.textView34);
        RequestQueue requestQueue;
        requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Request.Method.GET, "https://coronavirus-19-api.herokuapp.com/countries/india", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    int total = response.getInt("cases");
                    int todayCases = response.getInt("todayCases");
                    int recovered = response.getInt("recovered");
                    int death = response.getInt("deaths");
                    int active = response.getInt("active");
                    int tests = response.getInt("totalTests");
                    Total.setText(String.valueOf(total));
                    Cured.setText(String.valueOf(recovered));
                    Death.setText(String.valueOf(death));
                    Today.setText(String.valueOf(todayCases));
                    Active.setText(String.valueOf(active));
                    Tests.setText(String.valueOf(tests));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error", "something fishy " + error);
            }
        });
        requestQueue.add(jsonArrayRequest);

    }

    public void call(View view) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:1075"));
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            startActivity(callIntent);
        }


    }
}
