package daniele.progetto_mongo.service;

import daniele.progetto_mongo.model.Annuncio;
import daniele.progetto_mongo.model.Utente;
import daniele.progetto_mongo.repository.AnnuncioRepository;

import daniele.progetto_mongo.utility.Contratto;
import daniele.progetto_mongo.utility.FiltriAnnunci;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.PageFormat;
import org.springframework.data.domain.Pageable;
import java.awt.print.Printable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service("ricerca")
public class RicercaAnnuncioService {

    @Autowired
    AnnuncioRepository annuncio;



    // NB. QUANDO CARICHI UNA RICERCA NEL FRONT END FAI IN MODO CHE NELLA
    // BARRA DI RICERCA RIMAGA LA PAROLA CHIAVE
    /** Data una parola chiave compie una ricerca */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Annuncio> ricercaAnnuncioPerParolaChiave(String keyword, Pageable pageable){
        return annuncio.findAnnuncioByKeyword(keyword, pageable);
    }


    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Annuncio> findByDesire(Utente user, Pageable pageable){

        /*se l'utente ha specificato un tipo di contratto desiderato, devo restituie solo gli annunci
         * che offrono quel contratto */
        if(user.getContrattoDesiderato()==Contratto.NS)
            return annuncio.findByDesire(user.getMaggiorTitoloDiStudio().getTitolo(),
                    user.getLavoroIdeale(),
                    user.getMaggiorTitoloDiStudio().getGradoIstruzione(),pageable);

        return annuncio.findByDesire(user.getMaggiorTitoloDiStudio().getTitolo(),
                user.getLavoroIdeale(),
                user.getMaggiorTitoloDiStudio().getGradoIstruzione(),
                user.getContrattoDesiderato().name(),pageable);
    }



    /** Si applicano dei filtri alla ricerca per keyword. I filtri previsti sono:
     * tipo di contratto e titolo di studio.
     */
    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Annuncio> filtraRisultati(FiltriAnnunci filter, String keyword,Pageable pageable){
        if(filter.isSceltaContratti() && !filter.isSceltaIstruzione()){
           return annuncio.findByContrattiandKeyWord(keyword,filter.getContrattiAccettati(),pageable);
        }
        else if(!filter.isSceltaContratti() && filter.isSceltaIstruzione()){
            return annuncio.findByIstruzioniandKeyWord(keyword,filter.getIstruzioneAccettata(),pageable);
        }
        else if(filter.isSceltaContratti() && filter.isSceltaIstruzione()){
            return annuncio.findByContrattiandIstruzioneAndKeyWord(keyword,filter.getContrattiAccettati(),filter.getIstruzioneAccettata(),pageable);
        }
        return ricercaAnnuncioPerParolaChiave(keyword, pageable);
    }



}
