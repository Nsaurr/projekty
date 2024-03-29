import java.util.HashSet;
import java.util.Scanner;
import java.net.*;
import java.io.*;

public class Player {
    protected Game game;
    protected String name;
    protected String oppName;
    protected Socket socket; 
    protected DataInputStream in; 
    protected DataOutputStream out;

    protected boolean isOver;
    protected boolean winner;
    protected boolean myTurn;

    protected HashSet<String> previousMoves;

    private Scanner scan;

    public Player(String name, boolean myTurn) {
        this.name = name;
        this.oppName = "";
        this.myTurn = myTurn;
        this.scan = new Scanner(System.in);
        this.game = new Game();
        this.isOver = false;
        this.winner = false;
        this.previousMoves = new HashSet<String>();


        this.socket = null;
        this.in = null;
        this.out = null;
        
    }

    public void updatePlayerDataStreams() {
        try {
            this.out = new DataOutputStream(this.socket.getOutputStream());
            this.in = new DataInputStream(this.socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {    
        try {
            this.socket.close();
            this.in.close();
            this.out.close();
            this.scan.close();
        } catch (IOException e) {
            e.printStackTrace();
        }  
    }


    public void receiveMove() {
        System.out.println("Czekam na ruch przeciwnika...");
        String message = "";
        try {
            message = in.readUTF(); //odbierz ruch od innego gracza
            System.out.println("Ruch przeciwnika: " + message);
            String[] response = processOpponentMove(message);
            if (response[0].contains("Pudło")) {
                myTurn = true;
            } else if (response[3].equals("Koniec")) {
                isOver = true;
            }
            out.writeUTF(response[0]);
            out.writeUTF(response[1]);
            out.writeUTF(response[2]);
            out.writeUTF(response[3]);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMove() {
        String move = "";
        try {
            move = getMove();
            out.writeUTF(move); // wyślij ruch innemu graczowi
            String response = in.readUTF();
            String hits = in.readUTF();
            String miss = in.readUTF();
            String over = in.readUTF();
            System.out.println("Twój ruch : " + response);
            if(!response.contains("Trafienie")) {
                myTurn = false;
            }
            if (over.equals("Koniec")) {
                isOver = true;
                winner = true;
            }
            processPlayerMove(move, response, hits, miss);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public String getMove() {
        String move = "";
        do {
            System.out.println();
            if(!move.equals("")) {
                System.out.println("Nieprawidłowy ruch");
            }
            System.out.print("Podaj swój ruch: ");
            move = scan.nextLine();
            System.out.println();  
        } while (!validMove(move));   
        previousMoves.add(move);    
        return move;
    }

    public boolean validMove(String move) {
        if(move.equals("")) {
            return false;
        }
        if (previousMoves.contains(move)) {
            return false;
        }
        int[] check = game.oppBoard.stringToCoord(move);
        if (check[0] == -1) {
            return false;
        }
        return true;
    }

    public String[] processOpponentMove(String move) {
        String[] rval = new String[4];
        int[] coords = game.board.stringToCoord(move);
        int[] temp = new int[2];
        temp[0] = coords[1];
        temp[1] = coords[0];
        int option = game.board.updateBoard(temp);
        if(game.gameOver()) {
            rval[3] = "Koniec";
        } else {
            rval[3] = "";
        }
        switch(option) {
            case 0:
                rval[0] = "Pudło";
                break;
            case 1:
                rval[0] = "Trafienie";
                break;
            case 2:
                rval[0] = "Trafienie:Zatopiony";
                break;
            default:
                rval[0] = "Pudło";
        }
        rval[1] = game.board.hits;
        rval[2] = game.board.miss;
        return rval;
    }

    public void processPlayerMove(String move, String response, String hits, String miss) {
        int[] coords = game.oppBoard.stringToCoord(move);
        if (response.equals("Pudło")) {
            game.oppBoard.setValue(coords[1], coords[0], 3);
        } else if (response.equals("Trafienie")) {
            game.oppBoard.setValue(coords[1], coords[0], 2);
        } else if (response.equals("Trafienie:Zatopiony")) {
            String[] hitArray = hits.split(" ");
            String[] missArray = miss.split(" ");
            for (int i = 0; i < hitArray.length; i++) {
                int a = hitArray[i].charAt(0) - '0';
                int b = hitArray[i].charAt(1) - '0';
                game.oppBoard.setValue(a, b, 2);
            }
            for (int i = 0; i < missArray.length; i++) {
                int a = missArray[i].charAt(0) - '0';
                int b = missArray[i].charAt(1) - '0';
                game.oppBoard.setValue(a, b, 3);
            }
            
        }
    }

}
