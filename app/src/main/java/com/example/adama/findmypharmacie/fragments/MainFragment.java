package com.example.adama.findmypharmacie.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adama.findmypharmacie.models.DatabaseHelperPharmacie;
import com.example.adama.findmypharmacie.models.Pharmacie;
import com.example.adama.findmypharmacie.models.ValidForm;
import com.example.adama.findmypharmacie.utils.GPSTracker;
import com.example.adama.findmypharmacie.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    GPSTracker gps;
    public double latitude;
    public double longitude;

    TextView tvName;
    TextInputLayout textInputLayoutName;
    TextView tvAddress;
    TextInputLayout textInputLayoutAddress;
    TextView tvTelephone;
    TextInputLayout textInputLayoutTelephone;
    TextView tvLongitude;
    TextView tvLatitude;
    TextView tvAccuracy;
    Button btnSubmit;
    Button btnTakePhoto;

    private ImageView mPhotoCapturedImageView;

    private String imageFileLocation;

    private static final int ACTIVITY_START_CAMERA_APP = 0;

    private OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
        getActivity().setTitle("Formulaire d'ajout");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        this.tvName = (TextView) view.findViewById(R.id.name);
        this.textInputLayoutName = (TextInputLayout) view.findViewById(R.id.input_layout_name);
        this.tvAddress = (TextView) view.findViewById(R.id.neighborhood);
        this.textInputLayoutAddress = (TextInputLayout) view.findViewById(R.id.input_layout_address);
        this.tvTelephone = (TextView) view.findViewById(R.id.phone);
        this.textInputLayoutTelephone = (TextInputLayout) view.findViewById(R.id.input_layout_telephone);
        this.tvLongitude = (TextView) view.findViewById(R.id.logitude);
        this.tvLatitude = (TextView) view.findViewById(R.id.latitude);
        this.tvAccuracy = (TextView) view.findViewById(R.id.accuracy);
        this.btnSubmit = (Button) view.findViewById(R.id.submit);
        this.btnSubmit.setOnClickListener(this);
        this.btnTakePhoto = (Button) view.findViewById(R.id.takePic);
        this.btnTakePhoto.setOnClickListener(this);

        mPhotoCapturedImageView = (ImageView) view.findViewById(R.id.photoCaptured);

        Button btnRefresh = (Button)  view.findViewById(R.id.refresh);
        btnRefresh.setOnClickListener(this);
        btnRefresh.performClick();

        return view;

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

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.refresh){

            GPSTracker gps = new GPSTracker(getContext());
            double latitude = 0;
            double longitude = 0;
            double accuracy = 0;

            if(gps.canGetLocation()){
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
                accuracy = gps.getAccuracy();
                Toast.makeText(getContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude+ "\nAcc: " + accuracy, Toast.LENGTH_LONG).show();
                //btnSubmit.setEnabled(true);

            }else{
                gps.showSettingsAlert();
            }
            if((latitude!=0) && (longitude != 0)){

                this.tvLatitude.setText(latitude + "");
                this.tvLongitude.setText(longitude + "");
                this.tvAccuracy.setText(accuracy + "");
                this.btnSubmit.setEnabled(true);
                this.btnSubmit.setBackgroundResource(R.drawable.custom_button);
                this.btnSubmit.setPadding(60, 10, 60, 10);
                this.btnSubmit.setTextColor(Color.WHITE);

            }
        }else if(v.getId() == R.id.submit){
            submitForm(v);
        }else if(v.getId() == R.id.takePic){
            takePhoto(v);
        }
    }


    public void submitForm  (View view){
        ValidForm validForm = new ValidForm();
        if (!validForm.validString(tvName, textInputLayoutName,  getString(R.string.err_msg_name), getActivity())
                || !validForm.validString(tvAddress, textInputLayoutAddress, getString(R.string.err_msg_address), getActivity())
                || !validForm.validString(tvTelephone, textInputLayoutTelephone, getString(R.string.err_msg_telephone), getActivity())) {
            return;
        }

            TelephonyManager telephonyManager = (TelephonyManager)getActivity().getSystemService(Context.TELEPHONY_SERVICE);

            // Get values of Componants
            String strName = tvName.getText().toString();
            tvName.setText("");
            String strAddress = tvAddress.getText().toString();
            tvAddress.setText("");
            String strTelephone = tvTelephone.getText().toString();
            tvTelephone.setText("");
            String strEmei = telephonyManager.getDeviceId();
            String strLatitude = tvLatitude.getText().toString();
            tvLatitude.setText("");
            String strLongitude = tvLongitude.getText().toString();
            tvLongitude.setText("");
            String strAccuracy = tvAccuracy.getText().toString();
            tvAccuracy.setText("");
            DatabaseHelperPharmacie dbCon = new DatabaseHelperPharmacie(getContext());

            Pharmacie pharmacie = new Pharmacie(strName,strTelephone, strAddress, strEmei, strLatitude, strLongitude,strAccuracy, imageFileLocation);
            dbCon.insertPharmacie(pharmacie);
            Toast.makeText(getContext(),R.string.success_insert_pharmacie, Toast.LENGTH_LONG).show();
            mPhotoCapturedImageView.setImageResource(0);
            //updateList();
            // Intent i = new Intent(FormContact.this, MainActivity.class);
//                Fragment listPharmacie = new ListPharmacie();
//                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
//                fragmentTransaction.replace(R.id.list_pharmacie, listPharmacie, null);
//                fragmentTransaction.commit();

            // i.putExtra("Name",strName);
//                i.putExtra("Surname",strSurname);
//                i.putExtra("Sexe", sex);
//                i.putExtra("Login", strLogin);
//                i.putExtra("Password", strPassword);
//
            //startActivity(i);

    }

    public void takePhoto(View view){
        Intent callCameraApplicationIntent = new Intent();
        callCameraApplicationIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile = null;
        try {
            photoFile = createImageFile();
        }catch (IOException e){
            e.printStackTrace();
        }
        callCameraApplicationIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        startActivityForResult(callCameraApplicationIntent, ACTIVITY_START_CAMERA_APP);
    }

    public File createImageFile() throws IOException{
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "IMAGE_" + timeStamp + "_";
        File storageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(imageFileName,".jpg", storageDirectory);
        imageFileLocation = image.getAbsolutePath();
        return image;
    }

    public void setReducedImageSize(){
        int targetImageViewWidth = (int) getResources().getDimension(R.dimen.image_form_width);
        int targetImageViewHeight = (int) getResources().getDimension(R.dimen.image_form_height);

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFileLocation, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

    bmOptions.inSampleSize = Math.min(cameraImageWidth/targetImageViewWidth, cameraImageHeight/targetImageViewHeight);


        bmOptions.inJustDecodeBounds = false;

        Bitmap photoReducedSizeBitmap = BitmapFactory.decodeFile(imageFileLocation, bmOptions);
        mPhotoCapturedImageView.setImageBitmap(photoReducedSizeBitmap);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ACTIVITY_START_CAMERA_APP && resultCode == Activity.RESULT_OK){
            setReducedImageSize();
            Toast.makeText(getContext(),imageFileLocation, Toast.LENGTH_SHORT).show();
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
