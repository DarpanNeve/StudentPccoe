package com.Pccoe.Student;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Profile extends Fragment {
    View view;
    TextView name,email,heading,rollno,prn,branch,division;
    ImageView photo;
    GoogleSignInClient googleSignInClient;
    FirebaseAuth firebaseAuth;
    String email1,name1;
    private final String url="http://181.215.79.82";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_profile, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        photo= requireView().findViewById(R.id.photo);
        name= requireView().findViewById(R.id.name);
        email= requireView().findViewById(R.id.email);
        prn= requireView().findViewById(R.id.Prn);
        rollno= requireView().findViewById(R.id.RollNo);
        heading= requireView().findViewById(R.id.heading);
        branch= requireView().findViewById(R.id.Branch);
        division= requireView().findViewById(R.id.Division);

        // Initialize firebase auth
        firebaseAuth=FirebaseAuth.getInstance();

        // Initialize firebase user
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            // When firebase user is not equal to null
            // Set image on image view
            Glide.with(requireContext()).load(firebaseUser.getPhotoUrl()).circleCrop().into(photo);
            // set name on text view
            email.setText(firebaseUser.getEmail());
            email1=firebaseUser.getEmail();
            assert email1 != null;
            name1=email1.replace("."," ");
            name1=name1.replace("@pccoepune org","");
            name1 = name1.replaceAll("[0-9]","");
            name1=name1.toUpperCase();
            name.setText(name1);

        }
        // Initialize sign in client
        googleSignInClient= GoogleSignIn.getClient(requireContext(), GoogleSignInOptions.DEFAULT_SIGN_IN);
        rollno.setText(getActivity().getIntent().getStringExtra("rollno"));
        prn.setText(getActivity().getIntent().getStringExtra("prn"));
        division.setText(getActivity().getIntent().getStringExtra("division"));
        branch.setText(getActivity().getIntent().getStringExtra("branch"));



    }







}