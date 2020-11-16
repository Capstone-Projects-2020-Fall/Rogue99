package com.mygdx.game.server;

import com.badlogic.gdx.graphics.Color;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.mygdx.game.Packets;
import com.mygdx.game.map.Level;

import java.util.*;

public class ServerNetworkListener  extends Listener {

    Server server;
    GameServer gameServer;
    Kryo kryo;
    HashMap<Connection, Object> connectionInfoMap;
    String tempName;
    boolean gameStarted;
    Random rand;
    TableGenerator tableGenerator;
    List<String> headersList;
    List<List<String>> rowList;

    public ServerNetworkListener(Server server, GameServer gameServer, Kryo kryo){
        this.server = server;
        this.gameServer = gameServer;
        this.kryo = kryo;
        connectionInfoMap = new HashMap<>();
        gameStarted = false;
        rand = new Random();
        tableGenerator = new TableGenerator();
        headersList = new ArrayList<>();
        rowList = new ArrayList<>();
        headersList.add("Player Name");
        headersList.add("XPos");
        headersList.add("YPos");
        headersList.add("Health");
        headersList.add("Armor");
    }

    @Override
    public void connected(Connection connection) {

    }

    @Override
    public void disconnected(Connection connection) {
        Set<Connection> connections = connectionInfoMap.keySet();
        ArrayList<Connection> keys = new ArrayList<>();
        for(Connection c : connections){
            if(connection.equals(c)){
                keys.add(c);
                Packets.Packet010Disconnect disconnect = new Packets.Packet010Disconnect();
                disconnect.name =((Packets.Packet001Connection)connectionInfoMap.get(c)).name;
                server.sendToAllExceptTCP(connection.getID(), disconnect);
                removeTableRow(disconnect.name);
            }
        }
        for(int i = 0; i<keys.size();i++){
            connectionInfoMap.remove(keys.get(i));
        }
        if(connectionInfoMap.size() < 1){
            connectionInfoMap.clear();
            gameStarted = false;
            gameServer.seeds.clear();
        }
    }

    @Override
    public void received(Connection connection, Object object) {
        if(object instanceof Packets.Packet001Connection){
            Packets.Packet000ConnectionAnswer connectionAnswer = new Packets.Packet000ConnectionAnswer();
            if(gameStarted){
                connectionAnswer.answer = false;
            } else {
                connectionAnswer.answer = true;
                Packets.Packet001Connection connectionPacket = new Packets.Packet001Connection();
                connectionPacket.name = ((Packets.Packet001Connection) object).name;
                connectionPacket.spriteColor = Color.argb8888(256, rand.nextInt(256),rand.nextInt(256), rand.nextInt(256));
                server.sendToAllExceptTCP(connection.getID(), connectionPacket);
                if (!connectionInfoMap.isEmpty()) {
                    for (Connection c : connectionInfoMap.keySet()) {
                        connection.sendTCP(connectionInfoMap.get(c));
                    }
                }
                connectionInfoMap.put(connection, connectionPacket);
                //System.out.println("Player " + ((Packets.Packet001Connection) object).name + " Has Connected!");
                List<String> row = new ArrayList<>();
                row.add(((Packets.Packet001Connection) object).name);
                row.add("0");
                row.add("0");
                row.add("100");
                row.add("0");
                rowList.add(row);
                System.out.println(tableGenerator.generateTable(headersList, rowList));
                System.out.println("Number Of Players Connected: " + connectionInfoMap.size());
            }
            connection.sendTCP(connectionAnswer);
        }

        //server receives map request, sends seed
        else if(object instanceof Packets.Packet006RequestSeed){
            //if level seed is not in list, generate a new seed and add to list
            //then send seed at index=depth
//            System.out.println("Gameserver seeds size: " + gameServer.seeds.size());
            System.out.println("Server: depth requested: " + ((Packets.Packet006RequestSeed) object).depth);
            if(((Packets.Packet006RequestSeed) object).depth == gameServer.seeds.size()){
                Level level = new Level(null, ((Packets.Packet006RequestSeed) object).depth, null);
                level.generateFloorPlan();

                //System.out.println("Seed added to server seed list");
                gameServer.seeds.add(level.getSeed());
            }
            Packets.Packet002Map mapAnswer = new Packets.Packet002Map();
            mapAnswer.seed = gameServer.seeds.get(((Packets.Packet006RequestSeed) object).depth);
            mapAnswer.depth = ((Packets.Packet006RequestSeed) object).depth;
            connection.sendTCP(mapAnswer);
        } else if(object instanceof Packets.Packet003Movement){
            server.sendToAllExceptTCP(connection.getID(), object);
            updateTableRow(((Packets.Packet003Movement) object).name, String.valueOf(((Packets.Packet003Movement) object).xPos), String.valueOf(((Packets.Packet003Movement) object).yPos), null, null);
        }
        else if(object instanceof Packets.Packet004Potion || object instanceof Packets.Packet009Scroll){
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
            }
        } else if(object instanceof Packets.Packet008ServerMessage){
            System.out.println("Player " + ((Packets.Packet008ServerMessage) object).receivedBy + " Game Has Been Affected by Player " + ((Packets.Packet008ServerMessage) object).sentBy);
            server.sendToAllTCP(object);
        } else if(object instanceof Packets.Packet011StartGame){
            gameStarted = true;
            server.sendToAllTCP(object);
        } else if(object instanceof Packets.Packet005Stats){
            server.sendToAllExceptTCP(connection.getID(), object);
            updateTableRow(((Packets.Packet005Stats) object).name, null, null, String.valueOf(((Packets.Packet005Stats) object).health), String.valueOf(((Packets.Packet005Stats) object).armor));
            //System.out.println("Player " + ((Packets.Packet005Stats) object).name + " Has new Stats: " + ((Packets.Packet005Stats) object).health + "HP " + ((Packets.Packet005Stats) object).armor + "AR");
        } else {
            server.sendToAllExceptTCP(connection.getID(), object);
        }
    }

    private void updateTableRow(String name, String x, String y, String hp, String armor){
        for(List list : rowList){
            if(list.get(0).equals(name)){
                if(x != null){
                    list.set(1,x);
                }
                if(y !=null){
                    list.set(2,y);
                }
                if(hp !=null){
                    list.set(3,hp);
                }
                if(armor !=null){
                    list.set(4,armor);
                }
            }
        }
        System.out.println(tableGenerator.generateTable(headersList,rowList));
        System.out.println("Number Of Players Connected: " + connectionInfoMap.size());
    }
    private void removeTableRow(String name){
        for(List list : rowList){
            if(list.get(0).equals(name)){
                rowList.remove(list);
                return;
            }
        }
    }
}
