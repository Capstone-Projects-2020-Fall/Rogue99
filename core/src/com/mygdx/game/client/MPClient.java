package com.mygdx.game.client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;

public class MPClient {
    int portSocket; //TODO set equal to port #
    String ipAddress; //TODO set equal to ip address

    public Client client;
    private ClientNetworkListener cnl;

    public MPClient(){
        client = new Client();
        cnl = new ClientNetworkListener();

        client.addListener(cnl);

        client.start();

        try{
            client.connect(5000, ipAddress, portSocket);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void registerPackets(){
        Kryo kryo = client.getKryo();
        //kryo.register();  TODO pass in packet class
    }
}
