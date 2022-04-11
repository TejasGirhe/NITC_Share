package com.example.nitc_share;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
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

import com.example.nitc_share.constructors.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.util.regex.*;

public class EditProfileFragment extends Fragment {

    private static String name;
    private static String email;
    Button save;
    EditText etEmail,etPhone, etName;
    RatingBar ratingBar;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    TextView  purchases, sold;
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

        sold = view.findViewById(R.id.sold);
        purchases = view.findViewById(R.id.purchases);
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
                String str = etName.getText().toString();
                str = str.replaceAll(" ", "");
//                Toast.makeText(getContext(), str, Toast.LENGTH_SHORT).show();
                if(!isValidUsername(str)){
                    etName.setError("Please Enter Valid Name");
                    etName.requestFocus();
                } else if(etName.getText().toString().equals("") || etName.getText().toString().equals(null)){
                    etName.setError("Please Enter Valid Name");
                    etName.requestFocus();
                }else if(etPhone.getText().toString().equals("") || etPhone.getText().toString().length()!=10){
                    etPhone.setError("Please Enter Valid Contact");
                    etPhone.requestFocus();
                }else if(!isValidMobileNo(etPhone.getText().toString())) {
                    etPhone.setError("Please Enter Valid Contact");
                    etPhone.requestFocus();
                }else
                    {
                    AlertDialog.Builder builder =  new AlertDialog.Builder(getContext());
                    builder.setTitle("Edit Profile").setMessage("Are you sure?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(user.getUid());
                                    reference.child("name").setValue(etName.getText().toString());
                                    reference.child("phone").setValue(etPhone.getText().toString());

                                    Toast.makeText(getContext(), "Your profile was successfully updated", Toast.LENGTH_SHORT).show();
                                    Fragment fragment = new ProfileFragment();
                                    getParentFragmentManager().beginTransaction().replace(R.id.body_container,fragment).commit();
                                }
                            }).setNegativeButton("No", null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    sold.setText(user.getSold().toString());
                    purchases.setText(user.getBought().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    public static boolean isValidMobileNo(String str)
    {
        for(int i = 0; i < 10; i++){
            if(!(str.charAt(i) >= '0' && str.charAt(i) <= '9'))
            {
                return false;
            }
        }

        return true;
    }

    public static boolean isValidUsername(String name)
    {
        String regex = "^[A-Za-z]\\w{5,29}$";
        Pattern p = Pattern.compile(regex);
        if (name == null) {
            return false;
        }
        Matcher m = p.matcher(name);
        return m.matches();
    }
}