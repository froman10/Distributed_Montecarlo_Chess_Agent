import java.rmi.*;
import java.io.Serializable;

public interface MonteCarloSlaveInterface extends Remote {
    public void playOut(Board board, Move m, int numReps, String ipMaster, int numSlave) throws RemoteException;
}