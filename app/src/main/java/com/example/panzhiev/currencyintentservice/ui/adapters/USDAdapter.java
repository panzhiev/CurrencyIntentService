package com.example.panzhiev.currencyintentservice.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.panzhiev.currencyintentservice.R;
import com.example.panzhiev.currencyintentservice.model.Organization;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Tim on 07.04.2017.
 */

public class USDAdapter extends BaseAdapter {
    Context mContext;
    List<Organization> list;
    LayoutInflater inflater;

    public USDAdapter(Context context, List<Organization> list) {
        mContext = context;
        this.list = list;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {//количество элементов
        return list.size();
    }

    @Override
    public Object getItem(int position) {//получить элемент по указанной позиции
        return list.get(position);

    }

    @Override
    public long getItemId(int position) {//возвращаем номер позиции элемента
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = inflater.inflate(R.layout.usd_item, parent, false);//задаем вью нашу разметку
        Organization organization = (Organization) getItem(position);

        int logo = organization.getLogo();

        try{
            Picasso.with(view.getContext())
                    .load(logo)
                    .into((ImageView) view.findViewById(R.id.iv_bank));
        } catch (Exception e)
        {
            Picasso.with(view.getContext())
                    .load(R.mipmap.ic_launcher)
                    .into((ImageView) view.findViewById(R.id.iv_bank));
        }

        ((TextView) view.findViewById(R.id.tv_bank_title)).setText(organization.getTitle());
        ((TextView) view.findViewById(R.id.tv_ask_usd_value)).setText(String.valueOf(organization.getUsdASK()));
        ((TextView) view.findViewById(R.id.tv_bid_usd_value)).setText(String.valueOf(organization.getUsdBID()));
        return view;
    }
}
