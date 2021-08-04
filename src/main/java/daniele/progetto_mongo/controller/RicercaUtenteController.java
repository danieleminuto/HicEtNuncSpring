package daniele.progetto_mongo.controller;


import daniele.progetto_mongo.model.Utente;
import daniele.progetto_mongo.service.AnnuncioService;
import daniele.progetto_mongo.service.RicercaUtenteService;
import daniele.progetto_mongo.utility.AnnuncioDoesntExists;
import daniele.progetto_mongo.utility.Costanti;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;
import java.util.List;

@CrossOrigin()
@RestController
@RequestMapping("/cercaUtente")
public class RicercaUtenteController{

    @Autowired
    RicercaUtenteService utente;
    @Autowired
    AnnuncioService annuncio;

    @PreAuthorize("hasAuthority('Employer')")
    @GetMapping("/{keyword}/{p}")
    public ResponseEntity findByKeyword(@PathVariable String keyword, @PathVariable int p){
        Pageable page= PageRequest.of(p, Costanti.RISULTATIPERPAGINA);
        List<Utente> ret= utente.findbyKeyword(keyword,page);
        return new ResponseEntity(ret, HttpStatus.OK);
    }

    /** è richiesto l'id dell'annuncio */
    @PreAuthorize("hasAuthority('Employer')")
    @GetMapping("/idoneo/{id}/{p}")
    public ResponseEntity findByJob(@PathVariable String id,@PathVariable int p){
        try {
            Pageable page= PageRequest.of(p, Costanti.RISULTATIPERPAGINA);
            List<Utente> ret = utente.findByJob(annuncio.findById(id).get(),page);
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (AnnuncioDoesntExists annuncioDoesntExists) {
            return new ResponseEntity("ANNUNCIO_DOES_NOT_EXISTS", HttpStatus.BAD_REQUEST);
        }
    }
}
