import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.repository.EtudiantRepository;
import tn.esprit.tpfoyer.service.EtudiantServiceImpl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EtudiantServiceImplTest {

    @InjectMocks
    private EtudiantServiceImpl etudiantService;

    @Mock
    private EtudiantRepository etudiantRepository;

    private Etudiant mockEtudiant;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockEtudiant = new Etudiant();
        mockEtudiant.setIdEtudiant(1L);
        mockEtudiant.setNomEtudiant("TestNom");
        mockEtudiant.setPrenomEtudiant("TestPrenom");
        mockEtudiant.setCinEtudiant(12345678L);
        mockEtudiant.setDateNaissance(new Date());
    }

    // Integration Tests
    @Test
    void testRetrieveAllEtudiantsIntegration() {
        when(etudiantRepository.findAll()).thenReturn(List.of(mockEtudiant));
        List<Etudiant> etudiants = etudiantService.retrieveAllEtudiants();
        assertNotNull(etudiants);
        assertTrue(etudiants.size() > 0);
    }

    @Test
    void testAddEtudiantIntegration() {
        when(etudiantRepository.save(mockEtudiant)).thenReturn(mockEtudiant);
        Etudiant savedEtudiant = etudiantService.addEtudiant(mockEtudiant);
        assertNotNull(savedEtudiant);
        assertEquals("TestNom", savedEtudiant.getNomEtudiant());
    }

    @Test
    void testRetrieveEtudiantIntegration() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(mockEtudiant));
        Etudiant etudiant = etudiantService.retrieveEtudiant(1L);
        assertNotNull(etudiant);
        assertEquals(1L, etudiant.getIdEtudiant());
    }

    @Test
    void testModifyEtudiantIntegration() {
        mockEtudiant.setNomEtudiant("UpdatedNom");
        when(etudiantRepository.save(mockEtudiant)).thenReturn(mockEtudiant);
        Etudiant updatedEtudiant = etudiantService.modifyEtudiant(mockEtudiant);
        assertEquals("UpdatedNom", updatedEtudiant.getNomEtudiant());
    }

    @Test
    void testRemoveEtudiantIntegration() {
        doNothing().when(etudiantRepository).deleteById(1L);
        etudiantService.removeEtudiant(1L);
        verify(etudiantRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRecupererEtudiantParCinIntegration() {
        when(etudiantRepository.findEtudiantByCinEtudiant(12345678L)).thenReturn(mockEtudiant);
        Etudiant etudiant = etudiantService.recupererEtudiantParCin(12345678L);
        assertNotNull(etudiant);
        assertEquals(12345678L, etudiant.getCinEtudiant());
    }

    @Test
    void testFindEtudiantsByCourseIntegration() {
        when(etudiantRepository.findEtudiantsByCourseName("Mathematics")).thenReturn(List.of(mockEtudiant));
        List<Etudiant> etudiants = etudiantService.findEtudiantsByCourse("Mathematics");
        assertNotNull(etudiants);
        assertEquals(1, etudiants.size());
    }

    @Test
    void testFindEtudiantsByDateRangeIntegration() {
        Date startDate = new Date(System.currentTimeMillis() - 1000000L);
        Date endDate = new Date();
        when(etudiantRepository.findEtudiantsByDateRange(startDate, endDate)).thenReturn(List.of(mockEtudiant));
        List<Etudiant> etudiants = etudiantService.findEtudiantsByDateRange(startDate, endDate);
        assertNotNull(etudiants);
        assertEquals(1, etudiants.size());
    }

    // Unit Tests with Mockito
    @Test
    void testRetrieveAllEtudiantsUnit() {
        when(etudiantRepository.findAll()).thenReturn(Arrays.asList(mockEtudiant));
        List<Etudiant> etudiants = etudiantService.retrieveAllEtudiants();
        assertNotNull(etudiants);
        assertEquals(1, etudiants.size());
    }

    @Test
    void testAddEtudiantUnit() {
        when(etudiantRepository.save(mockEtudiant)).thenReturn(mockEtudiant);
        Etudiant savedEtudiant = etudiantService.addEtudiant(mockEtudiant);
        assertNotNull(savedEtudiant);
        assertEquals("TestNom", savedEtudiant.getNomEtudiant());
    }

    @Test
    void testRetrieveEtudiantUnit() {
        when(etudiantRepository.findById(1L)).thenReturn(Optional.of(mockEtudiant));
        Etudiant etudiant = etudiantService.retrieveEtudiant(1L);
        assertNotNull(etudiant);
        assertEquals(1L, etudiant.getIdEtudiant());
    }

    @Test
    void testModifyEtudiantUnit() {
        mockEtudiant.setNomEtudiant("UpdatedNom");
        when(etudiantRepository.save(mockEtudiant)).thenReturn(mockEtudiant);
        Etudiant updatedEtudiant = etudiantService.modifyEtudiant(mockEtudiant);
        assertEquals("UpdatedNom", updatedEtudiant.getNomEtudiant());
    }

    @Test
    void testRemoveEtudiantUnit() {
        doNothing().when(etudiantRepository).deleteById(1L);
        etudiantService.removeEtudiant(1L);
        verify(etudiantRepository, times(1)).deleteById(1L);
    }

    @Test
    void testRecupererEtudiantParCinUnit() {
        when(etudiantRepository.findEtudiantByCinEtudiant(12345678L)).thenReturn(mockEtudiant);
        Etudiant etudiant = etudiantService.recupererEtudiantParCin(12345678L);
        assertNotNull(etudiant);
        assertEquals(12345678L, etudiant.getCinEtudiant());
    }

    @Test
    void testFindEtudiantsByCourseUnit() {
        when(etudiantRepository.findEtudiantsByCourseName("Mathematics")).thenReturn(List.of(mockEtudiant));
        List<Etudiant> etudiants = etudiantService.findEtudiantsByCourse("Mathematics");
        assertNotNull(etudiants);
        assertEquals(1, etudiants.size());
    }

    @Test
    void testFindEtudiantsByDateRangeUnit() {
        Date startDate = new Date(System.currentTimeMillis() - 1000000L);
        Date endDate = new Date();
        when(etudiantRepository.findEtudiantsByDateRange(startDate, endDate)).thenReturn(List.of(mockEtudiant));
        List<Etudiant> etudiants = etudiantService.findEtudiantsByDateRange(startDate, endDate);
        assertNotNull(etudiants);
        assertEquals(1, etudiants.size());
    }
}
