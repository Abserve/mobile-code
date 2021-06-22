package com.DicHAc.demo.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.DicHAc.demo.Constant;
import com.DicHAc.demo.R;
import com.DicHAc.demo.UserInfo;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static java.security.AccessController.getContext;

public class ProfileActivity extends AppCompatActivity {
    TextView fullName,role,email,phone,adresse;
    Button edit;
    private CircleImageView imgProfile;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fullName=findViewById(R.id.textView2);
        role=findViewById(R.id.textView8);
        email=findViewById(R.id.textView12);
        adresse=findViewById(R.id.textView15);
        phone=findViewById(R.id.textView16);
        phone=findViewById(R.id.textView16);
        imgProfile=findViewById(R.id.imageView8);
        preferences = getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        edit=findViewById(R.id.button);
        edit.setOnClickListener(view -> {
            Intent intent= new Intent(ProfileActivity.this, UserInfo.class);
            startActivity(intent);

        });

        getData();

    }

    private void getData() {
         StringRequest request = new StringRequest(Request.Method.GET, Constant.SHOW_USER_INFO, res->{

            try {
                JSONObject object = new JSONObject(res);
                if (object.getBoolean("success")){
                    JSONObject user = object.getJSONObject("user");
                    Picasso.get().load(Constant.urll+"storage/profiles/"+user.getString("photo")).into(imgProfile);

                    fullName.setText(user.getString("full_name"));
                    role.setText(user.getString("role"));
                    email.setText(user.getString("email"));
                    adresse.setText(user.getString("adresse"));
                    phone.setText("+216 "+user.getString("phone"));

                    //imgUrl = Constant.urll+"storage/profiles/"+user.getString("photo");
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

        RequestQueue queue = Volley.newRequestQueue(ProfileActivity.this);
        queue.add(request);
    }


}