package com.example.carpool02;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

public class resulOfSearchedRide extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<recyclerListItem> recyclerListItems;
    String pickUpLoc,destLoc;
    Date  dateToSearch;
    TextView routeTV;
    private FirebaseFirestore dbref;
    CollectionReference RideCollectionRef,profileCollectionRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.rides_recycler_view,container,false);
        dbref=FirebaseFirestore.getInstance();
        RideCollectionRef = dbref.collection("rides");
        profileCollectionRef=dbref.collection("userProfiles");
        recyclerView = v.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerListItems = new ArrayList<>();

        Bundle bundle = getArguments();
        pickUpLoc=bundle.getString("pickupLocation");
        destLoc = bundle.getString("destinationLocation");
        final String date = bundle.getString("dateOfRide");

        routeTV=v.findViewById(R.id.route);

        try {
            dateToSearch = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        RideCollectionRef.whereEqualTo("pickUpLocation",pickUpLoc)
                .whereEqualTo("destinationLocation",destLoc)
                .whereEqualTo("dateOfBooking",dateToSearch).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        if(!queryDocumentSnapshots.isEmpty()){
                            Toast.makeText(getContext(),"query snapshot is not empty",Toast.LENGTH_SHORT).show();

                            List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                            routeTV.setText(pickUpLoc+" to "+destLoc);
                            for(DocumentSnapshot d : list){
                                final rides ri = d.toObject(rides.class);
                                String id = ri.getUserId();
                                final String bkngDate=ri.getDateOfBooking().toString().substring(0,10);
                                profileCollectionRef.document(id).get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                profileInfo pi = documentSnapshot.toObject(profileInfo.class);
                                                String time = ri.getHourOfBooking()+":"+ri.getMinuteOfBooking();
                                                recyclerListItem re = new recyclerListItem(pi.getname(),bkngDate,time,pi.getDiplayPictureUrl());
                                                recyclerListItems.add(re);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                    }
                                });
                            }
                            adapter = new RecyclerViewAdapter(recyclerListItems,getContext());
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);

                        }else{
                            Toast.makeText(getContext(),"query snapshot is empty"+dateToSearch.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT);

            }
        });




        return v;
    }
}
