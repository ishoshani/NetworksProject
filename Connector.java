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
        ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
        ObjectInputStream in = new ObjectInputStream(client.getInputStream());
        ){
          String inputLine, outputLine;
          while ((inputLine = (String)in.readObject()) != null) {
            outputLine=process(inputLine);
            out.writeObject(outputLine);
            }
        }catch(IOException e){
          System.err.println(e);
        }catch(ClassNotFoundException e){
          System.err.println(e);
        }
    }

  public String process(String input){
    if(input.matches("username (.*)")){
      username=input.split(" ")[1];
      return "username changed";
    }if(input.equals("hello")){
      return "hello "+username;
    }
    else{
      return "sorry, didn't get that "+input;
    }
  }

}
