package daniele.progetto_mongo.service;

import daniele.progetto_mongo.model.Annuncio;
import daniele.progetto_mongo.repository.AnnuncioRepository;
import daniele.progetto_mongo.utility.AnnuncioDoesntExists;
import daniele.progetto_mongo.utility.Contratto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service("annunciService")
public class AnnuncioService {
    @Autowired
    AnnuncioRepository annuncio;

    @Transactional
    public Annuncio create(Annuncio ann ){
        ann.setData(new Date().toString());
        if(ann.getContrattoOfferto()==null)
            ann.setContrattoOfferto(Contratto.NS);
        ann.setGradoIstruzioneRichiesto(ann.getIstruzioneRichiesta().ordinal());
        return annuncio.save(ann);
    }

    @Transactional
    public Optional<Annuncio> update(String id, Annuncio ann) throws AnnuncioDoesntExists {
        Optional<Annuncio> oldAnn= annuncio.findById(id);
        ann.setData(oldAnn.get().getData());
        if(oldAnn.isEmpty())
            throw new AnnuncioDoesntExists();
        //ogni cambiamento presente in ann deve essere effettivo. mi conviene impostare in usr l'id corretto.
        ann.setId(oldAnn.get().getId());
        ann.setData(oldAnn.get().getData());
        annuncio.delete(oldAnn.get());
        annuncio.save(ann);
        return Optional.of(ann);

    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Iterable<Annuncio> getAll(){

        return annuncio.findAll();
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<Annuncio> findById(String id) throws AnnuncioDoesntExists {
        Optional<Annuncio> oldAnn= annuncio.findById(id);
        if(oldAnn.isEmpty())
            throw new AnnuncioDoesntExists();
        return oldAnn;
    }

    @Transactional
    public boolean remove(String id) throws AnnuncioDoesntExists {
        Optional<Annuncio> oldAnn= annuncio.findById(id);
        if(oldAnn.isEmpty())
            throw new AnnuncioDoesntExists();
        annuncio.delete(oldAnn.get());
        return true;
    }

    @Transactional
    public Annuncio candidati(Annuncio job) throws AnnuncioDoesntExists {
        Annuncio ann=findById(job.getId()).get();
        ann.setNumCandidature( ann.getNumCandidature()+1 );
        annuncio.save(ann);
        return ann;
    }


}
