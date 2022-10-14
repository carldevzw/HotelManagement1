package com.example.hotelmanagement1;

import com.google.firebase.firestore.DocumentId;

public class RoomModel {
    private String Number, Price, Category, ImageSrc, ID;
    boolean Available;
    @DocumentId
    private String documentId;

    public RoomModel(String number, String price, boolean available, String imageSrc, String category, String ID) {
        Number = number;
        Price = price;
        ImageSrc = imageSrc;
        Category = category;
        this.ID= ID;
    }

    public RoomModel() {
    }
    public String getID() {
        return ID;
    }
    public String getNumber() {
        return Number;
    }
    public String getCategory() {
        return Category;
    }
    public boolean isAvailable() {
        return Available;
    }
    public String getDocumentId() {
        return documentId;
    }
    public String getPrice() {
        return Price;
    }
    public String getImageSrc() {
        return ImageSrc;
    }
}
