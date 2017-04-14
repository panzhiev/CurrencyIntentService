package com.example.panzhiev.currencyintentservice.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.example.panzhiev.currencyintentservice.R;
import com.example.panzhiev.currencyintentservice.model.Organization;
import com.example.panzhiev.currencyintentservice.ui.adapters.EURAdapter;
import com.example.panzhiev.currencyintentservice.ui.adapters.RUBAdapter;
import com.example.panzhiev.currencyintentservice.ui.adapters.USDAdapter;
import com.example.panzhiev.currencyintentservice.utils.LoaderReceiver;
import com.example.panzhiev.currencyintentservice.utils.ServiceTask;
import com.gc.materialdesign.views.ButtonRectangle;

import java.util.ArrayList;

public class MainActivity extends Activity implements View.OnClickListener, LoaderReceiver.Receiver {

    final String LOG_TAG = "MainActivity";
    ListView listView;
    ArrayList<Organization> list;
    private LoaderReceiver loaderReceiver;

    USDAdapter usdadapter;
    RUBAdapter rubadapter;
    EURAdapter euradapter;
    SwipeRefreshLayout mSwipeRefreshLayout;

    ButtonRectangle usdbtn;
    ButtonRectangle rubbtn;
    ButtonRectangle eurbtn;

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

        loaderReceiver = new LoaderReceiver(new Handler());
        loaderReceiver.setReceiver(this);
        Intent intent = new Intent(this, ServiceTask.class);
        intent.putExtra("receiver", loaderReceiver);
        startService(intent);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list.clear();
                startService(new Intent(MainActivity.this, ServiceTask.class));
            }
        });

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

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        switch (resultCode) {
            case 911:
            list = resultData.getParcelableArrayList("LIST_OF_ORGANIZATIONS");
            mSwipeRefreshLayout.setRefreshing(false);
            usdadapter.notifyDataSetChanged();
            rubadapter.notifyDataSetChanged();
            euradapter.notifyDataSetChanged();
            break;
        }
    }
}
//                mSwipeRefreshLayout.setRefreshing(false);
//                usdadapter.notifyDataSetChanged();
//                rubadapter.notifyDataSetChanged();
//                euradapter.notifyDataSetChanged();


