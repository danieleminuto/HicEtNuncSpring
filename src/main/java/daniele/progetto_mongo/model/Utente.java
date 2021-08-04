package daniele.progetto_mongo.model;


import com.mongodb.annotations.NotThreadSafe;
import com.mongodb.lang.NonNull;
import daniele.progetto_mongo.utility.Contratto;
import daniele.progetto_mongo.utility.TitoloDiStudio;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString
@Document(collection = "Utenti")
public class Utente {

    //++++++ anagrafica +++++/
    @Id
    private String email;
    @NonNull
    private String nome;
    @NonNull
    private String cognome;
    private String dataDiNascita;

//  private String cellulare;
    private String cittaResidenza;
    private String descrizione;

    //+++++++++ istruzione ++++++++//
    @NonNull
    private TitoloDiStudio maggiorTitoloDiStudio;
    private List<TitoloDiStudio> altriTitoli;
    private List<String> nomiTitoli;

    //+++++++++ lavoro +++++++++//
    private String lavoroIdeale;
    private Contratto contrattoDesiderato;



}
