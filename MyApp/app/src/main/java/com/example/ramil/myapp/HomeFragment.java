package com.example.ramil.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.ramil.myapp.networkutils.RetrofitClient;
import com.example.ramil.myapp.networkutils.ServiceAPI;
import com.example.ramil.myapp.model.Products;
import com.example.ramil.myapp.model.ProductsList;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";

    private ViewPager mViewPager;
    private RecyclerView mRecyclerView;
    private Timer mTimer;

    private Disposable mDisposable;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_head, container, false);

        mViewPager = view.findViewById(R.id.slider);
        mViewPager.setAdapter(new ViewPagerAdapter( getActivity() ));

        mRecyclerView = view.findViewById(R.id.popular_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new mTimerTask(), 2000, 4000);

        getPopularProducts();

        return view;
    }

    @Override
    public void onStop() {
        super.onStop();

        mTimer.cancel();
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed())
            mDisposable.dispose();

        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.action_bar_menu, menu);
    }

//    TODO action_bar_menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    // TODO перенести в DataDownloader
    public void getPopularProducts() {
        mDisposable = RetrofitClient.getInstance()
                .getPopularProducts()
                .subscribeOn(Schedulers.io())
                .map(ProductsList::getProducts)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (products) -> {
                                Log.d(TAG, "onResponse: Полученные данные:" + products.toString() );
                                mRecyclerView.setAdapter(new PopularAdapter(products));
                                },
                        (throwable) -> {
                                Log.e(TAG, "onFailure: Что-то пошло не так:" + throwable.getMessage() );
                                Toast.makeText(getActivity(), "Все плохо!", Toast.LENGTH_LONG).show();
                        }
                );
    }

    public class mTimerTask extends TimerTask {
        @Override
        public void run() {
            getActivity().runOnUiThread(() -> {
                if (mViewPager.getCurrentItem() == 0)
                    mViewPager.setCurrentItem(1);
                else if (mViewPager.getCurrentItem() == 1)
                    mViewPager.setCurrentItem(2);
                else
                    mViewPager.setCurrentItem(0);
            });
        }
    }


    private class PopularHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView mItemImageView;
        private Products mProduct;

        public PopularHolder(View itemView) {
            super(itemView);

            mItemImageView = itemView.findViewById(R.id.popular_item_image_view);
            itemView.setOnClickListener(this);
        }

        public void bindProductsItem(Products product) {
            mProduct = product;

            Picasso.with(getActivity())
                    .load(mProduct.getImage())
                    .into(mItemImageView);
        }

        @Override
        public void onClick(View v) {
            Intent intent = ProductActivity.newIntent(getActivity(), mProduct.getPid());
            startActivity(intent);
        }
    }

    private class PopularAdapter extends RecyclerView.Adapter<PopularHolder> {

        private List<Products> mProducts;

        public PopularAdapter(List<Products> products) {
            mProducts = products;
        }

        @NonNull
        @Override
        public PopularHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_popular, parent, false);

            return new PopularHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PopularHolder holder, int position) {
            Products product = mProducts.get(position);
            holder.bindProductsItem(product);
        }

        @Override
        public int getItemCount() {
            return mProducts == null ? 0 : mProducts.size();
        }
    }
}
