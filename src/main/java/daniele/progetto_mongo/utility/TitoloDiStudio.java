package daniele.progetto_mongo.utility;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TitoloDiStudio {
    private String titolo="";

    private Istruzione istruzione;
    /** utile solo per MaggiorTitoloDiStudio */
    private int gradoIstruzione;
    private int voto;
    private boolean inCorso;
    private String istituto;
    private String annoInizio;
    private String annoFine;

    @Override
    public String toString() {
        return "TitoloDiStudio{" +
                "titolo='" + titolo + '\'' +
                ", istruzione=" + istruzione +
                ", gradoIstruzione=" + gradoIstruzione +
                ", voto=" + voto +
                ", inCorso=" + inCorso +
                ", istituto='" + istituto + '\'' +
                ", dataInizio='" + annoInizio + '\'' +
                ", dataFine='" + annoFine + '\'' +
                '}';
    }
}


