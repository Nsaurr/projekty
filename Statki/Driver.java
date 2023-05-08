import java.util.Scanner;
import java.net.*;

public class Driver {
    public static void main(String[] args) throws UnknownHostException {
        Scanner scan = new Scanner(System.in);
        System.out.println("Witamy w Statkach autorstwa Mateusza Suchockiego i Mateusza Jagieły");
        System.out.print("Proszę wpisać swoją nazwę: ");
        String name = scan.nextLine();
        System.out.println("Witaj " + name);
        boolean isHost = false;
        while (true) {
            System.out.print("Czy chcesz hostować grę lub połączyć się z nią? (Wpisz \"host\" lub \"connect\"): ");
            String type = scan.nextLine(); 
            if (type.equals("host")) {
                isHost = true;
                break;
            } else if (type.equals("connect")) {
                break;
            } else {
                System.out.println("Błąd — wprowadź prawidłową opcję");
            }
        }

        if (isHost) {
            Server s = new Server(name);
        } else {
            System.out.print("Wprowadź informacje dotyczące gry, z którą chcesz się połączyć (ip:port): ");
            String[] temp = scan.nextLine().split(":");
            String ip = temp[0];
            int port = Integer.parseInt(temp[1]);
            Client c = new Client(ip, port, name);
        }
        

        scan.close();
    }
}