package daniele.progetto_mongo.utility;

public class EmailAlreadyRegistered extends Exception{
    public EmailAlreadyRegistered(){
        System.out.println("L'email è gia in uso");
    }
}
