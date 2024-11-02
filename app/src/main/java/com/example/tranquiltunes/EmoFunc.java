package com.example.tranquiltunes;


public class EmoFunc {
    private String emoname;
    private String emodescription;
    private String imageURL;

    public EmoFunc() {
        // Default constructor required for calls to DataSnapshot.getValue(EmoFunc.class)
    }

    public EmoFunc(String emoname, String emodescription, String imageURL) {
        this.emoname = emoname;
        this.emodescription = emodescription;
        this.imageURL = imageURL;
    }

    public String getEmoname() {
        return emoname;
    }

    public String getEmodescription() {
        return emodescription;
    }

    public String getImageURL() {
        return imageURL;
    }
}
