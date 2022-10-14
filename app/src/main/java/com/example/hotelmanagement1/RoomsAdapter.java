package com.example.hotelmanagement1;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;


import java.util.ArrayList;

public class RoomsAdapter extends RecyclerView.Adapter<RoomViewholder> {

    private static final String TAG = "Meal_Orders_Adapter";
    private Context context;
    private ArrayList<RoomModel> RoomModelArrayList;
    FirebaseFirestore db;

    public RoomsAdapter(Context context, ArrayList<RoomModel> cropModelArrayList) {
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
    public RoomViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_item, parent, false);
        return new RoomViewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RoomViewholder holder, int position) {

        RoomModel model = RoomModelArrayList.get(position);
        holder.tvNumber.setText(model.getNumber());
        holder.tvPrice.setText(model.getPrice());
        Glide.with(context)
                .load(model.getImageSrc())
                .placeholder(R.drawable.ic_baseline_image_not_supported_24)
                .centerCrop()
                .into(holder.ivRoom);

        FirebaseFirestore db;
        db = FirebaseFirestore.getInstance();
        CollectionReference collectionReference = db.collection("Room");
        DocumentReference documentReference = collectionReference.document(model.getDocumentId());

        documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value.getBoolean("Available") == false) {
                    holder.switchStatus.setChecked(true);
                }
            }
        });

        holder.switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                FirebaseFirestore db;
                db = FirebaseFirestore.getInstance();
                CollectionReference collectionReference = db.collection("Room");
                DocumentReference documentReference = collectionReference.document(model.getDocumentId());

                documentReference.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.getBoolean("Available") == true) {
                            documentReference.update("Available", false);
                        }
                    }
                });


            }
        });
    }


    @Override
    public int getItemCount() {
        return RoomModelArrayList.size();
    }
}

