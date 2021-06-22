package com.DicHAc.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
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
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static androidx.camera.core.CameraX.getContext;

public class confirm_add extends AppCompatActivity {
    DatePickerDialog picker;
    EditText fullname,email,phone,adresse,cin,birthd,qualification,embauche;
    private SharedPreferences preferences;
    Button valider;
    CheckBox checkBox;
    private ProgressDialog dialog;
    String idoperation,addName,embSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_add);
        fullname=(EditText) findViewById(R.id.fulln);
        email=(EditText) findViewById(R.id.editText5);
        phone=(EditText) findViewById(R.id.editText6);
        adresse=(EditText) findViewById(R.id.editText8);
        cin=(EditText) findViewById(R.id.editText7);
        birthd=(EditText) findViewById(R.id.date_naissance);
        qualification=(EditText) findViewById(R.id.editText10);
        embauche=(EditText) findViewById(R.id.editText11);
        checkBox=(CheckBox)findViewById(R.id.checkBox);
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        idoperation=getIntent().getStringExtra("id");
        addName=getIntent().getStringExtra("fullname");
        embSet=getIntent().getStringExtra("embSet");

        fullname.setText(addName);

        valider=(Button) findViewById(R.id.button2);
        dialog=new ProgressDialog(confirm_add.this);
        dialog.setCancelable(false);

        birthd.setInputType(InputType.TYPE_NULL);
        birthd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(confirm_add.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                birthd.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        embauche.setInputType(InputType.TYPE_NULL);
        embauche.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar cldr = Calendar.getInstance();
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(confirm_add.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                embauche.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }, year, month, day);
                picker.show();
            }
        });

        valider.setOnClickListener(view -> {
            posst();
        });

    }
    private void posst() {
        dialog.setMessage("Ajout de "+email.getText().toString().trim());
        dialog.show();
        System.out.println(embSet);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.OPERATEUR, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getBoolean("status")){
                        JSONObject userobj = object.getJSONObject("operateur");
                        String id=userobj.getString("id");
                        String adresse=userobj.getString("adresse");
                        String cin=userobj.getString("cin");
                        String email=userobj.getString("email");
                        String full_name=userobj.getString("full_name");
                        String phone=userobj.getString("phone");
                        String d_naissance=userobj.getString("d_naissance");
                        String qulification=userobj.getString("qulification");
                        String embauche_date=userobj.getString("embauche_date");

                        User user = new User();
                        user.setUserId(id);
                        user.setAdresse(adresse);
                        user.setBirthd(d_naissance);
                        user.setCin(cin);
                        user.setEmail(email);
                        user.setName(full_name);
                        user.setPhone(phone);
                        user.setQualification(qulification);
                        user.setEmbauche(embauche_date);

                        SharedPreferences sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(addName, embSet);

                        SharedPreferences sharedOperateur = getSharedPreferences("operateurs", Context.MODE_PRIVATE);
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editorOp = sharedOperateur.edit();
                        editorOp.putString("adresse",adresse);
                        editorOp.putString("d_naissance",d_naissance);
                        editorOp.putString("cin",cin);
                        editorOp.putString("full_name",addName);
                        editorOp.putString("embSet",embSet);
                        editorOp.putString("email",email);
                        editorOp.putString("phone",phone);
                        editorOp.putString("qulification",qulification);
                        editorOp.putString("embauche_date",embauche_date);
                        editorOp.putString("embauche_date",embauche_date);

                        editor.apply();
                        editorOp.apply();
                        if(getIntent().getStringExtra("ac").equals("addoperateur")){
                            Intent intent5 = new Intent(confirm_add.this,Home.class);
                            startActivity(intent5);
                            finish();
                        }else{
                        Intent intent = new Intent(confirm_add.this,MainActivity.class);
                            intent.putExtra("id",getIntent().getStringExtra("id"));
                         startActivity(intent);
                            finish();
                        }
                        @SuppressLint("ShowToast") Toast successToast = Toast.makeText(confirm_add.this, getString(R.string.addSuccessToast), Toast.LENGTH_SHORT);
                        successToast.show();                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();

                Log.e("Rest response",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Rest response",error.toString());
                dialog.dismiss();
                Toast.makeText(confirm_add.this, error.toString(), Toast.LENGTH_SHORT).show();

            }
        }){
            @Override
            protected Map<String,String> getParams(){
                Map<String,String> map = new HashMap<String,String>();
                map.put("adresse",adresse.getText().toString().trim());
                map.put("role","operateur");
                map.put("d_naissance",birthd.getText().toString().trim());
                map.put("embauche_date",embauche.getText().toString().trim());
                map.put("qulification",qualification.getText().toString().trim());
                map.put("cin",cin.getText().toString().trim());
                map.put("email",email.getText().toString().trim());
                map.put("full_name",fullname.getText().toString().trim());
                map.put("phone",phone.getText().toString().trim());

                map.put("embSet",embSet);
                if(checkBox.isChecked()){
                    map.put("indirect","0");
                }else{
                    map.put("indirect","1");
                }
                return map;
            }

            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                String token = preferences.getString("token","");
                map.put("Accept","application/json");
                map.put("Authorization","Bearer "+token);
                return map;
            }

        };
        RequestQueue queue = Volley.newRequestQueue(confirm_add.this);
        queue.add(stringRequest);

    }
   }