import tn.esprit.tpfoyer.TpFoyerApplication;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.repository.BlocRepository;
import tn.esprit.tpfoyer.service.BlocServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TpFoyerApplication.class)
class BlocServiceImplTest {

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private BlocServiceImpl blocService;

    private Bloc bloc1;
    private Bloc bloc2;

    @BeforeEach
    void setUp() {
        // Initialize Mockito annotations
        MockitoAnnotations.openMocks(this);

        // Initialize the Bloc objects with test data
        bloc1 = new Bloc();
        bloc1.setIdBloc(1L);
        bloc1.setNomBloc("Bloc A");
        bloc1.setCapaciteBloc(100);

        bloc2 = new Bloc();
        bloc2.setIdBloc(2L);
        bloc2.setNomBloc("Bloc B");
        bloc2.setCapaciteBloc(50);
    }

    @Test
    void retrieveAllBlocs() {
        // Mock repository behavior
        List<Bloc> blocList = List.of(bloc1, bloc2);
        when(blocRepository.findAll()).thenReturn(blocList);

        // Call the service method
        List<Bloc> result = blocService.retrieveAllBlocs();

        // Assertions
        assertEquals(2, result.size(), "The retrieved list should contain 2 blocs.");
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void retrieveBlocsSelonCapacite() {
        // Mock repository behavior
        List<Bloc> blocList = List.of(bloc1, bloc2);
        when(blocRepository.findAll()).thenReturn(blocList);

        // Call the service method
        List<Bloc> result = blocService.retrieveBlocsSelonCapacite(75);

        // Assertions
        assertEquals(1, result.size(), "Only Bloc A should be returned.");
        assertTrue(result.contains(bloc1), "Bloc A should be in the result.");
        assertFalse(result.contains(bloc2), "Bloc B should not be in the result.");
    }

    @Test
    void retrieveBloc() {
        // Mock repository behavior
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc1));

        // Call the service method
        Bloc result = blocService.retrieveBloc(1L);

        // Assertions
        assertNotNull(result, "The retrieved bloc should not be null.");
        assertEquals("Bloc A", result.getNomBloc(), "The name of the retrieved bloc should be Bloc A.");
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    void addBloc() {
        // Mock repository behavior
        when(blocRepository.save(bloc1)).thenReturn(bloc1);

        // Call the service method
        Bloc result = blocService.addBloc(bloc1);

        // Assertions
        assertNotNull(result, "The added bloc should not be null.");
        assertEquals(bloc1.getNomBloc(), result.getNomBloc(), "The name of the added bloc should be the same.");
        verify(blocRepository, times(1)).save(bloc1);
    }

    @Test
    void modifyBloc() {
        // Update bloc capacity and mock repository behavior
        bloc1.setCapaciteBloc(120);
        when(blocRepository.save(bloc1)).thenReturn(bloc1);

        // Call the service method
        Bloc result = blocService.modifyBloc(bloc1);

        // Assertions
        assertEquals(120, result.getCapaciteBloc(), "The capacity of the modified bloc should be updated to 120.");
        verify(blocRepository, times(1)).save(bloc1);
    }

    @Test
    void removeBloc() {
        // Call the service method
        blocService.removeBloc(1L);

        // Verify repository behavior
        verify(blocRepository, times(1)).deleteById(1L);
    }

    @Test
    void trouverBlocsSansFoyer() {
        // Mock repository behavior
        List<Bloc> blocList = List.of(bloc1);
        when(blocRepository.findAllByFoyerIsNull()).thenReturn(blocList);

        // Call the service method
        List<Bloc> result = blocService.trouverBlocsSansFoyer();

        // Assertions
        assertEquals(1, result.size(), "The list should contain only 1 bloc without a foyer.");
        assertTrue(result.contains(bloc1), "The bloc should be in the result.");
        verify(blocRepository, times(1)).findAllByFoyerIsNull();
    }

    @Test
    void trouverBlocsParNomEtCap() {
        // Mock repository behavior
        List<Bloc> blocList = List.of(bloc1);
        when(blocRepository.findAllByNomBlocAndCapaciteBloc("Bloc A", 100)).thenReturn(blocList);

        // Call the service method
        List<Bloc> result = blocService.trouverBlocsParNomEtCap("Bloc A", 100);

        // Assertions
        assertEquals(1, result.size(), "The list should contain only 1 bloc with name Bloc A and capacity 100.");
        assertTrue(result.contains(bloc1), "The bloc should be in the result.");
        verify(blocRepository, times(1)).findAllByNomBlocAndCapaciteBloc("Bloc A", 100);
    }
}
