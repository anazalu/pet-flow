package com.example.petflow;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.RequestBody;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.example.petflow.Pet.Species;

@SpringBootTest
@AutoConfigureMockMvc
class PetControllerTest {
    private static final String NAME = "Puff";
    private static final Integer DOB = 2023;
    private static final Species SPECIES = Species.CAT;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PetRepository petRepository;
    
    @BeforeEach
    void setUp() {
        petRepository.deleteAll();
    }

    @Test
    void testGetAllPets() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/pets")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    void testGetPetById() throws Exception {
        assert petRepository.findAll().size() == 0;
        Pet pet = new Pet(0L, NAME, DOB, SPECIES);
        Long id = petRepository.save(pet).getId();
        assert petRepository.findAll().size() == 1;
        
        mockMvc.perform(MockMvcRequestBuilders.get("/pets/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.id").value(id))
            .andExpect(jsonPath("$.name").exists())
            .andExpect(jsonPath("$.name").value(NAME))
            .andExpect(jsonPath("$.dob").exists())
            .andExpect(jsonPath("$.dob").value(DOB))
            .andExpect(jsonPath("$.species").exists())
            .andExpect(jsonPath("$.species").value(SPECIES.toString()));
    }

    @Test
    void testAddPet() throws Exception {
        String jsonRequest = String.format("{\"name\":\"%s\", \"dob\":\"%d\", \"species\":\"%s\"}", NAME, DOB, SPECIES);
        
        assert petRepository.findAll().size() == 0;

        mockMvc.perform(MockMvcRequestBuilders.post("/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(content().contentType("application/json"))
            .andExpect(status().isOk());
        
        assert petRepository.findAll().size() == 1;
        Pet addedPet = petRepository.findAll().get(0);
        assert addedPet != null;
        assert addedPet.getName().equals(NAME);
        assert addedPet.getDob().equals(DOB);
        assert addedPet.getSpecies().equals(SPECIES);
    }

    @Test
    void testDeletePet() throws Exception {
        assert petRepository.findAll().size() == 0;
        Pet pet = new Pet(0L, NAME, DOB, SPECIES);
        Long id = petRepository.save(pet).getId();
        assert petRepository.findAll().size() == 1;

        mockMvc.perform(MockMvcRequestBuilders.delete("/pets/{id}", id))
            .andExpect(status().isOk());

        assert petRepository.findById(id).isEmpty();
    }

    @Test
    void testUpdatePet() throws Exception {
        assert petRepository.findAll().size() == 0;
        Pet pet = new Pet();
        Long id = petRepository.save(pet).getId();
        assert petRepository.findAll().size() == 1;

        String newName = "New Name";
        Integer newDob = 2020;
        Species newSpecies = Species.TURTLE;

        String jsonRequest = String.format("{\"name\":\"%s\", \"dob\":\"%d\", \"species\":\"%s\"}", newName, newDob, newSpecies);
        
        mockMvc.perform(MockMvcRequestBuilders.patch("/pets/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonRequest))
            .andExpect(content().contentType("application/json"))
            .andExpect(status().isOk());
        
        assert petRepository.findAll().size() == 1;
        Pet updatedPet = petRepository.findById(id).orElse(null);
        assert updatedPet != null;
        assert updatedPet.getName().equals(newName);
        assert updatedPet.getDob().equals(newDob);
        assert updatedPet.getSpecies().equals(newSpecies);
    }
}
