import java.util.Scanner;
public class Game {
    public BattleBoard board;
    public BattleBoard oppBoard;

    public Game(){
        board = new BattleBoard();
        oppBoard = new BattleBoard();
        Scanner scan = new Scanner(System.in);
        System.out.println("Czy chcesz losować swoją tablicę? (t/n): ");
        String answer = scan.nextLine();
        if (answer.equals("n")){
            board.shipAdd();
        }else{
            board.shipRandom();
        }
        System.out.println("Twoja plansza: ");
        board.printBoard();
        // scan.close();
    }

    public boolean gameOver(){
        if (board.remainingShips == 0){
            return true;
        }
        return false;
    }
}
