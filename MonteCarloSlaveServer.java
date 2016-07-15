import java.net.*;
import java.rmi.*;

public class MonteCarloSlaveServer{
   public static void main(String[] args) {
      try {
         MonteCarloSlaveInterface monteCarloSlave = new MonteCarloSlave();
         Naming.rebind("monteCarloSlave", monteCarloSlave);
         System.out.println("Server is ready.");
      }
      catch (RemoteException rex) {
         System.out.println("Server exception.main(): " + rex);
      }
      catch (MalformedURLException ex) {
         System.out.println("MalformedURLException " + ex);
      }
   }
}