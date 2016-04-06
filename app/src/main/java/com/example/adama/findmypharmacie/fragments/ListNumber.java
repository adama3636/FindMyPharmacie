package com.example.adama.findmypharmacie.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.adama.findmypharmacie.ParmacieDetail;
import com.example.adama.findmypharmacie.R;
import com.example.adama.findmypharmacie.adapters.NumbersAdapter;
import com.example.adama.findmypharmacie.adapters.PharmacieAdapter;
import com.example.adama.findmypharmacie.models.DatabaseHelperPharmacie;
import com.example.adama.findmypharmacie.models.EmergencyNumbers;
import com.example.adama.findmypharmacie.models.Pharmacie;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListNumber.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListNumber#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListNumber extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView recyclerView;

    private Context context;
    private Activity activity;
    private NumbersAdapter eAdapter;
    private List<EmergencyNumbers> numberList = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public ListNumber() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ListPharmacie.
     */
    // TODO: Rename and change types and number of parameters
    public static ListNumber newInstance(String param1, String param2) {
        ListNumber fragment = new ListNumber();
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
        context = getContext();
        activity = getActivity();
        activity.setTitle("Numéros utiles");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_numbers, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.numbers_list_view);

        eAdapter = new NumbersAdapter(numberList, context);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(mLayoutManager);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        itemAnimator.setAddDuration(1000);
        itemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(itemAnimator);
        recyclerView.setAdapter(eAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(context, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                try {
                    TextView numberToCall = (TextView) view.findViewById(R.id.telephoneListNumber);
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+numberToCall.getText().toString()));
                    startActivity(callIntent);
                } catch (ActivityNotFoundException activityException) {
                    Log.e("Calling a Phone Number", "Call failed", activityException);
                }

            }

        }));
        preparePharmacieData();
        return view;
    }

    public interface ClickListener {
        void onClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ListNumber.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ListNumber.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildLayoutPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }


    private void preparePharmacieData() {

        numberList.add(new EmergencyNumbers("SAMU National","1515","",R.drawable.samu));
        numberList.add(new EmergencyNumbers("Urgences Cardio","338250404","",R.drawable.samu));
        numberList.add(new EmergencyNumbers("SOS Medecin","338891515","",R.drawable.sos_medecin));
        numberList.add(new EmergencyNumbers("SUMA Assistance","338242418","",R.drawable.suma));
        numberList.add(new EmergencyNumbers("Renseignements","1212","",0));
        numberList.add(new EmergencyNumbers("Police","17","",0));
        numberList.add(new EmergencyNumbers("Pompier","18","",0));
        numberList.add(new EmergencyNumbers("Gendarmerie","800802020","",0));



//        DatabaseHelperPharmacie db = new DatabaseHelperPharmacie(context);
//        ArrayList pharmacies = db.getPharmacie();
//        for (int i = 0; i < pharmacies.size(); i++){
//            String[] pharmacie = pharmacies.get(i).toString().split(",");
//            Pharmacie lignePharmacie = new Pharmacie(pharmacie[0], pharmacie[1], pharmacie[2],pharmacie[3], pharmacie[4],pharmacie[5],pharmacie[6],pharmacie[7]);
//            pharmacieList.add(lignePharmacie);
//        }
        eAdapter.notifyDataSetChanged();
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
