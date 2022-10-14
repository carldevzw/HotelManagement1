package com.example.hotelmanagement1;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

;

public class RoomUsersViewholder extends RecyclerView.ViewHolder {

    public ImageView  ivRoom;
    public TextView tvPrice, tvNumber, tvAvailable;
    public ImageButton btnEdit;

    public RoomUsersViewholder(@NonNull View itemView) {
        super(itemView);
        btnEdit = itemView.findViewById(R.id.btnReserve);
        ivRoom = itemView.findViewById(R.id.ivRoomImage);
        tvNumber = itemView.findViewById(R.id.tvNumber);
        tvPrice = itemView.findViewById(R.id.tvPrice);
    }
}
