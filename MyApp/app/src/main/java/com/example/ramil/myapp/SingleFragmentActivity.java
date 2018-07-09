package com.example.ramil.myapp;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

public abstract class SingleFragmentActivity extends AppCompatActivity {

    //Метод для создания экземпляра фрагмента
    protected abstract Fragment createFragment();

    //Сообщаем AS, что реализация метода должна возвращать действ.идентификатор ресурса макета
    @LayoutRes
    protected int getLayoutResId() {
        return R.layout.activity_fragment;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment == null) {
            fragment = createFragment();
            //Транзакция фрагмента
            fm.beginTransaction()
                    .add(R.id.fragment_container, fragment)  //Идентификация UI-фрагмента по ID ресурса
                    //его контейнерного представления
                    .commit();                               //Закрепить
        }
    }
}
