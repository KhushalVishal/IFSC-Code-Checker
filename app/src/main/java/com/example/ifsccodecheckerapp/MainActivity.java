package com.example.ifsccodecheckerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText ifscCodeEdt;
    private TextView bankDetailsTV;

    String ifscCode;

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ifscCodeEdt = findViewById(R.id.idedtIfscCode);
        Button getBankDetailsBtn = findViewById(R.id.idBtnGetBankDetails);
        bankDetailsTV = findViewById(R.id.idTVBankDetails);

        mRequestQueue = Volley.newRequestQueue(MainActivity.this);

        getBankDetailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ifscCode = ifscCodeEdt.getText().toString();

                if (TextUtils.isEmpty(ifscCode)) {
                    Toast.makeText(MainActivity.this, "Please enter valid IFSC code", Toast.LENGTH_SHORT).show();
                } else {
                    getDataFromIFSCCode(ifscCode);
                }
            }
        });
    }

    private void getDataFromIFSCCode(String ifscCode) {


        mRequestQueue.getCache().clear();

        String url = "https://ifsc.razorpay.com/" + ifscCode;

        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);


        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    String state = response.getString("STATE");
                    String bankName = response.getString("BANK");
                    String branch = response.getString("BRANCH");
                    String address = response.getString("ADDRESS");
                    String contact = response.getString("CONTACT");
                    String micrcode = response.getString("MICR");
                    String city = response.getString("CITY");

                    bankDetailsTV.setText("Bank Name : " + bankName + "\nBranch : " + branch + "\nAddress : " + address + "\nMICR Code : " + micrcode + "\nCity : " + city + "\nState : " + state + "\nContact : " + contact);

                } catch (JSONException e) {

                    e.printStackTrace();
                    bankDetailsTV.setText(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                bankDetailsTV.setText(error.toString());
            }
        });

        queue.add(objectRequest);
    }
}