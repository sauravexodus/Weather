package com.exodus.weather.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
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
import com.exodus.weather.interfaces.OnCitySelectedListener;
import com.exodus.weather.store.DaoSession;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class CitySearchDialog extends DialogFragment implements OnCitySelectedListener {

    @Inject
    DaoSession daoSession;

    @BindView(R.id.city_list_dialog_recycler_view)
    RecyclerView recyclerView;

    @BindView(R.id.city_list_dialog_search)
    EditText searchBar;

    Unbinder unbinder;
    CityListAdapter cityListAdapter;
    Timer timer;
    OnCitySelectedListener onCitySelectedListener;

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
                if (timer != null)
                    timer.cancel();
            }

            @Override
            public void afterTextChanged(final Editable s) {
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        getActivity().runOnUiThread(new TimerTask() {
                            @Override
                            public void run() {
                                try {
                                    cityListAdapter.reQuery(s.toString());
                                    recyclerView.scrollToPosition(0);
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                }, 2000);
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
        return view;
    }

    private void initializeRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        cityListAdapter = new CityListAdapter(getActivity());
        cityListAdapter.setCitySelectListener(this);
        cityListAdapter.reQuery("New York");
        recyclerView.setAdapter(cityListAdapter);
        cityListAdapter.notifyDataSetChanged();

    }

    public void setOnCitySelectedListener(OnCitySelectedListener listener) {
        this.onCitySelectedListener = listener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onCitySelected(long cityId) {
        onCitySelectedListener.onCitySelected(cityId);
        dismiss();
    }
}
