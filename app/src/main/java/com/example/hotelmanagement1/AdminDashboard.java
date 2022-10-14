package com.example.hotelmanagement1;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminDashboard extends AppCompatActivity {

    Button btnAdd;

    private ArrayList<RoomModel> roomModelArrayList;
    RecyclerView rvCrops;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        btnAdd= findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AdminDashboard.this, AddRoom.class));
            }
        });

        listCrops();

    }

    public void listCrops(){

        roomModelArrayList = new ArrayList<>();

        rvCrops= findViewById(R.id.rvCropItems);
        rvCrops.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        rvCrops.setLayoutManager(linearLayoutManager);


        db= FirebaseFirestore.getInstance();

        db.collection("Room")
                .orderBy("Number")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null){
                            Log.e(TAG, "Fetch may have returned null", error);
                        }else {
                            assert value != null;
                            for(DocumentChange dc: value.getDocumentChanges()){
                                if(dc.getType()== DocumentChange.Type.ADDED){
                                    roomModelArrayList.add(dc.getDocument().toObject(RoomModel.class));
                                }
                                RoomsAdapter cropsAdapter = new RoomsAdapter(AdminDashboard.this, roomModelArrayList);
                                rvCrops.setAdapter(cropsAdapter);
                                Log.i(TAG, "Fetched");
                                cropsAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}