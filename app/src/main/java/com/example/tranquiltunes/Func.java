package com.example.tranquiltunes;

public class Func {

    private String atmosname;
    private String atmosdescription;
    private String imageURL; // Add this field for the image URL

    // Default constructor required for calls to DataSnapshot.getValue(Func.class)
    public Func() {}

    // Optional constructor to initialize all fields
    public Func(String atmosname, String atmosdescription, String imageURL) {
        this.atmosname = atmosname;
        this.atmosdescription = atmosdescription;
        this.imageURL = imageURL; // Initialize imageURL
    }

    public String getAtmosname() {
        return atmosname;
    }

    public void setAtmosname(String atmosname) {
        this.atmosname = atmosname;
    }

    public String getAtmosdescription() {
        return atmosdescription;
    }

    public void setAtmosdescription(String atmosdescription) {
        this.atmosdescription = atmosdescription;
    }

    // Getter for imageURL
    public String getImageURL() {
        return imageURL;
    }

    // Setter for imageURL
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
