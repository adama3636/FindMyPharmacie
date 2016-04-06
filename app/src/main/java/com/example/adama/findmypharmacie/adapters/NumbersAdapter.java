package com.example.adama.findmypharmacie.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adama.findmypharmacie.R;
import com.example.adama.findmypharmacie.models.EmergencyNumbers;
import com.example.adama.findmypharmacie.models.Pharmacie;

import java.io.File;
import java.util.List;

public class NumbersAdapter extends RecyclerView.Adapter<NumbersAdapter.MyViewHolder>{
    private List<EmergencyNumbers> numbersList;
    private Context context;

    public NumbersAdapter(List<EmergencyNumbers> numbersList, Context context){
        this.numbersList = numbersList;
        this.context = context;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView name, telephone, type;
        private ImageView image;

        public MyViewHolder(View view) {
            super(view);
            this.name = (TextView) view.findViewById(R.id.nameListNumber);
            this.telephone = (TextView) view.findViewById(R.id.telephoneListNumber);
            this.type = (TextView) view.findViewById(R.id.typeListNumber);
            this.image = (ImageView) view.findViewById(R.id.icon_emergency_numbers);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout_numbers, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(NumbersAdapter.MyViewHolder holder, int position) {
        EmergencyNumbers emergencyNumbers = numbersList.get(position);
        holder.name.setText(emergencyNumbers.getName());
        holder.type.setText(emergencyNumbers.getType());
        holder.telephone.setText(emergencyNumbers.getTelephone());
       // holder.name.setText(String.valueOf(holder.image.getDrawable().getIntrinsicHeight()));
        if( emergencyNumbers.getImage() != 0){
            setReducedImageSize(holder, emergencyNumbers.getImage());
        }
    }

    @Override
    public int getItemCount() {
        return numbersList.size();
    }

    public void setReducedImageSize(NumbersAdapter.MyViewHolder holder, int imageRessource){

        int tagetImageViewWidth = (int) context.getResources().getDimension(R.dimen.image_row_width);
        int tagetImageViewHeight =(int) context.getResources().getDimension(R.dimen.image_row_height);

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), imageRessource, bmOptions);
        int cameraImageWidth = bmOptions.outWidth;
        int cameraImageHeight = bmOptions.outHeight;

    bmOptions.inSampleSize = Math.min(cameraImageWidth/tagetImageViewWidth, cameraImageHeight/tagetImageViewHeight);

        bmOptions.inJustDecodeBounds = false;

        Bitmap photoReducedSizeBitmap = BitmapFactory.decodeResource(context.getResources(), imageRessource, bmOptions);
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
