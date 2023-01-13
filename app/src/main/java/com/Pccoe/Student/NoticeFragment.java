package com.Pccoe.Student;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class NoticeFragment extends Fragment {
    RecyclerView recyclerView;
    NoticeAdapter NoticeAdapter;
    FirebaseMessaging FirebaseMessaging;
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
        try {
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        catch (Exception e){

        }
        FirebaseMessaging.getInstance().subscribeToTopic("n");
        recyclerView=getView().findViewById(R.id.recyclerView);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        FirebaseRecyclerOptions<NoticeModel> options
                = new FirebaseRecyclerOptions.Builder<NoticeModel>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("chats"), NoticeModel.class)
                .build();
        NoticeAdapter =new NoticeAdapter(options);
        recyclerView.setAdapter(NoticeAdapter);



    }

    @SuppressLint("NotifyDataSetChanged")
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
