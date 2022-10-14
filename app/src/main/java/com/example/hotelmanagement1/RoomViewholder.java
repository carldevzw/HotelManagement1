package com.example.hotelmanagement1;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

;import com.google.android.material.switchmaterial.SwitchMaterial;

public class RoomViewholder extends RecyclerView.ViewHolder {

    public ImageView  ivRoom;
    public TextView tvPrice, tvNumber, tvAvailable;
    public SwitchMaterial switchStatus;
    public ImageButton btnEdit;

    public RoomViewholder(@NonNull View itemView) {
        super(itemView);
        switchStatus = itemView.findViewById(R.id.switchStatus);
        ivRoom = itemView.findViewById(R.id.ivRoomImage);
        tvNumber = itemView.findViewById(R.id.tvNumber);
        tvPrice = itemView.findViewById(R.id.tvPrice);
    }
}
