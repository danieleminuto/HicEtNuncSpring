package daniele.progetto_mongo.service;

import daniele.progetto_mongo.model.Utente;
import daniele.progetto_mongo.repository.UtenteRepository;
import daniele.progetto_mongo.utility.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service("utentiService")
public class UtentiService{

    @Autowired
    UtenteRepository user;

    @Transactional
    public Utente create(Utente usr ) throws EmailAlreadyRegistered {
        Optional<Utente> us=user.findById(usr.getEmail());
        if(!us.isEmpty()){
            throw new EmailAlreadyRegistered();
        }
        usr= gestisciGradoIstruzioneETitoli(usr);
        if(usr.getAltriTitoli()==null){
            usr.setAltriTitoli(new ArrayList<TitoloDiStudio>());
        }
       return user.save(usr);
    }

    @Transactional
    public Optional<Utente> update(String email, Utente usr) throws UserDoesNotExists {
        Optional<Utente> oldUser= user.findById(email);
        if(oldUser.isEmpty())
            throw new UserDoesNotExists();
        //ogni cambiamento presente in usr deve essere effettivo. mi conviene impostare in usr l'email corretto.
        usr.setEmail(oldUser.get().getEmail());

        //devo gestire anche il grado di istruzione
        usr= gestisciGradoIstruzioneETitoli(usr);

        user.save(usr);
        return Optional.of(usr);

    }

    @Transactional
    Utente gestisciGradoIstruzioneETitoli(Utente usr) {
        if (usr.getMaggiorTitoloDiStudio() != null){
            usr.getMaggiorTitoloDiStudio().setGradoIstruzione(usr.getMaggiorTitoloDiStudio().getIstruzione().ordinal());

            List<String> titoli = new ArrayList<String>();
            titoli.add(usr.getMaggiorTitoloDiStudio().getTitolo());
            if (usr.getAltriTitoli() != null) {
                for (TitoloDiStudio tds : usr.getAltriTitoli()) {
                    tds.setGradoIstruzione(tds.getIstruzione().ordinal());
                    if (!titoli.contains(tds.getTitolo()))
                        titoli.add(tds.getTitolo());
                }
            }
            usr.setNomiTitoli(titoli);
            return user.save(usr);
        }
        usr.setAltriTitoli(null);
        usr.setNomiTitoli(null);
        return user.save(usr);
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Iterable<Utente> getAll(){
        return user.findAll();
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<Utente> getById(String email) throws UserDoesNotExists {
        Optional<Utente> usr= user.findById(email);
        if(usr.isEmpty())
            throw new UserDoesNotExists();
        if(usr.get().getAltriTitoli()==null){
            usr.get().setAltriTitoli(new ArrayList<TitoloDiStudio>());
        }
        return usr;
    }

    @Transactional
    public boolean delete(String email) throws UserDoesNotExists {
        Optional<Utente> oldUser= user.findById(email);
        if(oldUser.isEmpty())
            throw new UserDoesNotExists();
        user.delete(oldUser.get());
        return true;
    }

    /** Aggiunge un titolo di studio, se è maggiore rispetto al MaggiorTitoloDiStudio Corrente
     * aggiorna il MaggiorTitoloDiStudio Corrente ---  Si può usare anche per modificare un titolo
     * di studio già esistente purchè titolo e grado rimangano invariati */
    @Transactional
    public Utente aggiungiTitoloDiStudio(TitoloDiStudio nTitolo, String email) throws UserDoesNotExists {
        Utente usr=user.findById(email).get();
        if(usr==null)
            throw new UserDoesNotExists();
        if(nTitolo.getIstruzione().ordinal()>=usr.getMaggiorTitoloDiStudio().getGradoIstruzione()){

            //sto aggiornando il maggior titolo

            //sto modificando l'attuale maggior titolo
            if((usr.getMaggiorTitoloDiStudio().getTitolo().equals(nTitolo.getTitolo()) && usr.getMaggiorTitoloDiStudio().getGradoIstruzione()==nTitolo.getIstruzione().ordinal()))
                usr.setMaggiorTitoloDiStudio(nTitolo);
            else {
                TitoloDiStudio tmp= usr.getMaggiorTitoloDiStudio();
                usr.setMaggiorTitoloDiStudio(nTitolo);
                usr.setAltriTitoli(inserisciAltroTitolo(usr.getAltriTitoli(),tmp));
            }
        }
        else {
            usr.setAltriTitoli(inserisciAltroTitolo(usr.getAltriTitoli(),nTitolo));
        }
        return gestisciGradoIstruzioneETitoli(usr);
    }

    /** controlla che il titolo che si vuole inserire non sia già presente, in caso affermativo lo
     * sostituisce con quello nuovo*/
    @Transactional
    List<TitoloDiStudio> inserisciAltroTitolo(List<TitoloDiStudio> altriTitoli, TitoloDiStudio nTitolo){
        if (altriTitoli== null)
            altriTitoli= new ArrayList<TitoloDiStudio>();
        TitoloDiStudio titoloDiStudio=null;

        for(TitoloDiStudio t: altriTitoli){
            if (t.getTitolo().equals(nTitolo.getTitolo()) && t.getGradoIstruzione()==nTitolo.getIstruzione().ordinal())
                titoloDiStudio=t;
        }
        if( titoloDiStudio != null){
            altriTitoli.remove(titoloDiStudio);
        }
        altriTitoli.add(nTitolo);
        return altriTitoli;

    }

    /** rimuove titolo di studio. Se è il maggiore, trova un altro MaggiorTitoloDiStudio */
    @Transactional
    public Utente rimuoviTitoloDiStudio(String email, String nomeTitolo, Istruzione istruzione) throws UserDoesNotExists, TitoloDiStudioDoesNotExists {
        Utente usr=user.findById(email).get();
        if(usr==null)
            throw new UserDoesNotExists();

        //si sta cercando di rimuovere il maggior titolo di studio
        TitoloDiStudio max=new TitoloDiStudio();
        max.setGradoIstruzione(-1);
        if(usr.getMaggiorTitoloDiStudio().getTitolo().equals(nomeTitolo) &&
                usr.getMaggiorTitoloDiStudio().getGradoIstruzione()== istruzione.ordinal()){

            usr.setMaggiorTitoloDiStudio(null);

            //devo trovare un nuovo maggior titolo di studio

            for(TitoloDiStudio t: usr.getAltriTitoli()) {
                if (max.getGradoIstruzione() < t.getIstruzione().ordinal()) {
                    max = t;
                    max.setGradoIstruzione(t.getIstruzione().ordinal());
                }
            }
            usr.setMaggiorTitoloDiStudio(max);
            usr.getAltriTitoli().remove(max);
        }
        else{
            TitoloDiStudio daEliminare=null;
            for(TitoloDiStudio t: usr.getAltriTitoli()){
                if(t.getTitolo().equals(nomeTitolo)&& istruzione.ordinal()==t.getGradoIstruzione())
                    daEliminare=t;
            }
            if(daEliminare==null)
                throw new TitoloDiStudioDoesNotExists();
            usr.getAltriTitoli().remove(daEliminare);
        }
        return gestisciGradoIstruzioneETitoli(usr);
    }
   
}
