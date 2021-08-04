package daniele.progetto_mongo.service;

import daniele.progetto_mongo.model.Annuncio;
import daniele.progetto_mongo.model.Utente;
import daniele.progetto_mongo.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("ricerca Utente")
public class RicercaUtenteService {

    @Autowired
    UtenteRepository utente;

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Utente> findbyKeyword(String keyword, Pageable pageable){
       return utente.findByKeyword(keyword, pageable);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Utente> findByJob(Annuncio annuncio, Pageable pageable){
        return utente.findByJob(annuncio.getTitolo(), annuncio.getGradoIstruzioneRichiesto(), pageable);
    }
}

