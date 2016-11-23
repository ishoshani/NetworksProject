import java.io.*;
import java.net.*;
import java.util.Hashtable;
import java.util.Set;

public class Connector extends Thread{
  Socket client;
  String username;
  Integer CurrentGameID;
  Room CurrentGame;
  Integer uID;
  public Connector(Socket socket, Integer userNumber){
    super("Connection"+socket);
    username = "newUser"+userNumber;
    client = socket;
    uID = userNumber;
  }

  public void run(){
    try(
    ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
    ObjectInputStream in = new ObjectInputStream(client.getInputStream());
    ){
      ChatPacket inputPacket, outputPacket;
      while ((inputPacket = ((ChatPacket)in.readObject())) != null) {
        outputPacket=process(inputPacket);
        out.writeObject(outputPacket);
      }
    }catch(IOException e){
      System.err.println(e);
    }catch(ClassNotFoundException e){
      System.err.println(e);
    }
  }

  public ChatPacket process(ChatPacket input){
    ChatPacket c;
    if(input.packetType.equals("Start")){
      c = new ChatPacket("Message", "Welcome to IRC!");
      return c;
    }
    if(input.packetType.equals("Menu")){
      String command = input.packetMessage;
      if(command.matches("username (.*)")){
        username=command.split(" ")[1];
        c = new ChatPacket("Message", "Username is now "+username);
        return c;
      }if(command.equals("hello")){
        c = new ChatPacket("Message", "hello "+username);
        return c;
      }if(command.equals("play")){
          synchronized (ServerContainer.roomList){
            Integer gameKey = findGame();
            if(gameKey==null){
              ServerContainer.roomList.put(uID,new Room());
              Room newGame= ServerContainer.roomList.get(uID);
              newGame.AddPlayer(username);
              CurrentGame = newGame;;
              c = new ChatPacket("NoLobbies",uID.toString());
            }
            Room game = ServerContainer.roomList.get(gameKey);
            game.AddPlayer(username);
            CurrentGame = game;
            c = new ChatPacket("LobbyBegin", uID.toString());
          }
        }if(command.matches("status")){
        synchronized(ServerContainer.servState){
          c = new ChatPacket("Message", "there are currently "+ServerContainer.servState.currentUsers+" users");
        }
        return c;
      }
    }
    c = new ChatPacket("Message", "sorry didnt get that");
    return c;
  }
  public Integer findGame(){
    for(Integer k : ServerContainer.roomList.keySet()){
      if(ServerContainer.roomList.get(k).state==Room.WAITING){
        return k;
      }
    }
    return null;
  }

}
