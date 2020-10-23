package com.mygdx.game;

public class Packets {
    public static class Packet000ConnectionAnswer{public boolean answer;}
    public static class Packet001Connection{public String name;}
    public static class Packet002Map{public String seed;}
    public static class Packet003Movement{public String name; public int xPos; public int yPos;}
    public static class Packet004Potion{public int ID;}
    public static class Packet005Stats{public String name; public int armor; public int health;}
}
