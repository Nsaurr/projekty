import java.net.*;
import java.io.*; 

public class Client extends Player{
    private String ip; 
    private int port;  

    public Client(String ip, int port, String name) {
        super(name, true);
        this.ip = ip; 
        this.port = port;
        try {
            this.socket = new Socket(this.ip, this.port);
            updatePlayerDataStreams();
            out.writeUTF(name); //wysyłanie nazwy na serwer
            this.oppName = in.readUTF(); // pobranie nazwy z serwera
            System.out.println("Połączyłeś się do pojedynku " + oppName);
        }  catch (IOException e) {
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

        closeConnection();

    }
}
