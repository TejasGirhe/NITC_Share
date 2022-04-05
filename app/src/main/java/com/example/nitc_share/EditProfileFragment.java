package com.example.nitc_share;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class EditProfileFragment extends Fragment {

    private static String name;
    private static String email;
    Button save;
    EditText etEmail,etPhone, etName;
    RatingBar ratingBar;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    private static final String ARG_PARAM1 = "name";
    private static final String ARG_PARAM2 = "email";
    private static final String ARG_PARAM3 = "phone";
    private static final String ARG_PARAM4 = "rating";

    private String mParam1;
    private String mParam2;
    private String mParam3;
    private String mParam4;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    public static EditProfileFragment newInstance(String param1, String param2,String param3, String param4) {
        EditProfileFragment fragment = new EditProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putString(ARG_PARAM3, param3);
        args.putString(ARG_PARAM4, param4);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            mParam3 = getArguments().getString(ARG_PARAM3);
            mParam4 = getArguments().getString(ARG_PARAM4);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        etEmail = view.findViewById(R.id.tvEmail);
        etPhone = view.findViewById(R.id.tvPhone);
        etName = view.findViewById(R.id.tvName);
        save = view.findViewById(R.id.SaveBtn);
        ratingBar = view.findViewById(R.id.profile_rating);


//        Toast.makeText(getContext(),mParam1 + mParam2 + mParam3 + mParam4,Toast.LENGTH_SHORT).show();
        etName.setText(mParam1);
        etName.setGravity(Gravity.CENTER);
        etEmail.setText(mParam2);
        etPhone.setText(mParam3);
        etPhone.setGravity(Gravity.CENTER);
        ratingBar.setRating(Float.parseFloat(mParam4));


        etEmail.setBackground(null);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder =  new AlertDialog.Builder(getContext());
                builder.setTitle("Edit Profile").setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                                reference.child("name").setValue(etName.getText().toString());
                                reference.child("phone").setValue(etPhone.getText().toString());

                                Fragment fragment = new ProfileFragment();
                                getParentFragmentManager().beginTransaction().replace(R.id.body_container,fragment).commit();
                            }
                        }).setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        return view;
    }
}