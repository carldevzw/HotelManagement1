package com.example.hotelmanagement1;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class Reservations extends AppCompatActivity {

    FirebaseFirestore db;
    StorageReference storageReference;

    TextInputLayout txtFullNme, txtPeriodOfStay, txtPeriodOfStay2, txtPaymentMethod;
    Button btnAdd;
    ProgressDialog progressDialog;

    DatePickerDialog.OnDateSetListener setListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservations);

        btnAdd= findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postSale();
            }
        });

    }

    private void postSale() {

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        TextView chkInDate, chkOutDate;
        EditText checkInDate, checkOutDate;



        // Choose time zone in which you want to interpret your Date
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Central Africa"));
        int day = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(date);
        String todaysDate = dateFormat.format(date);

        progressDialog= new ProgressDialog(Reservations.this);
        progressDialog.setTitle("Making reservation...");
        progressDialog.show();

        txtFullNme= (TextInputLayout) findViewById(R.id.txtName);
        txtPeriodOfStay= (TextInputLayout) findViewById(R.id.txtPrice);
        txtPeriodOfStay2= (TextInputLayout) findViewById(R.id.txtPrice2);
        txtPaymentMethod= (TextInputLayout) findViewById(R.id.txtLocation);

        db= FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();


        String name= txtFullNme.getEditText().getText().toString().trim();
        String price= txtPeriodOfStay.getEditText().getText().toString().trim();
        String price2= txtPeriodOfStay2.getEditText().getText().toString().trim();
        String location= txtPaymentMethod.getEditText().getText().toString().trim();

        Intent intent = getIntent();
        String ID = intent.getStringExtra("documentID");

        Map<String, Object> reservation = new HashMap<>();
        reservation.put("Full Name", name);
        reservation.put("Check-in Date", price);
        reservation.put("Check-out Date", price2);
        reservation.put("Payment Method", location);
        reservation.put("Available", true);
        reservation.put("Category", location);
        reservation.put("Date", todaysDate);
        reservation.put("Room", ID);

        // Add a new document with a generated ID
        db.collection("Reservations").document(name + " " )
                .set(reservation)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                            txtFullNme.getEditText().setText(null);
                            txtPeriodOfStay.getEditText().setText(null);
                            txtPaymentMethod.getEditText().setText(null);
                            Toast.makeText(Reservations.this, "Room reserved", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if(progressDialog.isShowing()){
                            progressDialog.dismiss();
                            Toast.makeText(Reservations.this, "Error reserving room, SEE LOGS" + e, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        db.collection("Room").document(ID).update("Available", false);
    }
}
