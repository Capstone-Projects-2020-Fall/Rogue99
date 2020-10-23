package com.mygdx.game.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientNetworkListener extends Listener {
    private Client client;

    public void init(Client client){
        this.client = client;
    }

    public void connected(Connection c){
        System.out.println("CLIENT connected");
    }

    public void disconnected(Connection c){
        System.out.println("CLIENT disconnected");
    }

    public void received(Connection c, Object o){
        //TODO if(o instance of [PACKET CLASS])
        //same code as server received method
    }
}
