package com.examples.pccoe;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NoticeFragment extends Fragment {
    ImageView logout,logo;
    RecyclerView recyclerView;
    NoticeAdapter NoticeAdapter;
    DatabaseReference mDatabase;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notice, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView=getView().findViewById(R.id.recyclerView);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<NoticeModel> options
                = new FirebaseRecyclerOptions.Builder<NoticeModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("chats"), NoticeModel.class)
                .build();
        NoticeAdapter =new NoticeAdapter(options);
        recyclerView.setAdapter(NoticeAdapter);


    }

    @Override
    public void onStart() {
        super.onStart();
        recyclerView.getRecycledViewPool().clear();
        NoticeAdapter.notifyDataSetChanged();
        NoticeAdapter.startListening();
    }


    @Override
    public void onStop() {
        super.onStop();
        NoticeAdapter.stopListening();
    }
}
