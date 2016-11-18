java.import.*
java.io.*

public class ServerContainer{
  public static void main(String[] args) throws IOException{
    if (args.length!=1){
            System.err.println("Usage: java ServerContainer <port number>");
            System.exit(1);
          }

    int portNumber = Integer.parseInt(args[0]);
    try(
      ServerSocket serverSocket = new ServerSocket(portNumber);
      Socket clientSocket = new serverSocket.accept();
      PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
      BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      ){
        String inputLine;
        while((inputLine = in.readline()) != null){
          out.println(inputLine);
        }
      }catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port "
                + portNumber + " or listening for a connection");
            System.out.println(e.getMessage());
        }
      }
  }
