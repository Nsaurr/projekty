package com.mycompany.tictactoeserver;

// Klasa testująca serwer Kółko i krzyżyk.
import javax.swing.JFrame;

/**
 *
 * 
 * @author matys
 */
public class TicTacToeServerTest {

    public static void main(String[] args) {
        TicTacToeServer application = new TicTacToeServer();
        application.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        application.execute();
    }
}
