package com.mygdx.game.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ServerNetworkListener  extends Listener {

    public ServerNetworkListener(){

    }

    @Override
    public void connected(Connection connection) {
        super.connected(connection);
    }

    @Override
    public void disconnected(Connection connection) {
        super.disconnected(connection);
    }

    @Override
    public void received(Connection connection, Object object) {
        //TODO Deal with received Packtes after they are built
        if(object instanceof Packets.Packet01Map) {
            Packets.Packet01Map packet = (Packets.Packet01Map) object;
            packet.level.generate();
        }

        super.received(connection, object);
    }

}
