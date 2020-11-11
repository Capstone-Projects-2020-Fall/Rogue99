package com.mygdx.game.client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.mygdx.game.Packets;
import com.mygdx.game.Rogue99;
import com.mygdx.game.interactable.*;
import com.mygdx.game.item.Item;

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
        a.name = game.hero.getName();
        c.sendTCP(a);
    }

    public void disconnected(Connection c){
        System.out.println("CLIENT disconnected");
    }

    public void received(Connection c, Object o){
        //System.out.println("RECEIVED");
        if(o instanceof Packets.Packet000ConnectionAnswer){
            if(((Packets.Packet000ConnectionAnswer) o).answer == false){
                game.connectionRejected("Game in Progress!");
                client.close();
            } else {
                game.connectionAccepted();
            }
        } else if(o instanceof Packets.Packet001Connection){
            if(((Packets.Packet001Connection) o).name.equals(game.hero.getName())){
                // do not add yourself to the players list.
            } else {
                Hero player = new Hero(game, "players");
                player.depth = 0;
                player.setName(((Packets.Packet001Connection) o).name);
                player.setSpriteColor(((Packets.Packet001Connection) o).spriteColor);
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
            if(((Packets.Packet004Potion) o).ID == Item.DAMAGEPOTION) {
                game.getHero().takeDamage( ((Packets.Packet004Potion) o).value );
            } else if(((Packets.Packet004Potion) o).ID == Item.FREEZEPOTION) {
                game.getHero().freezeTime( ((Packets.Packet004Potion) o).value );
            }
        } else if(o instanceof Packets.Packet005Stats){
            for(Hero player : game.players){
                if(player.getName() == ((Packets.Packet005Stats) o).name){
                    player.setCurrHP(((Packets.Packet005Stats) o).health);
                    player.setArmor(((Packets.Packet005Stats) o).armor);
                }
            }
        } else if (o instanceof Packets.Packet008ServerMessage){
            game.popUpWindow(((Packets.Packet008ServerMessage) o).sentBy, ((Packets.Packet008ServerMessage) o).receivedBy);
        } else if(o instanceof Packets.Packet009Scroll){
            Packets.Packet007PlayerAffected playerAffected = new Packets.Packet007PlayerAffected();
            playerAffected.playerName = game.hero.getName();
            c.sendTCP(playerAffected);
            int x,y;
            boolean summoned = false;
            Random rand = new Random();
            //find open tile within radius around hero
            do {
                x = rand.nextInt(5);
                y = rand.nextInt(5);
                if ( game.level.getMap()[game.hero.getPosX() + x][game.hero.getPosY() + y].getType() == "floor" &&
                        game.level.getMap()[game.hero.getPosX() +x][game.hero.getPosY() + y].getEntities().isEmpty() ) {
                    //Enemy enemy = new Enemy( game.hero.depth, "wasp", game.level.getMap()[x][y], game);
                    //System.out.println("ENEMY GENERATED: " + enemy.getSprite());
                    summoned = true;
                }
            } while ( !summoned );
            //initialize appropriate enemy
            Enemy enemy = new Enemy();
            if(((Packets.Packet009Scroll) o).type.equals("rat")){
                enemy = new Rat(game.level.getMap()[game.hero.getPosX() + x][game.hero.getPosY() + y], game);
            } else if(((Packets.Packet009Scroll) o).type.equals("wasp")){
                enemy = new Wasp(game.level.getMap()[game.hero.getPosX() + x][game.hero.getPosY() + y], game);
            } else if(((Packets.Packet009Scroll) o).type.equals("slime")){
                enemy = new Slime(game.level.getMap()[game.hero.getPosX() + x][game.hero.getPosY() + y], game);
            } else if(((Packets.Packet009Scroll) o).type.equals("ghost")){
                enemy = new Ghost(game.level.getMap()[game.hero.getPosX() + x][game.hero.getPosY() + y], game);
            } else if(((Packets.Packet009Scroll) o).type.equals("zombie")){
                enemy = new Zombie(game.level.getMap()[game.hero.getPosX() + x][game.hero.getPosY() + y], game);
            }
            game.level.enemies.add(enemy);
            game.level.getMap()[game.hero.getPosX() + x][game.hero.getPosY() + y].getEntities().push(enemy);
        } else if(o instanceof Packets.Packet005Stats){
            for(Hero player : game.players){
                if(player.getName() == ((Packets.Packet005Stats) o).name){
                    player.setCurrHP(((Packets.Packet005Stats) o).health);
                    player.setArmor(((Packets.Packet005Stats) o).armor);
                }
            }
        } else if (o instanceof Packets.Packet008ServerMessage){
            game.popUpWindow(((Packets.Packet008ServerMessage) o).sentBy, ((Packets.Packet008ServerMessage) o).receivedBy);
        } else if (o instanceof Packets.Packet009Disconnect){
            game.removePLayer(((Packets.Packet009Disconnect) o).name);
        } else if (o instanceof Packets.Packet010StartGame){
            System.out.println("game started: " + ((Packets.Packet010StartGame) o).start);
            game.setGameStarted(((Packets.Packet010StartGame) o).start);
        }
    }
}
