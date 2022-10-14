package com.example.hotelmanagement1;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

public class AddRoom extends AppCompatActivity {

    TextInputLayout txtNumber, txtLocation, txtPrice;
    Button btnSelectImage, btnAdd;
    ProgressDialog progressDialog;

    private ActivityResultLauncher<String> selectImage;
    public Uri ImageUri;

    FirebaseFirestore db;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_room);

        selectImage= registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                new ActivityResultCallback<Uri>() {
                    @Override
                    public void onActivityResult(Uri result) {
                        ImageUri= result;
                        Toast.makeText(AddRoom.this, "Image selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        btnSelectImage= findViewById(R.id.btnUploadImage);
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage.launch("image/*");
            }
        });

        btnAdd= findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postSale(ImageUri);
            }
        });

    }
    private void postSale(Uri imageUri) {

        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        // Choose time zone in which you want to interpret your Date
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("Central Africa"));
        int day = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTime(date);
        String todaysDate = dateFormat.format(date);

        progressDialog= new ProgressDialog(AddRoom.this);
        progressDialog.setTitle("Adding room...");
        progressDialog.show();

        txtNumber= (TextInputLayout) findViewById(R.id.txtName);
        txtPrice= (TextInputLayout) findViewById(R.id.txtPrice);
        txtLocation= (TextInputLayout) findViewById(R.id.txtLocation);

        db= FirebaseFirestore.getInstance();
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String userID= firebaseUser.getUid();

        storageReference= FirebaseStorage.getInstance().getReference("images/"+txtNumber.getEditText().getText().toString().trim());

        storageReference.putFile(ImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if(task.isSuccessful()){
                                    String imageURL= task.getResult().toString();
                                    String name= txtNumber.getEditText().getText().toString().trim();
                                    String price= txtPrice.getEditText().getText().toString().trim();
                                    String location= txtLocation.getEditText().getText().toString().trim();


                                    Map<String, Object> room = new HashMap<>();
                                    room.put("Number", name);
                                    room.put("Price", price);
                                    room.put("ImageSrc", imageURL);
                                    room.put("Available", true);
                                    room.put("Category", location);
                                    room.put("Date", todaysDate);
                                    room.put("ID", userID);

                                    // Add a new document with a generated ID
                                    db.collection("Room").document(name + " " )
                                            .set(room)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    if (progressDialog.isShowing()) {
                                                        progressDialog.dismiss();
                                                        txtNumber.getEditText().setText(null);
                                                        txtLocation.getEditText().setText(null);
                                                        txtPrice.getEditText().setText(null);
                                                        Toast.makeText(AddRoom.this, "Room added", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    if(progressDialog.isShowing()){
                                                        progressDialog.dismiss();
                                                        Toast.makeText(AddRoom.this, "Error adding room, SEE LOGS" + e, Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }else{
                                    Toast.makeText(AddRoom.this, "Failed to collect URL", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddRoom.this, "Picture upload failed", Toast.LENGTH_LONG).show();
                    }
                });
    }
}