package daniele.progetto_mongo.repository;

import daniele.progetto_mongo.model.Annuncio;
import daniele.progetto_mongo.utility.Contratto;
import daniele.progetto_mongo.utility.Istruzione;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.BasicQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;


@Repository
public interface AnnuncioRepository extends MongoRepository<Annuncio, String> {

// ++++++++++++++++++++++++++++++++ Utente cerca Annuncio ++++++++++++++++++++++++++++++++++++++++++++//

        /** Ricerca Base: restituisce come risultati tutti gli articoli in cui la keyword (key insensitive)
         * fa parte del campo titolo o della descrizione */
    @Query(value="{$or:[{titolo: {$regex: /.*?0.*/, $options: 'i'}}," +
            "{descrizione: {$regex: /.*?0.*/, $options: 'i'}}]}")
    List<Annuncio> findAnnuncioByKeyword(String keyword, Pageable pageable);


    /** Ricerca per affinità: "pensiamo possa interessati questa posizione"
     * (in or) si cerca il nome del titolo di studio dell' utente o il nome del lavoro desiderato in titolo e descrizione
     * dell'annuncio. L'annuncio viene restituito se il proprio grado di istruzione è idoneo a quello richiesto.
     */
    @Query(value = "{$or:[{titolo: {$regex: /.*?0.*/, $options: 'i'}},{descrizione: {$regex: /.*?0.*/, $options: 'i'}}," +
            "{titolo: {$regex: /.*?1.*/, $options: 'i'}},{descrizione: {$regex: /.*?1.*/, $options: 'i'}}]," +
            "gradoIstruzioneRichiesto: {$lte : ?2}}")
    List<Annuncio> findByDesire(String keywordUtente, String keywordLavoroDesiderato, int gradoIstruzione, Pageable pageable);

    @Query(value = "{$or:[{titolo: {$regex: /.*?0.*/, $options: 'i'}},{descrizione: {$regex: /.*?0.*/, $options: 'i'}}," +
            "{titolo: {$regex: /.*?1.*/, $options: 'i'}},{descrizione: {$regex: /.*?1.*/, $options: 'i'}}]," +
            "gradoIstruzioneRichiesto: {$lte : ?2}, contrattoDesiderato: ?3}")
    List<Annuncio> findByDesire(String keywordUtente, String keywordLavoroDesiderato, int gradoIstruzione, String contrattoDesiderato, Pageable pageable);


    // ++++++++++++++++++++++++++++++ RICERCA PER FILTRI ++++++++++++++++++++++++++++++++//

    @Query(value = "{$or:[{titolo: {$regex: /.*?0.*/, $options: 'i'}},{descrizione: {$regex: /.*?0.*/, $options: 'i'}},]," +
            "istruzioneRichiesta: {$in: ?1}}")
    List<Annuncio> findByIstruzioniandKeyWord(String titolo,List<Istruzione> list, Pageable pageable);

    @Query(value = "{$or:[{titolo: {$regex: /.*?0.*/, $options: 'i'}},{descrizione: {$regex: /.*?0.*/, $options: 'i'}},]," +
            "contrattoOfferto: {$in: ?1}}")
    List<Annuncio> findByContrattiandKeyWord(String titolo,List<Contratto> list, Pageable pageable);

    @Query(value = "{$or:[{titolo: {$regex: /.*?0.*/, $options: 'i'}},{descrizione: {$regex: /.*?0.*/, $options: 'i'}},]," +
            "contrattoOfferto: {$in: ?1},istruzioneRichiesta: {$in: ?2} }")
    List<Annuncio> findByContrattiandIstruzioneAndKeyWord(String titolo,List<Contratto> list,List<Istruzione> list2, Pageable pageable);
}

