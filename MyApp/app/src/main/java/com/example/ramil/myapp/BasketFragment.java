package com.example.ramil.myapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramil.myapp.networkutils.RetrofitClient;
import com.example.ramil.myapp.networkutils.ServiceAPI;
import com.example.ramil.myapp.model.Basket;
import com.example.ramil.myapp.model.BasketList;
import com.example.ramil.myapp.model.BasketResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BasketFragment extends Fragment {

    private static final String TAG = "BasketFragment";

    private RecyclerView mRecyclerView;
    private BasketAdapter mAdapter;

    private TextView mCountTitle;
    private TextView mTotalPriceTitle;
    private TextView mEmptyBasketTitle;
    private Button mConfirmButton;

    private List<Basket> mBasketItems;

    private Disposable mDisposable;

    public static BasketFragment newInstance() {
        return new BasketFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_basket, container, false);

        mRecyclerView = view.findViewById(R.id.basket_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mCountTitle = view.findViewById(R.id.basket_count_title);
        mTotalPriceTitle = view.findViewById(R.id.basket_price_title);
        mEmptyBasketTitle = view.findViewById(R.id.empty_content_text);
        mConfirmButton = view.findViewById(R.id.confirm_button);

        getBasket();

        mConfirmButton.setOnClickListener((v) -> deleteBasket());

        return view;
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed())
            mDisposable.dispose();

        super.onDestroy();
    }

    private void updateUI(List<Basket> basket) {
        if (basket != null) {
            mAdapter = new BasketAdapter(basket);
            mRecyclerView.setAdapter(mAdapter);

            int count = 0;
            int totalPrice = 0;

            for (int i = 0; i < basket.size(); i++) {
                count += basket.get(i).getCount();
                totalPrice += basket.get(i).getTotalPrice();
            }

    //  TODO загрузка строк из strings.xml
            if (count == 1)
                mCountTitle.setText(String.valueOf(count) + " товар на сумму:");
            else if (count == 2 || count == 3 || count == 4)
                mCountTitle.setText(String.valueOf(count) + " товара на сумму:");
            else
                mCountTitle.setText(String.valueOf(count) + " товаров на сумму:");
            mTotalPriceTitle.setText(String.valueOf(totalPrice) + "р.");
        } else {
            mEmptyBasketTitle.setText("В корзине пока нет товаров");
            mConfirmButton.setEnabled(false);
        }
    }

    // TODO перенести в DataDownloader
    private void getBasket() {
        mDisposable = RetrofitClient.getInstance()
                .getBasket()
                .subscribeOn(Schedulers.io())
                .map(BasketList::getBasket)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                       basket -> {
                           Log.d(TAG, "onResponse: Полученные данные:" + basket.toString() );
                           updateUI(basket);
                       },
                       throwable -> {
                           Log.e(TAG, "onFailure: Что-то пошло не так:"
                                   + throwable.getMessage() );
                           Toast.makeText(getActivity(), "Все плохо!",
                                   Toast.LENGTH_LONG).show();
                       }
                );
    }

    // TODO перенести в DataDownloader
    private void deleteBasket() {
        mDisposable = RetrofitClient.getInstance()
                .deleteBasket()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        basketResponse -> getActivity().recreate(),
                        throwable -> {
                            Log.e(TAG, "onFailure: Что-то пошло не так:"
                                    + throwable.getMessage() );
                            Toast.makeText(getActivity(), "Все плохо!",
                                    Toast.LENGTH_LONG).show();
                        }
                );
    }

    // TODO перенести в DataDownloader
    private void deleteBasketItem(int id) {
        mDisposable = RetrofitClient.getInstance()
                .deleteBasketItem(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        basketResponse -> { },
                        throwable -> {
                            Log.e("BasketFragment", "onFailure: Что-то пошло не так:"
                                    + throwable.getMessage() );
                            Toast.makeText(getActivity(), "Все плохо!",
                                    Toast.LENGTH_LONG).show();
                        }
                );
    }

    private class BasketItemHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private ImageView mProductImage;
        private TextView mNameTitle;
        private TextView mPriceTitle;
        private ImageButton mDeleteButton;

        private Basket mBasket;

        public BasketItemHolder(View itemView) {
            super(itemView);

            mProductImage = itemView.findViewById(R.id.basket_item_image);
            mNameTitle = itemView.findViewById(R.id.basket_item_name);
            mPriceTitle = itemView.findViewById(R.id.basket_item_price);
            mDeleteButton = itemView.findViewById(R.id.basket_item_delete_button);

            itemView.setOnClickListener(this);
        }

        public void bindBasketItem(Basket basket) {
            mBasket = basket;

            mNameTitle.setText(mBasket.getName());
            mPriceTitle.setText(String.valueOf( mBasket.getTotalPrice() ) + "р.");

            Picasso.with(mProductImage.getContext())
                    .load(mBasket.getImage())
                    .resize(75, 75)
                    .into(mProductImage);

            mDeleteButton.setOnClickListener((v) -> {
                        deleteBasketItem(mBasket.getIdOrder());
                        mBasketItems.remove(getAdapterPosition());
                        mAdapter.notifyItemRemoved(getAdapterPosition());
                        mAdapter.notifyItemRangeChanged(getAdapterPosition(), mBasketItems.size());
                    });
        }

        @Override
        public void onClick(View v) {
            Intent intent = ProductActivity.newIntent(getActivity(), mBasket.getIdOrder());
            startActivity(intent);
        }
    }

    private class BasketAdapter extends RecyclerView.Adapter<BasketItemHolder> {

        public BasketAdapter(List<Basket> basket) {
            mBasketItems = basket;
        }

        @NonNull
        @Override
        public BasketItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_basket, parent, false);

            return new BasketItemHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull BasketItemHolder holder, int position) {
            Basket basket = mBasketItems.get(position);
            holder.bindBasketItem(basket);
        }

        @Override
        public int getItemCount() {
            return mBasketItems == null ? 0 : mBasketItems.size();
        }
    }
}
