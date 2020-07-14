import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
/**
 *
 * @author Maulid
 */
public class client {

    public static void main(String args[]) throws Exception
    {
        Socket sk=new Socket("127.0.0.1",5000);
        BufferedReader sin=new BufferedReader(new InputStreamReader(sk.getInputStream()));
        PrintStream sout=new PrintStream(sk.getOutputStream());
        BufferedReader stdin=new BufferedReader(new InputStreamReader(System.in));
        String s;

        while (  true )
        {
            System.out.print("Client : ");
            s=stdin.readLine();
            sout.println(s);

            System.out.println("=====================================================");
            String y ;

            while( ! (y = sin.readLine()).isEmpty()){
                System.out.println("Server : "+y);
            }
            System.out.println("=====================================================");


        }

    }

}
