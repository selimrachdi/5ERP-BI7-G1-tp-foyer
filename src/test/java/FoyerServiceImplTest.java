import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Foyer;
import tn.esprit.tpfoyer.repository.FoyerRepository;
import tn.esprit.tpfoyer.service.FoyerServiceImpl;

import java.util.*;
import java.util.logging.Level;

class FoyerServiceImplTest {

    @Mock
    private FoyerRepository foyerRepository;

    @InjectMocks
    private FoyerServiceImpl foyerService;

    private static final Logger LOGGER = Logger.getLogger(FoyerServiceImplTest.class.getName());

    @BeforeEach
    void setUp() {
        try {
            MockitoAnnotations.openMocks(this);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize mocks", e);
        }
    }

    @Test
    @DisplayName("Retrieve all foyers")
    void testRetrieveAllFoyers() {
        Foyer foyer1 = new Foyer();
        Foyer foyer2 = new Foyer();
        List<Foyer> foyers = Arrays.asList(foyer1, foyer2);

        when(foyerRepository.findAll()).thenReturn(foyers);

        List<Foyer> result = foyerService.retrieveAllFoyers();
        assertEquals(2, result.size());
        verify(foyerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Retrieve foyer by ID")
    void testRetrieveFoyer() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);

        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        Foyer result = foyerService.retrieveFoyer(1L);
        assertEquals(1L, result.getIdFoyer());
        verify(foyerRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Add foyer")
    void testAddFoyer() {
        Foyer foyer = new Foyer();
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.addFoyer(foyer);
        assertNotNull(result);
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
    @DisplayName("Modify existing foyer")
    void testModifyFoyer() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);

        when(foyerRepository.existsById(foyer.getIdFoyer())).thenReturn(true);
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.modifyFoyer(foyer);
        assertEquals(1L, result.getIdFoyer());
        verify(foyerRepository, times(1)).existsById(foyer.getIdFoyer());
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
    @DisplayName("Remove foyer by ID")
    void testRemoveFoyer() {
        Long foyerId = 1L;

        when(foyerRepository.existsById(foyerId)).thenReturn(true);
        doNothing().when(foyerRepository).deleteById(foyerId);

        foyerService.removeFoyer(foyerId);
        verify(foyerRepository, times(1)).existsById(foyerId);
        verify(foyerRepository, times(1)).deleteById(foyerId);
    }

    @Test
    @DisplayName("Test retrieving all foyers with empty result")
    void testRetrieveAllFoyersEmpty() {
        when(foyerRepository.findAll()).thenReturn(Collections.emptyList());
        List<Foyer> foyers = foyerService.retrieveAllFoyers();
        assertTrue(foyers.isEmpty(), "Expected an empty list of foyers");
        verify(foyerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Test retrieving foyer with null ID throws NoSuchElementException")
    void testRetrieveFoyerWithNullId() {
        assertThrows(NoSuchElementException.class, () -> foyerService.retrieveFoyer(null));
        verify(foyerRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Test modifying a foyer with partial update")
    void testModifyFoyerPartialData() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        Foyer updatedFoyer = new Foyer();
        updatedFoyer.setIdFoyer(1L);
        updatedFoyer.setNomFoyer("Updated Name");

        when(foyerRepository.existsById(foyer.getIdFoyer())).thenReturn(true);
        when(foyerRepository.save(foyer)).thenReturn(updatedFoyer);

        Foyer result = foyerService.modifyFoyer(foyer);
        assertEquals("Updated Name", result.getNomFoyer());
        verify(foyerRepository, times(1)).existsById(foyer.getIdFoyer());
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
    @DisplayName("Test addFoyer with large dataset")
    void testAddFoyerWithLargeDataset() {
        for (long i = 1; i <= 1000; i++) {
            Foyer foyer = new Foyer();
            foyer.setIdFoyer(i);
            when(foyerRepository.save(foyer)).thenReturn(foyer);

            Foyer result = foyerService.addFoyer(foyer);
            assertEquals(i, result.getIdFoyer());
            verify(foyerRepository, times(1)).save(foyer);
        }
    }

    @Test
    @DisplayName("Test modifying a Foyer and returning a different object")
    void testModifyFoyerReturnsDifferentObject() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        Foyer modifiedFoyer = new Foyer();
        modifiedFoyer.setIdFoyer(1L);
        modifiedFoyer.setNomFoyer("Modified Foyer");

        when(foyerRepository.existsById(foyer.getIdFoyer())).thenReturn(true);
        when(foyerRepository.save(foyer)).thenReturn(modifiedFoyer);

        Foyer result = foyerService.modifyFoyer(foyer);
        assertEquals("Modified Foyer", result.getNomFoyer());
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
    @DisplayName("Test removeFoyer with multiple calls")
    void testRemoveFoyerMultipleCalls() {
        Long foyerId = 1L;
        when(foyerRepository.existsById(foyerId)).thenReturn(true);
        doNothing().when(foyerRepository).deleteById(foyerId);

        foyerService.removeFoyer(foyerId);
        foyerService.removeFoyer(foyerId);

        verify(foyerRepository, times(2)).existsById(foyerId);
        verify(foyerRepository, times(2)).deleteById(foyerId);
    }

    @Test
    @DisplayName("Test addFoyer with Foyer having maximum capacity")
    void testAddFoyerMaxCapacity() {
        Foyer foyer = new Foyer();
        foyer.setCapaciteFoyer(Long.MAX_VALUE);
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.addFoyer(foyer);
        assertEquals(Long.MAX_VALUE, result.getCapaciteFoyer());
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
    @DisplayName("Test modifyFoyer with Foyer having capacity of zero")
    void testModifyFoyerZeroCapacity() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        foyer.setCapaciteFoyer(0L);
        when(foyerRepository.existsById(foyer.getIdFoyer())).thenReturn(true);
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.modifyFoyer(foyer);
        assertEquals(0L, result.getCapaciteFoyer());
        verify(foyerRepository, times(1)).existsById(foyer.getIdFoyer());
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
    @DisplayName("Test addFoyer with negative capacity")
    void testAddFoyerNegativeCapacity() {
        Foyer foyer = new Foyer();
        foyer.setCapaciteFoyer(-1L);
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.addFoyer(foyer);
        assertEquals(-1L, result.getCapaciteFoyer());
        verify(foyerRepository, times(1)).save(foyer);
    }
}
