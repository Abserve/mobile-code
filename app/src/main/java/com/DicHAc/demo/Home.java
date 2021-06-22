package com.DicHAc.demo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.DicHAc.demo.profile.ProfileActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {
    ImageButton imageButton;
    private DrawerLayout main_drawer;
    private ImageButton main_menu;
    private NavigationView main_navigation;
    private SharedPreferences preferences;
    private SharedPreferences crom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tryy();
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);

        setContentView(R.layout.activity_home);
         main_drawer = findViewById(R.id.main_drawer);
        main_menu = findViewById(R.id.main_menu);
        loadData();

        main_navigation = findViewById(R.id.main_navigation);

        main_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_drawer.openDrawer(GravityCompat.START);
            }
        });
        main_navigation.getMenu().findItem(R.id.profile).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                    }

                    @Override
                    public void onDrawerOpened(@NonNull View drawerView) {
                    }

                    @Override
                    public void onDrawerClosed(@NonNull View drawerView) {
                        startActivityForResult(new Intent(Home.this, ProfileActivity.class), 104);
                        main_drawer.removeDrawerListener(this);
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {
                    }
                };

                main_drawer.addDrawerListener(drawerListener);
                main_drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        main_navigation.getMenu().findItem(R.id.home).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                    }

                    @Override
                    public void onDrawerOpened(@NonNull View drawerView) {
                    }

                    @Override
                    public void onDrawerClosed(@NonNull View drawerView) {
                        startActivityForResult(new Intent(Home.this, Home.class), 104);
                        main_drawer.removeDrawerListener(this);
                    }
                    @Override
                    public void onDrawerStateChanged(int newState) {
                    }
                };

                main_drawer.addDrawerListener(drawerListener);
                main_drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        main_navigation.getMenu().findItem(R.id.logout).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                    }

                    @Override
                    public void onDrawerOpened(@NonNull View drawerView) {
                    }

                    @Override
                    public void onDrawerClosed(@NonNull View drawerView) {
                       // startActivityForResult(new Intent(Home.this, LoginActivity.class), 104);
                       // main_drawer.removeDrawerListener(this);
                        AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                        builder.setMessage("Voulez-vous déconnecter?");
                        builder.setPositiveButton("Déconnextion", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                logout();
                            }
                        });
                        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.show();
                    }
                    

                    @Override
                    public void onDrawerStateChanged(int newState) {
                    }
                };

                main_drawer.addDrawerListener(drawerListener);
                main_drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        //-------Affichage des operateurs----------

        CardView operateurCard = findViewById(R.id.operatorsCard);
        operateurCard.setOnClickListener(view -> {

            Intent intent = new Intent(this, MissionActivity.class);
            intent.putExtra("activity","operateur");
            startActivity(intent);
        });

        //------Ajouter un operateur------
        CardView valider= findViewById(R.id.valider);
        valider.setOnClickListener( view -> {
           // Intent intent2 = new Intent(this,confirm_add.class);
            Intent intent2 = new Intent(this,MissionActivity.class);
            intent2.putExtra("activity","addoperateur");
            startActivity(intent2);
        });
        //---------pointage---------
        CardView pointage= findViewById(R.id.pointageCard);
        pointage.setOnClickListener( view -> {
           // Intent intent3 = new Intent(this,MainActivity.class);
            Intent intent3 = new Intent(this,MissionActivity.class);
            intent3.putExtra("activity","pointage");
            startActivity(intent3);
        });
        //---------pointage fin---------
        CardView pointagefin= findViewById(R.id.pointagefinCard);
        pointagefin.setOnClickListener( view -> {
           // Intent intent3 = new Intent(this,MainActivity.class);
            Intent intent4 = new Intent(this,MissionActivity.class);
            intent4.putExtra("activity","pointagefin");
            startActivity(intent4);
        });


    }

    private void loadData() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, Constant.getembs, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    JSONArray emsets = object.getJSONArray("emsets");
                    for (int i = 0; i < emsets.length(); i++) {
                         try {
                            if (object.getBoolean("success")) {
                                JSONObject m = emsets.getJSONObject(i);
                                String nname=m.getString("full_name");
                                String eemb=m.getString("embSet");
                                SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(nname,eemb);
                                editor.apply();
                                Toast.makeText(Home.this,"toutes les visages sont importées \n",Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                     }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.e("Rest response 1 ",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Rest response 2 ",error.toString());
                if(error instanceof AuthFailureError){
                    crom = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor ey = crom.edit();
                    ey.clear();
                    ey.apply();
                    startActivity(new Intent(Home.this,LoginActivity.class));
                    finish();
                }else if (error instanceof NetworkError) {
                    Toast.makeText(Home.this,"Vous avez besoin d'une connexion Internet !",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(Home.this,"Désolé...Nous reviendrons bientôt",Toast.LENGTH_SHORT).show();
                }else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(Home.this,"Vous avez besoin d'une connexion Internet !",Toast.LENGTH_SHORT).show();

                }
            }
        }){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                String token = preferences.getString("token","");
                map.put("Accept","application/json");
                map.put("Authorization","Bearer "+token);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Home.this);
        queue.add(stringRequest);

    }

    private void tryy() {

        String datep= Constant.LOGOUT;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, datep, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("success")){
                        Toast.makeText(Home.this,"Bienvenue",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

                Log.e("Rest response 1 ",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Rest response 2 ",error.toString());
                if(error instanceof AuthFailureError){
                    crom = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                    SharedPreferences.Editor ey = crom.edit();
                    ey.clear();
                    ey.apply();
                    startActivity(new Intent(Home.this,LoginActivity.class));
                    finish();
                }else if (error instanceof NetworkError) {
                    Toast.makeText(Home.this,"Vous avez besoin d'une connexion Internet !",Toast.LENGTH_SHORT).show();
                } else if (error instanceof ServerError) {
                    Toast.makeText(Home.this,"Désolé...Nous reviendrons bientôt",Toast.LENGTH_SHORT).show();
                }else if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Toast.makeText(Home.this,"Vous avez besoin d'une connexion Internet !",Toast.LENGTH_SHORT).show();

                }
            }
        }){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                String token = preferences.getString("token","");
                map.put("Accept","application/json");
                map.put("Authorization","Bearer "+token);
                return map;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(Home.this);
        queue.add(stringRequest);

    }

    public void logout() {
        StringRequest request = new StringRequest(Request.Method.GET,Constant.LOGOUT, res->{

            try {
                JSONObject object = new JSONObject(res);
                if (object.getBoolean("success")){
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();
                    startActivity(new Intent(Home.this, LoginActivity.class));
                    finish();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        },error -> {
            error.printStackTrace();
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                String token = preferences.getString("token","");
                HashMap<String,String> map = new HashMap<>();
                map.put("Authorization","Bearer "+token);
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(Home.this);
        queue.add(request);
    }
}