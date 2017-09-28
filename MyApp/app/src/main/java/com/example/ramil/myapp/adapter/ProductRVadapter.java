package com.example.ramil.myapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ramil.myapp.R;
import com.example.ramil.myapp.activities.ProductActivity;
import com.example.ramil.myapp.model.Products;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductRVadapter extends RecyclerView.Adapter<ProductRVadapter.ProductViewHolder>
{
    private ArrayList<Products> data;
    private Context context;

    public static class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView nameTitle;
        TextView descriptionTitle;
        TextView priceTitle;
        ImageView productImage;
        ArrayList<Products> products = new ArrayList<Products>();
        Context ctx;

        public ProductViewHolder( View itemView, Context ctx, ArrayList<Products> products)
        {
            super(itemView);

            this.products = products;
            this.ctx = ctx;

            nameTitle = (TextView) itemView.findViewById(R.id.RVtitle1);
            descriptionTitle = (TextView) itemView.findViewById(R.id.RVtitle2);
            priceTitle = (TextView) itemView.findViewById(R.id.RVtitle3);
            productImage = (ImageView) itemView.findViewById(R.id.productIVsmall);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Products product = this.products.get(position);
            Intent intent = new Intent(this.ctx, ProductActivity.class);
            intent.putExtra( "pid", product.getPid() );
            this.ctx.startActivity(intent);
        }
    }

    public ProductRVadapter(ArrayList<Products> data, Context ctx){
        this.data = data;
        this.context = ctx;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from( parent.getContext() ).inflate(R.layout.product_item, parent, false);

        return new ProductViewHolder(view, context, data);
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position)
    {
        Products item = data.get(position);

        holder.nameTitle.setText(item.getName());
        holder.descriptionTitle.setText(item.getDescription());

        String str = Double.toString(item.getPrice());
        holder.priceTitle.setText(str);

        Picasso.with(holder.productImage.getContext())
                .load(item.getImage())
                .resize(75, 75)
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


}
