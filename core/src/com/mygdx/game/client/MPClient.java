package com.mygdx.game.client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

public class MPClient {
    int portSocket = 5000;
    String ipAddress = "100.34.155.72";

    public Client client;
    private ClientNetworkListener cnl;

    public MPClient(){
        client = new Client();
        cnl = new ClientNetworkListener();

        cnl.init(client);

        registerPackets();

        client.addListener(cnl);

        new Thread(client).start();

        try{
            client.connect(5000, ipAddress, portSocket);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void registerPackets(){
        Kryo kryo = client.getKryo();
        kryo.register(Packets.Packet001Connection.class);
    }

    public static void main(String[] args) {
        new MPClient();
    }
}
