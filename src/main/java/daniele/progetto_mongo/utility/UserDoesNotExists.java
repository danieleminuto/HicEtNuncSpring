package daniele.progetto_mongo.utility;

public class UserDoesNotExists extends Exception{
    public UserDoesNotExists(){
        System.out.println("Utente non esiste");
    }
}
