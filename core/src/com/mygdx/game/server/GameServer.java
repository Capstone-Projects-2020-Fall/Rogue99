package com.mygdx.game.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.Packets;

import java.io.IOException;

public class GameServer extends Server {

    int ServerPort = 5000;

    Server server;
    ServerNetworkListener serverNetworkListener;

    public GameServer() {
        server = new Server();
        serverNetworkListener = new ServerNetworkListener(server);
        server.addListener(serverNetworkListener);
        try {
            server.bind(ServerPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
        registerPackets();
        server.start();
    }

    private void registerPackets(){
        Kryo kryo = server.getKryo();

        //register packets
        kryo.register(Packets.Packet000ConnectionAnswer.class);
        kryo.register(Packets.Packet001Connection.class);
        kryo.register(Packets.Packet002Map.class);
        kryo.register(Packets.Packet003Movement.class);
        kryo.register(Packets.Packet004Potion.class);
        kryo.register(Packets.Packet005Stats.class);
    }

    public static void main(String[] args) {
        new GameServer();
    }
}
