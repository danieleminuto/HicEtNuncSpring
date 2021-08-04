package daniele.progetto_mongo.service;

import daniele.progetto_mongo.model.Annuncio;
import daniele.progetto_mongo.model.Employer;
import daniele.progetto_mongo.model.Utente;
import daniele.progetto_mongo.repository.EmployerRepository;
import daniele.progetto_mongo.utility.AnnuncioDoesntExists;
import daniele.progetto_mongo.utility.EmailAlreadyRegistered;
import daniele.progetto_mongo.utility.EmployerDoesnotExists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class EmployerService {

    @Autowired
    EmployerRepository employer;

    @Autowired
    AnnuncioService annuncio;

    @Transactional
    public Employer create(Employer empl ) throws EmailAlreadyRegistered {
        Optional<Employer> emp=employer.findById(empl.getEmail());
        if(!emp.isEmpty())
            throw new EmailAlreadyRegistered();
        return employer.save(empl);
    }

    /** Metodo non adatto ad agire sugli annunci, usare i metodi preposti */
    @Transactional
    public Optional<Employer> update(String email, Employer modificato) throws EmployerDoesnotExists {
        Optional<Employer> oldEmp= employer.findById(email);
        if(oldEmp.isEmpty())
            throw new EmployerDoesnotExists();
        //ogni cambiamento presente in modificato deve essere effettivo. mi conviene impostare in modificato l'email corretta.
        modificato.setEmail(oldEmp.get().getEmail());

        //la lista degli annunci deve essere la stessa
        modificato.setAnnunciPubblicati(oldEmp.get().getAnnunciPubblicati());

        employer.save(modificato);
        return Optional.of(modificato);

    }

    @Transactional
    public Iterable<Employer> getAll(){

        return employer.findAll();
    }


    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public List<Annuncio> getAnnunci(String emailEmployer, Pageable page) throws EmployerDoesnotExists {
        Employer empl=employer.findById(emailEmployer).get();
        if(empl==null){
            throw new EmployerDoesnotExists();
        }
        List<Annuncio> ret=null;
                if(empl.getAnnunciPubblicati().size()>page.getPageNumber()* page.getPageSize())
                    ret=empl.getAnnunciPubblicati().size()>= page.getPageNumber()* page.getPageSize()+page.getPageSize()?
                            empl.getAnnunciPubblicati().subList(page.getPageNumber()* page.getPageSize(),page.getPageNumber()* page.getPageSize()+page.getPageSize())
                            : empl.getAnnunciPubblicati().subList(page.getPageNumber()* page.getPageSize(),empl.getAnnunciPubblicati().size());
        return ret==null? new ArrayList<Annuncio>(): ret;
    }

    @Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
    public Optional<Employer> findByEmail(String email) throws EmployerDoesnotExists {
        Optional<Employer> oldEmp= employer.findById(email);
        if(oldEmp.isEmpty())
            throw new EmployerDoesnotExists();
        return oldEmp;
    }

    @Transactional
    public boolean delete(String email) throws EmployerDoesnotExists {
        Optional<Employer> oldEmp= employer.findById(email);
        if(oldEmp.isEmpty())
            throw new EmployerDoesnotExists();
        //bisogna rimuovere anche gli annunci
        List<Annuncio> annunciPubblicati=oldEmp.get().getAnnunciPubblicati();
        if(annunciPubblicati!=null)
            for(Annuncio a: annunciPubblicati) {
                try {
                    annuncio.remove(a.getId());
                } catch (AnnuncioDoesntExists annuncioDoesntExists) {
                    continue; //comunque devo continuare ad eliminare gli altri annunci, non posso fermarmi
                }
            }
        employer.delete(oldEmp.get());
        return true;
    }

    // ++++++++++++++++ Metodi per la gestione degli annunci ++++++++++++++++//

    /** aggiunge un annuncio e lo salva tra gli annunci dell'employer */

    @Transactional
    public Optional<Employer> addAnnuncio(Annuncio ann, String email) throws EmployerDoesnotExists {
        // bisogna aggiungerlo tra gli annunci generali
        ann=annuncio.create(ann);

        // bisogna aggiungerlo tra gli annunci del datore
        Optional<Employer> emp= findByEmail(email);
        if(emp.get().getAnnunciPubblicati()==null){
            emp.get().setAnnunciPubblicati(new ArrayList<Annuncio>());
        }
        emp.get().getAnnunciPubblicati().add(ann);
        return Optional.of(employer.save(emp.get()));
    }

    /** modifica un annuncio*/
    @Transactional
    public Optional<Employer> updateAnnuncio(String idAnnuncioOld, Annuncio ann, String email) throws AnnuncioDoesntExists, EmployerDoesnotExists {
        Optional<Annuncio> olDAnn= annuncio.findById(idAnnuncioOld);
        Optional<Employer> emp= findByEmail(email);
        ann.setId(olDAnn.get().getId());
        ann.setData(olDAnn.get().getData());
        //salvo tra gli annunci generali//
        annuncio.update(idAnnuncioOld, ann);

        //modifico la lista dell'employer//
        Annuncio daRimuovere=null;
        for(Annuncio a: emp.get().getAnnunciPubblicati()){
            if(a.getId().equals(idAnnuncioOld)){
                daRimuovere= a;
                break;
            }
        }
        if(daRimuovere!=null)
            emp.get().getAnnunciPubblicati().remove(daRimuovere);
        emp.get().getAnnunciPubblicati().add(ann);

        return emp;


    }

    @Transactional
    public boolean removeAnnuncio(String idAnnuncio, String email) throws AnnuncioDoesntExists {
        return annuncio.remove(idAnnuncio);
    }
}
