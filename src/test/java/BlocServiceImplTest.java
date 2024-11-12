package tn.esprit.tpfoyer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.repository.BlocRepository;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BlocServiceImplTest {

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private BlocServiceImpl blocService;

    private Bloc bloc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);  // Initialize mocks
        bloc = new Bloc(1L, "Bloc1", 100, null, null);
    }

    @Test
    void testRetrieveAllBlocs() {
        when(blocRepository.findAll()).thenReturn(Arrays.asList(bloc));

        var result = blocService.retrieveAllBlocs();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Bloc1", result.get(0).getNomBloc());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveBlocsSelonCapacite() {
        when(blocRepository.findAll()).thenReturn(Arrays.asList(bloc));

        var result = blocService.retrieveBlocsSelonCapacite(50);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(100, result.get(0).getCapaciteBloc());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    void testRetrieveBloc() {
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        var result = blocService.retrieveBloc(1L);

        assertNotNull(result);
        assertEquals(1L, result.getIdBloc());
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    void testAddBloc() {
        when(blocRepository.save(bloc)).thenReturn(bloc);

        var result = blocService.addBloc(bloc);

        assertNotNull(result);
        assertEquals("Bloc1", result.getNomBloc());
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    void testModifyBloc() {
        when(blocRepository.save(bloc)).thenReturn(bloc);

        var result = blocService.modifyBloc(bloc);

        assertNotNull(result);
        assertEquals(100, result.getCapaciteBloc());
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    void testRemoveBloc() {
        doNothing().when(blocRepository).deleteById(1L);

        blocService.removeBloc(1L);

        verify(blocRepository, times(1)).deleteById(1L);
    }

    @Test
    void testTrouverBlocsSansFoyer() {
        when(blocRepository.findAllByFoyerIsNull()).thenReturn(Arrays.asList(bloc));

        var result = blocService.trouverBlocsSansFoyer();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(blocRepository, times(1)).findAllByFoyerIsNull();
    }

    @Test
    void testTrouverBlocsParNomEtCap() {
        when(blocRepository.findAllByNomBlocAndCapaciteBloc("Bloc1", 100)).thenReturn(Arrays.asList(bloc));

        var result = blocService.trouverBlocsParNomEtCap("Bloc1", 100);

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(blocRepository, times(1)).findAllByNomBlocAndCapaciteBloc("Bloc1", 100);
    }
}
