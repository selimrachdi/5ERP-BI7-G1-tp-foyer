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

class FoyerServiceImplTest {

    @Mock
    private FoyerRepository foyerRepository;

    @InjectMocks
    private FoyerServiceImpl foyerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test retrieving all foyers
    @Test
    @DisplayName("Retrieve all foyers - success")
    void testRetrieveAllFoyers() {
        Foyer foyer1 = new Foyer();
        Foyer foyer2 = new Foyer();
        when(foyerRepository.findAll()).thenReturn(Arrays.asList(foyer1, foyer2));

        List<Foyer> foyers = foyerService.retrieveAllFoyers();
        assertEquals(2, foyers.size());
        verify(foyerRepository, times(1)).findAll();
    }

    // Test retrieving a foyer by ID
    @Test
    @DisplayName("Retrieve foyer by ID - success")
    void testRetrieveFoyer() {
        Foyer foyer = new Foyer();
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        Foyer result = foyerService.retrieveFoyer(1L);
        assertNotNull(result);
        verify(foyerRepository, times(1)).findById(1L);
    }

    // Test retrieving a non-existing foyer by ID
    @Test
    @DisplayName("Retrieve non-existing foyer by ID - throws exception")
    void testRetrieveFoyerNotFound() {
        when(foyerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> foyerService.retrieveFoyer(1L));
        verify(foyerRepository, times(1)).findById(1L);
    }

    // Test adding a valid foyer
    @Test
    @DisplayName("Add new foyer - success")
    void testAddFoyer() {
        Foyer foyer = new Foyer();
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.addFoyer(foyer);
        assertNotNull(result);
        verify(foyerRepository, times(1)).save(foyer);
    }

    // Test adding a null foyer
    @Test
    @DisplayName("Add null foyer - throws exception")
    void testAddFoyerNull() {
        assertThrows(IllegalArgumentException.class, () -> foyerService.addFoyer(null));
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    // Test modifying an existing foyer
    @Test
    @DisplayName("Modify existing foyer - success")
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

    // Test modifying a null foyer
    @Test
    @DisplayName("Modify null foyer - throws exception")
    void testModifyFoyerNull() {
        assertThrows(IllegalArgumentException.class, () -> foyerService.modifyFoyer(null));
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    // Test modifying a foyer with null ID
    @Test
    @DisplayName("Modify foyer with null ID - throws exception")
    void testModifyFoyerWithNullId() {
        Foyer foyer = new Foyer();
        assertThrows(IllegalArgumentException.class, () -> foyerService.modifyFoyer(foyer));
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    // Test modifying a non-existing foyer
    @Test
    @DisplayName("Modify non-existing foyer - throws exception")
    void testModifyFoyerNotFound() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        when(foyerRepository.existsById(foyer.getIdFoyer())).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> foyerService.modifyFoyer(foyer));
        verify(foyerRepository, times(1)).existsById(foyer.getIdFoyer());
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    // Test removing an existing foyer
    @Test
    @DisplayName("Remove existing foyer - success")
    void testRemoveFoyer() {
        Long foyerId = 1L;
        when(foyerRepository.existsById(foyerId)).thenReturn(true);
        doNothing().when(foyerRepository).deleteById(foyerId);

        foyerService.removeFoyer(foyerId);
        verify(foyerRepository, times(1)).existsById(foyerId);
        verify(foyerRepository, times(1)).deleteById(foyerId);
    }

    // Test removing a foyer with null ID
    @Test
    @DisplayName("Remove foyer with null ID - throws exception")
    void testRemoveFoyerNullId() {
        assertThrows(IllegalArgumentException.class, () -> foyerService.removeFoyer(null));
        verify(foyerRepository, never()).deleteById(anyLong());
    }

    // Test removing a non-existing foyer
    @Test
    @DisplayName("Remove non-existing foyer - throws exception")
    void testRemoveFoyerNotFound() {
        Long foyerId = 1L;
        when(foyerRepository.existsById(foyerId)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> foyerService.removeFoyer(foyerId));
        verify(foyerRepository, times(1)).existsById(foyerId);
        verify(foyerRepository, never()).deleteById(foyerId);
    }

    // Dynamic tests for testing foyer capacities
    @TestFactory
    @DisplayName("Dynamic tests for foyer capacities")
    Stream<DynamicTest> dynamicTestsForFoyerCapacity() {
        return Stream.of(
                new Foyer(null, "Small Foyer", 50L, null, null),
                new Foyer(null, "Large Foyer", 500L, null, null)
        ).map(foyer -> DynamicTest.dynamicTest("Test capacity for foyer: " + foyer.getCapaciteFoyer(),
                () -> {
                    when(foyerRepository.save(foyer)).thenReturn(foyer);
                    Foyer result = foyerService.addFoyer(foyer);

                    assertEquals(foyer.getCapaciteFoyer(), result.getCapaciteFoyer());
                    verify(foyerRepository, times(1)).save(foyer);
                }));
    }
}
