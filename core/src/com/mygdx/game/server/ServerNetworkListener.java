package com.mygdx.game.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.Packets;
import com.mygdx.game.map.Level;

public class ServerNetworkListener  extends Listener {

    Server server;
    GameServer gameServer;
    Kryo kryo;

    public ServerNetworkListener(Server server, GameServer gameServer, Kryo kryo){
        this.server = server;
        this.gameServer = gameServer;
        this.kryo = kryo;
    }

    @Override
    public void connected(Connection connection) {
//        Packets.Packet002Map mapPacket = new Packets.Packet002Map();
//        mapPacket.seed = gameServer.seed;
//        connection.sendTCP(mapPacket);
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

        //server receives map request, sends seed
        if(object instanceof Packets.Packet006RequestSeed){
            //if level seed is not in list, generate a new seed and add to list
            //then send seed at index=depth
            System.out.println("Gameserver seeds size: " + gameServer.seeds.size());
            System.out.println("Server: depth requested: " + ((Packets.Packet006RequestSeed) object).depth);
            if(((Packets.Packet006RequestSeed) object).depth == gameServer.seeds.size()){
                //TODO call generateFloorplan to get working seed
                Level level = new Level(null, ((Packets.Packet006RequestSeed) object).depth, null);
                level.generateFloorPlan();

                System.out.println("Seed added to server seed list");
                gameServer.seeds.add(level.getSeed());
            }
            Packets.Packet002Map mapAnswer = new Packets.Packet002Map();
            mapAnswer.seed = gameServer.seeds.get(((Packets.Packet006RequestSeed) object).depth);
            mapAnswer.depth = ((Packets.Packet006RequestSeed) object).depth;
            connection.sendTCP(mapAnswer);
        }
    }
}
