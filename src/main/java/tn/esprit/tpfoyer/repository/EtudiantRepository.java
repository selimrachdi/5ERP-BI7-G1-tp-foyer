package tn.esprit.tpfoyer.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import tn.esprit.tpfoyer.entity.Etudiant;

import java.util.Date;
import java.util.List;

@Repository
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {

    Etudiant findEtudiantByCinEtudiant(long cin);

    @Query("SELECT e FROM Etudiant e JOIN e.courses c WHERE c.name = :courseName")
    List<Etudiant> findEtudiantsByCourseName(String courseName);

    @Query("SELECT e FROM Etudiant e WHERE e.dateNaissance BETWEEN :startDate AND :endDate")
    List<Etudiant> findEtudiantsByDateRange(Date startDate, Date endDate);

}
