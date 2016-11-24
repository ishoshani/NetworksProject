import java.net.*;
import java.io.*;

public class Room{
  String[] players;
  Integer[] playerID;
  Connector[] connections;
  ChatPacket nextMessage;
  int turnSwitch =0;
  Integer turn = 0;
  Integer state;
  Game game;
  final static int WAITING = 0;
  final static int PLAYING = 1;
  final static int DONE = 2;


  public Room(){
    players = new String[2];
    playerID = new Integer[2];
    connections = new Connector[2];
    game = new TicTacToe(this);
    nextMessage = null;
    state = WAITING;
  }
  public String welcomeMessage(){
    String s = game.welcomeMessage();
    return s;
  }
  public String SendCommand(Integer id, String Message) throws InvalidMoveException{
    if(turn == playerID[0]){
      if(id != playerID[0]){
        return "please wait your turn";
      }
      ChatPacket out = new ChatPacket("yourTurn", game.move(Message));

        nextMessage = out;
        if(state != DONE){
        turnSwitch = 1;
        turn = playerID[turnSwitch];
        }
      return "";
    }
    if(turn == playerID[1]){
      if(id != playerID[1]){
        return "please wait your turn";
      }
      ChatPacket out = new ChatPacket("yourTurn", game.move(Message));
        nextMessage = out;
      if(state != DONE){
        turnSwitch=0;
      turn = playerID[turnSwitch];
      }
      return "";
    }
    return "wut";

  }
  public String finish(){
    String s = game.finish();
    return s;
  }
  public boolean AddPlayer(String username, Integer id, Connector connector){
    if(playerID[0]==null){
      players[0] = username;
      playerID[0] = id;
      connections[0] = connector;
      return true;
    }
    if(playerID[1]==null){
      players[1] = username;
      playerID[1] = id;
      connections[1] = connector;
      state = PLAYING;
      turnSwitch=0;
      turn = playerID[turnSwitch];
      return true;
    }
    System.err.println("someone tried to connect to a full lobby");
    return false;
  }

  public ChatPacket getNextMessage(){
    ChatPacket n;
      n =  nextMessage;
      if(n == null){
        n = new ChatPacket("otherTurn","-");
      }else{
        nextMessage=null;
      }
    return n;
  }




}
