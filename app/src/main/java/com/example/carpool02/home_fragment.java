package com.example.carpool02;

import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class home_fragment extends Fragment {

    Button findARide ;
    BottomNavigationView btmNav;
    Button offerAride;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        View v1 = inflater.inflate(R.layout.activity_navigation_bottom,null);
        btmNav=v1.findViewById(R.id.bottom_navigation);

        findARide=(Button) view.findViewById(R.id.findARide);
        findARide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                search_fragment newsearchfragment = new search_fragment();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, newsearchfragment);
                fragmentTransaction.addToBackStack(null).commit();


//                navigation_bottom nvb = new navigation_bottom();
//                nvb.bottomNav.setSelectedItemId(R.id.navigation_search);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new search_fragment).commit();

            }
        });
        offerAride = view.findViewById(R.id.offerARide);
        offerAride.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                offerARide newOfferFragment = new offerARide();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container,newOfferFragment);
                fragmentTransaction.addToBackStack(null).commit();
            }
        });


        return view;
    }
}
