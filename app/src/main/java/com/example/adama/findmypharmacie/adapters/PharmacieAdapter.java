package com.example.adama.findmypharmacie.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adama.findmypharmacie.R;
import com.example.adama.findmypharmacie.models.Pharmacie;

import java.util.List;

public class PharmacieAdapter extends RecyclerView.Adapter<PharmacieAdapter.MyViewHolder>{
    private List<Pharmacie> pharmacieList;
    private Context context;

    public PharmacieAdapter(List<Pharmacie> pharmacieList, Context context){
        this.pharmacieList = pharmacieList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, telephone, address, emei, latitude, logitude, accracy;
        private ImageView image;

        public MyViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.nameList);
            this.telephone = (TextView) view.findViewById(R.id.telephoneList);
            this.address = (TextView) view.findViewById(R.id.addressList);
            this.emei = (TextView) view.findViewById(R.id.emei);
            this.latitude = (TextView) view.findViewById(R.id.latitude);
            this.logitude = (TextView) view.findViewById(R.id.logitude);
            this.accracy = (TextView) view.findViewById(R.id.accuracy);
            this.image = (ImageView) view.findViewById(R.id.icon_pharmacie);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PharmacieAdapter.MyViewHolder holder, int position) {
        Pharmacie pharmacie = pharmacieList.get(position);
        holder.name.setText(pharmacie.getName());
        holder.address.setText(pharmacie.getAddress());
        holder.telephone.setText(pharmacie.getTelephone());
       // holder.name.setText(String.valueOf(holder.image.getDrawable().getIntrinsicHeight()));
        if(pharmacie.getImage().length() > 5){
            setReducedImageSize(holder, pharmacie.getImage());
        }


    }

    @Override
    public int getItemCount() {
        return pharmacieList.size();
    }

    public void setReducedImageSize(PharmacieAdapter.MyViewHolder holder, String imageFileLocation){

        int tagetImageViewWidth = (int) context.getResources().getDimension(R.dimen.image_row_width);
        int tagetImageViewHeight =(int) context.getResources().getDimension(R.dimen.image_row_height);

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imageFileLocation, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

    bmOptions.inSampleSize = Math.min(cameraImageWidth/tagetImageViewWidth, cameraImageHeight/tagetImageViewHeight);

        bmOptions.inJustDecodeBounds = false;

        Bitmap photoReducedSizeBitmap = BitmapFactory.decodeFile(imageFileLocation, bmOptions);
        holder.image.setImageBitmap(cropToSquare(photoReducedSizeBitmap));
    }
    public static Bitmap cropToSquare(Bitmap bitmap){
        int width  = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = (height > width) ? width : height;
        int newHeight = (height > width)? height - ( height - width) : height;
        int cropW = (width - height) / 2;
        cropW = (cropW < 0)? 0: cropW;
        int cropH = (height - width) / 2;
        cropH = (cropH < 0)? 0: cropH;

        return Bitmap.createBitmap(bitmap, cropW, cropH, newWidth, newHeight);
    }

}
