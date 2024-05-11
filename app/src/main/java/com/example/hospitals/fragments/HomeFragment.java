package com.example.hospitals.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hospitals.DataGenerator;
import com.example.hospitals.HospitalAdapter;
import com.example.hospitals.HospitalItem;
import com.example.hospitals.R;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    private RecyclerView recyclerView;
    private HospitalAdapter adapter;
    private ArrayList<HospitalItem> hospitalList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        setupRecyclerView();

        return view;
    }

    private void setupRecyclerView() {
        hospitalList = DataGenerator.generateFakeData();
        adapter = new HospitalAdapter(hospitalList, requireContext());
        recyclerView.setAdapter(adapter);
    }
}