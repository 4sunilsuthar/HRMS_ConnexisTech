package com.lms.admin.lms;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class ContactUsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);
/*
        FragmentManager myFM = getActivity().getSupportFragmentManager();

        final SupportMapFragment myMAPF = (SupportMapFragment) myFM
                .findFragmentById(R.id.google_map);
        myMAPF.getMapAsync(this);*/

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map);
        mapFragment.getMapAsync(this);
        return view;

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView btnImgCopyContent, btnImgWebsite, btnImgEmail, btnImgFacebook, btnImgYouTube, btnImgLinkedIn;
        btnImgCopyContent = view.findViewById(R.id.btn_copy_to_clipboard);
        btnImgWebsite = view.findViewById(R.id.btn_connect_to_website);
        btnImgEmail = view.findViewById(R.id.btn_connect_to_email);
        btnImgFacebook = view.findViewById(R.id.btn_connect_to_facebook);
        btnImgYouTube = view.findViewById(R.id.btn_connect_to_youtube);
        btnImgLinkedIn = view.findViewById(R.id.btn_connect_to_linked_in);

        //btn to copy text to the clipboard
        btnImgCopyContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("myLabel", "This is FlexiHR AppText");
                clipboard.setPrimaryClip(clip);
                Toast.makeText(getContext(), "Address Copied to Clipboard!!!", Toast.LENGTH_SHORT).show();
            }
        });

        //btn to visit to website

        //btn to email

        //btn to facebook

        //btn to youtube

        //btn to linked-in

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mGoogleMap = googleMap;
        LatLng connexisAddress = new LatLng(18.511366, 73.782253); //change here if office address changes
        mGoogleMap.addMarker(new MarkerOptions().position(connexisAddress)
                .title("Connexis Technologies"));
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(connexisAddress, 15.0f));
    }
}
