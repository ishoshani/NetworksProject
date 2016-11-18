import java.io.*;
import java.net.*;

public class Connector extends Thread{
  Socket client;
  String username;

  public Connector(Socket socket){
    super("Connection"+socket);
    username = socket.toString();
    client = socket;
  }

  public void run(){
    try(
        PrintWriter out = new PrintWriter(client.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        ){
          String inputLine, outputLine;
          out.println("Welcome to IRC! send a command");
          while ((inputLine = in.readLine()) != null) {
            outputLine=process(inputLine);
            out.println(outputLine);
            }
        }catch(IOException e){
          System.err.println(e);
        }
    }

  public String process(String input){
    if(input.matches("username +(.*)")){
      username=input.split(" ")[1];
      return "hello "+username;
    }if(input.equals("hello")){
      return "hello "+username;
    }
    else{
      return "sorry, didn't get that "+input;
    }
  }

}
