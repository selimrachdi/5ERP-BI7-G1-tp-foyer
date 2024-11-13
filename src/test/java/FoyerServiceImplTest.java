import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.DynamicTest;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.repository.FoyerRepository;
import tn.esprit.tpfoyer.service.FoyerServiceImpl;

import java.util.*;
import java.util.stream.Stream;

public class FoyerServiceImplTest {

    @Mock
    private FoyerRepository foyerRepository;

    @InjectMocks
    private FoyerServiceImpl foyerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test de la récupération de tous les foyers
    @Test
    @DisplayName("Test de la récupération de tous les foyers")
    void testRetrieveAllFoyers() {
        Foyer foyer1 = new Foyer();
        Foyer foyer2 = new Foyer();
        when(foyerRepository.findAll()).thenReturn(Arrays.asList(foyer1, foyer2));

        // Vérifie que la méthode renvoie exactement 2 foyers
        List<Foyer> foyers = foyerService.retrieveAllFoyers();
        assertEquals(2, foyers.size());
        verify(foyerRepository, times(1)).findAll();
    }

    // Test de la récupération d'un foyer par son ID
    @Test
    @DisplayName("Test de la récupération d'un foyer par ID")
    void testRetrieveFoyer() {
        Foyer foyer = new Foyer();
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        // Vérifie que la méthode renvoie bien le foyer trouvé
        Foyer result = foyerService.retrieveFoyer(1L);
        assertNotNull(result);
        verify(foyerRepository, times(1)).findById(1L);
    }

    // Test de la récupération d'un foyer avec un ID inexistant
    @Test
    @DisplayName("Test de la récupération d'un foyer inexistant")
    void testRetrieveFoyerNotFound() {
        when(foyerRepository.findById(1L)).thenReturn(Optional.empty());

        // Vérifie que la méthode lance une exception pour un foyer inexistant
        assertThrows(NoSuchElementException.class, () -> foyerService.retrieveFoyer(1L));
        verify(foyerRepository, times(1)).findById(1L);
    }

    // Test de l'ajout d'un foyer
    @Test
    @DisplayName("Test de l'ajout d'un foyer")
    void testAddFoyer() {
        Foyer foyer = new Foyer();
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        // Vérifie que la méthode ajoute et renvoie le foyer
        Foyer result = foyerService.addFoyer(foyer);
        assertNotNull(result);
        verify(foyerRepository, times(1)).save(foyer);
    }

    // Test de l'ajout d'un foyer null
    @Test
    @DisplayName("Test de l'ajout d'un foyer null")
    void testAddFoyerNull() {
        // Vérifie que la méthode lance une exception pour un foyer null
        assertThrows(IllegalArgumentException.class, () -> foyerService.addFoyer(null));
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    // Test de modification d'un foyer existant
    @Test
    @DisplayName("Test de la modification d'un foyer existant")
    void testModifyFoyer() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        when(foyerRepository.existsById(foyer.getIdFoyer())).thenReturn(true);
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        // Vérifie que la méthode modifie et renvoie le foyer
        Foyer result = foyerService.modifyFoyer(foyer);
        assertNotNull(result);
        verify(foyerRepository, times(1)).existsById(foyer.getIdFoyer());
        verify(foyerRepository, times(1)).save(foyer);
    }

    // Test de modification d'un foyer null
    @Test
    @DisplayName("Test de la modification d'un foyer null")
    void testModifyFoyerNull() {
        // Vérifie que la méthode lance une exception pour un foyer null
        assertThrows(IllegalArgumentException.class, () -> foyerService.modifyFoyer(null));
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    // Test de modification d'un foyer avec un ID null
    @Test
    @DisplayName("Test de la modification d'un foyer avec ID null")
    void testModifyFoyerWithNullId() {
        Foyer foyer = new Foyer();
        // Vérifie que la méthode lance une exception pour un ID null
        assertThrows(IllegalArgumentException.class, () -> foyerService.modifyFoyer(foyer));
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    // Test de modification d'un foyer inexistant
    @Test
    @DisplayName("Test de la modification d'un foyer inexistant")
    void testModifyFoyerNotFound() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        when(foyerRepository.existsById(foyer.getIdFoyer())).thenReturn(false);

        // Vérifie que la méthode lance une exception pour un foyer inexistant
        assertThrows(NoSuchElementException.class, () -> foyerService.modifyFoyer(foyer));
        verify(foyerRepository, times(1)).existsById(foyer.getIdFoyer());
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    // Test de suppression d'un foyer existant
    @Test
    @DisplayName("Test de la suppression d'un foyer existant")
    void testRemoveFoyer() {
        Long foyerId = 1L;
        when(foyerRepository.existsById(foyerId)).thenReturn(true);
        doNothing().when(foyerRepository).deleteById(foyerId);

        // Vérifie que la méthode supprime le foyer
        foyerService.removeFoyer(foyerId);
        verify(foyerRepository, times(1)).existsById(foyerId);
        verify(foyerRepository, times(1)).deleteById(foyerId);
    }

    // Test de suppression d'un foyer avec ID null
    @Test
    @DisplayName("Test de la suppression d'un foyer avec ID null")
    void testRemoveFoyerNullId() {
        // Vérifie que la méthode lance une exception pour un ID null
        assertThrows(IllegalArgumentException.class, () -> foyerService.removeFoyer(null));
        verify(foyerRepository, never()).deleteById(anyLong());
    }

    // Test de suppression d'un foyer inexistant
    @Test
    @DisplayName("Test de la suppression d'un foyer inexistant")
    void testRemoveFoyerNotFound() {
        Long foyerId = 1L;
        when(foyerRepository.existsById(foyerId)).thenReturn(false);

        // Vérifie que la méthode lance une exception pour un foyer inexistant
        assertThrows(NoSuchElementException.class, () -> foyerService.removeFoyer(foyerId));
        verify(foyerRepository, times(1)).existsById(foyerId);
        verify(foyerRepository, never()).deleteById(foyerId);
    }

    // Tests dynamiques pour vérifier les capacités du foyer
    @TestFactory
    @DisplayName("Tests dynamiques pour la capacité des foyers")
    Stream<DynamicTest> dynamicTestsForFoyerCapacity() {
        return Stream.of(
                new Foyer(null, "Petit Foyer", 50L, null, null),
                new Foyer(null, "Grand Foyer", 500L, null, null)
        ).map(foyer -> DynamicTest.dynamicTest("Test de capacité pour foyer: " + foyer.getCapaciteFoyer(),
                () -> {
                    when(foyerRepository.save(foyer)).thenReturn(foyer);
                    Foyer result = foyerService.addFoyer(foyer);

                    // Vérifie que la capacité enregistrée est correcte
                    assertEquals(foyer.getCapaciteFoyer(), result.getCapaciteFoyer());
                    verify(foyerRepository, times(1)).save(foyer);
                }));
    }
}
