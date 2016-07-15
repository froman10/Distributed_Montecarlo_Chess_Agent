import java.rmi.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.rmi.server.UnicastRemoteObject;

class Request extends Thread{
	private Board b;
	private int TOTAL;
	private String ipSlave;
	private int firstMove;
	private int lastMove;
	private int numSlave;

	Request(Board b, int total, String ip, int firstMove, int lastMove, int numSlave){

		this.b = b;
		this.TOTAL = total;
		this.ipSlave = ip;
		this.firstMove = firstMove;
		this.lastMove = lastMove;
		this.numSlave = numSlave;
	}

	@Override
	public void run() {
		try{
			int numOfMoves = b.getValidMoves().length;
			Move[] moves = b.getValidMoves();
			int result;
			Object o;
	        MonteCarloSlaveInterface monteCarloSlave;

	        for(int j = firstMove; j < lastMove; j++){
        		o = Naming.lookup("rmi://"+ipSlave+"/monteCarloSlave");
        		monteCarloSlave = (MonteCarloSlaveInterface) o;
        		result = 0;
				//long ts = System.currentTimeMillis();
				monteCarloSlave.playOut(b, moves[j], TOTAL, InetAddress.getLocalHost().getHostAddress(), numSlave);
				//long te = System.currentTimeMillis();
				//System.out.println("Tiempo de espera: "+(te-ts)+ " para esclavo: "+numSlave);
        	}
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
		catch (java.net.UnknownHostException ex){
			System.err.println("No se encuentra el host");
		}
	}
}