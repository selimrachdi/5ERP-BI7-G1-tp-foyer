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

        assertThrows(NoSuchElementException.class, () -> foyerService.retrieveFoyer(1L));
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
        assertThrows(IllegalArgumentException.class, () -> foyerService.addFoyer(null));
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    @Test
    void testModifyFoyer() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);  // Remplacer setId() par setIdFoyer()
        when(foyerRepository.existsById(foyer.getIdFoyer())).thenReturn(true);
        when(foyerRepository.save(foyer)).thenReturn(foyer);

        Foyer result = foyerService.modifyFoyer(foyer);
        assertNotNull(result);
        verify(foyerRepository, times(1)).existsById(foyer.getIdFoyer());  // Remplacer getId() par getIdFoyer()
        verify(foyerRepository, times(1)).save(foyer);
    }

    @Test
    void testModifyFoyerNull() {
        assertThrows(IllegalArgumentException.class, () -> foyerService.modifyFoyer(null));
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    @Test
    void testModifyFoyerWithNullId() {
        Foyer foyer = new Foyer();
        assertThrows(IllegalArgumentException.class, () -> foyerService.modifyFoyer(foyer));
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    @Test
    void testModifyFoyerNotFound() {
        Foyer foyer = new Foyer();
        foyer.setIdFoyer(1L);  // Remplacer setId() par setIdFoyer()
        when(foyerRepository.existsById(foyer.getIdFoyer())).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> foyerService.modifyFoyer(foyer));
        verify(foyerRepository, times(1)).existsById(foyer.getIdFoyer());  // Remplacer getId() par getIdFoyer()
        verify(foyerRepository, never()).save(any(Foyer.class));
    }

    @Test
    void testRemoveFoyer() {
        Long foyerId = 1L;
        when(foyerRepository.existsById(foyerId)).thenReturn(true);
        doNothing().when(foyerRepository).deleteById(foyerId);

        foyerService.removeFoyer(foyerId);
        verify(foyerRepository, times(1)).existsById(foyerId);
        verify(foyerRepository, times(1)).deleteById(foyerId);
    }

    @Test
    void testRemoveFoyerNullId() {
        assertThrows(IllegalArgumentException.class, () -> foyerService.removeFoyer(null));
        verify(foyerRepository, never()).deleteById(anyLong());
    }

    @Test
    void testRemoveFoyerNotFound() {
        Long foyerId = 1L;
        when(foyerRepository.existsById(foyerId)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> foyerService.removeFoyer(foyerId));
        verify(foyerRepository, times(1)).existsById(foyerId);
        verify(foyerRepository, never()).deleteById(foyerId);
    }
}
