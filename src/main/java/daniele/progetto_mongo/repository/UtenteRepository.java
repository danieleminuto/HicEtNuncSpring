package daniele.progetto_mongo.repository;


import daniele.progetto_mongo.model.Utente;
import daniele.progetto_mongo.utility.TitoloDiStudio;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Repository
public interface UtenteRepository extends MongoRepository<Utente,String> {

    List<Utente> findByNomeAndMaggiorTitoloDiStudio(String nome, TitoloDiStudio ts);


//++++++++++++++++++++++++++++++++++++++++++ Employer Cerca Utente +++++++++++++++++++++++++++++++++++++//

    /** Ricerca base per parola chiave **/
    @Query(value="{$or:[{lavoroIdeale: {$regex: /.*?0.*/, $options: 'i'}}," +
            "{nomiTitoli: {$regex: /.*?0.*/, $options: 'i'}}]}")
    List<Utente> findByKeyword(String keyword, Pageable pageable);

    /** Ricerca per annuncio: "abbiamo trovato un utente idoneo alla posizione" **/
    @Query(value ="{$or:[{lavoroIdeale: {$regex: /.*?0.*/, $options: 'i'}}," +
            "{nomiTitoli: {$regex: /.*?0.*/, $options: 'i'}}], 'maggiorTitoloDiStudio.gradoIstruzione': {$gte: ?1 }}" )
    List<Utente> findByJob(String titoloAnnuncio, int gradoIstruzioneRichiesto, Pageable pageable);
}
