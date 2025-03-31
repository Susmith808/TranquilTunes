package com.example.tranquiltunes;

public class BinauralBeat {
    private String name;
    private String description;
    private int frequency;

    public BinauralBeat(String name, String description, int frequency) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getFrequency() {
        return frequency;
    }
}
