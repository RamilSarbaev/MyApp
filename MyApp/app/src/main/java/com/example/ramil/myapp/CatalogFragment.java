package com.example.ramil.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramil.myapp.networkutils.RetrofitClient;
import com.example.ramil.myapp.networkutils.ServiceAPI;
import com.example.ramil.myapp.model.Products;
import com.example.ramil.myapp.model.ProductsList;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CatalogFragment extends Fragment {

    private static final String TAG = "CatalogFragment";

    private static final String ARG_ID = "category_id";
    private static final String ARG_NAME = "category_name";

    private TextView mEmptyContentTextView;
    private RecyclerView mRecyclerView;
    private ProductAdapter mAdapter;

    private String mCategoryName;
    private int mCategoryId;

    private Disposable mDisposable;

    public static CatalogFragment newInstance(int categoryId, String categoryName) {
        Bundle args = new Bundle();
        args.putInt(ARG_ID, categoryId);
        args.putString(ARG_NAME, categoryName);

        CatalogFragment fragment = new CatalogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_catalog, container, false);

        mCategoryId = getArguments().getInt(ARG_ID, 0);
        mCategoryName = getArguments().getString(ARG_NAME);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setTitle(mCategoryName);

        mRecyclerView = view.findViewById(R.id.catalog_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        DividerItemDecoration dividerItemDecoration =
                new DividerItemDecoration(mRecyclerView.getContext(), LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        mEmptyContentTextView = view.findViewById(R.id.emptyContentText);

        getAllProducts();

        return view;
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed())
            mDisposable.dispose();

        super.onDestroy();
    }

    private void updateUI(List<Products> products) {
        if (products != null) {
            mAdapter = new ProductAdapter(products);
            mRecyclerView.setAdapter(mAdapter);
        } else
            mEmptyContentTextView.setText("В этой категории нет товаров");
    }

    // TODO перенести в DataDownloader
    private void getAllProducts() {
        mDisposable = RetrofitClient.getInstance()
                .getAllProducts(mCategoryId)
                .subscribeOn(Schedulers.io())
                .map(ProductsList::getProducts)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        products -> {
                            Log.d(TAG, "onResponse: Полученные данные:" + products.toString() );
                            updateUI(products);
                        },
                        throwable -> {
                            Log.e(TAG, "onFailure: Что-то пошло не так:" + throwable.getMessage() );
                            Toast.makeText(getActivity(), "Все плохо!", Toast.LENGTH_LONG).show();
                        }
                );
    }

    private class ProductHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mNameTitle;
        private TextView mDescriptionTitle;
        private TextView mPriceTitle;
        private ImageView mProductImage;

        private Products mProduct;

        public ProductHolder(View itemView) {
            super(itemView);

            mNameTitle = itemView.findViewById(R.id.product_recycler_view_name_title);
            mDescriptionTitle = itemView.findViewById(R.id.product_recycler_view_description_title);
            mPriceTitle = itemView.findViewById(R.id.product_recycler_view_price_title);
            mProductImage = itemView.findViewById(R.id.product_image_view_small);

            itemView.setOnClickListener(this);
        }

        public void bindCatalogItem(Products product) {
            mProduct = product;

            mNameTitle.setText(product.getName());
            mDescriptionTitle.setText(product.getDescription());

            String str = Double.toString(product.getPrice());
            mPriceTitle.setText(str);

            Picasso.with(getActivity())
                    .load(product.getImage())
                    .resize(75, 75)
                    .into(mProductImage);
        }

        @Override
        public void onClick(View v) {
            Intent intent = ProductActivity.newIntent(getActivity(), mProduct.getPid());
            startActivity(intent);
        }
    }

    private class ProductAdapter extends RecyclerView.Adapter<ProductHolder> {

        List<Products> mProducts;

        public ProductAdapter(List<Products> products) {
            mProducts = products;
        }

        @NonNull
        @Override
        public ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_product, parent, false);

            return new ProductHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ProductHolder holder, int position) {
            Products product = mProducts.get(position);
            holder.bindCatalogItem(product);
        }

        @Override
        public int getItemCount() {
            return mProducts == null ? 0 : mProducts.size();
        }
    }
}
