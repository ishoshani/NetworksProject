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
    String hostName = arg[0];
    int portNumber = Integer.parseInt(arg[1]);
    try(
      Socket echoSocket = new Socket(hostName,portNumber);
      PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
      BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
      ){
        String userInput;
        while((userInput = stdin.readline()) != null){
          out.println(userInput);
          System.out.println("echo: "+ in.readline());
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
    }
  }
}
