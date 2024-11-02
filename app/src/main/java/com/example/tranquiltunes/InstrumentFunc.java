package com.example.tranquiltunes;

public class InstrumentFunc {
    private String instrumentname;
    private String instrumentdescription;

    public InstrumentFunc() {
        // Default constructor required for calls to DataSnapshot.getValue(InstrumentFunc.class)
    }

    public InstrumentFunc(String instrumentname, String instrumentdescription) {
        this.instrumentname = instrumentname;
        this.instrumentdescription = instrumentdescription;
    }

    public String getInstrumentname() {
        return instrumentname;
    }

    public String getInstrumentdescription() {
        return instrumentdescription;
    }
}
