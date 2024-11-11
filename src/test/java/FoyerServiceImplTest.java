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

    @Test
    void testRetrieveAllFoyers() {
        Foyer foyer1 = new Foyer();
        Foyer foyer2 = new Foyer();
        when(foyerRepository.findAll()).thenReturn(Arrays.asList(foyer1, foyer2));

        List<Foyer> foyers = foyerService.retrieveAllFoyers();
        assertEquals(2, foyers.size());
        verify(foyerRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveFoyer() {
        Foyer foyer = new Foyer();
        when(foyerRepository.findById(1L)).thenReturn(Optional.of(foyer));

        Foyer result = foyerService.retrieveFoyer(1L);
        assertNotNull(result);
        verify(foyerRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveFoyerNotFound() {
        when(foyerRepository.findById(1L)).thenReturn(Optional.empty());

        Foyer result = foyerService.retrieveFoyer(1L);
        assertNull(result);  // Assuming retrieveFoyer returns null if not found
        verify(foyerRepository, times(1)).findById(1L);
    }

    @Test
    void testAddFoyer() {
        Foyer foyer = new Foyer();
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.addFoyer(foyer);
        assertNotNull(result);
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
    void testAddFoyerNull() {
        Foyer result = foyerService.addFoyer(null);
        assertNull(result);  // Assuming addFoyer returns null for null input
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    @Test
    void testModifyFoyer() {
        Foyer foyer = new Foyer();
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.modifyFoyer(foyer);
        assertNotNull(result);
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
    void testModifyFoyerNotFound() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);  // Using idFoyer as per the Foyer entity
        when(foyerRepository.findById(foyer.getIdFoyer())).thenReturn(Optional.empty());

        Foyer result = foyerService.modifyFoyer(foyer);
        assertNull(result);  // Assuming modifyFoyer returns null if not found
        verify(foyerRepository, never()).save(foyer);
    }

    @Test
    void testRemoveFoyer() {
        Long foyerId = 1L;
        doNothing().when(foyerRepository).deleteById(foyerId);

        foyerService.removeFoyer(foyerId);
        verify(foyerRepository, times(1)).deleteById(foyerId);
    }

    @Test
    void testRemoveFoyerNonExistentId() {
        Long nonExistentId = 99L;
        doThrow(new IllegalArgumentException("Foyer not found")).when(foyerRepository).deleteById(nonExistentId);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            foyerService.removeFoyer(nonExistentId);
        });
        assertEquals("Foyer not found", exception.getMessage());
        verify(foyerRepository, times(1)).deleteById(nonExistentId);
    }
}
