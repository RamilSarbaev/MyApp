package com.example.ramil.myapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramil.myapp.networkutils.RetrofitClient;
import com.example.ramil.myapp.model.Products;
import com.example.ramil.myapp.model.ProductsList;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class ProductFragment extends Fragment {

    private static final String TAG = "ProductFragment";

    private static final String ARG_PID = "product_id";

    private TextView mNameTitle, mAvailableTitle, mPriceTitle, mDecriptionTitle;
    private ImageView mImageview;
    private Button mBuyButton;
    private int mId, mAvailable;
    private int mProductId;

    private Disposable mDisposable;

    public static ProductFragment newInstance(int productId) {
        Bundle args = new Bundle();
        args.putInt(ARG_PID, productId);

        ProductFragment fragment = new ProductFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_product, container, false);

        mProductId = getArguments().getInt(ARG_PID, 0);

        mNameTitle = view.findViewById(R.id.product_title);
        mAvailableTitle = view.findViewById(R.id.product_available_title);
        mPriceTitle = view.findViewById(R.id.product_price_title);
        mDecriptionTitle = view.findViewById(R.id.product_description_title);
        mImageview = view.findViewById(R.id.product_image);
        mBuyButton = view.findViewById(R.id.buy_button);

        getProduct();

        mBuyButton.setOnClickListener( (v) -> {
                //TODO выбор количества
        });

        return view;
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed())
            mDisposable.dispose();

        super.onDestroy();
    }

    private void updateUI(List<Products> products) {
        String str = Double.toString( products.get(0).getPrice() );

        mNameTitle.setText(products.get(0).getName());
        mAvailableTitle.setText( "В наличии: " + products.get(0).getAvailable() + "шт");
        mPriceTitle.setText(str + "р.");
        mDecriptionTitle.setText(products.get(0).getDescription());

        Picasso.with(mImageview.getContext())
                .load(products.get(0).getImage())
                .resize( 400, 200 )
                .into(mImageview);

        mId = products.get(0).getPid();
        mAvailable = products.get(0).getAvailable();
    }

    // TODO перенести в DataDownloader
    private void getProduct() {
        mDisposable = RetrofitClient.getInstance()
                .getProduct(mProductId)
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

    // TODO перенести в DataDownloader
    private void createOrder(int id, int count) {
        mDisposable = RetrofitClient.getInstance()
                .createOrder(id, count)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        basketResponse -> {
                            Log.d(TAG, "onResponse: Ответ Сервера:" + basketResponse.toString() );
                            Toast.makeText(getActivity(), "Товар был перенесен в корзину",
                                    Toast.LENGTH_LONG).show();
                        },
                        throwable -> {
                            Log.e(TAG, "onFailure: Что-то пошло не так:" + throwable.getMessage() );
                            Toast.makeText(getActivity(), "Все плохо!", Toast.LENGTH_LONG).show();
                        }
                );
    }
}
