class Choose{
    private String link;
    public void category(int input){
        switch (input){
            case 1:{
                this.link = "https://v2.jokeapi.dev/joke/Programming?";
                break;
            }
            case 2:{
                this.link = "https://v2.jokeapi.dev/joke/Misc?";
                break;
            }
            case 3:{
                this.link = "https://v2.jokeapi.dev/joke/Dark?";
                break;
            }
            case 4:{
                this.link = "https://v2.jokeapi.dev/joke/Pun?";
                break;
            }
            case 5:{
                this.link = "https://v2.jokeapi.dev/joke/Spooky?";
                break;
            }
            case 6:{
                this.link = "https://v2.jokeapi.dev/joke/Christmast?";
                break;
            }
            case 7:{
                this.link = "https://v2.jokeapi.dev/joke/Any?";
                break;
            }
            default:{
                System.out.println("Nie ma takiej opcji");
                break;
            }
        }
    }
    public void languageAndCount(int ilosc, String jezyk){
        if(ilosc <= 0){
            System.out.println("Nie ma takiej opcji");
        }else if(ilosc > 10){
            System.out.println("Nie ma takiej opcji");
        }else if(ilosc == 1){
            if(jezyk.equals("es")){
                this.link = this.link+"lang=es";
            }else if(jezyk.equals("en")){
                this.link = this.link+"lang=en";
            }else if(jezyk.equals("de")){
                this.link = this.link+"lang=de";
            }else if(jezyk.equals("fr")){
                this.link = this.link+"lang=fr";
            }else if(jezyk.equals("pt")){
                this.link = this.link+"lang=pt";
            }else if(jezyk.equals("cs")){
                this.link = this.link+"lang=cs";
            } 
        }
        else{
            if(jezyk.equals("es")){
                this.link = this.link+"lang=es&amount="+ilosc;
            }else if(jezyk.equals("en")){
                this.link = this.link+"lang=en&amount="+ilosc;
            }else if(jezyk.equals("de")){
                this.link = this.link+"lang=de&amount="+ilosc;
            }else if(jezyk.equals("fr")){
                this.link = this.link+"lang=fr&amount="+ilosc;
            }else if(jezyk.equals("pt")){
                this.link = this.link+"lang=pt&amount="+ilosc;
            }else if(jezyk.equals("cs")){
                this.link = this.link+"lang=cs&amount="+ilosc;
            }            
        }
    }
    public String getLink() {
        return link;
    }
}