package com.mycompany.tictactoeclient;

// Strona klienta programu klient/serwer Kółko i krzyżyk.
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 *
 * 
 * @author matys
 */
public final class TicTacToeClient extends JFrame implements Runnable {

    private final JTextField idField; // pole tekstowe do wyświetlenia znaku gracza
    private final JTextArea displayArea; // JTextArea do wyświetlenia danych wyjściowych
    private final JPanel boardPanel; // panel do gry w kółko i krzyżyk
    private final JPanel panel2; // panel do trzymania tablicy
    private final Square[][] board; // tablica do gry w kółko i krzyżyk
    private Square currentSquare; // aktualny kwadrat
    private Socket connection; // połączenie z serwerem
    private Scanner input; // wejście z serwera
    private Formatter output; // wyjście na serwer
    private final String ticTacToeHost; // nazwa hosta dla serwera
    private String myMark; // znak tego klienta
    private boolean myTurn; // określa, który klient wykonuje ruch
    private final String X_MARK = "X"; // znak dla pierwszego klienta
    private final String O_MARK = "O"; // znak dla drugiego klienta

    // skonfiguruj interfejs użytkownika i tablicę
    public TicTacToeClient(String host) {
        ticTacToeHost = host; // ustaw nazwę serwera
        displayArea = new JTextArea(4, 30); // ustaw JTextArea
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.SOUTH);

        boardPanel = new JPanel(); // ustaw panel dla kwadratów na planszy
        boardPanel.setLayout(new GridLayout(3, 3, 0, 0));
        board = new Square[3][3]; // utwórz planszę

        // pętla nad wierszami tablicy
        for (int row = 0; row < board.length; row++) {
            // pętla nad kolumnami tablicy
            for (int column = 0; column < board[row].length; column++) {
                // utwórz kwadrat
                board[row][column] = new Square(" ", row * 3 + column);
                boardPanel.add(board[row][column]); // dodaj kwadrat      
            }
        }

        idField = new JTextField(); // ustaw pole tekstowe
        idField.setEditable(false);
        add(idField, BorderLayout.NORTH);

        panel2 = new JPanel(); // skonfiguruj panel tak, aby zawierał boardPanel
        panel2.add(boardPanel, BorderLayout.CENTER); // dodaj panel tablicy
        add(panel2, BorderLayout.CENTER); // dodaj panel kontenera
        setTitle("Kółko i krzyżyk Klient");
        setSize(300, 225); // ustaw rozmiar okna
        setResizable(false);
        setVisible(true); // Pokaż okno
        
