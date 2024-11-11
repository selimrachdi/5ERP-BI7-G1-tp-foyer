import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Universite;
import tn.esprit.tpfoyer.repository.UniversiteRepository;
import tn.esprit.tpfoyer.service.UniversiteServiceImpl;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;

class UniversiteServiceImplMockitoTest {

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
        universiteService.addUniversite(universite1);
        verify(universiteRepository, times(1)).save(universite1);
    }

    @Test
    void testRetrieveUniversite() {
        when(universiteRepository.findById(1L)).thenReturn(Optional.of(universite1));
        universiteService.retrieveUniversite(1L);
        verify(universiteRepository, times(1)).findById(1L);
    }

    @Test
    void testRetrieveAllUniversites() {
        when(universiteRepository.findAll()).thenReturn(Arrays.asList(universite1, universite2));
        universiteService.retrieveAllUniversites();
        verify(universiteRepository, times(1)).findAll();
    }

    @Test
    void testModifyUniversite() {
        when(universiteRepository.save(universite1)).thenReturn(universite1);
        universiteService.modifyUniversite(universite1);
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
        when(universiteRepository.findByNomUniversite("University"))
                .thenReturn(Arrays.asList(universite1, universite2));
        universiteService.findUniversitesByName("University");
        verify(universiteRepository, times(1)).findByNomUniversite("University");
    }
}
