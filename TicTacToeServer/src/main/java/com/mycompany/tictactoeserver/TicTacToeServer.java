package com.mycompany.tictactoeserver;

// Strona serwera programu klient/serwer Kółko i krzyżyk.
import java.awt.BorderLayout;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
import java.util.Formatter;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * 
 * @author matys
 */
public class TicTacToeServer extends JFrame {

    private final String[] board = new String[9]; // tablica do gry w kółko i krzyżyk
    private final JTextArea outputArea; // do wysyłania ruchów
    private final Player[] players; // tablica graczy
    private ServerSocket server; // gniazdo serwera do łączenia się z klientami
    private int currentPlayer; // śledzi gracza z bieżącym ruchem
    private final static int PLAYER_X = 0; // stała dla pierwszego gracza
    private final static int PLAYER_O = 1; // stała dla drugiego gracza
    private final static String[] MARKS = {"X", "O"}; // tablica znaków
    private final ExecutorService runGame; // uruchomi grę
    private final Lock gameLock; // zablokuje grę do synchronizacji
    private final Condition otherPlayerConnected; // czekać na innego gracza
    private final Condition otherPlayerTurn; // czekać na kolejkę innego gracza

    // skonfiguruj serwer kółko i krzyżyk oraz GUI, który wyświetla komunikaty
    public TicTacToeServer() {
        super("Kółko i krzyżyk Server"); // ustaw tytuł okna

        // utwórz ExecutorService z wątkiem dla każdego gracza
        runGame = Executors.newFixedThreadPool(2);
        gameLock = new ReentrantLock(); // utwórz blokadę dla gry

        // zmienna warunkowa dla obu podłączonych graczy
        otherPlayerConnected = gameLock.newCondition();

        // zmienna warunkowa dla tury drugiego gracza
        otherPlayerTurn = gameLock.newCondition();

        for (int i = 0; i < 9; i++) {
            board[i] = ""; // utwórz planszę do gry w kółko i krzyżyk
        }
        players = new Player[2]; // utwórz tablicę graczy
        currentPlayer = PLAYER_X; // ustawia bieżącego gracza na pierwszego gracza

        try {
            server = new ServerSocket(12345, 2); // skonfiguruj ServerSocket
        } catch (IOException ioException) {
            System.out.println(ioException.toString());
            System.exit(1);
        }

        outputArea = new JTextArea(); // utwórz JTextArea dla wyjścia
        add(outputArea, BorderLayout.CENTER);
        outputArea.setText("Serwer oczekuje na połączenia\n");
        setSize(300, 300); // ustaw rozmiar okna
        setResizable(false);
        setVisible(true); // Pokaż okno
    }

    // poczekaj na dwa połączenia, aby można było rozpocząć grę
    public void execute() {
        // czekaj na połączenie każdego klienta
        for (int i = 0; i < players.length; i++) {
            // czekaj na połączenie, utwórz gracza, uruchom go
            try {
                /**
                 * Kiedy klient się łączy, tworzony jest nowy obiekt Player
                 * zarządzaj połączeniem jako osobnym wątkiem, a wątek jest
                 * wykonywane w puli wątków runGame.
                 *
                 * Konstruktor Player otrzymuje obiekt Socket
                 * reprezentujący połączenie z klientem i pobiera
                 * powiązane strumienie wejściowe i wyjściowe.
                 */
                players[i] = new Player(server.accept(), i);
                runGame.execute(players[i]); // wykonaj player runnable
            } catch (IOException ioException) {
                System.out.println(ioException.toString());
                System.exit(1);
            }
        }

        gameLock.lock(); // zablokuj grę, aby zasygnalizować wątek gracza X

        try {
            players[PLAYER_X].setSuspended(false); // wznowienie gracza X
            otherPlayerConnected.signal(); // obudź wątek gracza X
        } finally {
            gameLock.unlock(); // odblokuj grę po sygnalizacji graczowi X
        }
    }

    // wyświetl wiadomość w outputArea
    private void displayMessage(final String messageToDisplay) {
        // wyświetl komunikat z wątku wykonania event-dispatch
        SwingUtilities.invokeLater(() -> {
           // aktualizuje outputArea
            outputArea.append(messageToDisplay); // Dodaj wiadomość
        });
    }

    // określa, czy ruch jest prawidłowy
    public boolean validateAndMove(int location, int player) {
        // jeśli nie jest aktualnym graczem, musi czekać na swoją kolejkę
        while (player != currentPlayer) {
            gameLock.lock(); // zablokuj grę, aby czekać na przejście innego gracza

            try {
                otherPlayerTurn.await(); // czekaj na kolej gracza
            } catch (InterruptedException exception) {
                System.out.println(exception.toString());
            } finally {
                gameLock.unlock(); // odblokuj grę po odczekaniu
            }
        }

        // jeśli lokalizacja nie jest zajęta, wykonaj ruch
        if (!isOccupied(location)) {
            board[location] = MARKS[currentPlayer]; // ustaw ruch na pokładzie
            currentPlayer = (currentPlayer + 1) % 2; // Zmień gracza

            // poinformuj nowego obecnego gracza, że ​​nastąpił ruch
            players[currentPlayer].otherPlayerMoved(location);

            gameLock.lock(); // zablokuj grę, aby zasygnalizować innemu graczowi przejście

            try {
                otherPlayerTurn.signal();// zasygnalizuj innemu graczowi, aby kontynuował
            } finally {
                gameLock.unlock(); // odblokuj grę po sygnalizacji
            }

            return true; // powiadamia gracza, że ​​ruch był prawidłowy
        } else {
            // ruch był nieprawidłowy
            return false; // powiadomi gracza, że ​​ruch był nieprawidłowy
        }
    }

