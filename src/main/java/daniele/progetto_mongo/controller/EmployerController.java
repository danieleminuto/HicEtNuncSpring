package daniele.progetto_mongo.controller;

import daniele.progetto_mongo.authentication.*;
import daniele.progetto_mongo.model.Annuncio;
import daniele.progetto_mongo.model.Employer;
import daniele.progetto_mongo.service.EmployerService;
import daniele.progetto_mongo.utility.AnnuncioDoesntExists;
import daniele.progetto_mongo.utility.Costanti;
import daniele.progetto_mongo.utility.EmailAlreadyRegistered;
import daniele.progetto_mongo.utility.EmployerDoesnotExists;
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
@RequestMapping("/employer")
public class EmployerController {
    @Autowired
    private EmployerService employer;


    @PreAuthorize("hasAuthority('Employer')")
    @PostMapping("/add")
    public ResponseEntity addEmployer(@RequestBody Employer empl) {
        empl.setEmail(Utils.getEmail());
        try {
            Employer ret = employer.create(empl);
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (EmailAlreadyRegistered emailAlreadyRegistered) {
            return new ResponseEntity("Email already used", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('Employer')")
    @GetMapping("/get")
    public ResponseEntity getById() {
        try {
            Employer ret = employer.findByEmail(Utils.getEmail()).get();
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (EmployerDoesnotExists employerDoesnotExists) {
            return new ResponseEntity("Employer does not exists", HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/getAll")
    public Iterable<Employer> getAll() {

        return employer.getAll();
    }

    @PreAuthorize("hasAuthority('Employer')")
    @GetMapping("/myRole")
    public ResponseEntity getRuolo(){
        return new ResponseEntity(true, HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('Employer')")
    @PostMapping("/update")
    public ResponseEntity updateUser(@RequestBody Employer empl) {
        try {
            Employer ret = employer.update(Utils.getEmail(), empl).get();
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (EmployerDoesnotExists employerDoesnotExists) {
            return new ResponseEntity("Employer does not Exists", HttpStatus.BAD_REQUEST);
        }

    }

    @PreAuthorize("hasAuthority('Employer')")
    @DeleteMapping("/delete")
    public ResponseEntity deleteUser() {
        try {
            boolean ret = employer.delete(Utils.getEmail());
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (EmployerDoesnotExists employerDoesnotExists) {
            return new ResponseEntity("Employer does not exists", HttpStatus.BAD_REQUEST);
        }
    }

// ++++++++++++++++++++++++ gestione annunci +++++++++++++++++++++++++++++//

    @PreAuthorize("hasAuthority('Employer')")
    @PostMapping("/addAnnuncio")
    public ResponseEntity addAnnuncio(@RequestBody Annuncio ann) {
        ann.setEmailDatore(Utils.getEmail());
        try {
            Employer ret = employer.addAnnuncio(ann, Utils.getEmail()).get();
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (EmployerDoesnotExists employerDoesnotExists) {
            return new ResponseEntity("Employer does not exists", HttpStatus.BAD_REQUEST);
        }
    }


    //NON IMPLEMENTATO FRONT
    @PreAuthorize("hasAuthority('Employer')")
    @PostMapping("/updateAnnuncio/{idOld}")
    public ResponseEntity updateAnnuncio(@PathVariable String idOld, @RequestBody Annuncio ann) {
        try {
            Employer ret = null;
            ret = employer.updateAnnuncio(idOld, ann, Utils.getEmail()).get();
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (AnnuncioDoesntExists annuncioDoesntExists) {
            return new ResponseEntity("ANNUNCIO_DOES_NOT_EXISTS", HttpStatus.BAD_REQUEST);
        } catch (EmployerDoesnotExists employerDoesnotExists) {
            return new ResponseEntity("Employer does not exists", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('Employer')")
    @GetMapping("/deleteAnnuncio/{idAnnuncio}")
    public ResponseEntity removeAnnuncio(@PathVariable String idAnnuncio) {
        try {
            Boolean ret = employer.removeAnnuncio(idAnnuncio, Utils.getEmail());
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (AnnuncioDoesntExists annuncioDoesntExists) {
            return new ResponseEntity("ANNUNCIO_DOES_NOT_EXISTS", HttpStatus.BAD_REQUEST);
        }
    }

    @PreAuthorize("hasAuthority('Employer')")
    @GetMapping("/getAnnunci/{p}")
    public ResponseEntity getAnnunci(@PathVariable int p) {
        try {
            Pageable page= PageRequest.of(p, Costanti.RISULTATIPERPAGINA);
            List<Annuncio> ret = employer.getAnnunci(Utils.getEmail(),page);
            return new ResponseEntity(ret, HttpStatus.OK);
        } catch (EmployerDoesnotExists employerDoesnotExists) {
            return new ResponseEntity("Employer does not exists", HttpStatus.BAD_REQUEST);
        }
    }
}
