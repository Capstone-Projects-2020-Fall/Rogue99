package com.mygdx.game.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.Packets;

public class ServerNetworkListener  extends Listener {

    public ServerNetworkListener(){

    }

    @Override
    public void connected(Connection connection) {
        Packets.Packet001Connection a = new Packets.Packet001Connection();
        a.name = "This is the server";
        connection.sendTCP(a);
        super.connected(connection);
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
    }

    @Override
    public void received(Connection connection, Object object) {
        //TODO Deal with received Packtes after they are built
        if(object instanceof Packets.Packet001Connection){
            System.out.println(connection.toString() + ((Packets.Packet001Connection) object).name);
        }
        super.received(connection, object);
    }

}
