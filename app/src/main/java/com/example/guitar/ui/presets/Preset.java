package com.example.guitar.ui.presets;

public class Preset {
    private String name, volume, delay, chorus, dis, wah, reverb;

    public Preset(String name, String volume, String delay, String chorus, String dis, String wah, String reverb) {
        this.name = name;
        this.volume = volume;
        this.delay = delay;
        this.chorus = chorus;
        this.dis = dis;
        this.wah = wah;
        this.reverb = reverb;
    }

    public String getName() {
        return name;
    }

    public String getVolume() {
        return volume;
    }

    public String getDelay() {
        return delay;
    }

    public String getChorus() {
        return chorus;
    }

    public String getDis() {
        return dis;
    }

    public String getWah() {
        return wah;
    }

    public String getReverb() {
        return reverb;
    }
}
