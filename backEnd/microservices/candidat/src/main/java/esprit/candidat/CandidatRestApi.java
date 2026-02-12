package esprit.candidat;

import java.util.List;
import esprit.candidat.entities.Candidat;
import esprit.candidat.repositories.CandidatRepository;
import esprit.candidat.services.CandidatService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("mic1/candidats")
public class CandidatRestApi {

    @Autowired
    private CandidatRepository candidatRepository;

    @Autowired
    private CandidatService candidatService;

    // 1. GET all candidats
    @GetMapping
    public List<Candidat> getAllCandidats() {
        return candidatRepository.findAll();
    }

    // 2. GET candidat by ID
    @GetMapping("/{id}")
    public Candidat getCandidatById(@PathVariable int id) {
        return candidatRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidat non trouvé avec id: " + id));
    }

    // 3. GET candidat by Name (recherche par nom)
    @GetMapping("/search/candidatByNom")
    public Page<Candidat> getCandidatByNom(
            @RequestParam("name") String nom,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        return candidatRepository.candidatByNom(nom, PageRequest.of(page, size));
    }

    // 4. POST - Ajouter un nouveau candidat
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Candidat> createCandidat(@RequestBody Candidat candidat) {
        return new ResponseEntity<>(candidatService.addCandidat(candidat), HttpStatus.CREATED);
    }

    // 5. PUT - Modifier un candidat
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Candidat> updateCandidat(
            @PathVariable(value = "id") int id,
            @RequestBody Candidat candidat) {
        return new ResponseEntity<>(candidatService.updateCandidat(id, candidat), HttpStatus.OK);
    }

    // 6. DELETE - Supprimer un candidat
    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> deleteCandidat(@PathVariable(value = "id") int id) {
        return new ResponseEntity<>(candidatService.deleteCandidat(id), HttpStatus.OK);
    }
}