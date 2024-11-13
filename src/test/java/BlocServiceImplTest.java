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

@SpringBootTest
class BlocServiceImplTest {

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private BlocServiceImpl blocService;

    private Bloc bloc1;
    private Bloc bloc2;

    @BeforeEach
    void setUp() {
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
        List<Bloc> blocList = List.of(bloc1, bloc2);
        when(blocRepository.findAll()).thenReturn(blocList);

        List<Bloc> result = blocService.retrieveAllBlocs();

        assertEquals(2, result.size(), "The retrieved list should contain 2 blocs.");
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void retrieveBlocsSelonCapacite() {
        List<Bloc> blocList = List.of(bloc1, bloc2);
        when(blocRepository.findAll()).thenReturn(blocList);

        List<Bloc> result = blocService.retrieveBlocsSelonCapacite(75);

        assertEquals(1, result.size(), "Only Bloc A should be returned.");
        assertTrue(result.contains(bloc1), "Bloc A should be in the result.");
        assertFalse(result.contains(bloc2), "Bloc B should not be in the result.");
    }

    @Test
    void retrieveBloc() {
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc1));

        Bloc result = blocService.retrieveBloc(1L);

        assertNotNull(result, "The retrieved bloc should not be null.");
        assertEquals("Bloc A", result.getNomBloc(), "The name of the retrieved bloc should be Bloc A.");
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    void addBloc() {
        when(blocRepository.save(bloc1)).thenReturn(bloc1);

        Bloc result = blocService.addBloc(bloc1);

        assertNotNull(result, "The added bloc should not be null.");
        assertEquals(bloc1.getNomBloc(), result.getNomBloc(), "The name of the added bloc should be the same.");
        verify(blocRepository, times(1)).save(bloc1);
    }

    @Test
    void modifyBloc() {
        bloc1.setCapaciteBloc(120);
        when(blocRepository.save(bloc1)).thenReturn(bloc1);

        Bloc result = blocService.modifyBloc(bloc1);

        assertEquals(120, result.getCapaciteBloc(), "The capacity of the modified bloc should be updated to 120.");
        verify(blocRepository, times(1)).save(bloc1);
    }

    @Test
    void removeBloc() {
        blocService.removeBloc(1L);

        verify(blocRepository, times(1)).deleteById(1L);
    }

    @Test
    void trouverBlocsSansFoyer() {
        List<Bloc> blocList = List.of(bloc1);
        when(blocRepository.findAllByFoyerIsNull()).thenReturn(blocList);

        List<Bloc> result = blocService.trouverBlocsSansFoyer();

        assertEquals(1, result.size(), "The list should contain only 1 bloc without a foyer.");
        assertTrue(result.contains(bloc1), "The bloc should be in the result.");
        verify(blocRepository, times(1)).findAllByFoyerIsNull();
    }

    @Test
    void trouverBlocsParNomEtCap() {
        List<Bloc> blocList = List.of(bloc1);
        when(blocRepository.findAllByNomBlocAndCapaciteBloc("Bloc A", 100)).thenReturn(blocList);

        List<Bloc> result = blocService.trouverBlocsParNomEtCap("Bloc A", 100);

        assertEquals(1, result.size(), "The list should contain only 1 bloc with name Bloc A and capacity 100.");
        assertTrue(result.contains(bloc1), "The bloc should be in the result.");
        verify(blocRepository, times(1)).findAllByNomBlocAndCapaciteBloc("Bloc A", 100);
    }
}
