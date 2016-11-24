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
  Integer gameID;
  Game game;
  final static int WAITING = 0;
  final static int PLAYING = 1;
  final static int DONE = 2;


  public Room(int gameID){
    players = new String[2];
    playerID = new Integer[2];
    connections = new Connector[2];
    this.gameID = gameID;
    game = getGame(gameID);
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
      n = new ChatPacket("otherTurn","");
    }else{
      nextMessage=null;
    }
    return n;
  }
  public void changeTurnSwitch(){
    if (turnSwitch==0){
      turnSwitch=1;
    }else{
      turnSwitch=0;
    }
  }
  public Game getGame(int gameID){
    if(gameID == 0){
      return new Game(this);
    }if(gameID == 1){
      return new TicTacToe(this);
    }
    return new Game(this);
  }
}
