package com.example.petflow;

import java.util.List;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PetController {
    @Autowired
    private PetService petService;

    @GetMapping("/pets")
    public List<Pet> getAllPets() {
        return petService.getAllPets();
    }

    @GetMapping("/pets/{id}")
    public Pet getPetById(@PathVariable Long id) throws BadRequestException {
        try {
            return petService.getPetById(id).orElse(null);
        } catch (Exception RuntimeException) {
            throw new BadRequestException("Bad request in GET");
        }
    }

    @PostMapping("/pets")
    public Pet addPet(@RequestBody Pet pet) {
        return petService.addPet(pet.getName(), pet.getDob(), pet.getSpecies());
    }

    @DeleteMapping("/pets/{id}")
    public void deletePetById(@PathVariable Long id) {
        petService.removePetById(id);
    }

    @PatchMapping("/pets/{id}")
    public ResponseEntity<Pet> updatePetById(@PathVariable Long id, @RequestBody Pet pet) {        
        pet.setId(id);        
        Pet petNew = petService.updatePetById(pet);
        if (petNew == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(petNew);
    }
}
