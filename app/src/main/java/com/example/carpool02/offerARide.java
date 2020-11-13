package com.example.carpool02;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class offerARide extends Fragment  {
    TextView textViewDate,textViewTime;
    AutoCompleteTextView OsourceLoc,OdestinationLoc;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    TimePickerDialog.OnTimeSetListener onTimeSetListener;
    Date dateOfRide;
    RadioGroup radioGroup_NoOfRides;
    Integer hourOfRide,minuteOfRide,noOfSeats;
    CardView cardView_offer;
    private FirebaseAuth mAuth;
    private FirebaseFirestore dbreference;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.offeraride,container,false);
        textViewDate=view.findViewById(R.id.textView_date);
        textViewTime=view.findViewById(R.id.textViewTime);
        dbreference = FirebaseFirestore.getInstance();
        mAuth=FirebaseAuth.getInstance();
        final CollectionReference dbrides= dbreference.collection("rides");
        textViewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c =Calendar.getInstance();
                int hour=c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),R.style.DatePickerDialogTheme,onTimeSetListener,hour,minute,false);
                timePickerDialog.show();

            }
        });
        onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
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
                textViewTime.setText(hourOfDay+":"+minute+ampm);
            }
        };
        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),R.style.DatePickerDialogTheme,onDateSetListener,year,month,day);
                datePickerDialog.show();
            }
        });

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month+=1;
                String date = dayOfMonth+"/"+month+"/"+year;
                try {
                    dateOfRide = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                textViewDate.setText(date);
            }
        };

        cardView_offer=view.findViewById(R.id.cardViewOfferARide);
        cardView_offer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OsourceLoc = view.findViewById(R.id.autoCompleteSourceLoc);
                OdestinationLoc = view.findViewById(R.id.autoCompleteDestinationLoc);
                radioGroup_NoOfRides=view.findViewById(R.id.radioGroup_seats);
                String destLoc=OdestinationLoc.getText().toString();
                String sourceLoc=OsourceLoc.getText().toString();
                radioGroup_NoOfRides.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        switch (checkedId){
                            case R.id.radioButton2:
                                noOfSeats=1;
                                break;
                            case R.id.radioButton3:
                                noOfSeats=2;
                                break;
                            case R.id.radioButton4:
                                noOfSeats=3;
                                break;
                        }
                    }
                });
                rides ride = new rides(sourceLoc,destLoc,mAuth.getCurrentUser().getUid(),noOfSeats,hourOfRide,minuteOfRide,dateOfRide);
                dbrides.add(ride).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(),"RIDE ADDED",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(getContext(),"some error occured",Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }

        });

        return view;
    }


}
