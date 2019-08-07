package com.example.travlemantics;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealViewHolder>{
    ArrayList<com.example.travelmantics.TravelDeal> deals;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private ChildEventListener childEventListener;
    private ImageView imageDeal;
    public DealAdapter(){

        firebaseDatabase = FirebaseUtil.mFirebaseDatabase;
        databaseReference = FirebaseUtil.mDatabaseReference;
        deals = FirebaseUtil.mDeals;
        childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                com.example.travelmantics.TravelDeal td = dataSnapshot.getValue(com.example.travelmantics.TravelDeal.class);
                Log.d("Deal",td.getTitle());
                td.setId(dataSnapshot.getKey());
                deals.add(td);
                notifyItemInserted(deals.size()-1);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        databaseReference.addChildEventListener(childEventListener);
    }
    @Override
    public DealViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.rv_row,parent, false);
        return new DealViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DealViewHolder holder, int position) {
        com.example.travelmantics.TravelDeal deal = deals.get(position);
        holder.bind(deal);
    }

    @Override
    public int getItemCount() {
        return deals.size();
    }

    public class DealViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        TextView tvDiscription;
        TextView tvPrice;
        public DealViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView)itemView.findViewById(R.id.tvTitle);
            tvDiscription = (TextView)itemView.findViewById(R.id.tvDescription);
            tvPrice = (TextView)itemView.findViewById(R.id.tvPrice);
            imageDeal = itemView.findViewById(R.id.imageDeal);
            itemView.setOnClickListener(this);
        }
        public void bind(com.example.travelmantics.TravelDeal deal){
            tvTitle.setText(deal.getTitle());
            tvPrice.setText(deal.getPrice());
            tvDiscription.setText(deal.getDescription());
            showImage(deal.getImageUrl());
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Log.d("Clicked",String.valueOf(position));
            com.example.travelmantics.TravelDeal selectedDeal = deals.get(position);
            Intent intent = new Intent(view.getContext(),DealActivity.class);
            intent.putExtra("Deal",selectedDeal);
            view.getContext().startActivity(intent);
        }

        private void showImage(String uri){
            if (uri != null){
                int width = Resources.getSystem().getDisplayMetrics().widthPixels;
                Picasso.with(imageDeal.getContext())
                        .load(uri)
                        .resize(160,160)
                        .centerCrop()
                        .into(imageDeal);
                Log.d("image", "my image is showing oooooooo");
            }
        }
    }
}
