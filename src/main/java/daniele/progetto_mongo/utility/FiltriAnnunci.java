package daniele.progetto_mongo.utility;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Setter
@Getter
public class FiltriAnnunci {
    private List<Contratto> contrattiAccettati;
    private boolean sceltaContratti;
    private List<Istruzione> istruzioneAccettata;
    private boolean sceltaIstruzione;


    public void setContrattiAccettati(List<Contratto> contratti){
        this.contrattiAccettati=contratti;
        this.sceltaContratti=true;
    }

    public void setIstruzioneAccettata(List<Istruzione> istruzioneAccettata){
        this.istruzioneAccettata =istruzioneAccettata;
        sceltaIstruzione =true;
    }




}
