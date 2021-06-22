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

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperationActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager layoutManager;
    List<Operation> OperationUtilsList;
    private static final String TAG = "OperationActivity";

    String activity;
    private SharedPreferences preferences;
    private SharedPreferences crom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operation);
        // data to populate the RecyclerView with
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        recyclerView = (RecyclerView) findViewById(R.id.operationecycler);
        recyclerView.addItemDecoration(new DividerItemDecoration(OperationActivity.this,
                DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);

        OperationUtilsList = new ArrayList<>();

        ///////////////////




        // set up the RecyclerView
        getData();

    }


    private void getData() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String op= Constant.url+getIntent().getStringExtra("idmission")+"/Operation/";
        System.out.println("oppp"+op);

        StringRequest request = new StringRequest(Request.Method.GET, op, res->{
            JSONObject object = null;
            try {
                object = new JSONObject(res);
                JSONArray operations = object.getJSONArray("operations");
                for (int i = 0; i < operations.length(); i++) {
                    Operation operation = new Operation();
                    try {

                        if (object.getBoolean("success")) {

                            JSONObject m = operations.getJSONObject(i);

                            System.out.println("yesss"+m);
                            operation.setId(m.getString("id"));
                            operation.setDate_operation(m.getString("date_operation"));

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    OperationUtilsList.add(operation);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            progressDialog.dismiss();
            OperationAdapter   mAdapter = new OperationAdapter(OperationActivity.this, OperationUtilsList);
            mAdapter.setOnItemClickListener(new OperationAdapter.ClickListener() {
                @Override
                public void onItemClick(String d,int position, View v) {
                    if(getIntent().getStringExtra("ac").equals("operateur")){
                        Intent intent1 = new Intent(OperationActivity.this, OperateurActivity.class);
                        intent1.putExtra("id",d);
                        intent1.putExtra("ac",getIntent().getStringExtra("ac"));

                        startActivity(intent1);
                        finish();
                    } else if(getIntent().getStringExtra("ac").equals("addoperateur")){
                        Intent intent2 = new Intent(OperationActivity.this,addFace.class);
                        intent2.putExtra("id",d);
                        String uu = getIntent().getStringExtra("ac");
                        intent2.putExtra("ac",uu);
                        startActivity(intent2);
                        finish();
                    }else if(getIntent().getStringExtra("ac").equals("pointage")){
                        Intent intent3 = new Intent(OperationActivity.this,MainActivity.class);
                        intent3.putExtra("id",d);
                        intent3.putExtra("ac",getIntent().getStringExtra("ac"));
                        startActivity(intent3);
                        finish();
                    }else if(getIntent().getStringExtra("ac").equals("pointagefin")){
                        Intent intent3 = new Intent(OperationActivity.this,MainActivity.class);
                        intent3.putExtra("id",d);
                        intent3.putExtra("ac",getIntent().getStringExtra("ac"));
                        startActivity(intent3);
                        finish();
                    }
                    Toast.makeText(OperationActivity.this, "Mission "+getIntent().getStringExtra("idmission")+"Opération " + d, Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onItemLongClick(String d,int position, View v) {
                    Toast.makeText(OperationActivity.this, "onItemLongClick pos = " + position, Toast.LENGTH_SHORT).show();

                    Log.d(TAG, "onItemLongClick pos = " + position);
                }
            });

            recyclerView.setAdapter(mAdapter);
        },error -> {
            error.printStackTrace();
            if(error instanceof AuthFailureError){
                crom = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                SharedPreferences.Editor ey = crom.edit();
                ey.clear();
                ey.apply();
                startActivity(new Intent(OperationActivity.this,LoginActivity.class));
                finish();
            }else if (error instanceof NetworkError) {
                Toast.makeText(OperationActivity.this,"Vous avez besoin d'une connexion Internet !",Toast.LENGTH_SHORT).show();
            } else if (error instanceof ServerError) {
                Toast.makeText(OperationActivity.this,"Désolé...Nous reviendrons bientôt",Toast.LENGTH_SHORT).show();
            }else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                Toast.makeText(OperationActivity.this,"Vous avez besoin d'une connexion Internet !",Toast.LENGTH_SHORT).show();

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

        RequestQueue queue = Volley.newRequestQueue(OperationActivity.this);
        queue.add(request);
    }

}