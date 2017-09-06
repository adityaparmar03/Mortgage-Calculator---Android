package com.example.adityaparmar.mortgagecalculator;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;


public class MapsActivity extends Fragment implements GoogleMap.OnMarkerClickListener,OnMapReadyCallback {


    MapView mMapView;
    public GoogleMap mMap;
    String[][] details;
    int k;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_maps, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume(); // needed to get the map to display immediately

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mMapView.getMapAsync(this);
       /* mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {


            }});*/
        ID.Id=0;
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }



    public void opendialogbox(final int i, final Marker marker){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        String str = "Property Type :"+details[i][5]+
                "\nStreet Address: "+details[i][1]+
                "\nCity: "+details[i][2]+
                "\nLoan amount:  $"+details[i][11]+
                "\nAPR:  "+details[i][8]+"%"+
                "\nMonthly payment:  $"+details[i][10];








        alertDialogBuilder.setTitle("Details")
                .setMessage(str)



                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        DatabaseOperations databaseOperations = new DatabaseOperations(getActivity().getBaseContext(), TableData.TableInfo.DATABASE_NAME, null, 1);
                        databaseOperations.getWritableDatabase();
                        int ra = databaseOperations.delete(databaseOperations,Integer.parseInt(details[i][0]));
                        if (ra>0)
                        {
                            marker.remove();
                            Toast.makeText(getActivity(), "Calculation is successfully deleted.", Toast.LENGTH_LONG).show();

                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Something went wrong, Try next time", Toast.LENGTH_LONG).show();

                        }
                    }
                })
                .setNeutralButton("Edit",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        android.app.FragmentManager fragmentManager = getFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.content_frame,new CalculationViewFragment()).commit();
                        ID.Id=Integer.parseInt(details[i][0]);

                    }
                })
                .setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //MapsActivity.this.getDialog().cancel();
                    }
                });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String ID = marker.getId();
        Integer EndPoint = ID.length();
        Integer Markernumber =  Integer.parseInt(ID.substring(1,EndPoint));

        opendialogbox(Markernumber,marker);
        //getdetails();
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        DatabaseOperations operations = new DatabaseOperations(getActivity().getBaseContext(),TableData.TableInfo.DATABASE_NAME, null, 1);
        details  = operations.getInfo(operations);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();



        //int k;
        if(details.length > 0) {
            for (k = 0; k < details.length; k++) {

                String locationName;


                locationName = details[k][1] + "," + details[k][2] + ","+ details[k][3] + "," +details[k][4];


                Geocoder geocoder = new Geocoder(getActivity());
                List<Address> addressList = null;


                if (locationName != null || locationName != " ") {
                    try {

                        addressList = geocoder.getFromLocationName(locationName, 1);

                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                }

                if(addressList != null) {
                    if (!addressList.isEmpty()) {

                        Address address = addressList.get(0);
                        LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                        mMap.addMarker(new MarkerOptions().position(latLng));
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


                        builder.include(latLng);


                    }
                }


            }
            if(details.length>0) {

                LatLngBounds bounds = builder.build();
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 150));
            }

            mMap.setOnMarkerClickListener(this);
            //mMap.setOnMarkerClickListener();

        }


    }
}