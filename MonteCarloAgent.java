import java.rmi.*;
import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class MonteCarloAgent extends UnicastRemoteObject implements MonteCarloAgentInterface {
   
    public MonteCarloAgent () throws RemoteException {
      super();
   }
	ArrayList<Move> moves;
	ArrayList<Integer> chances;
	String[] ips;
	int NUMBEROFSLAVES;
	int CURRENTFINISHEDMOVES = 0;
	int NUMSLAVESEXECUTION;
	int NUMAGENTEXECUTION;
	int CURRENTAGENTEXECUTION = 0;
	long STARTTIME = System.currentTimeMillis();
	int NUMOFMOVES;

   public  void initGame(String[] ips) {
   	  this.ips = ips;
   	  NUMBEROFSLAVES = ips.length-3;
   	  NUMSLAVESEXECUTION = Integer.parseInt(ips[ips.length-2]);
   	  NUMAGENTEXECUTION = Integer.parseInt(ips[ips.length-3]);


   	  moves = new ArrayList<Move>();
	  chances = new ArrayList<Integer>();
 
        int[][] board = new int[8][8];
		Board b=new Board();
		try{
			BufferedReader input =   new BufferedReader(new FileReader(ips[ips.length-1]));
			for (int i=0; i<8; i++) {
				String line=input.readLine();
				String[] pieces=line.split("\\s");
				for (int j=0; j<8; j++) {
					board[i][j]=Integer.parseInt(pieces[j]);
				}
			}
			String turn=input.readLine();
			b.fromArray(board);
			if (turn.equals("N")) b.setTurn(b.TURNBLACK);
			else b.setTurn(b.TURNWHITE);
			b.setShortCastle(b.TURNWHITE,false);
			b.setLongCastle(b.TURNWHITE,false);
			b.setShortCastle(b.TURNBLACK,false);
			b.setLongCastle(b.TURNBLACK,false);
		
			String st=input.readLine();
			while (st!=null) {
				if (st.equals("EnroqueC_B")) b.setShortCastle(b.TURNWHITE,true);
				if (st.equals("EnroqueL_B")) b.setLongCastle(b.TURNWHITE,true);
				if (st.equals("EnroqueC_N")) b.setShortCastle(b.TURNBLACK,true);
				if (st.equals("EnroqueL_N")) b.setLongCastle(b.TURNBLACK,true);
				st=input.readLine();
			}
		} catch (Exception e) {}
		NUMOFMOVES = b.getValidMoves().length;
        int aux = NUMOFMOVES/NUMBEROFSLAVES;
        for(int i = 0; i < NUMBEROFSLAVES; i++){
        	(new Request(b, NUMSLAVESEXECUTION, ips[i], aux*i, aux*(i+1), i)).start();
        	//System.out.println("Slave: "+i+" evaluara desde "+(aux*i)+" hasta "+(aux*(i+1))+" aux "+aux+" mumOfMoves "+NUMOFMOVES+ " NUMBEROFSLAVES "+NUMBEROFSLAVES);

        	if(aux*(i+2) > NUMOFMOVES){
	        	(new Request(b, NUMSLAVESEXECUTION, ips[i], aux*(i+1), NUMOFMOVES, i)).start();
	        	//System.out.println("Slave: "+i+" evaluara desde "+(aux*(i+1))+" hasta "+NUMOFMOVES+" aux "+aux+" mumOfMoves "+NUMOFMOVES+ " NUMBEROFSLAVES "+NUMBEROFSLAVES);
	        	continue;
        	}
        }

   }

   public void receiveResult(int result, Move move, int numSlave){
   		Lock lock = new ReentrantLock();
   		lock.lock();
   		CURRENTFINISHEDMOVES++;
   		chances.add(result);
   		moves.add(move);
   		if(CURRENTFINISHEDMOVES >= NUMOFMOVES){
   			CURRENTAGENTEXECUTION++;
   			CURRENTFINISHEDMOVES = 0;

   			if(CURRENTAGENTEXECUTION < NUMAGENTEXECUTION){
   				System.out.println("CURRENTAGENTEXECUTION:  " + CURRENTAGENTEXECUTION);
   				System.out.println("NUMAGENTEXECUTION:  " +NUMAGENTEXECUTION);
   				moves = new ArrayList<Move>();
	  			chances = new ArrayList<Integer>();
   				initGame(ips);
   			}
   			else if(CURRENTAGENTEXECUTION == NUMAGENTEXECUTION){
   				System.out.println("CURRENTAGENTEXECUTION:  " + CURRENTAGENTEXECUTION);
   				System.out.println("NUMAGENTEXECUTION:  " +NUMAGENTEXECUTION);
	   			int major_index = 0;
				int major_value = chances.get(0);
				for (int i = 1; i<chances.size();i++) {
					if(major_value < chances.get(i))
					{
						major_value = chances.get(i);
						major_index = i;
					}
				}
				System.out.println(chances);
				System.out.println("best move = " + major_index);
				System.out.println(moves.get(major_index));
				int avgExTime = ((int)((System.currentTimeMillis() - STARTTIME)))/ NUMAGENTEXECUTION;
				System.out.println(" Execution Time in msec: " + avgExTime);
				System.out.println(" Execution Time in sec: " + ((avgExTime/1000)%60));


	   		}
   		}
   		lock.unlock();
   		
   }
}

