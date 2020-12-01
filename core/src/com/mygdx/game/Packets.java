package com.mygdx.game;

public class Packets {
    public static class Packet000ConnectionAnswer{public boolean answer;}
    public static class Packet001Connection{public String name; public int spriteColor;}
    public static class Packet002Map{public int depth; public String seed;}
    public static class Packet003Movement{public String name; public int xPos; public int yPos; public int depth;}
    public static class Packet004Potion{public int ID; public int value; public String playerName;}
    public static class Packet005Stats{public String name; public int armor; public int health;}
    //whenever a client generates a new level, it sends a request for the seed for a specified depth
    //server has list of seeds which it has stored, sends seed at index=depth
    public static class Packet006RequestSeed{public int depth;}
    public static class Packet007PlayerAffected{public String playerName;}
    public static class Packet008ServerMessage{public String sentBy; public String receivedBy; public String itemType;}
    public static class Packet009Scroll{public int ID; public String type; public String playerName;}
    public static class Packet010Disconnect {public String name;}
    public static class Packet011StartGame {public boolean start;}
    public static class Packet012Score {public String name; public int score;}
}
