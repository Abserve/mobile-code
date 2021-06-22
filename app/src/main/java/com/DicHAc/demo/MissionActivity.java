package com.DicHAc.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.DicHAc.demo.profile.ProfileActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MissionActivity extends AppCompatActivity  {
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<Mission> MissionUtilsList;
    private static final String TAG = "MissionActivity";
    private SharedPreferences crom;
    String activity;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mission);
        // data to populate the RecyclerView with
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView = (RecyclerView) findViewById(R.id.missionrecycler);
        recyclerView.addItemDecoration(new DividerItemDecoration(MissionActivity.this,
                DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        MissionUtilsList = new ArrayList<>();


        ///////////////////




        // set up the RecyclerView
        getData();

    }


    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Chargement...");
        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.GET, Constant.MISSION, res->{
            JSONObject object = null;
            try {
                object = new JSONObject(res);
                JSONArray missions = object.getJSONArray("missions");
                for (int i = 0; i < missions.length(); i++) {
                Mission mission = new Mission();
                try {
                    if (object.getBoolean("sucess")) {
                        JSONObject m = missions.getJSONObject(i);
                        mission.setId(m.getString("id"));
                        mission.setDescription(m.getString("description"));
                        mission.setDate_declanche(m.getString("date_declanche"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MissionUtilsList.add(mission);
            }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
            MissionAdapter   mAdapter = new MissionAdapter(MissionActivity.this, MissionUtilsList);
            mAdapter.setOnItemClickListener(new MissionAdapter.ClickListener() {
                @Override
                public void onItemClick(String d,int position, View v) {
                    Intent intent = getIntent();
                    activity = intent.getStringExtra("activity");
                    if(getIntent().getStringExtra("activity").equals("operateur")){
                        Intent intent1 = new Intent(MissionActivity.this, OperationActivity.class);
                        intent1.putExtra("idmission",d);
                        intent1.putExtra("ac",getIntent().getStringExtra("activity"));
                        startActivity(intent1);
                        finish();
                    } else if(getIntent().getStringExtra("activity").equals("addoperateur")){
                        Intent intent2 = new Intent(MissionActivity.this,OperationActivity.class);
                        intent2.putExtra("ac",getIntent().getStringExtra("activity"));
                        intent2.putExtra("idmission",d);
                        startActivity(intent2);
                        finish();
                    }else if(getIntent().getStringExtra("activity").equals("pointage")){
                        Intent intent3 = new Intent(MissionActivity.this,OperationActivity.class);
                        intent3.putExtra("idmission",d);
                        intent3.putExtra("ac",getIntent().getStringExtra("activity"));
                        startActivity(intent3);finish();
                    }else if(getIntent().getStringExtra("activity").equals("pointagefin")){
                        Intent intent3 = new Intent(MissionActivity.this,OperationActivity.class);
                        intent3.putExtra("idmission",d);
                        intent3.putExtra("ac",getIntent().getStringExtra("activity"));
                        startActivity(intent3);finish();
                    }
                }

                @Override
                public void onItemLongClick(String d,int position, View v) {
                    Toast.makeText(MissionActivity.this, "onItemLongClick pos = " + position, Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "onItemLongClick pos = " + position);
                }
            });

            recyclerView.setAdapter(mAdapter);
        },error -> {
            error.printStackTrace();
            Log.e("Rest response ",error.toString());
            if(error instanceof AuthFailureError){
                crom = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor ey = crom.edit();
                ey.clear();
                ey.apply();
                startActivity(new Intent(MissionActivity.this,LoginActivity.class));
                finish();
            }else if (error instanceof NetworkError) {
                Toast.makeText(MissionActivity.this,"Vous avez besoin d'une connexion Internet !",Toast.LENGTH_SHORT).show();
            } else if (error instanceof ServerError) {
                Toast.makeText(MissionActivity.this,"Désolé...Nous reviendrons bientôt",Toast.LENGTH_SHORT).show();
            }else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                Toast.makeText(MissionActivity.this,"Vous avez besoin d'une connexion Internet !",Toast.LENGTH_SHORT).show();

            }
            progressDialog.dismiss();
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(MissionActivity.this);
        queue.add(request);
    }

    /*
      @Override
    public void onItemClick(View view, int position) {
        Intent intent = getIntent();
        activity = intent.getStringExtra("activity");
        if(getIntent().getStringExtra("activity").equals("operateur")){
            Intent intent1 = new Intent(this, Operateur.class);
            startActivity(intent1);
        } else if(getIntent().getStringExtra("activity").equals("addoperateur")){
            Intent intent2 = new Intent(this,confirm_add.class);
            startActivity(intent2);
        }else if(getIntent().getStringExtra("activity").equals("pointage")){
            Intent intent3 = new Intent(this,MainActivity.class);
            startActivity(intent3);
        }

        //Toast.makeText(this, "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }*/
}