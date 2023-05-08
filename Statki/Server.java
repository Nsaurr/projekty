import java.net.*;
import java.io.*;

public class Server extends Player{
    private InetAddress ip; 
    private ServerSocket server; 

    public Server(String name) {
        super(name, false);
        try {
            this.ip = InetAddress.getLocalHost();
            this.server = new ServerSocket(0, 1, ip);
            System.out.printf("Utworzyłeś serwer w %s na porcie %d\n", ip.getHostAddress(), server.getLocalPort());
            System.out.println("Oczekiwanie na połączenie...");
        
            this.socket = server.accept();
            updatePlayerDataStreams(); //musi to zostać wywołane tutaj, aby wejście i wyjście zostały poprawnie zainicjowane
            String oppName = in.readUTF();
            System.out.println(oppName + " podłączony z " + this.socket.getInetAddress() + ":" + this.socket.getPort());
            out.writeUTF(name);
            
        } catch (IOException e) {
            e.printStackTrace();
        }

        
        while (!isOver) {
            if(myTurn) {
                sendMove();
                game.board.printBothBoards(game.oppBoard);
            }else {
                receiveMove();
                game.board.printBothBoards(game.oppBoard);
            }
        }


        if (this.winner) {
            System.out.println("Gratulacje! Wygrałeś!");
        } else {
            System.out.println(this.oppName + " Zwycięża. Więcej szczęścia następnym razem");
        }


        try {
            server.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        closeConnection();
        
    }
}
