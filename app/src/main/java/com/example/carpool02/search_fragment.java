package com.example.carpool02;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.carpool02.adapter.PlaceautoSuggestAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class search_fragment extends Fragment {
    Context thiscontext;
    TextView datetv,timetv;
    String date;
    DatePickerDialog.OnDateSetListener dateSetListener;
    TimePickerDialog.OnTimeSetListener timeSetListener;
    Date dateOFRidetoSearch;
    Integer hourOfRide,minuteOfRide;
    Button buttonSearch;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dbref;
    CollectionReference RideCollectionRef;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search, container, false);

        final AutoCompleteTextView autoCompleteTextView1= v.findViewById(R.id.pickupLoc);
        autoCompleteTextView1.setAdapter(new PlaceautoSuggestAdapter(getContext(),android.R.layout.simple_list_item_1));
        final AutoCompleteTextView autoCompleteTextView2= v.findViewById(R.id.detinationLoc);
        autoCompleteTextView2.setAdapter(new PlaceautoSuggestAdapter(getContext(),android.R.layout.simple_list_item_1));
        datetv = v.findViewById(R.id.textView_SearchDate);
        timetv = v.findViewById(R.id.TextView_searchTime);
        buttonSearch = v.findViewById(R.id.button_searchRide);
        mAuth = FirebaseAuth.getInstance();
        dbref = FirebaseFirestore.getInstance();
        RideCollectionRef = dbref.collection("rides");
        datetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),R.style.DatePickerDialogTheme,dateSetListener,year,month,day);
                datePickerDialog.show();
            }
        });
        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month+=1;
                date = dayOfMonth+"/"+month+"/"+year;
                datetv.setText(date);
                try {
                    dateOFRidetoSearch = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        };
        timetv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),R.style.DatePickerDialogTheme,timeSetListener,hour,minute,false);
                timePickerDialog.show();
            }
        });
        timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String ampm;
                hourOfRide = hourOfDay;
                minuteOfRide=minute;
                if (hourOfDay>=12){
                    ampm="PM";
                    hourOfDay-=12;
                }
                else {
                    ampm="AM";
                }
                timetv.setText(hourOfDay+":"+minute+ampm);

            }
        };
        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                RideCollectionRef.whereEqualTo("pickUpLocation",autoCompleteTextView1.getText().toString())
//                        .whereEqualTo("destinationLocation",autoCompleteTextView2.getText().toString())
//                        .whereEqualTo("dateOfBooking",dateOFRidetoSearch).get()
//                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                            @Override
//                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                                Toast.makeText(getContext(),"something happend",Toast.LENGTH_SHORT).show();
//                                for (QueryDocumentSnapshot querySnapshot : queryDocumentSnapshots){
//                                    rides ridee = querySnapshot.toObject(rides.class);
//                                }
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.i("FireStore error",e.toString());
//                        Toast.makeText(getContext(),"asdjasdmbasmbd",Toast.LENGTH_SHORT).show();
//                    }
//                });
//                Intent in = new Intent(getContext(),resulOfSearchedRide.class);
//                in.putExtra("pickupLocation",autoCompleteTextView1.getText().toString());
//                in.putExtra("destinationLocation",autoCompleteTextView2.getText().toString());
//                in.putExtra("dateOfRide",dateOFRidetoSearch);
//                startActivity(in);
                Bundle bundle = new Bundle();
                bundle.putString("pickupLocation",autoCompleteTextView1.getText().toString());
                bundle.putString("destinationLocation",autoCompleteTextView2.getText().toString());
                bundle.putString("dateOfRide",date);
                resulOfSearchedRide newresulOfSearchedRide = new resulOfSearchedRide();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                newresulOfSearchedRide.setArguments(bundle);
                fragmentTransaction.replace(R.id.fragment_container,newresulOfSearchedRide);
                fragmentTransaction.addToBackStack(null).commit();

            }
        });

        return v;
    }

}
