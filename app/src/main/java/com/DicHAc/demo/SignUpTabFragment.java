package com.DicHAc.demo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SignUpTabFragment extends Fragment {
    EditText email,password,fullName;
    private ProgressDialog dialog;
     Button sinup;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.signup_tab_fragment,container,false);
        email=root.findViewById(R.id.emails);
        fullName=root.findViewById(R.id.name);
        password=root.findViewById(R.id.passs);
       // phone=root.findViewById(R.id.phone);
        sinup=root.findViewById(R.id.signup);
        dialog=new ProgressDialog(getContext());
        dialog.setCancelable(false);

        sinup.setOnClickListener(view -> {
            register();

        });

        return root;
    }
    public void register(){
        dialog.setMessage("Inscription de "+fullName.getText().toString().trim());
        dialog.show();
        StringRequest request = new StringRequest(Request.Method.POST,Constant.signup, response ->{
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    JSONObject data = object.getJSONObject("user");
                    JSONObject orig = data.getJSONObject("original");

                    JSONObject user = orig.getJSONObject("user");

                    //sharedpref
                    SharedPreferences userPref = getActivity().getApplicationContext().getSharedPreferences("user",getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor=userPref.edit();
                    editor.putString("token",orig.getString("token"));
                    editor.putString("email",user.getString("email"));
                   // editor.putString("phone",user.getString("phone"));
                    editor.putString("full_name",user.getString("full_name"));
                    editor.putString("photo",user.getString("photo"));
                    editor.putBoolean("isLoggedIn",true);
                    editor.apply();
                    startActivity(new Intent(((LoginActivity)getContext()), UserInfo.class));
                    ((LoginActivity) getContext()).finish();
                    Toast.makeText(getContext(), "Inscription réussie !", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        },error -> {
            error.printStackTrace();
            dialog.dismiss();
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> map = new HashMap<>();
                map.put("email",email.getText().toString().trim());
                map.put("password",password.getText().toString().trim());
              //  map.put("phone",phone.getText().toString().trim());
                map.put("full_name",fullName.getText().toString().trim());
                map.put("role","admin");
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);

    }
}
