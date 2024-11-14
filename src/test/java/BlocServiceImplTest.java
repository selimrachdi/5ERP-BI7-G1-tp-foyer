package tn.esprit.tpfoyer.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer.entity.Bloc;
import tn.esprit.tpfoyer.repository.BlocRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BlocServiceImplTest {

    @Mock
    BlocRepository blocRepository;

    @InjectMocks
    BlocServiceImpl blocService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRetrieveAllBlocs() {
        List<Bloc> blocs = new ArrayList<>();
        blocs.add(new Bloc(1L, "BlocA", 100, null, null));
        blocs.add(new Bloc(2L, "BlocB", 200, null, null));

        when(blocRepository.findAll()).thenReturn(blocs);

        List<Bloc> result = blocService.retrieveAllBlocs();

        assertEquals(2, result.size());
        verify(blocRepository, times(1)).findAll();
    }

    @Test
    public void testRetrieveBlocsSelonCapacite() {
        List<Bloc> blocs = new ArrayList<>();
        blocs.add(new Bloc(1L, "BlocA", 100, null, null));
        blocs.add(new Bloc(2L, "BlocB", 200, null, null));

        when(blocRepository.findAll()).thenReturn(blocs);

        List<Bloc> result = blocService.retrieveBlocsSelonCapacite(150);

        assertEquals(1, result.size());
        assertEquals("BlocB", result.get(0).getNomBloc());
    }

    @Test
    public void testRetrieveBloc() {
        Bloc bloc = new Bloc(1L, "BlocA", 100, null, null);
        when(blocRepository.findById(1L)).thenReturn(Optional.of(bloc));

        Bloc result = blocService.retrieveBloc(1L);

        assertNotNull(result);
        assertEquals("BlocA", result.getNomBloc());
        verify(blocRepository, times(1)).findById(1L);
    }

    @Test
    public void testAddBloc() {
        Bloc bloc = new Bloc(1L, "BlocA", 100, null, null);
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.addBloc(bloc);

        assertNotNull(result);
        assertEquals("BlocA", result.getNomBloc());
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    public void testModifyBloc() {
        Bloc bloc = new Bloc(1L, "BlocA", 100, null, null);
        when(blocRepository.save(bloc)).thenReturn(bloc);

        Bloc result = blocService.modifyBloc(bloc);

        assertNotNull(result);
        assertEquals("BlocA", result.getNomBloc());
        verify(blocRepository, times(1)).save(bloc);
    }

    @Test
    public void testRemoveBloc() {
        doNothing().when(blocRepository).deleteById(1L);

        blocService.removeBloc(1L);

        verify(blocRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testTrouverBlocsSansFoyer() {
        List<Bloc> blocs = new ArrayList<>();
        blocs.add(new Bloc(1L, "BlocA", 100, null, null));

        when(blocRepository.findAllByFoyerIsNull()).thenReturn(blocs);

        List<Bloc> result = blocService.trouverBlocsSansFoyer();

        assertEquals(1, result.size());
        assertNull(result.get(0).getFoyer());
        verify(blocRepository, times(1)).findAllByFoyerIsNull();
    }

    @Test
    public void testTrouverBlocsParNomEtCap() {
        List<Bloc> blocs = new ArrayList<>();
        blocs.add(new Bloc(1L, "BlocA", 100, null, null));

        when(blocRepository.findAllByNomBlocAndCapaciteBloc("BlocA", 100)).thenReturn(blocs);

        List<Bloc> result = blocService.trouverBlocsParNomEtCap("BlocA", 100);

        assertEquals(1, result.size());
        assertEquals("BlocA", result.get(0).getNomBloc());
        assertEquals(100, result.get(0).getCapaciteBloc());
        verify(blocRepository, times(1)).findAllByNomBlocAndCapaciteBloc("BlocA", 100);
    }
}
