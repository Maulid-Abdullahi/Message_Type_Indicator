import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 *
 * @author maulid
 */
public class server {

    int port;
    ServerSocket server=null;
    Socket client=null;
    ExecutorService pool = null;
    int clientcount=0;
    static MessageFactory msgFactory = new MessageFactory();

    public static void main(String[] args) throws IOException {
        server serverobj=new server(5000);
        serverobj.startServer();
    }

    server(int port){
        this.port=port;
        pool = Executors.newFixedThreadPool(5);
    }

    public void startServer() throws IOException {

        server=new ServerSocket(5000);

        System.out.println("Server Booted");
        System.out.println("Any client can stop the server by sending -1");
        while(true)
        {
            client=server.accept();
            clientcount++;
             Thread th = new Thread(new ServerThread(client, clientcount, this));
             th.start();
//
//            ServerThread runnable= new ServerThread(client,clientcount,this);
//            pool.execute(runnable);
        }

    }

    private static class ServerThread implements Runnable {

        //private final server server;
        server svr;
        Socket client;
        BufferedReader cin;
        PrintStream cout;
        Scanner sc=new Scanner(System.in);
        int id;
        String s;

        ServerThread(Socket client, int count , server svrr ) throws IOException {

            this.client=client;
            this.svr=svrr;
            this.id=count;
            System.out.println("Connection "+id+"established with client "+client);

            cin=new BufferedReader(new InputStreamReader(client.getInputStream()));
            cout=new PrintStream(client.getOutputStream());

        }

        public void parsemti(String c) throws ISO8583Exception{
            try{
                Integer.parseInt(c);
            }
            catch(NumberFormatException ex ){
                throw new ISO8583Exception("Number Not ISO 8583 Format:");
            }
        }

        @Override
        public void run() {
            //int x=1;
            try{
                while(true){
                    s=cin.readLine();

                    System. out.print("Client("+id+") :"+s+"\n");

                    String mti = s.substring(0, 4);

                    System.out.println(mti);


                    try{
                        parsemti(mti);
                    }
                    catch(ISO8583Exception e){
                        cout.println("Your number format is incorrect!!!!!!!");
                        System.err.println(e.getMessage());

                    }



                    String [] mtiDigits = mti.split("");

                    // Interpret
                    int versionNo = Integer.parseInt(mtiDigits[0]);
                    int classNo = Integer.parseInt(mtiDigits[1]);
                    int functionNo = Integer.parseInt(mtiDigits[2]);
                    int originNo = Integer.parseInt(mtiDigits[3]);

                    String version = msgFactory.version.get(versionNo);
                    String msgClass = msgFactory.messageClass.get(classNo);
                    String msgFunction = msgFactory.messageFunction.get(functionNo);
                    String msgOrigin = msgFactory.messageOrigin.get(originNo);

                    System.out.println("=====================================================");
                    String res = String.format("ISO 8583 version for (%s is : %s)%n" +
                            "Message class  for (%s is : %s)%n" +
                            "Message function  for (%s is : %s)%n" +
                            "Message origin  for (%s is : %s)%n",
                            versionNo, version,classNo, msgClass,functionNo, msgFunction,originNo, msgOrigin);

                    cout.println(res);
                    cout.flush();


//                    cin.close();
//                    client.close();
//                    cout.close();

                }

            }
           catch(IOException e){

               System.err.println(e.getMessage());
        }
    }
}
}
