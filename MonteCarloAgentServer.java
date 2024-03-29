import java.net.*;
import java.rmi.*;

public class MonteCarloAgentServer{
   public static void main(String[] args) {
      try {
         MonteCarloAgentInterface monteCarloAgent = new MonteCarloAgent();
         Naming.rebind("monteCarloAgent", monteCarloAgent);

         monteCarloAgent.initGame(args);
         System.out.println("Server is running.");
      }
      catch (RemoteException rex) {
         System.out.println("Server exception.main(): " + rex);
      }
      catch (MalformedURLException ex) {
         System.out.println("MalformedURLException " + ex);
      }
   }
}