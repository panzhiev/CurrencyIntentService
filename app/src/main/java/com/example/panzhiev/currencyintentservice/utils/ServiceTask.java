package com.example.panzhiev.currencyintentservice.utils;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.panzhiev.currencyintentservice.R;
import com.example.panzhiev.currencyintentservice.model.Organization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Panzhiev on 14.04.2017.
 */

public class ServiceTask extends IntentService {

    final int resultCode = 911;

    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String response = "";
    ArrayList<String> whiteList;
    ArrayList<Organization> list;
    int logo = 0;
    final String LOG_TAG = "ServiceTask";

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

        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
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

        } catch (JSONException e) {
            e.printStackTrace();
        }
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("LIST_OF_ORGANIZATIONS", list);
        receiver.send(resultCode, bundle);
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