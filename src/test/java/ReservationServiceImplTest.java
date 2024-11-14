import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.tpfoyer.entity.Etudiant;
import tn.esprit.tpfoyer.entity.Reservation;
import tn.esprit.tpfoyer.repository.ReservationRepository;
import tn.esprit.tpfoyer.service.ReservationServiceImpl;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ReservationServiceImplTest {

    @InjectMocks
    private ReservationServiceImpl reservationService;

    @Mock
    private ReservationRepository reservationRepository;

    private Reservation reservation1;
    private Reservation reservation2;
    private Etudiant etudiant1;
    private Etudiant etudiant2;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
//this is a commentaazpao
        // Initialize Etudiant instances///
        etudiant1 = new Etudiant();
        etudiant1.setIdEtudiant(1L);
        etudiant1.setNomEtudiant("John");
        etudiant1.setPrenomEtudiant("Doe");

        etudiant2 = new Etudiant();
        etudiant2.setIdEtudiant(2L);
        etudiant2.setNomEtudiant("Jane");
        etudiant2.setPrenomEtudiant("Smith");

        // Initialize Reservation instances
        reservation1 = new Reservation();
        reservation1.setIdReservation("1");
        reservation1.setAnneeUniversitaire(new Date());
        reservation1.setEstValide(true);
        Set<Etudiant> etudiantsSet1 = new HashSet<>();
        etudiantsSet1.add(etudiant1);
        reservation1.setEtudiants(etudiantsSet1);

        reservation2 = new Reservation();
        reservation2.setIdReservation("2");
        reservation2.setAnneeUniversitaire(new Date());
        reservation2.setEstValide(true);
        Set<Etudiant> etudiantsSet2 = new HashSet<>();
        etudiantsSet2.add(etudiant1);
        etudiantsSet2.add(etudiant2);
        reservation2.setEtudiants(etudiantsSet2);
    }

    @Test
    public void testAddReservation_Success() {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation1);

        Reservation addedReservation = reservationService.addReservation(reservation1);

        assertNotNull(addedReservation, "La réservation ajoutée ne doit pas être nulle");
        assertEquals("1", addedReservation.getIdReservation(), "L'ID de la réservation doit correspondre");
        assertTrue(addedReservation.isEstValide(), "La réservation doit être valide");
        verify(reservationRepository, times(1)).save(reservation1);
    }

    @Test
    public void testRetrieveReservation_Success() {
        when(reservationRepository.findById("1")).thenReturn(Optional.of(reservation1));

        Reservation retrievedReservation = reservationService.retrieveReservation("1");

        assertNotNull(retrievedReservation, "La réservation récupérée ne doit pas être nulle");
        assertEquals("1", retrievedReservation.getIdReservation(), "L'ID de la réservation doit correspondre");
        assertTrue(retrievedReservation.isEstValide(), "La réservation doit être valide");
        verify(reservationRepository, times(1)).findById("1");
    }

    @Test
    public void testRemoveReservation_Success() {
        // Simulate successful removal by ensuring no exceptions are thrown
        doNothing().when(reservationRepository).deleteById("1");

        reservationService.removeReservation("1");

        verify(reservationRepository, times(1)).deleteById("1");
    }

    @Test
    public void testFindReservationsByDateAndStatus_Success() {
        List<Reservation> reservations = List.of(reservation1, reservation2);

        when(reservationRepository.findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(true)))
                .thenReturn(reservations);

        List<Reservation> foundReservations = reservationService.trouverResSelonDateEtStatus(new Date(), true);

        assertNotNull(foundReservations, "La liste de réservations ne doit pas être nulle");
        assertEquals(2, foundReservations.size(), "La liste doit contenir 2 réservations");
        verify(reservationRepository, times(1)).findAllByAnneeUniversitaireBeforeAndEstValide(any(Date.class), eq(true));
    }

    @Test
    public void testModifyReservation_Success() {
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation1);

        reservation1.setEstValide(false);
        Reservation updatedReservation = reservationService.modifyReservation(reservation1);

        assertNotNull(updatedReservation, "La réservation modifiée ne doit pas être nulle");
        assertFalse(updatedReservation.isEstValide(), "La réservation doit être marquée comme non valide");
        verify(reservationRepository, times(1)).save(reservation1);
    }
}
