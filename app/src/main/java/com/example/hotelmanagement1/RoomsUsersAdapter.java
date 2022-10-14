package com.example.hotelmanagement1;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class RoomsUsersAdapter extends RecyclerView.Adapter<RoomUsersViewholder> {

    private static final String TAG = "Meal_Orders_Adapter";
    private Context context;
    private ArrayList<RoomModel> RoomModelArrayList;
    FirebaseFirestore db;

    public RoomsUsersAdapter(Context context, ArrayList<RoomModel> cropModelArrayList) {
        this.context = context;
        RoomModelArrayList = cropModelArrayList;
    }

   /* public void deleteFromCart(int position){
        CropModel model = CropModelArrayList.get(position);
        db= FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String userId= firebaseUser.getUid();

        CollectionReference collectionReference= db.collection("users").document(userId).collection("orders");
        DocumentReference documentReference= db.collection("users").document(userId).collection("orders").document(model.getDocumentID());

        documentReference.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: Order removed from database.");
                        }
                    }
                });
    }*/

    @NonNull
    @Override
    public RoomUsersViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item_user1, parent, false);
        return new RoomUsersViewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RoomUsersViewholder holder, int position) {

        RoomModel model = RoomModelArrayList.get(position);
        holder.tvNumber.setText(model.getNumber());
        holder.tvPrice.setText("$" + model.getPrice()+ "/Night");
        Glide.with(context)
                .load(model.getImageSrc())
                .placeholder(R.drawable.ic_baseline_image_not_supported_24)
                .centerCrop()
                .into(holder.ivRoom);
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(RoomsUsersAdapter.this.context, Reservations.class).putExtra("documentID", model.getDocumentId()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return RoomModelArrayList.size();
    }
}

