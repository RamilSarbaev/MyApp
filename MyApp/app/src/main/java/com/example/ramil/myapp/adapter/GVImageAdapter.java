package com.example.ramil.myapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.ramil.myapp.activities.ProductActivity;
import com.example.ramil.myapp.model.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GVImageAdapter extends BaseAdapter
{
    private Context mContext;
    private ArrayList<Products> data;

    public GVImageAdapter(Context c, ArrayList<Products> data){
        this.mContext = c;
        this.data = data;
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null)
        {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(350, 350));
            imageView.setScaleType(ImageView.ScaleType.CENTER);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(imageView.getContext())
                .load(data.get(position).getImage())
                .resize(350, 350)
                .into(imageView);

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Products product = data.get(position);
                Intent intent = new Intent(mContext, ProductActivity.class);
                intent.putExtra( "pid", product.getPid() );
                mContext.startActivity(intent);
            }
        });

        return imageView;
    }

}
