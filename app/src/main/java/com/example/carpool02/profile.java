package com.example.carpool02;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.Inflater;

import static android.app.Activity.RESULT_OK;

public class profile extends Fragment {
    private static final int CHOOSE_IMAGE =101 ;
    Uri UriProfileImage;
    FirebaseFirestore db;
    private StorageReference mStorageRef;
    CollectionReference profileDBReference;
    FirebaseAuth mAuth;
    String profileImageUrl;
    Date dateOfBirth;
    TextView textViewDOB;
    String displayName,email,phoneNumber,carModel,carNumber,gender;
    EditText editTextdisplayName,editTextemail,editTextphone,editTextcarModel,editTextcarNumber;
    RadioGroup RGgender;
    RadioButton rb1,rb2,rb3;
    ImageView displayPicture;
    CardView save,logOut;
    DatePickerDialog.OnDateSetListener dateSetListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.profile,container,false);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        editTextdisplayName = v.findViewById(R.id.editTextName);
        editTextemail=v.findViewById(R.id.editTextEmail);
        editTextcarNumber=v.findViewById(R.id.editTextCarNumber);
        editTextphone = v.findViewById(R.id.editTextPhone);
        editTextcarModel=v.findViewById(R.id.editTextCarModel);
        RGgender=v.findViewById(R.id.radioGroup_gender);
        rb1=v.findViewById(R.id.radioButtonGen1);
        rb2=v.findViewById(R.id.radioButtonGen2);
        mStorageRef = FirebaseStorage.getInstance().getReference();
        rb3=v.findViewById(R.id.radioButtonGen3);
        displayPicture=v.findViewById(R.id.displayPicture);
        save = v.findViewById(R.id.cardViewSave);
        logOut = v.findViewById(R.id.cardViewLogout);
        profileDBReference = db.collection("userProfiles");
        textViewDOB = v.findViewById(R.id.textViewDOB);
        textViewDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),R.style.DatePickerDialogTheme,dateSetListener,year,month,day);
                datePickerDialog.show();
                dateSetListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month+=1;
                        String date=dayOfMonth+"/"+month+"/"+year;
                        try {
                            dateOfBirth = new SimpleDateFormat("dd/MM/yyyy").parse(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        textViewDOB.setText(date);
                    }
                };
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageToFirebase();
            }
            private void uploadImageToFirebase() {
                final StorageReference profilepicRef = mStorageRef.child("profilePics/"+System.currentTimeMillis()+".jpg");
                if (UriProfileImage!=null){
                    profilepicRef.putFile(UriProfileImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            profilepicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    profileImageUrl = uri.toString();
                                    saveUserInfo();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    saveUserInfo();
                }

            }



            private void saveUserInfo() {
                displayName = editTextdisplayName.getText().toString();
                email = editTextemail.getText().toString();
                phoneNumber = editTextphone.getText().toString();
                carModel = editTextcarModel.getText().toString();
                carNumber= editTextcarNumber.getText().toString();
                if(RGgender.getCheckedRadioButtonId()==R.id.radioButtonGen1){
                    gender=rb1.getText().toString();
                }else if(RGgender.getCheckedRadioButtonId()==R.id.radioButtonGen2){
                    gender=rb2.getText().toString();
                }else if(RGgender.getCheckedRadioButtonId()==R.id.radioButtonGen3){
                    gender=rb3.getText().toString();
                }

                profileInfo pi = new profileInfo(displayName,email,phoneNumber,gender,carModel,carNumber,mAuth.getCurrentUser().getUid(),profileImageUrl,dateOfBirth);
//                profileDBReference.add(pi).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentReference> task) {
//                        if(task.isSuccessful()){
//                            Toast.makeText(getContext(),"profile saved",Toast.LENGTH_SHORT).show();
//                        }else {
//                            Toast.makeText(getContext(),"profile not saved",Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
                profileDBReference.document(mAuth.getCurrentUser().getUid()).set(pi).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(),"profile saved",Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getContext(),"profile not saved",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                getActivity().finish();
                startActivity(new Intent(getActivity(),loginActivity.class));
            }
        });

        displayPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }


        });
        loadUserInfo();

        return v;
    }



    private void showImageChooser() {
        Intent in = new Intent();
        in.setType("image/*");
        in.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(in,"select Profile Image"),CHOOSE_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CHOOSE_IMAGE && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            Bitmap bitmap=null;
            UriProfileImage = data.getData();

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(),UriProfileImage);
                displayPicture.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()==null){
            getActivity().finish();
            startActivity(new Intent(getActivity(),loginActivity.class));
        }

    }

    private void loadUserInfo() {
        profileDBReference.document(mAuth.getCurrentUser().getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                profileInfo pi = documentSnapshot.toObject(profileInfo.class);
                if (pi!=null) {
                    editTextdisplayName.setText(pi.getname());
                    editTextcarModel.setText(pi.getCarModel());
                    editTextcarNumber.setText(pi.getCarNumber());
                    editTextemail.setText(pi.getEmail());
                    editTextphone.setText(pi.getPhoneNUmber());
                    if (pi.getGender() != null) {
                        if (pi.getGender().equals("Male")) {
                            RGgender.check(R.id.radioButtonGen1);
                        } else if (pi.getGender().equals("Female")) {
                            RGgender.check(R.id.radioButtonGen2);
                        } else {
                            RGgender.check(R.id.radioButtonGen3);
                        }
                    }

                    if (pi.getDob() != null) {
                        String date1 = new SimpleDateFormat("dd/MM/yyyy").format(pi.getDob());
                        textViewDOB.setText(date1);
                    }

                    if (pi.getDiplayPictureUrl() != null) {
                        profileImageUrl = pi.getDiplayPictureUrl();
                        Glide.with(getContext())
                                .load(pi.getDiplayPictureUrl())
                                .into(displayPicture);
                    }

                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error Loading profile.."+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
}
