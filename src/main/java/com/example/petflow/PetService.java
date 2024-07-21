package com.example.petflow;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.petflow.Pet.Species;

@Service
public class PetService {
    @Autowired
    private PetRepository petRepository;

    public Pet addPet(String name, Integer dob, Species species) {
        Pet pet = new Pet(0L, name, dob, species);
        Pet addedPet = petRepository.save(pet);
        return addedPet;
    }

    public Optional<Pet> getPetById(Long id) {
        petRepository.findById(id).orElseThrow(RuntimeException::new);
        return petRepository.findById(id);
    }

    public List<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public void removePetById(Long id) {
        petRepository.findById(id).orElseThrow(RuntimeException::new);
        petRepository.deleteById(id);
    }

    public Pet updatePetById(Pet petNew) {
        Pet existingPet = petRepository.findById(petNew.getId()).orElse(null);
        if (existingPet == null) {
            return null;
        }

        if (petNew.getName() != null) {
            existingPet.setName(petNew.getName());
        }
        
        if (petNew.getDob() != null) {
            existingPet.setDob(petNew.getDob());
        }
        
        if (petNew.getSpecies() != null) {
            existingPet.setSpecies(petNew.getSpecies());
        }

        Pet updatedPet = petRepository.save(existingPet);
        return updatedPet;
    }
}
