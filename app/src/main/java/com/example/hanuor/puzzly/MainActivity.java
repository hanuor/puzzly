package com.example.hanuor.puzzly;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.hanuor.puzzly.constants.CommonGlobalVariables;
import com.example.hanuor.puzzly.constants.Logger;
import com.example.hanuor.puzzly.utils.ProgressDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.puzzledata)
    TextView puzzleData;

    @BindView(R.id.puzzletitle)
    TextView puzzleTitle;

    @BindView(R.id.nextpuzzle)
    Button nextPuzzle;

    @BindView(R.id.aboutcontainer)
    RelativeLayout aboutContainer;

    @BindView(R.id.detailscontainer)
    RelativeLayout detailsContainer;

    @BindView(R.id.aboutapptext)
    TextView aboutAppText;

    @BindView(R.id.scrollview)
    ScrollView headerContainer;


    Typeface typeface;
    LinkedHashMap<Integer, String> storeRiddlePuzzles;
    int currentPageCount = 1;
    Iterator its;
    Dialog progressDialog;
    String aboutAppTextPuzzly;

    Boolean showAboutApp = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        showPage(showAboutApp);
        storeRiddlePuzzles = new LinkedHashMap<Integer, String>();

        AssetManager am = this.getApplicationContext().getAssets();

        typeface = Typeface.createFromAsset(am,
                String.format(Locale.US, "fonts/%s", "Parchment.ttf"));
        progressDialog = ProgressDialog.progressDialog(MainActivity.this);

        puzzleTitle.setTypeface(typeface);
        puzzleData.setTypeface(typeface);
        nextPuzzle.setTypeface(typeface);
        aboutAppText.setTypeface(typeface);
        get_data(currentPageCount);

        nextPuzzle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPage(showAboutApp);
                if (its.hasNext()) {
                    Map.Entry pair = (Map.Entry) its.next();
                    puzzleData.setText(Html.fromHtml(pair.getValue().toString()));
                } else {
                    Log.d(CommonGlobalVariables.DEBUG_LOG, currentPageCount + " Page");
                    progressDialog.show();
                    currentPageCount++;
                    get_data(currentPageCount);
                }
            }
        });

        puzzleTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPage(showAboutApp);
            }
        });

    }

    public void get_data(int pageNumber) {
        Log.d(CommonGlobalVariables.VERBOSE_LOG, pageNumber + " This is the number");
        storeRiddlePuzzles.clear();
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

        String aboutUrl = CommonGlobalVariables.ABOUT_APP_URL;

        StringRequest aboutStringRequest = new StringRequest(Request.Method.GET, aboutUrl,
                new Response.Listener<String>() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    @Override
                    public void onResponse(String response) {
                        aboutAppTextPuzzly = response;
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(CommonGlobalVariables.ERROR_LOG, error.getMessage());
            }
        });
        progressDialog.dismiss();
        queue.add(aboutStringRequest);



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

            for (int i = 0; i < jsonArray.length(); i++) {
                Boolean isRiddle = false;
                JSONObject itemObject = jsonArray.getJSONObject(i);
                JSONArray tagsArray = itemObject.getJSONArray("tags");
                if (tagsArray.length() < 2 && tagsArray.get(0).toString().equalsIgnoreCase("riddle")) {
                    isRiddle = true;
                } else {
                    for (int j = 0; j < tagsArray.length(); j++) {
                        if (tagsArray.get(j).toString().equalsIgnoreCase("riddle")) {
                            isRiddle = true;
                        }
                    }
                }
                if (isRiddle) {
                    storeRiddlePuzzles.put(itemObject.getInt("question_id"), itemObject.getString("body"));
                }
            }


            Log.d(CommonGlobalVariables.DEBUG_LOG, storeRiddlePuzzles.size() + "");
            its = storeRiddlePuzzles.entrySet().iterator();

            Map.Entry pair = (Map.Entry) its.next();

            int key = (int) pair.getKey();
            String value = (String) pair.getValue();
            puzzleData.setText(Html.fromHtml(value));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void showPage(Boolean showAbout) {
        if (showAbout) {
            headerContainer.setVisibility(View.GONE);
            aboutContainer.setVisibility(View.VISIBLE);
            aboutAppText.setText(Html.fromHtml(aboutAppTextPuzzly));
            showAboutApp = false;
        } else {
            headerContainer.setVisibility(View.VISIBLE);
            aboutContainer.setVisibility(View.GONE);
            showAboutApp = true;
        }
    }

}
