package com.example.panzhiev.currencyintentservice.ui.activities;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.panzhiev.currencyintentservice.R;
import com.example.panzhiev.currencyintentservice.model.Organization;
import com.example.panzhiev.currencyintentservice.ui.adapters.EURAdapter;
import com.example.panzhiev.currencyintentservice.ui.adapters.RUBAdapter;
import com.example.panzhiev.currencyintentservice.ui.adapters.USDAdapter;
import com.example.panzhiev.currencyintentservice.utils.Logos;
import com.gc.materialdesign.views.ButtonRectangle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener {

    final String LOG_TAG = "MainActivity";
    ListView listView;
    ArrayList<Organization> list;

    USDAdapter usdadapter;
    RUBAdapter rubadapter;
    EURAdapter euradapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    ButtonRectangle usdbtn;
    ButtonRectangle rubbtn;
    ButtonRectangle eurbtn;

    int logo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        listView = (ListView) findViewById(R.id.ListView);

        list = new ArrayList<Organization>();

        usdbtn = (ButtonRectangle) findViewById(R.id.button_USD);
        usdbtn.setOnClickListener(this);

        rubbtn = (ButtonRectangle) findViewById(R.id.button_RUB);
        rubbtn.setOnClickListener(this);

        eurbtn = (ButtonRectangle) findViewById(R.id.button_EUR);
        eurbtn.setOnClickListener(this);

        usdadapter = new USDAdapter(MainActivity.this, list);
        rubadapter = new RUBAdapter(MainActivity.this, list);
        euradapter = new EURAdapter(MainActivity.this, list);


        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list = new ArrayList<Organization>();
                startService(new Intent(MainActivity.this, ServiceTask.class));
//                new RequestTask().execute();
            }
        });
        startService(new Intent(MainActivity.this, ServiceTask.class));
//        new RequestTask().execute();

        usdbtn.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
        usdbtn.invalidate();
        rubbtn.getBackground().clearColorFilter();
        rubbtn.invalidate();
        eurbtn.getBackground().clearColorFilter();
        eurbtn.invalidate();
        listView.setAdapter(usdadapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_USD:
                usdbtn.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                usdbtn.invalidate();
                rubbtn.getBackground().clearColorFilter();
                rubbtn.invalidate();
                eurbtn.getBackground().clearColorFilter();
                eurbtn.invalidate();
                listView.setAdapter(usdadapter);
                Log.d(LOG_TAG, "toDoUSD");
                break;
            case R.id.button_RUB:
                usdbtn.getBackground().clearColorFilter();
                usdbtn.invalidate();
                rubbtn.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                rubbtn.invalidate();
                eurbtn.getBackground().clearColorFilter();
                eurbtn.invalidate();
                listView.setAdapter(rubadapter);
                Log.d(LOG_TAG, "toDoRUB");
                break;
            case R.id.button_EUR:
                usdbtn.getBackground().clearColorFilter();
                usdbtn.invalidate();
                rubbtn.getBackground().clearColorFilter();
                rubbtn.invalidate();
                eurbtn.getBackground().setColorFilter(0xe0f47521, PorterDuff.Mode.SRC_ATOP);
                eurbtn.invalidate();
                listView.setAdapter(euradapter);
                Log.d(LOG_TAG, "toDoEUR");
                break;
            default:
                break;
        }
    }

    class ServiceTask extends IntentService {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String response = "";
        ArrayList<String> whiteList;
        
        /**
         * Creates an IntentService.  Invoked by your subclass's constructor.
         *
         * @param name Used to name the worker thread, important only for debugging.
         */
        public ServiceTask(String name) {
            super(name);
            whiteList = new ArrayList<String>();
            whiteList.add(getResources().getString(R.string.bank_A_bank));
            whiteList.add(getResources().getString(R.string.alfa_bank));
            whiteList.add(getResources().getString(R.string.vtb_bank));
            whiteList.add(getResources().getString(R.string.otp_bank));
            whiteList.add(getResources().getString(R.string.pivdennyj));
            whiteList.add(getResources().getString(R.string.bank_privat));
            whiteList.add(getResources().getString(R.string.radabank));
            whiteList.add(getResources().getString(R.string.bank_aval));
        }

        @Override
        protected void onHandleIntent(@Nullable Intent intent) {

            JSONObject dataJsonObj = null;

            try {
                URL url = new URL("http://resources.finance.ua/ru/public/currency-cash.json");

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
                response = buffer.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                dataJsonObj = new JSONObject(response); //organizations
                JSONArray organizations = dataJsonObj.getJSONArray("organizations");

                Log.d("WhiteList", whiteList.toString());

                for (int i = 0; i < organizations.length(); i++) {

                    JSONObject bank = organizations.getJSONObject(i);
                    JSONObject currencies = bank.getJSONObject("currencies");
                    String title = bank.getString("title");

                    switch (title) {
                        case "А-Банк":logo = Logos.LOGOS[0];break;
                        case "Альфа-Банк":logo = Logos.LOGOS[1];break;
                        case "ВТБ Банк":logo = Logos.LOGOS[2];break;
                        case "ОТП Банк":logo = Logos.LOGOS[3];break;
                        case "ПИВДЕННЫЙ":logo = Logos.LOGOS[4];break;
                        case "ПриватБанк":logo = Logos.LOGOS[5];break;
                        case "РАДАБАНК":logo = Logos.LOGOS[6];break;
                        case "Райффайзен Банк Аваль":logo = Logos.LOGOS[7];break;
                    }
                    Log.d("LOGO", "" + logo);

                    JSONObject eur = null;
                    JSONObject rub = null;
                    JSONObject usd = null;
                    double eurASK = 0.0;
                    double eurBID = 0.0;
                    double rubASK = 0.0;
                    double rubBID = 0.0;
                    double usdASK = 0.0;
                    double usdBID = 0.0;
                    try {
                        eur = currencies.getJSONObject("EUR");
                        eurASK = eur.getDouble("ask");
                        eurBID = eur.getDouble("bid");
                    } catch (JSONException e) {
                    }
                    try {
                        rub = currencies.getJSONObject("RUB");
                        rubASK = rub.getDouble("ask");
                        rubBID = rub.getDouble("bid");
                    } catch (JSONException e) {
                    }
                    try {
                        usd = currencies.getJSONObject("USD");
                        usdASK = usd.getDouble("ask");
                        usdBID = usd.getDouble("bid");
                    } catch (JSONException e) {
                    }

                    if (isInWhiteList(title)) {
                        Organization organization = new Organization(title, logo, eurASK, eurBID, rubASK, rubBID, usdASK, usdBID);
                        list.add(organization);
                    }
                }

                for (Organization o : list) {
                    Log.d(LOG_TAG, "latest" + o.toString());
                }

//                mSwipeRefreshLayout.setRefreshing(false);
                usdadapter.notifyDataSetChanged();
                rubadapter.notifyDataSetChanged();
                euradapter.notifyDataSetChanged();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        boolean isInWhiteList(String title) {
            boolean res = false;
            for (String s : whiteList) {
                Log.d(LOG_TAG, "comporation title =" + title + "  s =" + s);
                if (title.equals(s)) {
                    Log.d(LOG_TAG, "equals");
                    res = true;
                }
            }
            return res;
        }
    }
}
