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
      ClientContainer.state="WaitingForLobby";
      return;
    }
    if(input.packetType.equals("LobbyBegin")){
      System.out.println("got Into game");
      ClientContainer.gameID=Integer.parseInt(input.packetMessage);
      ClientContainer.state="BeginPlay";
      return;
    }
    if(input.packetType.equals("YourTurn")){
      System.out.println(input.packetMessage);
      ClientContainer.state="Playing";
      return;
    }
    if(input.packetType.equals("OtherTurn")){
      System.out.print(input.packetMessage);
      try{
        Thread.sleep(3000);
      }catch(InterruptedException e){
        System.err.println("Sleep interrupted" + e);
        System.exit(1);
      }
      ClientContainer.state="WaitingForTurn";
      return;
    }
    if(input.packetType.equals("FinishGame")){
      ClientContainer.state="Menu";
      System.out.println(input.packetMessage);
      System.out.println("Game was finished. Returning to Menu");
      return;
    }
    if(input.packetType.equals("Error")){
      System.out.println(input);
      return;
    }
    if(input.packetType.equals("KeepAlive")){
      try{
        Thread.sleep(500);
      }catch(InterruptedException e){
        System.err.println("Sleep interrupted" + e);
        System.exit(1);
      }
      return;
    }
    if(input.packetType.equals("SafeToExit")){
      System.out.println("Ready to Exit");
      ClientContainer.state="Exit";
    }
    System.out.println("cound not process packet "+ input);
    return;
  }
}
