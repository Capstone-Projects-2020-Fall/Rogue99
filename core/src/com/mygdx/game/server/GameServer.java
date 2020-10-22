package com.mygdx.game.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;

public class GameServer extends Server {

    int ServerPort = 3000;

    Server server;
    ServerNetworkListener serverNetworkListener;

    public GameServer() {
        server = new Server();
        serverNetworkListener = new ServerNetworkListener();
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
        kryo.register(Packets.Packet01Map.class);
        //TODO Register Packets After they are built

    }

    public static void main(String[] args) {
        new GameServer();
    }
}
