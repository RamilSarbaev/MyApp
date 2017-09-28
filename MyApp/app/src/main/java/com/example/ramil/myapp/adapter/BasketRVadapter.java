package com.example.ramil.myapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramil.myapp.R;
import com.example.ramil.myapp.activities.BasketActivity;
import com.example.ramil.myapp.activities.ProductActivity;
import com.example.ramil.myapp.interfaces.ServiceAPI;
import com.example.ramil.myapp.model.Basket;

import com.example.ramil.myapp.model.BasketResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasketRVadapter extends RecyclerView.Adapter<BasketRVadapter.BasketViewHolder>
{
    private ArrayList<Basket> data;
    private Context context;
    private ServiceAPI api;

    public static class BasketViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        CardView cardView;
        ImageView image;
        TextView nameTitle;
        TextView priceTitle;
        ImageButton deleteBtn;

        ArrayList<Basket> basket = new ArrayList<Basket>();
        Context ctx;

        public BasketViewHolder(View itemView, final Context ctx, final ArrayList<Basket> basket)
        {
            super(itemView);

            this.basket = basket;
            this.ctx = ctx;

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            image = (ImageView) itemView.findViewById(R.id.basketItemImage);
            nameTitle = (TextView) itemView.findViewById(R.id.basketItemName);
            priceTitle = (TextView) itemView.findViewById(R.id.basketItemPrice);
            deleteBtn = (ImageButton) itemView.findViewById(R.id.basketItemDeleteBtn);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Basket basket = this.basket.get(position);
            Intent intent = new Intent(this.ctx, ProductActivity.class);
            intent.putExtra( "pid", basket.getId_order() );
            this.ctx.startActivity(intent);
        }

    }

    public BasketRVadapter(ArrayList<Basket> data, Context ctx, ServiceAPI service){
        this.data = data;
        this.context = ctx;
        this.api = service;
    }

    @Override
    public BasketRVadapter.BasketViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from( parent.getContext() ).inflate(R.layout.basket_item, parent, false);

        return new BasketRVadapter.BasketViewHolder(view, context, data);
    }

    @Override
    public void onBindViewHolder(final BasketRVadapter.BasketViewHolder holder, final int position)
    {
        final Basket item = data.get(position);

        holder.nameTitle.setText(item.getName());
        holder.priceTitle.setText( String.valueOf( item.getTotal_price() ) + "р." );

        Picasso.with(holder.image.getContext())
                .load(item.getImage())
                .resize(75, 75)
                .into(holder.image);

        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteBasketItem(item.getId_order());
                data.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, data.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    private void deleteBasketItem(int id)
    {
        Call<BasketResponse> call = api.deleteBasketsItem(id);

        call.enqueue(new Callback<BasketResponse>() {
            @Override
            public void onResponse(Call<BasketResponse> call, Response<BasketResponse> response) {
                Log.d("", "onResponse: Ответ Сервера:" + response.body().toString() );
            }

            @Override
            public void onFailure(Call<BasketResponse> call, Throwable t) {
                Log.e("BasketRVadapter", "onFailure: Что-то пошло не так:" + t.getMessage() );
                Toast.makeText(context, "Все плохо!", Toast.LENGTH_LONG).show();
            }
        });
    }
}
