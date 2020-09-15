package com.example.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity  {
    ListView musiclist;
    final String url = "https://rallycoding.herokuapp.com/api/music_albums";
    List<Album> alist;
    MyAdapter adapter;
    RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        musiclist = findViewById(R.id.musiclistview);
        alist = new ArrayList<>();
        requestQueue = Volley.newRequestQueue(this);//This will take care of    
        //background newtwork activities    
        getdata();
        adapter = new MyAdapter(this,R.layout.customcell,alist);
        musiclist.setAdapter(adapter);

    }

    private void getdata() {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONArray jsonArray = response;
                try {
                    for(int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String albumname = jsonObject.getString("title");
                        String albumimageurl = jsonObject.getString("image");
                        alist.add(new Album(albumname,albumimageurl));
                        Toast.makeText(getApplicationContext(),alist.toString(),Toast.LENGTH_LONG).show();
                    }
                    adapter.notifyDataSetChanged();//To prevent app from crashing when updating
                    //UI through background Thread
                }
                catch (Exception w)
                {
                    Toast.makeText(MainActivity.this,w.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonArrayRequest);
    }


}
