package com.example.nitc_share;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.nitc_share.constructors.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView tvName, tvPhone, tvEmail, tvPassword;

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase database;
    String sName, sEmail, sPhone;
    DatabaseReference reference;
    RatingBar ratingBar;
    SharedPreferences sharedPreferences;

    private  static  final String SHARED_PREF_NAME = "myPref";
    private  static  final String KEY_EMAIL = "email";
    private  static  final String KEY_PASSWORD= "password";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        tvName = view.findViewById(R.id.tvName);
        tvEmail = view.findViewById(R.id.tvEmail);
        tvPhone = view.findViewById(R.id.tvPhone);
        ratingBar = view.findViewById(R.id.profile_rating);
        ratingBar.setClickable(false);
        ratingBar.setFocusable(false);
        TextView logout = view.findViewById(R.id.logoutBtn);
        ImageButton logoutImg = view.findViewById(R.id.logoutImg);
        TextView Edit = view.findViewById(R.id.EditBtn);
        ImageButton Editimg = view.findViewById(R.id.editImg);

        sharedPreferences = getContext().getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =  new AlertDialog.Builder(getContext());
                builder.setTitle("Logout").setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseAuth.signOut();
                                SharedPreferences.Editor editor= sharedPreferences.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }).setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });
        logoutImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder =  new AlertDialog.Builder(getContext());
                builder.setTitle("Logout").setMessage("Are you sure?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                firebaseAuth.signOut();
                                SharedPreferences.Editor editor= sharedPreferences.edit();
                                editor.clear();
                                editor.commit();
                                Intent intent = new Intent(getContext(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }).setNegativeButton("No", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

        reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                tvName.setText(user.getName());
                tvEmail.setText(user.getEmail());
                tvPhone.setText(user.getPhone());
                ratingBar.setRating(user.getRating());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(getActivity(), EditProfileFragment.class);
//                i.putExtra("name",tvName.getText().toString());
//                i.putExtra("email",tvEmail.getText().toString());
//                i.putExtra("phone",tvPhone.getText().toString());
//                i.putExtra("rating",ratingBar.getRating());
//                startActivity(i);
                Bundle bundle = new Bundle();
                Fragment fragment = new EditProfileFragment();
                bundle.putString("name",tvName.getText().toString());
                bundle.putString("email",tvEmail.getText().toString());
                bundle.putString("phone",tvPhone.getText().toString());
                bundle.putString("rating", String.valueOf(ratingBar.getRating()));
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.body_container,fragment).commit();
            }
        });

        Editimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(getActivity(), EditProfileFragment.class);
//                i.putExtra("name",tvName.getText().toString());
//                i.putExtra("email",tvEmail.getText().toString());
//                i.putExtra("phone",tvPhone.getText().toString());
//                i.putExtra("rating",ratingBar.getRating());
//                startActivity(i);
                Bundle bundle = new Bundle();
                Fragment fragment = new EditProfileFragment();
                bundle.putString("name",tvName.getText().toString());
                bundle.putString("email",tvEmail.getText().toString());
                bundle.putString("phone",tvPhone.getText().toString());
                bundle.putString("rating", String.valueOf(ratingBar.getRating()));
                fragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction().replace(R.id.body_container,fragment).commit();
            }
        });


        return view;
    }
}