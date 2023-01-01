import java.util.Scanner;

class Joke{
    public static void main(String[] args) {
        SaveToFile file = new SaveToFile();
        GetJoke getJoke = new GetJoke();
        Choose choose = new Choose();
        Scanner sc = new Scanner(System.in);
        System.out.println("Wybierz kategorię: 1.Programming 2.Misc 3.Dark 4.Pun 5.Spooky 6.Christmast 7.Losowa");
        int input = sc.nextInt();
        choose.category(input);
        System.out.println("Podaj ilość żartów max: 10 :");
        int ilosc = sc.nextInt();
        System.out.println("Wybierz język cs-Czeski en-Hiszpański de-Niemiecki en-Angielski fr-Francuski pt-Portugalski: ");
        String jezyk = sc.next();
        choose.languageAndCount(ilosc, jezyk);
        getJoke.getJokes(choose.getLink());
        System.out.println(getJoke.getResult());
        System.out.println("Czy chcesz zapisać swoje żarty do pliku 1.Tak 2.Nie: ");
        int wybor = sc.nextInt();
        if(wybor == 1){
            file.saveToFile(getJoke.getResult());
        }else{
            System.out.println("Koniec programu");
        }
    }
}