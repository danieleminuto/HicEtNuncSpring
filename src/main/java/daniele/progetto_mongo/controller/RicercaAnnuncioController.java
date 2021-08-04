package daniele.progetto_mongo.controller;

import daniele.progetto_mongo.authentication.*;
import daniele.progetto_mongo.model.Annuncio;
import daniele.progetto_mongo.model.Utente;
import daniele.progetto_mongo.service.RicercaAnnuncioService;
import daniele.progetto_mongo.service.UtentiService;
import daniele.progetto_mongo.utility.Costanti;
import daniele.progetto_mongo.utility.FiltriAnnunci;
import daniele.progetto_mongo.utility.UserDoesNotExists;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Optional;

/** classe che specifica le ricerche compiute dall'utente semplice che vuole trovare un annuncio */

@CrossOrigin()
@RestController
@RequestMapping("/cercaAnnuncio")
public class RicercaAnnuncioController {
    @Autowired
    RicercaAnnuncioService finder;

    @Autowired
    UtentiService user;

    @PreAuthorize("hasAuthority('User')")
    @GetMapping("/{keyword}/{p}")
    public ResponseEntity ricercaPerParolaChiave(@PathVariable String keyword,@PathVariable int p){
        Pageable page= PageRequest.of(p, Costanti.RISULTATIPERPAGINA);
        List<Annuncio> ret=finder.ricercaAnnuncioPerParolaChiave(keyword,page);
        return new ResponseEntity(ret, HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('User')")
    @GetMapping("/desire/{p}")
    public ResponseEntity ricercaPerAffinità(@PathVariable int p){
        Pageable page= PageRequest.of(p, Costanti.RISULTATIPERPAGINA);
        Optional<Utente> usr= null;
        try {
            usr = user.getById(Utils.getEmail());
        } catch (UserDoesNotExists userDoesNotExists) {
            return new ResponseEntity("User does not exists",HttpStatus.BAD_REQUEST);
        }
        List<Annuncio>ret= finder.findByDesire(usr.get(),page);
        return new ResponseEntity(ret, HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('User')")
    @PostMapping("/filtered/{keyword}/{p}")
    public ResponseEntity ricercaConFiltri(@RequestBody FiltriAnnunci filtri, @PathVariable String keyword,@PathVariable int p){
        Pageable page= PageRequest.of(p, Costanti.RISULTATIPERPAGINA);
        System.out.println(filtri.toString()+" and "+keyword);
        List<Annuncio> ret= finder.filtraRisultati(filtri, keyword, page);
        return new ResponseEntity(ret, HttpStatus.OK);
    }


}