        startClient();
    }

    // uruchom wątek klienta
    public void startClient() {
        // łączymy się z serwerem i pobieramy dane
        try {
            // nawiąż połączenie z serwerem
            connection = new Socket(InetAddress.getByName(ticTacToeHost), 12345);

            // pobierz dane wejściowe i wyjściowe
            input = new Scanner(connection.getInputStream());
            output = new Formatter(connection.getOutputStream());
        } catch (IOException ioException) {
            System.out.println(ioException.toString());
        }

        // utwórz i uruchom wątek roboczy dla tego klienta
        ExecutorService worker = Executors.newFixedThreadPool(1);
        worker.execute(this); // wykonaj klienta
    }

   /**
     * Metoda uruchamiania kontroluje informacje wysyłane do i
     * otrzymane od klienta.
     */
    @Override
    public void run() {
        myMark = input.nextLine(); // pobierz znak gracza (X lub O)

        SwingUtilities.invokeLater(() -> {
            // wyświetl znak gracza
            idField.setText("Masz znak: \"" + myMark + "\"");
        });

        myTurn = (myMark.equals(X_MARK)); // określamy, czy to kolej klienta

        // odbierz komunikaty wysłane do klienta i wyślij je
        while (true) {
            if (input.hasNextLine()) {
                processMessage(input.nextLine());
            }
        }
    }

    // przetwarzamy komunikaty wysyłane do klienta
    private void processMessage(String message) {
        // wystąpił prawidłowy ruch
        switch (message) {
            case "Prawidłowy ruch":
                displayMessage("Prawidłowy ruch, proszę czekać.\n");
                setMark(currentSquare, myMark); // ustaw znacznik w kwadracie
                break;
            case "Nieprawidłowy ruch, spróbuj ponownie":
                displayMessage(message + "\n"); // wyświetl nieprawidłowy ruch
                myTurn = true; // jeszcze tura tego klienta
                break;
            case "Przeciwnik poruszył się":
                int location = input.nextInt(); // pobierz lokalizację przeniesienia
                input.nextLine(); // pomiń znak nowej linii po lokalizacji typu int
                int row = location / 3; // oblicz wiersz
                int column = location % 3; // oblicz kolumnę
                setMark(board[row][column],
                        (myMark.equals(X_MARK) ? O_MARK : X_MARK)); // zaznacz ruch 
                displayMessage("Przeciwnik poruszył się. Twoja kolej.\n");
                myTurn = true; // teraz kolej na tego klienta
                break;
            case "PORAŻKA":
            case "REMIS":
            case "ZWYCIĘSTWO":
                // Gra zakończona, wyświetl wyniki i zatrzymaj grę
                displayMessage(message + "\n"); // wyświetl komunikat
                myTurn = false;
                break;
            default:
                displayMessage(message + "\n");// wyświetl komunikat
                break;
        }
    }

    // manipulowanie displayArea w wątku wysyłania zdarzeń
    private void displayMessage(final String messageToDisplay) {
        SwingUtilities.invokeLater(() -> {
            displayArea.append(messageToDisplay); // aktualizuje dane wyjściowe
        });
    }

    // metoda narzędziowa do ustawiania znaku w wątku wysyłania zdarzeń
    private void setMark(final Square squareToMark, final String mark) {
        SwingUtilities.invokeLater(() -> {
            squareToMark.setMark(mark); // ustaw znacznik w kwadracie
        });
    }

    // wyślij wiadomość do serwera wskazującą kliknięty kwadrat
    public void sendClickedSquare(int location) {
        // jeśli jest moja kolej
        if (myTurn) {
            output.format("%d\n", location); // wyślij lokalizację na serwer
            output.flush();
            myTurn = false; // już nie moja kolej
        }
    }

    // ustaw bieżący Kwadrat
    public void setCurrentSquare(Square square) {
        currentSquare = square; // ustaw bieżący kwadrat na argument
    }

    // prywatna klasa wewnętrzna dla pól na planszy
    private class Square extends JPanel {

        private String mark; // znak do narysowania w tym kwadracie
        private final int location; // lokalizacja kwadratu

        public Square(String squareMark, int squareLocation) {
            mark = squareMark; // ustaw znacznik dla tego kwadratu
            location = squareLocation; // ustaw położenie tego kwadratu

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    setCurrentSquare(Square.this); // ustaw bieżący kwadrat

                    // wyślij lokalizację tego kwadratu
                    sendClickedSquare(getSquareLocation());
                }
            });
        }

        // zwróć preferowany rozmiar kwadratu
        @Override
        public Dimension getPreferredSize() {
            return new Dimension(30, 30); // zwróć preferowany rozmiar kwadratu
        }

        // zwróć minimalny rozmiar kwadratu
        @Override
        public Dimension getMinimumSize() {
            return getPreferredSize(); // zwróć minimalny rozmiar kwadratu
        }

        // ustaw znacznik dla kwadratu
        public void setMark(String newMark) {
            mark = newMark; // ustaw znacznik dla kwadratu
            repaint(); // ustaw wartość kwadrat
        }

        // zwróć lokalizację kwadratu
        public int getSquareLocation() {
            return location; // zwróć lokalizację kwadratu
        }

        // narysuj Kwadrat
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawRect(0, 0, 29, 29); // narysuj Kwadrat
            g.drawString(mark, 11, 20); // narysuj znak  
        }
    }

}
