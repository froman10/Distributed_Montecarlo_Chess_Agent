import java.rmi.*;
import java.io.Serializable;

public interface MonteCarloAgentInterface extends Remote {
    public void receiveResult(int result, Move move, int numSlave) throws RemoteException;
    public  void initGame(String[] ips) throws RemoteException;
}