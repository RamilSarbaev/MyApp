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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ramil.myapp.networkutils.RetrofitClient;
import com.example.ramil.myapp.networkutils.ServiceAPI;
import com.example.ramil.myapp.model.Categories;
import com.example.ramil.myapp.model.CategoriesList;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoriesFragment extends Fragment {

    private static final String TAG = "CategoriesActivity";

    private RecyclerView mRecyclerView;

    private Disposable mDisposable;

    public static CategoriesFragment newInstance() {
        return new CategoriesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        mRecyclerView = view.findViewById(R.id.categories_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        getAllCategories();

        return view;
    }

    @Override
    public void onDestroy() {
        if (mDisposable != null && !mDisposable.isDisposed())
            mDisposable.dispose();

        super.onDestroy();
    }

    // TODO перенести в DataDownloader
    private void getAllCategories() {

        mDisposable = RetrofitClient.getInstance()
                .getAllCategories()
                .subscribeOn(Schedulers.io())
                .map(CategoriesList::getCategories)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        (categories) -> {
                            Log.d(TAG, "onResponse: Полученные данные:" + categories.toString() );
                            mRecyclerView.setAdapter(new CategoryAdapter(categories));
                        },
                        (throwable) -> {
                            Log.e(TAG, "onFailure: Что-то пошло не так:" + throwable.getMessage() );
                            Toast.makeText(getActivity(), "Все плохо!", Toast.LENGTH_LONG).show();
                        }
                );
    }

    private class CategoryHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView mNameTitle;
        private Categories mCategory;

        public CategoryHolder(View itemView) {
            super(itemView);

            mNameTitle = itemView.findViewById(R.id.categegory_title);

            itemView.setOnClickListener(this);
        }

        public void bindCategory(Categories category) {
            mCategory = category;
            mNameTitle.setText(category.getName());
        }

        @Override
        public void onClick(View v) {
            Intent intent = CatalogActivity.newIntent(getActivity(), mCategory.getPid(),
                    mCategory.getName());
            startActivity(intent);
        }
    }

    private class CategoryAdapter extends RecyclerView.Adapter<CategoryHolder> {

        List<Categories> mCategories;

        public CategoryAdapter(List<Categories> categories) {
            mCategories = categories;
        }

        @NonNull
        @Override
        public CategoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_category, parent, false);

            return new CategoryHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CategoryHolder holder, int position) {
            Categories category = mCategories.get(position);
            holder.bindCategory(category);
        }

        @Override
        public int getItemCount() {
            return mCategories == null ? 0 : mCategories.size();
        }
    }
}
