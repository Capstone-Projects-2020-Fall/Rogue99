package com.mygdx.game.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.Packets;
import com.mygdx.game.Rogue99;
import com.mygdx.game.interactable.Enemy;
import com.mygdx.game.item.Item;
import com.mygdx.game.map.Level;
import com.mygdx.game.map.Tile;

import java.nio.ByteBuffer;
import java.util.Random;

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
            String servermsg = ((Packets.Packet001Connection) o).name;
            System.out.println(servermsg);
        } else if(o instanceof Packets.Packet002Map){
            System.out.println("SEED: " + ((Packets.Packet002Map) o).seed);
            //receives seed, sets seed of level at specified depth, generates level
            //game.generateLevel(((Packets.Packet002Map) o).seed, ((Packets.Packet002Map) o).depth);
            game.setSeed(((Packets.Packet002Map) o).seed, ((Packets.Packet002Map) o).depth);
        } else if(o instanceof Packets.Packet003Movement){
            //TODO receives player name and position, updates map
        } else if(o instanceof Packets.Packet004Potion){
            if(((Packets.Packet004Potion) o).ID == Item.DAMAGEPOTION) {
                if (game.getHero().getCurrHP() - ((Packets.Packet004Potion) o).value > 0) {
                    game.getHero().setCurrHP(game.getHero().getCurrHP() - ((Packets.Packet004Potion) o).value);
                } else {
                    game.getHero().setCurrHP(0);
                }
            }
            else if(((Packets.Packet004Potion) o).ID == Item.SUMMONSCROLL) {
                int x,y;
                boolean summoned = false;
                Random rand = new Random();
                do {
                    x = rand.nextInt() % game.level.getWidth();
                    y = rand.nextInt() % game.level.getHeight();
                    if ( game.level.getMap()[x][y].getType() == "floor" && game.level.getMap()[x][y].getEntities().isEmpty() ) {
                        Enemy enemy = new Enemy( ((Packets.Packet004Potion) o).ID, "wasp", game.level.getMap()[x][y], game);
                        //System.out.println("ENEMY GENERATED: " + enemy.getSprite());
                        game.level.enemies.add(enemy);
                        game.level.getMap()[x][y].getEntities().push(enemy);
                        summoned = true;
                    }
                } while ( !summoned );
            }
        } else if(o instanceof Packets.Packet005Stats){
            //TODO receives stats of other player
        }
    }
}
