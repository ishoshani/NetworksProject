
import java.io.*;
import java.net.*;

public class ClientContainer{
  public static void main(String[] args) {
    if(args.length != 2){
      System.err.println(
        "Usage: needs two arguments, java ClientContainer <hostname> <portnumber>"
      );
      System.exit(1);
    }
    String hostName = args[0];
    int portNumber = Integer.parseInt(args[1]);
    try(
      Socket echoSocket = new Socket(hostName,portNumber);
      ObjectOutputStream out = new ObjectOutputStream(echoSocket.getOutputStream());
      ObjectInputStream in = new ObjectInputStream(echoSocket.getInputStream());
      BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in))
      ){
        String userInput;
        out.writeObject(new ChatPacket("Start"));
        System.out.println("echo: "+ ((ChatPacket)in.readObject()).packetMessage);
        while((userInput = stdin.readLine()) != null){
          out.writeObject(new ChatPacket("Command", userInput));
          System.out.println("echo: "+ ((ChatPacket)in.readObject()).packetMessage);
        }
      }catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
      }
      catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                hostName);
            System.exit(1);
      }
      catch (ClassNotFoundException e){
          System.err.println("Unexpected type of Object" + e);
          System.exit(1);

      }
    }
  }
