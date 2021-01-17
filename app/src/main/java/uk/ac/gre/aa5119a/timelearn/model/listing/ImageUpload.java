package uk.ac.gre.aa5119a.timelearn.model.listing;

import android.widget.ImageView;

public class ImageUpload {

    private String imageUrl;

    public ImageUpload(){
        // empty constructor needed for firebase
    }


    public ImageUpload( String imageUrl) {
        this.imageUrl = imageUrl;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
