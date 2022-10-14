package com.example.hotelmanagement1;

import static android.content.ContentValues.TAG;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class UsersHome extends AppCompatActivity {

    private ArrayList<RoomModel> roomModelArrayList;
    RecyclerView rvCrops;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_home);

        listCrops();
    }

    public void listCrops(){

        roomModelArrayList = new ArrayList<>();

        rvCrops= findViewById(R.id.rvRoomItems);
        rvCrops.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);

        rvCrops.setLayoutManager(linearLayoutManager);


        db= FirebaseFirestore.getInstance();

        db.collection("Room")
                .whereEqualTo("Available", true)
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
                                RoomsUsersAdapter cropsAdapter = new RoomsUsersAdapter(UsersHome.this, roomModelArrayList);
                                rvCrops.setAdapter(cropsAdapter);
                                Log.i(TAG, "Fetched");
                                cropsAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
    }
}