package org.example.Controllers;

import org.example.Models.Pet;
import org.example.Services.PetService;

import java.util.List;

public class PetController {
    private PetService petService;

    public PetController() {
        this.petService = new PetService();
    }

    public List<Pet> getAllPets() {
        return petService.getAllPets();
    }

    public boolean addPet(Pet pet) {
        return petService.addPet(pet);
    }

    public boolean updatePet(Pet pet) {
        return petService.updatePet(pet);
    }

    public boolean deletePet(int petId) {
        return petService.deletePet(petId);
    }


    public String getPetNameById(int petId) {
        return petService.getPetById(petId).getName();
    }

    public List<Pet> getAvailablePets() {
        return petService.getAvailablePets();
    }
}
