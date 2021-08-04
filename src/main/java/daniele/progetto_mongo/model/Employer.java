package daniele.progetto_mongo.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Setter
@Getter
@Document(collection = "Employers")
public class Employer {

    @Id
    String email;       //non deve essere modificata
    String nome;
    String cognome;
    String azienda;
    @DBRef
    List<Annuncio> annunciPubblicati;
}