    // określa, czy lokalizacja jest zajęta
    public boolean isOccupied(int location) {
        return board[location].equals(MARKS[PLAYER_X]) || board[location].equals(MARKS[PLAYER_O]);
    }

    /**
     * Sprawdź, czy są 3 takie same znaki z rzędu.
     *
     * @return Prawda, jeśli istnieje zwycięzca, fałsz, jeśli nie ma zwycięzcy.
     */
    public boolean hasWinner() {
        return (!board[0].isEmpty() && board[0].equals(board[1]) && board[0].equals(board[2]))
                || (!board[3].isEmpty() && board[3].equals(board[4]) && board[3].equals(board[5]))
                || (!board[6].isEmpty() && board[6].equals(board[7]) && board[6].equals(board[8]))
                || (!board[0].isEmpty() && board[0].equals(board[3]) && board[0].equals(board[6]))
                || (!board[1].isEmpty() && board[1].equals(board[4]) && board[1].equals(board[7]))
                || (!board[2].isEmpty() && board[2].equals(board[5]) && board[2].equals(board[8]))
                || (!board[0].isEmpty() && board[0].equals(board[4]) && board[0].equals(board[8]))
                || (!board[2].isEmpty() && board[2].equals(board[4]) && board[2].equals(board[6]));
    }

    /**
     * Sprawdź, czy plansza jest pełna.
     *
     * @return Prawda, jeśli plansza jest pełna, fałsz, jeśli jest puste miejsce.
     */
    public boolean boardFilledUp() {
        for (int i = 0; i < board.length; ++i) {
            if (board[i].isEmpty()) {
                return false;
            }
        }

        return true;
    }

    // określanie czy gra się zakończyła
    public boolean isGameOver() {
        return hasWinner() || boardFilledUp();
    }

    // prywatna klasa wewnętrzna Gracz zarządza każdym graczem jako runnable
    private class Player implements Runnable {

        private final Socket connection; // połączenie z klientem
        private Scanner input; // wejście od klienta
        private Formatter output; // wyjście do klienta
        private final int playerNumber; // śledzi, który to gracz
        private final String mark; // znak dla tego gracza
        private boolean suspended = true; // czy wątek jest zawieszony

        // założyć wątek gracza
        public Player(Socket socket, int number) {
            playerNumber = number; // zapisz numer tego gracza
            mark = MARKS[playerNumber]; // określ znak gracza
            connection = socket; // przechowuj gniazdo dla klienta

            // pobierz dane z gniazda
            try {
                input = new Scanner(connection.getInputStream());
                output = new Formatter(connection.getOutputStream());
            } catch (IOException ioException) {
                System.out.println(ioException.toString());
                System.exit(1);
            }
        }

        // wyślij wiadomość, że inny gracz się poruszył
        public void otherPlayerMoved(int location) {
            output.format("Przeciwnik poruszył się\n");
            output.format("%d\n", location); // wyślij lokalizację ruchu
            output.flush(); // wyślij infromację do klienta
            output.format(hasWinner() ? "PORAŻKA\n" : boardFilledUp() ? "REMIS\n" : "");
            output.flush();
        }

        // sterowanie wykonaniem wątku
        @Override
        public void run() {
            // wyślij klientowi jego znak (X lub O), przetwórz komunikaty od klienta
            try {
                displayMessage("Gracz " + mark + " połączył się\n");
                output.format("%s\n", mark); // wyślij znak gracza
                output.flush(); // wyślij infromację do klienta

                /**
                 * Poczekaj, aż oba gracze się połączą
                 */
                // jeśli gracz X, poczekaj na przybycie innego gracza
                if (playerNumber == PLAYER_X) {
                    output.format("%s\n%s", "Gracz X połączony", "Czekaj na innego gracza\n");
                    output.flush(); // wyślij infromację do klienta
                    gameLock.lock(); // zablokuj grę, aby czekała na drugiego gracza

                    try {
                        while (suspended) {
                            otherPlayerConnected.await(); // poczekaj na gracza O
                        }
                    } catch (InterruptedException exception) {
                        System.out.println(exception.toString());
                    } finally {
                        gameLock.unlock(); // odblokuj grę po połączeniu się drugiego gracza
                    }

                    // wyślij wiadomość, że inny gracz się połączył
                    output.format("Drugi gracz się połączył. Twój ruch.\n");
                    output.flush(); // wyślij infromację do klienta
                } else {
                    output.format("Gracz O podłączony, proszę czekać\n");
                    output.flush(); // wyślij infromację do klienta
                }

                // dopóki gra się nie kończy
                while (!isGameOver()) {
                    int location = 0; // zainicjuj lokalizację

                    if (input.hasNext()) {
                        location = input.nextInt(); // pobierz lokalizację przeniesienia
                    }
                    // sprawdź prawidłowy ruch
                    if (validateAndMove(location, playerNumber)) {
                        displayMessage("\nLokalizacja: " + location);
                        output.format("Prawidłowy ruch\n"); // powiadom klienta
                        output.flush(); // wyślij infromację do klienta
                        output.format(hasWinner() ? "ZWYCIĘSTWO\n" : boardFilledUp() ? "REMIS\n" : "");
                        output.flush();
                    } else {
                        // ruch był nieprawidłowy
                        output.format("Nieprawidłowy ruch, spróbuj ponownie\n");
                        output.flush(); // wyślij infromację do klienta
                    }
                }
            } finally {
                try {
                    connection.close(); // zamknięcie połączenia z klientem
                } catch (IOException ioException) {
                    System.out.println(ioException.toString());
                    System.exit(1);
                }
            }
        }

        // ustawia czy wątek ma być zawieszony
        public void setSuspended(boolean status) {
            suspended = status; // ustaw wartość zawieszonego wątku
        }
    }

}
