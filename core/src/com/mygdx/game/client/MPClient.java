package com.mygdx.game.client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.mygdx.game.Packets;
import com.mygdx.game.Rogue99;

public class MPClient {

    private Rogue99 game;

    int portSocket = 5000;
    String ipAddress = "hfarrash.ddns.net";

    public Client client;
    private ClientNetworkListener cnl;

    public MPClient(Rogue99 game){
        this.game = game;

        client = new Client();
        cnl = new ClientNetworkListener();

        cnl.init(client, this.game);

        registerPackets();

        client.addListener(cnl);

        new Thread(client).start();

        try{
            client.connect(5000, ipAddress, portSocket);
        } catch(Exception e){
            e.printStackTrace();
            game.connectionRejected("Unable to Connect To Server!");
        }
    }

    private void registerPackets(){
        Kryo kryo = client.getKryo();
        kryo.register(Packets.Packet000ConnectionAnswer.class);
        kryo.register(Packets.Packet001Connection.class);
        kryo.register(Packets.Packet002Map.class);
        kryo.register(Packets.Packet003Movement.class);
        kryo.register(Packets.Packet004Potion.class);
        kryo.register(Packets.Packet005Stats.class);
        kryo.register(Packets.Packet006RequestSeed.class);
        kryo.register(Packets.Packet007PlayerAffected.class);
        kryo.register(Packets.Packet008ServerMessage.class);
        kryo.register(Packets.Packet009Scroll.class);
        kryo.register(Packets.Packet010Disconnect.class);
        kryo.register(Packets.Packet011StartGame.class);
    }

    public Rogue99 getGame(){
        return this.game;
    }

//    public static void main(String[] args) {
//        new MPClient();
//    }
}
