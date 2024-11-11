package tn.esprit.tpfoyer.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.repository.FoyerRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class FoyerServiceImpl implements IFoyerService {

    private final FoyerRepository foyerRepository;

    @Override
    public List<Foyer> retrieveAllFoyers() {
        return foyerRepository.findAll();
    }

    @Override
    public Foyer retrieveFoyer(Long foyerId) {
        return foyerRepository.findById(foyerId)
                .orElseThrow(() -> new NoSuchElementException("Foyer not found with ID: " + foyerId));
    }

    @Override
    public Foyer addFoyer(Foyer f) {
        if (f == null) {
            throw new IllegalArgumentException("Foyer cannot be null");
        }
        return foyerRepository.save(f);
    }

    @Override
    public Foyer modifyFoyer(Foyer foyer) {
        if (foyer == null || foyer.getIdFoyer() == null) {
            throw new IllegalArgumentException("Foyer or Foyer ID cannot be null");
        }
        if (!foyerRepository.existsById(foyer.getIdFoyer())) {
            throw new NoSuchElementException("Cannot modify non-existing Foyer with ID: " + foyer.getIdFoyer());
        }
        return foyerRepository.save(foyer);
    }

    @Override
    public void removeFoyer(Long foyerId) {
        if (foyerId == null) {
            throw new IllegalArgumentException("Foyer ID cannot be null");
        }
        if (!foyerRepository.existsById(foyerId)) {
            throw new NoSuchElementException("Cannot delete non-existing Foyer with ID: " + foyerId);
        }
        foyerRepository.deleteById(foyerId);
    }
}
