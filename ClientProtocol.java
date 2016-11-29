import java.io.*;
import java.net.*;
/**
Choice Tree for input from server to Client
**/
public class ClientProtocol{
  public static void processProcedure(ChatPacket input){
    if(input.packetType.equals("Message")){//handle generic message
      System.out.println(input);
      return;
    }
    if(input.packetType.equals("NoLobbies")){//handle lack of lobbies found
      System.out.println("NoLobby : waiting for lobby with name of"+input.packetMessage);
      ClientContainer.state="WaitingForLobby";
      return;
    }
    if(input.packetType.equals("LobbyBegin")){//let server know that client is ready to play
      System.out.println("got Into game");
      ClientContainer.gameID=Integer.parseInt(input.packetMessage);
      ClientContainer.state="BeginPlay";
      return;
    }
    if(input.packetType.equals("YourTurn")){//handle notice that it is the clients turn
      System.out.println(input.packetMessage);
      ClientContainer.state="Playing";
      return;
    }
    if(input.packetType.equals("OtherTurn")){//handle notice that it is someone elses turn
      System.out.print(input.packetMessage);
      try{
        Thread.sleep(3000);//check every 3 seconds
      }catch(InterruptedException e){
        System.err.println("Sleep interrupted" + e);
        System.exit(1);
      }
      ClientContainer.state="WaitingForTurn";
      return;
    }
    if(input.packetType.equals("FinishGame")){//handle endo of game
      ClientContainer.state="Menu";
      System.out.println(input.packetMessage);
      System.out.println("Game was finished. Returning to Menu");
      return;
    }
    if(input.packetType.equals("Error")){//handle error from server
      System.out.println(input);
      return;
    }
    if(input.packetType.equals("KeepAlive")){//handle notice that Server Connection is fine
      try{
        if(ClientContainer.showKeepAlive){//only print message if this is On for debug
        System.out.println(input);
        }
        Thread.sleep(500);
      }catch(InterruptedException e){
        System.err.println("Sleep interrupted" + e);
        System.exit(1);
      }
      return;
    }
    if(input.packetType.equals("SafeToExit")){//Handle Exit Procedure
      System.out.println("Ready to Exit");
      ClientContainer.state="Exit";
    }
    System.out.println("cound not process packet "+ input);//Error in packet.
    return;
  }
}
