package com.mygdx.game.server;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.Packets;
import com.mygdx.game.item.Item;
import com.mygdx.game.map.Level;

import java.util.HashMap;
import java.util.Random;

import java.util.ArrayList;

public class ServerNetworkListener  extends Listener {

    Server server;
    GameServer gameServer;
    Kryo kryo;
    ArrayList<Object> connectionsInfo;
    HashMap<Connection, Object> connectionInfoMap;
    String tempName;

    public ServerNetworkListener(Server server, GameServer gameServer, Kryo kryo){
        this.server = server;
        this.gameServer = gameServer;
        this.kryo = kryo;
        connectionsInfo = new ArrayList<>();
        connectionInfoMap = new HashMap<>();
    }

    @Override
    public void connected(Connection connection) {

    }

    @Override
    public void disconnected(Connection connection) {
        for(Connection c : connectionInfoMap.keySet()){
            if(connection.equals(c)){
                Packets.Packet009Disconnect disconnect = new Packets.Packet009Disconnect();
                disconnect.name =((Packets.Packet001Connection)connectionInfoMap.get(c)).name;
                server.sendToAllExceptTCP(connection.getID(), disconnect);
                connectionInfoMap.remove(c);
                return;
            }
        }
    }

    @Override
    public void received(Connection connection, Object object) {
        if(object instanceof Packets.Packet001Connection){
            //TODO connection handling
            Packets.Packet000ConnectionAnswer connectionAnswer = new Packets.Packet000ConnectionAnswer();
            connectionAnswer.answer = true;
            connection.sendTCP(connectionAnswer);
            server.sendToAllExceptTCP(connection.getID(), object);
            if(!connectionInfoMap.isEmpty()){
                for(Connection c : connectionInfoMap.keySet()){
                    connection.sendTCP(connectionInfoMap.get(c));
                }
            }
            connectionInfoMap.put(connection, object);
        }

        //server receives map request, sends seed
        else if(object instanceof Packets.Packet006RequestSeed){
            //if level seed is not in list, generate a new seed and add to list
            //then send seed at index=depth
            System.out.println("Gameserver seeds size: " + gameServer.seeds.size());
            System.out.println("Server: depth requested: " + ((Packets.Packet006RequestSeed) object).depth);
            if(((Packets.Packet006RequestSeed) object).depth == gameServer.seeds.size()){
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
                // do nothing? no other player is connected.
            } else {
                Random rand = new Random();
                int i = rand.nextInt(connectionList.length);
                while (connectionList[i].equals(connection) || !connectionList[i].isConnected()) {
                    i = rand.nextInt(connectionList.length);
                }
                connectionList[i].sendTCP(object);
                tempName = ((Packets.Packet004Potion) object).playerName;
            }
        } else if(object instanceof Packets.Packet007PlayerAffected){
            Packets.Packet008ServerMessage serverMessage = new Packets.Packet008ServerMessage();
            serverMessage.sentBy = tempName;
            serverMessage.receivedBy = ((Packets.Packet007PlayerAffected) object).playerName;
            server.sendToAllTCP(serverMessage);
        } else {
            server.sendToAllExceptTCP(connection.getID(), object);
        }
    }
}
