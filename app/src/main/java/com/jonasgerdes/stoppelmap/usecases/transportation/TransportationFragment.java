package com.jonasgerdes.stoppelmap.usecases.transportation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jonasgerdes.stoppelmap.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Jonas on 03.07.2016.
 */
public class TransportationFragment extends Fragment {

    private Unbinder mUnbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transportation, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnbinder = ButterKnife.bind(this, view);
    }

    @Override
    public void onDestroyView() {
        if (mUnbinder != null) {
            mUnbinder.unbind();
        }
        super.onDestroyView();
    }

    public static TransportationFragment newInstance() {

        Bundle args = new Bundle();

        TransportationFragment fragment = new TransportationFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
