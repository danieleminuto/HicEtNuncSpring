package daniele.progetto_mongo.controller;

import daniele.progetto_mongo.model.Annuncio;

import daniele.progetto_mongo.service.AnnuncioService;
import daniele.progetto_mongo.utility.AnnuncioDoesntExists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin()
@RestController
@RequestMapping("/job")
public class AnnuncioController {

    @Autowired
    AnnuncioService annuncio;

    @PreAuthorize("hasAuthority('User')")
    @PostMapping("/candidatura")
    public  ResponseEntity candidati(@RequestBody Annuncio ann){
        try {
            Annuncio ret = annuncio.candidati(ann);
            return new ResponseEntity<>(ret, HttpStatus.OK);
        } catch (AnnuncioDoesntExists annuncioDoesntExists) {
            return new ResponseEntity<>("ANNUNCIO_DOES_NOT_EXISTS", HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasAuthority('Employer')")
    @PostMapping("/update/{id}")
    public ResponseEntity updateJob(@PathVariable String id, @RequestBody Annuncio ann){
        try {
           Annuncio ret= annuncio.update(id, ann).get();
           return new ResponseEntity(ret, HttpStatus.OK);
        }catch (AnnuncioDoesntExists e){
            return new ResponseEntity<>("ANNUNCIO_DOES_NOT_EXISTS", HttpStatus.BAD_REQUEST);
        }
    }

    /*  Un ANNUNCIO può essere creato solo tramite Employer
    @PreAuthorize("hasAuthority('Employer')")
    @PutMapping("/add")
    public Annuncio addJob(@RequestBody Annuncio ann){
        return annuncio.create(ann);
    }
    */

    // POTREBBE SERVIRE
    @GetMapping("/getAll")
    public ResponseEntity getJobs(){
        Iterable<Annuncio> ret= annuncio.getAll();
        return new ResponseEntity(ret, HttpStatus.OK);
    }


    @PreAuthorize("hasAuthority('Employer')")
    @GetMapping("/get/{id}")
    public ResponseEntity getById(@PathVariable String id){
        try {
            Annuncio ret= annuncio.findById(id).get();
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (AnnuncioDoesntExists annuncioDoesntExists) {
            return new ResponseEntity("ANNUNCIO_DOES_NOT_EXISTS", HttpStatus.BAD_REQUEST);
        }
    }


    @PreAuthorize("hasAuthority('Employer')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteJob(@PathVariable String id){
        try {
            boolean ret = annuncio.remove(id);
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (AnnuncioDoesntExists annuncioDoesntExists) {
            return new ResponseEntity("ANNUNCIO_DOES_NOT_EXISTS", HttpStatus.BAD_REQUEST);
        }
    }

}
