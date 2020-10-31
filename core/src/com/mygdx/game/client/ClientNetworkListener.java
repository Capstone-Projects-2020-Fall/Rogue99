package com.mygdx.game.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.Packets;
import com.mygdx.game.Rogue99;
import com.mygdx.game.interactable.Hero;

public class ClientNetworkListener extends Listener {
    private Client client;
    private Rogue99 game;

    public void init(Client client, Rogue99 game){
        this.client = client;
        this.game = game;
    }

    public void connected(Connection c){
        Packets.Packet001Connection a = new Packets.Packet001Connection();
        a.name = "test client";
        c.sendTCP(a);
    }

    public void disconnected(Connection c){
        System.out.println("CLIENT disconnected");
    }

    public void received(Connection c, Object o){
        //System.out.println("RECEIVED");
        if(o instanceof Packets.Packet000ConnectionAnswer){
            //TODO if o is false, return client to main menu and show message, close connection
        } else if(o instanceof Packets.Packet001Connection){
            if(((Packets.Packet001Connection) o).name.equals(game.hero.getName())){
                // do not add yourself to the players list.
            } else {
                Hero player = new Hero(game, "players");
                player.depth = 0;
                player.setName(((Packets.Packet001Connection) o).name);
                game.addPlayer(player);
            }
        } else if(o instanceof Packets.Packet002Map){
            System.out.println("SEED: " + ((Packets.Packet002Map) o).seed);
            //receives seed, sets seed of level at specified depth, generates level
            game.setSeed(((Packets.Packet002Map) o).seed, ((Packets.Packet002Map) o).depth);
        } else if(o instanceof Packets.Packet003Movement){
            for(Hero player : game.players){
                System.out.println("player in list: " + player.getName() + " received name: " + ((Packets.Packet003Movement) o).name);
                if(player.getName().equals(((Packets.Packet003Movement) o).name)){
                    player.setPosX(((Packets.Packet003Movement) o).xPos);
                    player.setPosY(((Packets.Packet003Movement) o).yPos);
                    player.depth = ((Packets.Packet003Movement) o).depth;
                    System.out.println("player in list depth: " + player.depth + " received depth: " + ((Packets.Packet003Movement) o).depth);
                }
            }
        } else if(o instanceof Packets.Packet004Potion){
            if(game.getHero().getCurrHP() - ((Packets.Packet004Potion) o).value > 0) {
                game.getHero().setCurrHP(game.getHero().getCurrHP() - ((Packets.Packet004Potion) o).value);
            }
            else {
                game.getHero().setCurrHP(0);
            }
        } else if(o instanceof Packets.Packet005Stats){
            for(Hero player : game.players){
                if(player.getName() == ((Packets.Packet005Stats) o).name){
                    player.setCurrHP(((Packets.Packet005Stats) o).health);
                    player.setArmor(((Packets.Packet005Stats) o).armor);
                }
            }
        }
    }
}
