package com.mygdx.game.map;

public class GenerationSettings {
    public double probability;        //0.47
    public int deathLimit;
    public int birthLimit;
    public int iterations;

    public GenerationSettings(double probability, int deathLimit, int birthLimit, int iterations){
        this.probability = probability;
        this.deathLimit = deathLimit;
        this.birthLimit = birthLimit;
        this.iterations = iterations;
    }
}
