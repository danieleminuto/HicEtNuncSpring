package daniele.progetto_mongo.controller;

import daniele.progetto_mongo.model.Utente;
import daniele.progetto_mongo.service.UtentiService;
import daniele.progetto_mongo.authentication.*;
import daniele.progetto_mongo.utility.EmailAlreadyRegistered;
import daniele.progetto_mongo.utility.TitoloDiStudio;
import daniele.progetto_mongo.utility.TitoloDiStudioDoesNotExists;
import daniele.progetto_mongo.utility.UserDoesNotExists;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.token.TokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@CrossOrigin()
@RestController
@RequestMapping("/user")
public class UtentiController {

   @Autowired
    private UtentiService utente;


   @PreAuthorize("hasAuthority('User')")
   @PostMapping("/update")
    public ResponseEntity updateUser(@RequestBody Utente user){
       try {
           Utente ret= utente.update(Utils.getEmail(), user).get();
           return new ResponseEntity(ret, HttpStatus.OK);
       } catch (UserDoesNotExists userDoesNotExists) {
           return new ResponseEntity("User does not exists",HttpStatus.BAD_REQUEST);
       }
   }

    @PreAuthorize("hasAuthority('User')")
    @PostMapping("/update/addTitolo")
    public ResponseEntity addTitoloDiStudio(@RequestBody TitoloDiStudio titolo){

        try {
            Utente ret=utente.aggiungiTitoloDiStudio(titolo, Utils.getEmail());
            System.out.println(ret.toString());
            return new ResponseEntity(ret,HttpStatus.OK);
        } catch (UserDoesNotExists userDoesNotExists) {
            return new ResponseEntity("User does not exists",HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('User')")
    @PostMapping("/removeTitolo")
    public ResponseEntity removeTitoloDiStudio( @RequestBody TitoloDiStudio titoloDiStudio) {
        try {
           Utente ret= utente.rimuoviTitoloDiStudio(Utils.getEmail(), titoloDiStudio.getTitolo(), titoloDiStudio.getIstruzione());
           return new ResponseEntity(ret,HttpStatus.OK);
        } catch (UserDoesNotExists userDoesNotExists) {
            return new ResponseEntity("User does not exists",HttpStatus.BAD_REQUEST);
        } catch (TitoloDiStudioDoesNotExists titoloDiStudioDoesNotExists) {
            return new ResponseEntity("Titolo di studio does not exists",HttpStatus.BAD_REQUEST);
        }
    }


    @PreAuthorize("hasAuthority('User')")
    @PostMapping("/add")
    public ResponseEntity addUser(@RequestBody Utente user){
        user.setEmail(Utils.getEmail());
        try {
            Utente ret= utente.create(user);
            return new ResponseEntity(ret,HttpStatus.OK);
        } catch (EmailAlreadyRegistered emailAlreadyRegistered) {
            return new ResponseEntity("Email already in use",HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('User')")
    @GetMapping("/myRole")
    public ResponseEntity getRuolo(){
        return new ResponseEntity(true, HttpStatus.OK);
    }

    /* forse non deve essere usato
    @PreAuthorize("hasAuthority('User')")
    @GetMapping("/getAll")
    public ResponseEntity getUsers(){
        Iterable<Utente> ret= utente.getAll();
        return new ResponseEntity(ret, HttpStatus.OK);
    }

*/

    @PreAuthorize("hasAuthority('User')")
    @GetMapping("/get")
    public ResponseEntity getById(){
        try {
            Utente ret=utente.getById(Utils.getEmail()).get();
            return new ResponseEntity(ret, HttpStatus.OK);

        } catch (UserDoesNotExists userDoesNotExists) {
            return new ResponseEntity("User does not exists",HttpStatus.BAD_REQUEST);
        }

    }



    @PreAuthorize("hasAuthority('User')")
    @DeleteMapping("/delete/")
    public ResponseEntity deleteUser(){
        try {
            boolean ret= utente.delete(Utils.getEmail());
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (UserDoesNotExists userDoesNotExists) {
            return new ResponseEntity("User does not exists",HttpStatus.BAD_REQUEST);
        }
    }



    @GetMapping("/getId")
    public String getId(){
        return Utils.getAuthServerId();
    }


}
