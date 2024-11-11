import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Universite;
import tn.esprit.tpfoyer.repository.UniversiteRepository;
import tn.esprit.tpfoyer.service.UniversiteServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UniversiteServiceImplTest {

    @Mock
    UniversiteRepository universiteRepository;

    @InjectMocks
    UniversiteServiceImpl universiteService;

    Universite universite1, universite2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        universite1 = new Universite(1L, "University A", "Address A", "Description A", "123456", null);
        universite2 = new Universite(2L, "University B", "Address B", "Description B", "789101", null);
    }

    @Test
    void testAddUniversite() {
        when(universiteRepository.save(universite1)).thenReturn(universite1);
        Universite result = universiteService.addUniversite(universite1);
        assertEquals("University A", result.getNomUniversite());
        verify(universiteRepository, times(1)).save(universite1);
    }

    @Test
    void testRetrieveUniversite() {
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite1));
        Universite result = universiteService.retrieveUniversite(1L);
        assertNotNull(result);
        assertEquals("University A", result.getNomUniversite());
        verify(universiteRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveAllUniversites() {
        when(universiteRepository.findAll()).thenReturn(Arrays.asList(universite1, universite2));
        List<Universite> universites = universiteService.retrieveAllUniversites();
        assertEquals(2, universites.size());
        verify(universiteRepository, times(1)).findAll();
    }

    @Test
    void testModifyUniversite() {
        universite1.setNomUniversite("Updated University A");
        when(universiteRepository.save(universite1)).thenReturn(universite1);
        Universite result = universiteService.modifyUniversite(universite1);
        assertEquals("Updated University A", result.getNomUniversite());
        verify(universiteRepository, times(1)).save(universite1);
    }

    @Test
    void testRemoveUniversite() {
        doNothing().when(universiteRepository).deleteById(1L);
        universiteService.removeUniversite(1L);
        verify(universiteRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindUniversitesByName() {
        when(universiteRepository.findByNomUniversite("University")).thenReturn(Arrays.asList(universite1, universite2));
        List<Universite> universites = universiteService.findUniversitesByName("University");
        assertEquals(2, universites.size());
        verify(universiteRepository, times(1)).findByNomUniversite("University");
    }
}
