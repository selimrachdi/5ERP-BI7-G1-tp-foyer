import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tn.esprit.tpfoyer.entity.Universite;
import tn.esprit.tpfoyer.service.UniversiteServiceImpl;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UniversiteServiceImplJUnitTest {

    UniversiteServiceImpl universiteService;
    Universite universite1, universite2;

    @BeforeEach
    void setUp() {
        universiteService = new UniversiteServiceImpl(null);  // Passing null since no actual repository is used here
        universite1 = new Universite(1L, "University A", "Address A", "Description A", "123456", null);
        universite2 = new Universite(2L, "University B", "Address B", "Description B", "789101", null);
    }

    @Test
    void testAddUniversite() {
        Universite result = universiteService.addUniversite(universite1);
        assertEquals("University A", result.getNomUniversite());
    }

    @Test
    void testRetrieveUniversite() {
        Universite result = universiteService.retrieveUniversite(1L);
        assertNotNull(result);
        assertEquals("University A", result.getNomUniversite());
    }

    @Test
    void testRetrieveAllUniversites() {
        List<Universite> universites = universiteService.retrieveAllUniversites();
        assertEquals(2, universites.size());
    }

    @Test
    void testModifyUniversite() {
        universite1.setNomUniversite("Updated University A");
        Universite result = universiteService.modifyUniversite(universite1);
        assertEquals("Updated University A", result.getNomUniversite());
    }
}
