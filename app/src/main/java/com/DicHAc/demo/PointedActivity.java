package com.DicHAc.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PointedActivity extends AppCompatActivity implements PointedAdapter.ItemClickListener {
    Map<String, String> embs;
    Map<String, String> ch;
    Map<String, String> cromba;
    Map<String, String> iddd;
    private ProgressDialog dialog;
    Integer idid;
    private static final String TAG = "PointedActivity";
    String activity;
    private SharedPreferences preferences;
    private SharedPreferences sharedPreferences;
    private SharedPreferences check;
    private SharedPreferences maybe;
    private SharedPreferences ddone;
    private SharedPreferences dids;
    PointedAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pointed);
        // data to populate the RecyclerView with

        ///////////////////

        // data to populate the RecyclerView with
        ArrayList<String> animalNames = new ArrayList<>();
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("mylist");

        ArrayList<String> sss = (ArrayList<String>) args.getSerializable("ARRAYLIST");

        HashSet hs = new HashSet();
        hs.addAll(sss); //remove duplicates

        sss.clear();
        sss.addAll(hs);
        animalNames = sss;
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        dids = getSharedPreferences("dids", Context.MODE_PRIVATE);
        check = getSharedPreferences("done", Context.MODE_PRIVATE);

        embs = (Map<String, String>) sharedPreferences.getAll();
        iddd = (Map<String, String>) dids.getAll();
        ch = (Map<String, String>) check.getAll();


        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerViewpointed);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PointedAdapter(this, animalNames);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        dialog = new ProgressDialog(PointedActivity.this);
        dialog.setCancelable(false);
        ArrayList<String> finalAnimalNames = animalNames;
        ImageView imgFavorite = (ImageView) findViewById(R.id.add_confirmed);


        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);


        imgFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < finalAnimalNames.size(); i++) {
                    for (Map.Entry<String, String> entry : embs.entrySet()) {
                        if (finalAnimalNames.get(i).equals(entry.getKey())) {
                            SharedPreferences.Editor editor = check.edit();
                            editor.putString(entry.getKey(), entry.getValue());
                            editor.apply();
                        }
                    }

                }
                ddone = getSharedPreferences("done", Context.MODE_PRIVATE);

                cromba = (Map<String, String>) ddone.getAll();


                for (Map.Entry<String, String> entry : cromba.entrySet()) {
                    getidd(entry.getValue(), new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            if (getIntent().getStringExtra("ac").equals("pointagefin")) {
                                datefinpointage(result, entry.getKey());
                            } else {
                                datepointage(result, entry.getKey());
                            }
                        }
                    });
                }

            }
        });
        imgFavorite.setClickable(true);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        maybe = getSharedPreferences("done", Context.MODE_PRIVATE);
        SharedPreferences.Editor ddone = maybe.edit();
        ddone.clear();
        ddone.apply();
    }

    //getting id by the embset
    private void getidd(String embss, final VolleyCallback callback) {
        ArrayList<String> trying = new ArrayList<>();

        Pointed pointed = new Pointed();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.getid, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    JSONArray opid = object.getJSONArray("id_is");
                    for (int i = 0; i < opid.length(); i++) {
                        try {
                            if (object.getBoolean("success")) {
                                JSONObject m = opid.getJSONObject(i);
                                callback.onSuccess(m.getString("id"));

                                // cromba(m.getString("id"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("Rest response", response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Rest response", error.toString());


            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                map.put("embSet", embss);
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                String token = preferences.getString("token", "");
                map.put("Accept", "application/json");
                map.put("Authorization", "Bearer " + token);
                return map;
            }

        };

        RequestQueue queue = Volley.newRequestQueue(PointedActivity.this);
        queue.add(stringRequest);

    }


    public interface VolleyCallback {
        void onSuccess(String result);
    }

    private void datepointage(String userid, String nom) {

        String datep = Constant.url + getIntent().getStringExtra("idop") + "/datepointage";
        dids = getSharedPreferences("dids", Context.MODE_PRIVATE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, datep, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    System.out.println("obb" + object);
                    if (object.getBoolean("status")) {
                        JSONObject userobj = object.getJSONObject("operateur");
                        Toast.makeText(PointedActivity.this, nom + "a pointé !", Toast.LENGTH_SHORT).show();
                        SharedPreferences.Editor didtor = dids.edit();
                        didtor.putString(userobj.getString("user_id"), userobj.getString("id"));
                        System.out.println("dddddddd" + userobj.getString("id"));
                        didtor.apply();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
                dialog.dismiss();

                Log.e("Rest response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Rest response", error.toString());

                dialog.dismiss();

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> map = new HashMap<String, String>();
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
                String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
                String tt = new SimpleDateFormat("HH", Locale.getDefault()).format(new Date());
                String maj = getPartOfTheDay(Integer.parseInt(tt));
                Date todayDate = new Date();
                String thisDate = currentDate.format(todayDate);
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                Date d = new Date();
                String dayOfTheWeek = sdf.format(d);
                if (dayOfTheWeek == "Saturday") {
                    map.put("majoration", "Samedi " + maj);
                } else if (dayOfTheWeek == "Sunday") {
                    map.put("majoration", "Dimanche");
                } else {
                    map.put("majoration", maj);
                }
                map.put("date_pointage", thisDate);
                map.put("heur_deb", currentTime);
                map.put("user_id", userid);


                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                String token = preferences.getString("token", "");
                map.put("Accept", "application/json");
                map.put("Authorization", "Bearer " + token);
                return map;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(PointedActivity.this);
        queue.add(stringRequest);

    }

    private void datefinpointage(String userid, String nom) {

        for (Map.Entry<String, String> entry : iddd.entrySet()) {

            System.out.println(entry.getKey() + entry.getValue()+userid);
            if (entry.getKey().equals(userid)) {


                String datep = Constant.url + getIntent().getStringExtra("idop") + "/datepointage/" + entry.getValue();
                System.out.println(datep);
                StringRequest stringRequest = new StringRequest(Request.Method.PUT, datep, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if (object.getBoolean("status")) {
                                JSONObject userobj = object.getJSONObject("datepointages");
                                Toast.makeText(PointedActivity.this, nom + "a pointé Sortie!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                        dialog.dismiss();

                        Log.e("Rest response", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Rest response", error.toString());

                        dialog.dismiss();

                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> map = new HashMap<String, String>();

                        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

                        map.put("heure_fin", currentTime);
                        return map;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> map = new HashMap<String, String>();
                        String token = preferences.getString("token", "");
                        map.put("Accept", "application/json");
                        map.put("Authorization", "Bearer " + token);
                        return map;
                    }

                };
                RequestQueue queue = Volley.newRequestQueue(PointedActivity.this);
                queue.add(stringRequest);

            }
        }
        SharedPreferences.Editor editor = dids.edit();
        editor.clear();
        editor.apply();
    }

    public static String getPartOfTheDay(final int hour) {
        if (hour > 6 && hour < 14) {
            if (hour <= 8) {
                return "Matin";
            } else if (hour > 8 && hour < 11) {
                return "Matin";
            }

            return "Matin";
        } else if (hour >= 14 && hour < 22) {
            if (hour >= 14 && hour <= 15) {
                return "Après-midi";
            } else if (hour >= 16) {
                return "Après-midi";
            }

            return "Après-midi";
        } else if (hour >= 22 && hour < 6) {
            return "Nuit";
        } else {
            return "Nuit";
        }
    }

    @Override
    public void onItemClick(String name, View view, int position) {
        Toast.makeText(this, "Vous avez cliqué " + adapter.getItem(position) + " sur le numéro de ligne " + position, Toast.LENGTH_SHORT).show();
    }

}