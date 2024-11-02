// GlobalData.java
package com.example.tranquiltunes;

public class GlobalData {

    // Step 1: Create private static instance variable
    private static GlobalData instance;

    // Variables to store the selected emotion and instrument
    private String selectedEmotion;
    private String selectedInstrument;

    // Step 2: Private constructor to prevent instantiation
    private GlobalData() {}

    // Step 3: Public method to get the single instance of the class
    public static synchronized GlobalData getInstance() {
        if (instance == null) {
            instance = new GlobalData();
        }
        return instance;
    }

    // Getter and setter for selectedEmotion
    public String getSelectedEmotion() {
        return selectedEmotion;
    }

    public void setSelectedEmotion(String selectedEmotion) {
        this.selectedEmotion = selectedEmotion;
    }

    // Getter and setter for selectedInstrument
    public String getSelectedInstrument() {
        return selectedInstrument;
    }

    public void setSelectedInstrument(String selectedInstrument) {
        this.selectedInstrument = selectedInstrument;
    }
}
