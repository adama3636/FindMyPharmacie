package com.example.adama.findmypharmacie.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.adama.findmypharmacie.MainActivity;
import com.example.adama.findmypharmacie.ParmacieDetail;
import com.example.adama.findmypharmacie.R;
import com.example.adama.findmypharmacie.adapters.PharmacieAdapter;
import com.example.adama.findmypharmacie.models.DatabaseHelperPharmacie;
import com.example.adama.findmypharmacie.models.Pharmacie;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListPharmacie.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListPharmacie#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListPharmacie extends Fragment {
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
    private PharmacieAdapter eAdapter;
    private List<Pharmacie> pharmacieList = new ArrayList<>();

    private OnFragmentInteractionListener mListener;

    public ListPharmacie() {
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
    public static ListPharmacie newInstance(String param1, String param2) {
        ListPharmacie fragment = new ListPharmacie();
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
        activity.setTitle("Liste de pharmacies");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_pharmacie, container, false);


        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                MainFragment mainFragment= new MainFragment();
                 getFragmentManager().beginTransaction()
                        .replace(R.id.content_fragment, mainFragment,null)
                        .addToBackStack(null)
                        .commit();
            }
        });


        recyclerView = (RecyclerView) view.findViewById(R.id.main_list_view);
//        ListM1Adapter listM1Adapter =new ListM1Adapter(getApplicationContext(),R.layout.row_layout);
        eAdapter = new PharmacieAdapter(pharmacieList, context);
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

                ProgressDialog prog = new ProgressDialog(context);
                prog.setMessage("Veuillez patienter");
                prog.setCancelable(false);
                prog.setIndeterminate(true);
                prog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                prog.show();

                Pharmacie pharmacie = pharmacieList.get(position);
                Intent i = new Intent(getActivity(), ParmacieDetail.class);
                i.putExtra("Name", pharmacie.getName());
                i.putExtra("Telephone", pharmacie.getTelephone());
                i.putExtra("Address", pharmacie.getAddress());
                i.putExtra("Emei", pharmacie.getEmei());
                i.putExtra("Latitude", pharmacie.getLatitude());
                i.putExtra("Longitude", pharmacie.getLongitude());
                i.putExtra("Accuracy", pharmacie.getAccracy());
                i.putExtra("Image", pharmacie.getImage());
                startActivity(i);

            }

            @Override
            public void onLongClick(View view, int position) {
                final Pharmacie pharmacie = pharmacieList.get(position);
                AlertDialog.Builder myAlert = new AlertDialog.Builder(activity);
                myAlert.setMessage("Voulez vous afficher les informations de l'utilisateur selectionné ?")
                        .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //dialog.dismiss();
                                // Get values of Componants

//                                String etudiant_name = etudiant.getEtudiant_name();
//                                String etudiant_prenom = etudiant.getEtudiant_prenom();
//                                int etudiant_resource = etudiant.getEtudiant_resource();
//                                String heure = etudiant.getHeure();
//                                String telephone = etudiant.getTelephone();
//                                int type_action = etudiant.getType_action();
//                                int libelle_action = etudiant.getLibelle_action();
//
//                                Intent i = new Intent(MainActivity.this, ContactDetails.class);
//
//                                i.putExtra("Nom_etudiant",etudiant_name);
//                                i.putExtra("Prenom_etudiant",etudiant_prenom);
//                                i.putExtra("Etudiant_resource",etudiant_resource);
//                                i.putExtra("Heure", heure);
//                                i.putExtra("Telephone", telephone);
//                                i.putExtra("Type_action", type_action);
//                                i.putExtra("Libelle_action", libelle_action);
//
//                                startActivity(i);
                            }
                        })
                        .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                                //.setTitle(" ")
                                //.setIcon(R.drawable.kinder)
                        .create();
                myAlert.show();
            }

        }));
        preparePharmacieData();
        return view;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ListPharmacie.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ListPharmacie.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                    }
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

        DatabaseHelperPharmacie db = new DatabaseHelperPharmacie(context);
        ArrayList pharmacies = db.getPharmacie();
        for (int i = 0; i < pharmacies.size(); i++){
            String[] pharmacie = pharmacies.get(i).toString().split(",");
            Pharmacie lignePharmacie = new Pharmacie(pharmacie[0], pharmacie[1], pharmacie[2],pharmacie[3], pharmacie[4],pharmacie[5],pharmacie[6],pharmacie[7]);
            pharmacieList.add(lignePharmacie);
        }
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
