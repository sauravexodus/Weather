package com.exodus.weather.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.exodus.weather.MyApplication;
import com.exodus.weather.R;
import com.exodus.weather.adapters.CityListAdapter;
import com.exodus.weather.store.DaoSession;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.blurry.Blurry;

public class CitySearchDialog extends DialogFragment {

    @Inject
    DaoSession daoSession;

    @BindView(R.id.city_list_dialog_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.city_list_dialog_search)
    EditText searchBar;

    Unbinder unbinder;
    CityListAdapter cityListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, R.style.AppTheme);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        unbinder = ButterKnife.bind(this, view);
        ((MyApplication) getActivity().getApplication()).getObjectsComponent().inject(this);


        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cityListAdapter.reQuery(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        initializeRecyclerView();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_city_dialog, null);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

        Blurry.with(getContext()).radius(10).sampling(2).onto(container);
        return view;
    }

    private void initializeRecyclerView() {
        cityListAdapter = new CityListAdapter(getActivity());
        cityListAdapter.reQuery("");
        recyclerView.setAdapter(cityListAdapter);
        cityListAdapter.notifyDataSetChanged();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
