import java.io.FileWriter;

class SaveToFile{
    public void saveToFile(String str){
        
        try {
            FileWriter output
                = new FileWriter("zarty.txt");
            output.write(str);
            System.out.println("Pomyślnie zapisano");
            output.close();
        }
        catch (Exception e) {
            e.getStackTrace();
        }
    }
}
