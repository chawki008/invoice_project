package com.cheikh.invoice.repository;

import com.cheikh.invoice.domain.Correction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Correction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorrectionRepository extends JpaRepository<Correction, Long> {

    @Query("select correction from Correction correction where correction.sasisseur.login = ?#{principal.username}")
    List<Correction> findBySasisseurIsCurrentUser();

    @Query("select correction from Correction correction where correction.verificateur.login = ?#{principal.username}")
    List<Correction> findByVerificateurIsCurrentUser();

}
