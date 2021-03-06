package com.example.quizapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.quizapp.Common.Common;
import com.example.quizapp.Interface.ItemClickListener;
import com.example.quizapp.Model.Catagory;
import com.example.quizapp.ViewHolder.CatagoryViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class CatagoryFragment extends Fragment {

    View myFragment;

    RecyclerView listCatagory;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Catagory, CatagoryViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference catagory;

    public static CatagoryFragment newInstance(){
        CatagoryFragment catagoryFragment = new CatagoryFragment();
        return catagoryFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        database = FirebaseDatabase.getInstance();
        catagory = database.getReference("Catagory");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        myFragment = inflater.inflate(R.layout.fragment_catagory, container,false);

        listCatagory = (RecyclerView)myFragment.findViewById(R.id.listCatagory);
        listCatagory.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(container.getContext());
        listCatagory.setLayoutManager(layoutManager);

        loadCatagories();

        return  myFragment;
    }

    private void loadCatagories() {
        adapter = new FirebaseRecyclerAdapter<Catagory, CatagoryViewHolder>(
                Catagory.class,
                R.layout.catagoty_layout,
                CatagoryViewHolder.class,
                catagory
        ) {
            @Override
            protected void populateViewHolder(CatagoryViewHolder holder, final Catagory model, int position) {
                holder.catagory_name.setText(model.getName());
                Picasso.with(getActivity())
                        .load(model.getImage())
                        .into(holder.catagory_image);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        Toast.makeText(getActivity(), String.format("%s|%s",adapter.getRef(position).getKey(),model.getName()),Toast.LENGTH_SHORT).show();
                        Intent startGame = new Intent(getActivity(),Start.class);
                        Common.categoryId = adapter.getRef(position).getKey();
                        Common.categoryName = model.getName();
                        startActivity(startGame);
                    }
                });
            }
        };
        adapter.notifyDataSetChanged();
        listCatagory.setAdapter(adapter);

    }
}
