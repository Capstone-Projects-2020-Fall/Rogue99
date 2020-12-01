package com.mygdx.game.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.Packets;

import java.io.IOException;
import java.util.ArrayList;

public class GameServer extends Server {

    int ServerPort = 5000;

    public Server server;
    ServerNetworkListener serverNetworkListener;

    public ArrayList<String> seeds;
    Kryo kryo;

    public GameServer() {
        server = new Server();
        kryo = server.getKryo();

        serverNetworkListener = new ServerNetworkListener(server, this, kryo);
        registerPackets();
        server.addListener(serverNetworkListener);

        seeds = new ArrayList<>();


        try {
            server.bind(ServerPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.start();
    }

    private void registerPackets(){

        //register packets
        kryo.register(Packets.Packet000ConnectionAnswer.class);
        kryo.register(Packets.Packet001Connection.class);
        kryo.register(Packets.Packet002Map.class);
        kryo.register(Packets.Packet003Movement.class);
        kryo.register(Packets.Packet004Potion.class);
        kryo.register(Packets.Packet005Stats.class);
        kryo.register(Packets.Packet006RequestSeed.class);
        kryo.register(Packets.Packet007PlayerAffected.class);
        kryo.register(Packets.Packet008ServerMessage.class);
        kryo.register(Packets.Packet009Scroll.class);
        kryo.register(Packets.Packet010Disconnect.class);
        kryo.register(Packets.Packet011StartGame.class);
        kryo.register(Packets.Packet012Score.class);
    }

    public static void main(String[] args) {
        new GameServer();
    }
}
