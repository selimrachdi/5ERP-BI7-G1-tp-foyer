import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.repository.FoyerRepository;
import tn.esprit.tpfoyer.service.FoyerServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class FoyerServiceImplTest {

    @Mock
    private FoyerRepository foyerRepository;

    @InjectMocks
    private FoyerServiceImpl foyerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test pour récupérer tous les foyers
    @Test
    void testRetrieveAllFoyers() {
        Foyer foyer1 = new Foyer();
        Foyer foyer2 = new Foyer();
        when(foyerRepository.findAll()).thenReturn(Arrays.asList(foyer1, foyer2));

        List<Foyer> foyers = foyerService.retrieveAllFoyers();
        assertEquals(2, foyers.size(), "Le nombre de foyers retournés doit être 2.");
        verify(foyerRepository, times(1)).findAll();
    }

    // Test pour récupérer un foyer par ID
    @Test
    void testRetrieveFoyer() {
        Foyer foyer = new Foyer();
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        Foyer result = foyerService.retrieveFoyer(1L);
        assertNotNull(result, "Le foyer ne doit pas être null.");
        verify(foyerRepository, times(1)).findById(1L);
    }

    // Test pour gérer le cas où un foyer n'est pas trouvé
    @Test
    void testRetrieveFoyerNotFound() {
        when(foyerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> foyerService.retrieveFoyer(1L));
        verify(foyerRepository, times(1)).findById(1L);
    }

    // Test pour ajouter un foyer
    @Test
    void testAddFoyer() {
        Foyer foyer = new Foyer();
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.addFoyer(foyer);
        assertNotNull(result, "Le foyer ajouté ne doit pas être null.");
        verify(foyerRepository, times(1)).save(foyer);
    }

    // Test pour gérer l'ajout d'un foyer null
    @Test
    void testAddFoyerNull() {
        assertThrows(IllegalArgumentException.class, () -> foyerService.addFoyer(null));
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    // Test pour modifier un foyer existant
    @Test
    void testModifyFoyer() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        when(foyerRepository.existsById(foyer.getIdFoyer())).thenReturn(true);
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.modifyFoyer(foyer);
        assertNotNull(result, "Le foyer modifié ne doit pas être null.");
        verify(foyerRepository, times(1)).existsById(foyer.getIdFoyer());
        verify(foyerRepository, times(1)).save(foyer);
    }

    // Test pour gérer la modification d'un foyer null
    @Test
    void testModifyFoyerNull() {
        assertThrows(IllegalArgumentException.class, () -> foyerService.modifyFoyer(null));
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    // Test pour modifier un foyer avec un ID null
    @Test
    void testModifyFoyerWithNullId() {
        Foyer foyer = new Foyer();
        assertThrows(IllegalArgumentException.class, () -> foyerService.modifyFoyer(foyer));
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    // Test pour gérer la modification d'un foyer non trouvé
    @Test
    void testModifyFoyerNotFound() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        when(foyerRepository.existsById(foyer.getIdFoyer())).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> foyerService.modifyFoyer(foyer));
        verify(foyerRepository, times(1)).existsById(foyer.getIdFoyer());
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    // Test pour supprimer un foyer
    @Test
    void testRemoveFoyer() {
        Long foyerId = 1L;
        when(foyerRepository.existsById(foyerId)).thenReturn(true);
        doNothing().when(foyerRepository).deleteById(foyerId);

        foyerService.removeFoyer(foyerId);
        verify(foyerRepository, times(1)).existsById(foyerId);
        verify(foyerRepository, times(1)).deleteById(foyerId);
    }

    // Test pour gérer la suppression d'un foyer avec un ID null
    @Test
    void testRemoveFoyerNullId() {
        assertThrows(IllegalArgumentException.class, () -> foyerService.removeFoyer(null));
        verify(foyerRepository, never()).deleteById(anyLong());
    }

    // Test pour gérer la suppression d'un foyer non trouvé
    @Test
    void testRemoveFoyerNotFound() {
        Long foyerId = 1L;
        when(foyerRepository.existsById(foyerId)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> foyerService.removeFoyer(foyerId));
        verify(foyerRepository, times(1)).existsById(foyerId);
        verify(foyerRepository, never()).deleteById(foyerId);
    }
}
