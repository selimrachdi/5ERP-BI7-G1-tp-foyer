import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.repository.FoyerRepository;
import tn.esprit.tpfoyer.service.FoyerServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class FoyerServiceImplTest {

    @Mock
    private FoyerRepository foyerRepository;

    @InjectMocks
    private FoyerServiceImpl foyerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test de récupération de tous les foyers (méthode de récupération en masse)
    @Test
    void testRetrieveAllFoyers() {
        Foyer foyer1 = new Foyer();
        Foyer foyer2 = new Foyer();
        when(foyerRepository.findAll()).thenReturn(Arrays.asList(foyer1, foyer2));

        List<Foyer> foyers = foyerService.retrieveAllFoyers();
        assertEquals(2, foyers.size());
        verify(foyerRepository, times(1)).findAll();
    }

    // Test de récupération d'un foyer spécifique par ID (méthode de récupération unique)
    @Test
    void testRetrieveFoyer() {
        Foyer foyer = new Foyer();
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        Foyer result = foyerService.retrieveFoyer(1L);
        assertNotNull(result);
        verify(foyerRepository, times(1)).findById(1L);
    }

    // Test de récupération d'un foyer inexistant (exception attendue)
    @Test
    void testRetrieveFoyerNotFound() {
        when(foyerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> foyerService.retrieveFoyer(1L));
        verify(foyerRepository, times(1)).findById(1L);
    }

    // Test d'ajout d'un foyer (insertion dans le dépôt)
    @Test
    void testAddFoyer() {
        Foyer foyer = new Foyer();
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.addFoyer(foyer);
        assertNotNull(result);
        verify(foyerRepository, times(1)).save(foyer);
    }

    // Test d'ajout d'un foyer null (validation des arguments)
    @Test
    void testAddFoyerNull() {
        assertThrows(IllegalArgumentException.class, () -> foyerService.addFoyer(null));
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    // Test de modification d'un foyer existant (mise à jour d'un enregistrement)
    @Test
    void testModifyFoyer() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        when(foyerRepository.existsById(foyer.getIdFoyer())).thenReturn(true);
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.modifyFoyer(foyer);
        assertNotNull(result);
        verify(foyerRepository, times(1)).existsById(foyer.getIdFoyer());
        verify(foyerRepository, times(1)).save(foyer);
    }

    // Test de modification d'un foyer null (validation des arguments)
    @Test
    void testModifyFoyerNull() {
        assertThrows(IllegalArgumentException.class, () -> foyerService.modifyFoyer(null));
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    // Test de modification d'un foyer avec un ID null (validation des arguments)
    @Test
    void testModifyFoyerWithNullId() {
        Foyer foyer = new Foyer();
        assertThrows(IllegalArgumentException.class, () -> foyerService.modifyFoyer(foyer));
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    // Test de modification d'un foyer inexistant (exception attendue)
    @Test
    void testModifyFoyerNotFound() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        when(foyerRepository.existsById(foyer.getIdFoyer())).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> foyerService.modifyFoyer(foyer));
        verify(foyerRepository, times(1)).existsById(foyer.getIdFoyer());
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    // Test de suppression d'un foyer existant (suppression dans le dépôt)
    @Test
    void testRemoveFoyer() {
        Long foyerId = 1L;
        when(foyerRepository.existsById(foyerId)).thenReturn(true);
        doNothing().when(foyerRepository).deleteById(foyerId);

        foyerService.removeFoyer(foyerId);
        verify(foyerRepository, times(1)).existsById(foyerId);
        verify(foyerRepository, times(1)).deleteById(foyerId);
    }

    // Test de suppression avec un ID null (validation des arguments)
    @Test
    void testRemoveFoyerNullId() {
        assertThrows(IllegalArgumentException.class, () -> foyerService.removeFoyer(null));
        verify(foyerRepository, never()).deleteById(anyLong());
    }

    // Test de suppression d'un foyer inexistant (exception attendue)
    @Test
    void testRemoveFoyerNotFound() {
        Long foyerId = 1L;
        when(foyerRepository.existsById(foyerId)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> foyerService.removeFoyer(foyerId));
        verify(foyerRepository, times(1)).existsById(foyerId);
        verify(foyerRepository, never()).deleteById(foyerId);
    }
}
