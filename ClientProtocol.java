import java.io.*;
import java.net.*;

public class ClientProtocol{
  public static void processProcedure(ChatPacket input){
    if(input.packetType.equals("Message")){
      System.out.println(input);
      return;
    }
    if(input.packetType.equals("NoLobbies")){
      System.out.println("NoLobby : waiting for lobby with name of"+input.packetMessage);
      ClientContainer.state="Waiting";
      return;
    }
    if(input.packetType.equals("LobbyBegin")){
      System.out.println("got Into game");
      ClientContainer.gameID=Integer.parseInt(input.packetMessage);
      ClientContainer.state="BeginPlay";
      return;
    }
    if(input.packetType.equals("yourTurn")){
      System.out.println(input.packetMessage);
      ClientContainer.state="Playing";
      return;
    }
    if(input.packetType.equals("otherTurn")){
      try{
        Thread.sleep(3000);
      }catch(InterruptedException e){
        System.err.println("Sleep interrupted" + e);
        System.exit(1);
      }
      ClientContainer.state="WaitingForTurn";
      return;
    }
    if(input.packetType.equals("StillAlive")){
      System.out.println("StillWaiting");
      try{
        Thread.sleep(3000);
      }catch(InterruptedException e){
        System.err.println("Sleep interrupted" + e);
        System.exit(1);
      }
      ClientContainer.state="Waiting";
      return;
    }
    System.out.println("cound not process packet "+ input);
    return;
  }
}
