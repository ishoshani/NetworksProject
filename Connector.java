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
      System.err.println("in Connector" + e);
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
    if(input.packetType.equals("KeepAlive")){
      return new ChatPacket("KeepAlive");
    }
    if(input.packetType.equals("WaitingForLobby")){
      if (CurrentGame.state==Room.PLAYING){
        c = new ChatPacket("LobbyBegin", uID.toString());
        return c;
      }else{
        return new ChatPacket("KeepAlive");
      }
    }

    if(input.packetType.equals("BeginPlay")){
      String output = CurrentGame.welcomeMessage();
      String type;
      if(CurrentGame.turn == uID){
        type = "YourTurn";
      }
      else{
        type = "OtherTurn";
      }
      c = new ChatPacket(type, output, input.gameID);
      return c;
    }
    if(input.packetType.equals("WaitingForTurn")){
      if(CurrentGame.state==Room.DONE){
        return new ChatPacket("FinishGame",CurrentGame.finish());
      }
      return CurrentGame.getNextMessage();
    }
    if(input.packetType.equals("Playing")){
      try{
        return new ChatPacket("OtherTurn", CurrentGame.SendCommand(uID, input.packetMessage), input.gameID);
      }catch (InvalidMoveException e){
        return new ChatPacket("YourTurn", e.toString(), input.gameID);
      }
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
      }if(command.matches("lobby(.*)")){
        String[] cPieces= command.split(" ");
        if(cPieces.length!=3){
          return new ChatPacket("Message", "Usage: lobby [password] [game] 0 for Convo 1 for TicTacToe)");
        }
        String pass = cPieces[1];
        int gamePick = Integer.parseInt(cPieces[2]);
        synchronized(ServerContainer.privateRooms){
          if(ServerContainer.privateRooms.containsKey(pass)){
            CurrentGame = ServerContainer.privateRooms.get(pass);
            CurrentGame.AddPlayer(username, uID, this);
            CurrentGame.state=Room.PLAYING;
            c = new ChatPacket("LobbyBegin", uID.toString());
            return c;
          }else{
            ServerContainer.privateRooms.put(pass,new Room(gamePick));
            CurrentGame = ServerContainer.privateRooms.get(pass);
            CurrentGame.AddPlayer(username,uID, this);
            c = new ChatPacket("NoLobbies",uID.toString());
            return c;
          }
        }
      }if(command.matches("quickplay(.*)")){
        int gamePick;
        try{
          gamePick = Integer.parseInt(command.split(" ")[1]);
        }catch(ArrayIndexOutOfBoundsException e){
          return new ChatPacket("Message","Usage: play [game], 0 for Convo 1 for TicTacToe\n");
        }
        if(gamePick!=0&&gamePick!=1){
          return new ChatPacket("Message","please select a game you want to play, 0 for Convo 1 for TicTacToe\n");
        }
        synchronized (ServerContainer.roomList){
          Integer gameKey = findGame(gamePick);
          if(gameKey==null){
            ServerContainer.roomList.put(uID,new Room(gamePick));
            Room newGame= ServerContainer.roomList.get(uID);
            newGame.AddPlayer(username, uID, this);
            CurrentGame = newGame;
            c = new ChatPacket("NoLobbies",uID.toString());
            return c;
          }
          Room game = ServerContainer.roomList.get(gameKey);
          game.AddPlayer(username, uID, this);
          CurrentGame = game;
          c = new ChatPacket("LobbyBegin", uID.toString());
          return c;
        }
      }if(command.matches("status")){
        synchronized(ServerContainer.servState){
          c = new ChatPacket("Message", "there are currently "+ServerContainer.servState.currentUsers+" users");
        }
        return c;
      }
    }
    c = new ChatPacket("Message", "sorry didnt get that" + input);
    return c;
  }
  public Integer findGame(int gameID){
    for(Integer k : ServerContainer.roomList.keySet()){
      if(ServerContainer.roomList.get(k).gameID==gameID){
        if(ServerContainer.roomList.get(k).state==Room.WAITING){
          return k;
        }
      }
    }
    return null;
  }
  public void SendtoClient(ChatPacket outputPacket){
    try(
    ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
    ){
      out.writeObject(outputPacket);
    }catch(IOException e){
      System.err.println(e);
    }
  }

}
