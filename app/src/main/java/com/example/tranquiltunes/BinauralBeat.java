package com.example.tranquiltunes;

public class BinauralBeat {
    private final String name;
    private final String description;
    private final int frequency;

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
