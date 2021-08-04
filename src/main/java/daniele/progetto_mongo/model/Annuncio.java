package daniele.progetto_mongo.model;


import com.mongodb.lang.NonNull;
import daniele.progetto_mongo.utility.Contratto;
import daniele.progetto_mongo.utility.Istruzione;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@Document(collection = "Annunci")
public class Annuncio {
    @Id
    private String id;
    private String titolo;
    /* ricorda che questo non lo devi dare in input, devi farlo scegliere. è un' enum*/
    private Istruzione istruzioneRichiesta;
    /** grado di conoscenza permette di compiere query con operatori di $lt o $gt per
     * trovate i gli utenti che abbiano un'istruzione maggiore o uguale a quella richiesta.
     */
    private int gradoIstruzioneRichiesto;
    private String nomeDatore;
    private String emailDatore;
    private String requisiti;
    private Contratto contrattoOfferto;
    private int retribuzioneAnnua;
    private String descrizione;
    private int numCandidature;
    private String data;
    @Version Long version;
    }
