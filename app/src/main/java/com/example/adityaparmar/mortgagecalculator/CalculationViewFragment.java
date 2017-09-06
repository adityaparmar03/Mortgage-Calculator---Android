package com.example.adityaparmar.mortgagecalculator;

import android.app.Fragment;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static com.example.adityaparmar.mortgagecalculator.R.id.zip;

/**
 * Created by adityaparmar on 3/20/17.
 */

public class CalculationViewFragment extends Fragment {

    View view;
    Button bcalculate, bclear, bsave;
    int pp, dp, rate, period, payment;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.calculationviewfragmentlayout,container,false);



        final EditText property_price = (EditText)view.findViewById(R.id.propertyprice);
        final EditText down_payment = (EditText)view.findViewById(R.id.downpayment);
        final EditText annual_rate = (EditText)view.findViewById(R.id.rate);
        final Spinner term = (Spinner)view.findViewById(R.id.spinnerterm);
        bcalculate = (Button)view.findViewById(R.id.bcalculate);
        bclear = (Button)view.findViewById(R.id.bclear);
        bsave = (Button) view.findViewById(R.id.bsave);

        //bsave.setEnabled(false);

        final EditText street = (EditText) view.findViewById(R.id.street);
        final EditText city = (EditText) view.findViewById(R.id.city);
        final Spinner state = (Spinner) view.findViewById(R.id.state);
        final EditText zipcode = (EditText) view.findViewById(zip);
        final Spinner propertytype = (Spinner)view.findViewById(R.id.spinnerpropertytype);

        String[] list1 = new String[]{"Alaska", "Alabama",
                "Arkansas",
                "Arizona",
                "California",
                "Colorado",
                "Connecticut",
                "Delaware",
                "Florida",
                "Georgia",
                "Hawaii",
                "Iowa",
                "Idaho",
                "Illinois",
                "Indiana",
                "Kansas",
                "Kentucky",
                "Louisiana",
                "Massachusetts",
                "Maryland",
                "Maine",
                "Michigan",
                "Minnesota",
                "Missouri",
                "Mississippi",
                "Montana",
                "North Carolina",
                "North Dakota",
                "Nebraska",
                "New Hampshire",
                "New Jersey",
                "New Mexico",
                "Nevada",
                "New York",
                "Ohio",
                "Oklahoma",
                "Oregon",
                "Pennsylvania",
                "Rhode Island",
                "South Carolina",
                "South Dakota",
                "Tennessee",
                "Texas",
                "Utah",
                "Virginia",
                "Vermont",
                "Washington",
                "Wisconsin",
                "West Virginia",
                "Wyoming"};
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, list1);



        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                bsave.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {
                bsave.setEnabled(true);
            }
        };

        //e_street.addTextChangedListener(tw);
        zipcode.addTextChangedListener(tw);





        Spinner dropdown = (Spinner) view.findViewById(R.id.spinnerpropertytype);
        String[] list = new String[]{"House", "TownHouse", "Condo"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, list);

        final TextView m_payment = (TextView) view.findViewById(R.id.text_monthly_payment);
        dropdown.setAdapter(adapter);
        state.setAdapter(adapter1);


        String[] list2 = new String[]{"15", "30"};
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, list2);


        term.setAdapter(adapter2);


        if(ID.Id > 0) {


            DatabaseOperations databaseOperations = new DatabaseOperations(getActivity().getBaseContext(), TableData.TableInfo.DATABASE_NAME, null, 1);
            String[][] details = databaseOperations.getInfobyID(databaseOperations, ID.Id);

            street.setText(details[0][1]);
            city.setText(details[0][2]);

            int stateid = Arrays.asList(list1).indexOf(details[0][3]);
            state.setSelection(stateid);
            zipcode.setText(details[0][4]);
            int propertytypeid = Arrays.asList(list).indexOf(details[0][5]);
            propertytype.setSelection(propertytypeid);
            property_price.setText(details[0][6]);
            down_payment.setText(details[0][7]);
            annual_rate.setText(details[0][8]);
            int termid = Arrays.asList(list).indexOf(details[0][9]);
            propertytype.setSelection(termid);
            m_payment.setText("Monthly payment: $"+details[0][10]);
        }






        bcalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String PropertyPrice = property_price.getText().toString().trim();
                String downPayment = down_payment.getText().toString().trim();
                String annualrate = annual_rate.getText().toString().trim();
                String speriod = term.getSelectedItem().toString().trim();

                if(PropertyPrice.equals(""))
                {
                    Toast.makeText(getActivity(),"Please enter valid Property price ",Toast.LENGTH_LONG).show();
                }
                else if(downPayment.equals("")){

                    Toast.makeText(getActivity(),"Please enter valid Downpayment ",Toast.LENGTH_LONG).show();
                }
                else if(annualrate.equals("")){

                    Toast.makeText(getActivity(),"Please enter valid Annual Rate ",Toast.LENGTH_LONG).show();
                }
                else if(speriod.equals("")){

                    Toast.makeText(getActivity(),"Please enter valid Terms ",Toast.LENGTH_LONG).show();
                }
                else if(Double.parseDouble(downPayment) > Double.parseDouble(PropertyPrice)){

                    Toast.makeText(getActivity(), "Downpayment should be less than Property Price ", Toast.LENGTH_LONG).show();
                }
                else
                {





                    payment = calculate_mortgage(Double.parseDouble(PropertyPrice), Double.parseDouble(downPayment), Double.parseDouble(annualrate), Integer.parseInt(speriod));
                    m_payment.setText("Monthly payment: $"+String.valueOf(payment));

                }

            }
        });

        bclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                property_price.setText("");
                down_payment.setText("");
                annual_rate.setText("");
                //term.setText("");
                m_payment.setText("");
                street.setText("");
                city.setText("");
                zipcode.setText("");
                ID.Id=0;

                state.setSelection(0);
                term.setSelection(0);
                propertytype.setSelection(0);
            }
        });

        Context mContext = getActivity().getBaseContext();
        final DatabaseOperations handler = new DatabaseOperations(mContext, TableData.TableInfo.DATABASE_NAME, null, 1);


        bsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){



                final String Street = street.getText().toString();
                final String City = city.getText().toString();
                final String State = state.getSelectedItem().toString();
                final String Zip = zipcode.getText().toString();
                final String Propertytype = propertytype.getSelectedItem().toString();
                final String PropertyPrice = property_price.getText().toString();
                final String Downpayment = down_payment.getText().toString();
                final String APR = annual_rate.getText().toString();
                final String Terms = term.getSelectedItem().toString();







                if (Street.equals("")) {
                        Toast.makeText(getActivity(), "Please enter Street ", Toast.LENGTH_LONG).show();
                    } else if (City.equals("")) {

                        Toast.makeText(getActivity(), "Please enter City ", Toast.LENGTH_LONG).show();
                    } else if (Zip.equals("")) {

                        Toast.makeText(getActivity(), "Please enter Zipcode ", Toast.LENGTH_LONG).show();

                    }else if(PropertyPrice.equals("")) {

                        Toast.makeText(getActivity(), "Please enter Property Price ", Toast.LENGTH_LONG).show();
                    }
                    else if(Downpayment.equals("")) {

                        Toast.makeText(getActivity(), "Please enter Downpayment ", Toast.LENGTH_LONG).show();
                    }
                    else if(Double.parseDouble(Downpayment) > Double.parseDouble(PropertyPrice)){

                        Toast.makeText(getActivity(), "Downpayment should be less than Property Price ", Toast.LENGTH_LONG).show();
                    }
                    else if(APR.equals("")) {

                        Toast.makeText(getActivity(), "Please enter Annual Rate of Interest ", Toast.LENGTH_LONG).show();
                    }
                    else if(Terms.equals("")) {

                        Toast.makeText(getActivity(), "Please enter Annual Number of years ", Toast.LENGTH_LONG).show();
                    }else {

                    final String MonthlyInstallment= String.valueOf(calculate_mortgage(Double.parseDouble(PropertyPrice), Double.parseDouble(Downpayment), Double.parseDouble(APR), Integer.parseInt(Terms)));

                    List<Address> addressList = null;
                        try {
                            addressList = validate_address(Street, City, State, Zip);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if(addressList != null) {
                            if (!addressList.isEmpty()) {
                                if (ID.Id == 0) {
                                    handler.getWritableDatabase();
                                    long k = handler.insertInfo(handler, Street, City, State, Zip, Propertytype, PropertyPrice, Downpayment, APR, Terms, MonthlyInstallment);
                                    if (k > 0) {
                                        Toast.makeText(getActivity(), "Calculation is successfully saved.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Something went wrong, Try next time", Toast.LENGTH_LONG).show();

                                    }
                                    handler.close();
                                } else {
                                    handler.getWritableDatabase();

                                    long k = handler.update(handler, Street, City, State, Zip, Propertytype, PropertyPrice, Downpayment, APR, Terms, MonthlyInstallment);
                                    if (k > 0) {
                                        Toast.makeText(getActivity(), "Calculation is successfully updated.", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(getActivity(), "Something went wrong, Try next time", Toast.LENGTH_LONG).show();

                                    }
                                    handler.close();
                                }
                                //Log.d("Save", String.valueOf(k));
                            } else {
                                Toast.makeText(getActivity(), "Please enter Valid Address ", Toast.LENGTH_LONG).show();
                            }
                        }
                    }


            }

        });

        return view;
    }

    public int calculate_mortgage(double pp, double dp, double rate, int period){
        double principal = pp - dp;
        double r = (rate/12.0)/100.0;
        int n = period*12;
        double monthly_payment;
        monthly_payment = principal*(((r* Math.pow((1+r), n))/(Math.pow((1+r), n) -1)));
        return (int) Math.ceil(monthly_payment);
    }

    public List<Address> validate_address(String street, String city, String state, String zip) throws Exception{

        String locationName= street+","+city+","+state+","+zip;




        Geocoder geocoder = new Geocoder(getActivity());
        List<Address> addressList = null;



        if(locationName != null || locationName != " "){
            try {

                addressList = geocoder.getFromLocationName (locationName, 1);

            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        return addressList;

    }

}