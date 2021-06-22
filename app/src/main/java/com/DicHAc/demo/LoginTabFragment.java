package com.DicHAc.demo;

import android.app.ProgressDialog;
import android.content.Context;
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

import com.DicHAc.demo.profile.ProfileActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.toolbox.Volley.newRequestQueue;


public class LoginTabFragment extends Fragment {
EditText email,password;
TextView forgot;
Button login;
    private ProgressDialog dialog;
float v = 0;

     @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container,false);
        email=root.findViewById(R.id.email);
        password=root.findViewById(R.id.pass);
        forgot=root.findViewById(R.id.forgot);
        login=root.findViewById(R.id.login);
         email.setTranslationY(800);
         password.setTranslationY(800);
         forgot.setTranslationY(800);
         login.setTranslationY(800);
         email.setAlpha(v);
         password.setAlpha(v);
         forgot.setAlpha(v);
         login.setAlpha(v);
         email.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(300).start();
         password.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
         forgot.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(500).start();
         login.animate().translationY(0).alpha(1).setDuration(800).setStartDelay(700).start();
         dialog=new ProgressDialog(getContext());
         dialog.setCancelable(false);
         login.setOnClickListener(view -> {
             login();
          //   Intent i = new Intent(view.getContext(), Home.class);
            // view.getContext().startActivity(i);
         });

        return root;
    }

    private void login() {
        dialog.setMessage("Login "+email.getText().toString().trim());
        dialog.show();
        StringRequest  request = new StringRequest(Request.Method.POST,Constant.login,response ->{
            try {
                JSONObject object = new JSONObject(response);
                if (object.getBoolean("success")){
                    JSONObject user = object.getJSONObject("user");
                    //sharedpref
                    SharedPreferences userPref = getActivity().getApplicationContext().getSharedPreferences("user",getContext().MODE_PRIVATE);
                    SharedPreferences.Editor editor=userPref.edit();
                    editor.putString("token",object.getString("token"));
                    System.out.println("token is : "+object.getString("token"));
                    editor.putString("email",user.getString("email"));
                    editor.putString("phone",user.getString("phone"));
                    editor.putInt("id",user.getInt("id"));
                    editor.putString("full_name",user.getString("full_name"));
                    editor.putString("photo",user.getString("photo"));
                    editor.putBoolean("isLoggedIn",true);
                    editor.apply();
                    startActivity(new Intent(((LoginActivity)getContext()), Home.class));
                    ((LoginActivity) getContext()).finish();
                    Toast.makeText(getContext(), "Connexion Réussie!", Toast.LENGTH_SHORT).show();

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
                return map;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(request);

    }

}
