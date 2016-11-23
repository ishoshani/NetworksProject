import java.net.*;
import java.io.*;

public class Room{
  String[] players;
  Integer[] playerID;
  Connector[] connections;
  String nextMessage;
  Integer turn = 0;
  Integer state;
  final static int WAITING = 0;
  final static int PLAYING = 1;
  final static int DONE = 2;

  public Room(){
    players = new String[2];
    playerID = new Integer[2];
    connections = new Connector[2];
    nextMessage = null;
    state = WAITING;
  }
  public String welcomeMessage(){
    String s = "Hello to "+players[0]+" and "+players[1]+". Let us Begin. The rules are as follows:\n";
    s+="This is simply a polite conversation. Take turns speaking, and wait until the other is complete. "+players[0]+" will go first.\n";
    return s;
  }
  public String SendCommand(Integer id, String Message){
    if(turn == 0){
      if(id != playerID[0]){
        return "please wait your turn";
      }
      ChatPacket out = new ChatPacket("Play", Message);
      connections[1].SendtoClient(out);
      turn = 1;
      return "";
    }
    else{
      if(id != playerID[1]){
        return "please wait your turn";
      }
      ChatPacket out = new ChatPacket("Play", Message);
      connections[0].SendtoClient(out);
      turn = 0;
      return "";
    }

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
      return true;
    }
    System.err.println("someone tried to connect to a full lobby");
    return false;
  }





}
