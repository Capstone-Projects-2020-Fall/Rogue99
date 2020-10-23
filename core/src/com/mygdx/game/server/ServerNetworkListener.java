package com.mygdx.game.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.Packets;

import java.util.ArrayList;
import java.util.HashMap;

public class ServerNetworkListener  extends Listener {

    Server server;

    public ServerNetworkListener(Server server){
        this.server = server;
    }

    @Override
    public void connected(Connection connection) {
    }

    @Override
    public void disconnected(Connection connection) {
    }

    @Override
    public void received(Connection connection, Object object) {
        if(object instanceof Packets.Packet001Connection){
            //TODO connection handling
            Packets.Packet000ConnectionAnswer answer = new Packets.Packet000ConnectionAnswer();
            connection.sendTCP(answer);
        } else {
            server.sendToAllExceptTCP(connection.getID(), object);
        }
    }

}
