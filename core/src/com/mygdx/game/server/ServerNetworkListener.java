package com.mygdx.game.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.Packets;
import com.mygdx.game.map.Level;

import java.util.Random;

import java.util.ArrayList;

public class ServerNetworkListener  extends Listener {

    Server server;
    GameServer gameServer;
    Kryo kryo;
    ArrayList<Object> connectionsInfo;

    public ServerNetworkListener(Server server, GameServer gameServer, Kryo kryo){
        this.server = server;
        this.gameServer = gameServer;
        this.kryo = kryo;
        connectionsInfo = new ArrayList<>();
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
            Packets.Packet000ConnectionAnswer connectionAnswer = new Packets.Packet000ConnectionAnswer();
            connectionAnswer.answer = true;
            connection.sendTCP(connectionAnswer);
            server.sendToAllExceptTCP(connection.getID(), object);
            if(connectionsInfo.size() > 0){
                for(Object o : connectionsInfo){
                    connection.sendTCP(o);
                }
            }
            connectionsInfo.add(object);
        }

        //server receives map request, sends seed
        else if(object instanceof Packets.Packet006RequestSeed){
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
        } else if(object instanceof Packets.Packet003Movement){
            server.sendToAllExceptTCP(connection.getID(), object);
        }
        else if(object instanceof Packets.Packet004Potion){
            Connection[] connectionList = server.getConnections();
            if (connectionList.length < 2) {
                connection.sendTCP(object);
            }

            Random rand = new Random();
            int i = rand.nextInt() % connectionList.length;
            while (connectionList[i] == connection && connectionList.length < 2) {
                i = rand.nextInt() % connectionList.length;
            }
            connectionList[i].sendTCP(object);
        }
    }
}
