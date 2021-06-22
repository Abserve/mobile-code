package com.DicHAc.demo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperateurActivity extends AppCompatActivity {


    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    List<User> OperateurUtilsList;
    private static final String TAG = "OperateurActivity";

    String activity;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operateur);
        // data to populate the RecyclerView with
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        OperateurUtilsList = new ArrayList<>();
        ///////////////////
        //---------add button icon-----------
        ImageView add_icon = findViewById(R.id.add_icon);
        add_icon.setOnClickListener(view -> {
            Intent intent = new Intent(this, com.DicHAc.demo.addFace.class);
            intent.putExtra("id",getIntent().getStringExtra("id"));
            intent.putExtra("ac",getIntent().getStringExtra("ac"));
            startActivity(intent);
        });
        //---------affichage recycler---------
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // set up the RecyclerView
        getData();
    }
    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String op = Constant.url + getIntent().getStringExtra("id") + "/datepointage/show_operateur";
        System.out.println("operateurrr"+op);
        StringRequest request = new StringRequest(Request.Method.GET, op, res -> {
            JSONObject object = null;
                    try {
                        object = new JSONObject(res);
                        JSONArray operations = object.getJSONArray("operateur");
                        for (int i = 0; i < operations.length(); i++) {
                            User user = new User();
                            try {
                                if (object.getBoolean("success")) {
                                    JSONObject m = operations.getJSONObject(i);
                                    user.setUserId(m.getString("id"));
                                    user.setName(m.getString("full_name"));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                    OperateurUtilsList.add(user);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
            MyAdapter mAdapter = new MyAdapter(OperateurActivity.this, OperateurUtilsList);
            mAdapter.setOnItemClickListener(new MyAdapter.ClickListener() {
                @Override
                public void onItemClick(String d, int position, View v) {
                    Toast.makeText(OperateurActivity.this, "Opération" + getIntent().getStringExtra("id") + "Opérateur " + d, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onItemLongClick(String d, int position, View v) {
                    Toast.makeText(OperateurActivity.this, "onItemLongClick pos = " + position, Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "onItemLongClick pos = " + position);
                }
            });

            recyclerView.setAdapter(mAdapter);
        }, error -> {
            error.printStackTrace();
            progressDialog.dismiss();
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token", "");
                HashMap<String, String> map = new HashMap<>();
                map.put("Authorization", "Bearer " + token);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(OperateurActivity.this);
        queue.add(request);
    }
}
