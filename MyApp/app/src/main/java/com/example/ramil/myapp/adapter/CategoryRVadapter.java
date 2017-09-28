package com.example.ramil.myapp.adapter;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ramil.myapp.activities.CatalogActivity;
import com.example.ramil.myapp.R;
import com.example.ramil.myapp.model.Categories;

import java.util.ArrayList;

public class CategoryRVadapter extends RecyclerView.Adapter<CategoryRVadapter.CategoryViewHolder>
{
    private ArrayList<Categories> data;
    private Context context;

    public static class CategoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        CardView cardView;
        TextView nameTitle;
        ArrayList<Categories> categories = new ArrayList<Categories>();
        Context ctx;

        public CategoryViewHolder(View itemView, Context ctx, ArrayList<Categories> categories)
        {
            super(itemView);

            this.categories = categories;
            this.ctx = ctx;

            cardView = (CardView) itemView.findViewById(R.id.cardView);
            nameTitle = (TextView) itemView.findViewById(R.id.categRVtitle);

            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Categories category = this.categories.get(position);
            Intent intent = new Intent(this.ctx, CatalogActivity.class);
            intent.putExtra( "id_cat", category.getPid() );
            intent.putExtra( "name", category.getName() );
            this.ctx.startActivity(intent);
        }
    }

    public CategoryRVadapter(ArrayList<Categories> data, Context ctx )
    {
        this.data = data;
        this.context = ctx;
    }

    @Override
    public CategoryRVadapter.CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from( parent.getContext() ).inflate(R.layout.category_item, parent, false);

        return new CategoryRVadapter.CategoryViewHolder(view, context, data);
    }

    @Override
    public void onBindViewHolder(CategoryRVadapter.CategoryViewHolder holder, int position)
    {
        Categories item = data.get(position);
        holder.nameTitle.setText(item.getName());
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

}
