package com.example.leetop.lab5;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    public ArrayList<String> list;
    Context context;

    public MyAdapter(ArrayList<String> list) {
        this.list = list;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        //get the comma delimited string from invisible textView that holds all the data.
        String[]  results = (list.get(position).toString()).split(",");
        holder.nameTxt.setText("Name: " + results[0]);
        holder.scoreTxt.setText("Score: " + results[5]);
        holder.ratingTxt.setText("Rating: " + results[6]);
        holder.infoTxt.setText(list.get(position).toString());

        //only show the photo if it exists
        //if(!results[3].equals("none")) {
        //    try {
        //        final Bitmap bitmap = CameraUtils.scaleDownAndRotatePic(results[3]);
        //        holder.thumbnailImg.setImageBitmap(bitmap);
        //    } catch (NullPointerException e) {
        //        e.printStackTrace();
        //    }
        //}

        //show icon
        holder.nameTxt.setCompoundDrawablesWithIntrinsicBounds(android.R.drawable.ic_menu_info_details, 0, 0, 0);

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //create the row textviews
        public TextView nameTxt;
        public TextView scoreTxt;
        public TextView ratingTxt;
        public TextView infoTxt;
        public ImageView thumbnailImg;
        public LinearLayout myLayout;

        Context context;

        public MyViewHolder(View itemView) {
            super(itemView);
            myLayout = (LinearLayout) itemView;

            nameTxt = (TextView) itemView.findViewById(R.id.nameEntry);
            scoreTxt = (TextView) itemView.findViewById(R.id.scoreText);
            ratingTxt = (TextView) itemView.findViewById(R.id.ratingText);
            infoTxt = (TextView) itemView.findViewById(R.id.infoText); //invisible Textview that holds all the data
            //thumbnailImg = itemView.findViewById(R.id.imgThumbnail);

            itemView.setOnClickListener(this);
            context = itemView.getContext();

        }

        @Override
        public void onClick(View view) {

            //Send all data to LocationInfo activity to be viewed
            Intent intent= new Intent(context, locationInfoActivity.class);
            intent.putExtra("info", infoTxt.getText());
            intent.putExtra("index", Integer.toString((this.getAdapterPosition()+1)));

            context.startActivity(intent);
        }
    }
}