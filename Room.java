import java.net.*;
import java.io.*;

public class Room{
  String[] players;
  String nextMessage;
  Integer state;
  final static int WAITING = 0;
  final static int PLAYING = 1;
  final static int DONE = 2;

  public Room(){
    players = new String[2];
    nextMessage = null;
    state = WAITING;
  }
  public boolean AddPlayer(String username){
    if(players[0]==null){
      players[0] = username;
      return true;
    }
    if(players[1]==null){
      players[1] = username;
      state = PLAYING;
      return true;
    }
    System.err.println("someone tried to connect to a full lobby");
    return false;
  }





}
