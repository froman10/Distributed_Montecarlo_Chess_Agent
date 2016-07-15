import java.io.Serializable;
import java.rmi.*;
import java.net.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Random;

public class MonteCarloSlave extends UnicastRemoteObject implements MonteCarloSlaveInterface {
   
    public MonteCarloSlave () throws RemoteException {
      super();
   }
   public void playOut(Board board, Move m, int numReps, String ipMaster, int numSlave){
    System.out.println("Servidor "+numSlave+" recibe peticion");
    int result = 0;
    for (int i=0;i < numReps ;i++ ) {
        result += play(board, m);
      }
    try{
        Object o = Naming.lookup("rmi://"+ipMaster+"/monteCarloAgent");
        MonteCarloAgentInterface monteCarloAgent = (MonteCarloAgentInterface) o;
        //Thread.sleep((long)(Math.random() * 1000));
        monteCarloAgent.receiveResult(result, m, numSlave);
    }
    catch (MalformedURLException ex) {
       System.err.println("URL de RMI invalida");
    }
    catch (RemoteException ex) {
       System.err.println("Objeto remoto genero excepcion " + ex);
    }
    catch (NotBoundException ex) {
       System.err.println("No se encuentra objeto remoto en el servidor");
    }
  }

  private int play(Board board, Move m){
    Boolean firstMove = true;
    Board b = board.clone();
    Random r=new Random();
      while(true)
      {
          if(!b.isStalemate() && !b.isCheckMate())//queda por jugar
          {
              if(!firstMove){
                Move[] moves=b.getValidMoves();
                m = moves[r.nextInt(moves.length)];
              }
              firstMove = false;
              b.makeMove(m);
          }else
          {
            break;
          } 
      }
      if(b.isStalemate())
      {
          return 0;
      }else if(b.isCheckMate() && board.turn == b.turn)
      {
          return -1;
      }
      return 1;
    }
  }