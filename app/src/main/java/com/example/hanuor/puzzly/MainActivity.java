package com.example.hanuor.puzzly;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hanuor.puzzly.Constants.CommonGlobalVariables;
import com.example.hanuor.puzzly.Constants.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.puzzledata)
    TextView puzzleData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
get_data(1);
    }


    public void get_data(int pageNumber) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String page = "page=" + pageNumber;
        String url = CommonGlobalVariables.API_MAIN_URL + page + CommonGlobalVariables.API_END_URL + CommonGlobalVariables.API_WITH_BODY_URL;

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        Log.e(CommonGlobalVariables.ERROR_LOG, response);
                        Logger.d(CommonGlobalVariables.DEBUG_LOG, response);
                        data_response_handler(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(CommonGlobalVariables.ERROR_LOG, error.getMessage());
            }
        });

        queue.add(stringRequest);
    }

    @TargetApi(Build.VERSION_CODES.N)
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void data_response_handler(String response) {
        Log.d(CommonGlobalVariables.DEBUG_LOG, response);

        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.v(CommonGlobalVariables.VERBOSE_LOG, jsonObject.toString());
            JSONArray jsonArray = new JSONArray(jsonObject.getString("items"));
            Log.e(CommonGlobalVariables.ERROR_LOG, String.valueOf(jsonArray.length()));

            for (int i=0; i<jsonArray.length(); i++) {
                JSONObject itemObject = jsonArray.getJSONObject(i);
                puzzleData.setText(Html.fromHtml(itemObject.getString("body"), Html.FROM_HTML_MODE_LEGACY));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
