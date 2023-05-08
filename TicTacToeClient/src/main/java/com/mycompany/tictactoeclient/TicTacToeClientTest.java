package com.mycompany.tictactoeclient;

// Klasa testowa dla klienta Kółko i krzyżyk.
import javax.swing.JFrame;

/**
 *
 * 
 * @author matys
 */
public class TicTacToeClientTest {

    public static void main(String[] args) {
        TicTacToeClient application; // zadeklaruj aplikację klienta

        // Sprawdź argumenty wiersza poleceń
        if (args.length == 0) {
            application = new TicTacToeClient("127.0.0.1"); // localhost
        } else {
            application = new TicTacToeClient(args[0]); // użyj args
        }

        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
