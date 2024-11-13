import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
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

    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    @DisplayName("Retrieve all foyers")
    void testRetrieveAllFoyers() {
        Foyer foyer1 = new Foyer();
        Foyer foyer2 = new Foyer();
        when(foyerRepository.findAll()).thenReturn(Arrays.asList(foyer1, foyer2));

        List<Foyer> foyers = foyerService.retrieveAllFoyers();

        assertEquals(2, foyers.size());
        verify(foyerRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Retrieve foyer by ID")
    void testRetrieveFoyer() {
        Foyer foyer = new Foyer();
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        Foyer result = foyerService.retrieveFoyer(1L);

        assertNotNull(result);
        verify(foyerRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Retrieve non-existing foyer by ID")
    void testRetrieveFoyerNotFound() {
        when(foyerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> foyerService.retrieveFoyer(1L));
        verify(foyerRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Add new foyer")
    void testAddFoyer() {
        Foyer foyer = new Foyer();
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.addFoyer(foyer);

        assertNotNull(result);
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
    @DisplayName("Add null foyer")
    void testAddFoyerNull() {
        assertThrows(IllegalArgumentException.class, () -> foyerService.addFoyer(null));
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    @Test
    @DisplayName("Modify existing foyer")
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

    @Test
    @DisplayName("Modify null foyer")
    void testModifyFoyerNull() {
        assertThrows(IllegalArgumentException.class, () -> foyerService.modifyFoyer(null));
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    @Test
    @DisplayName("Modify foyer with null ID")
    void testModifyFoyerWithNullId() {
        Foyer foyer = new Foyer();
        assertThrows(IllegalArgumentException.class, () -> foyerService.modifyFoyer(foyer));
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    @Test
    @DisplayName("Modify non-existing foyer")
    void testModifyFoyerNotFound() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);
        when(foyerRepository.existsById(foyer.getIdFoyer())).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> foyerService.modifyFoyer(foyer));
        verify(foyerRepository, times(1)).existsById(foyer.getIdFoyer());
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    @Test
    @DisplayName("Remove existing foyer")
    void testRemoveFoyer() {
        Long foyerId = 1L;
        when(foyerRepository.existsById(foyerId)).thenReturn(true);
        doNothing().when(foyerRepository).deleteById(foyerId);

        foyerService.removeFoyer(foyerId);

        verify(foyerRepository, times(1)).existsById(foyerId);
        verify(foyerRepository, times(1)).deleteById(foyerId);
    }

    @Test
    @DisplayName("Remove foyer with null ID")
    void testRemoveFoyerNullId() {
        assertThrows(IllegalArgumentException.class, () -> foyerService.removeFoyer(null));
        verify(foyerRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("Remove non-existing foyer")
    void testRemoveFoyerNotFound() {
        Long foyerId = 1L;
        when(foyerRepository.existsById(foyerId)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> foyerService.removeFoyer(foyerId));
        verify(foyerRepository, times(1)).existsById(foyerId);
        verify(foyerRepository, never()).deleteById(foyerId);
    }

    @TestFactory
    @DisplayName("Dynamic tests for foyer capacity")
    Stream<DynamicTest> dynamicTestsForFoyerCapacity() {
        return Stream.of(
                new Foyer(null, "Small Foyer", 50L, null, null),
                new Foyer(null, "Large Foyer", 500L, null, null)
        ).map(foyer -> DynamicTest.dynamicTest("Capacity test for foyer: " + foyer.getCapaciteFoyer(),
                () -> {
                    when(foyerRepository.save(foyer)).thenReturn(foyer);
                    Foyer result = foyerService.addFoyer(foyer);

                    assertEquals(foyer.getCapaciteFoyer(), result.getCapaciteFoyer());
                    verify(foyerRepository, times(1)).save(foyer);
                }));
    }
}
