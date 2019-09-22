package com.cheikh.invoice.repository;

import com.cheikh.invoice.domain.Facture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import com.cheikh.invoice.domain.User;
import java.time.Instant;


/**
 * Spring Data  repository for the Facture entity.
 */
@Repository
public interface FactureRepository extends JpaRepository<Facture, Long>, JpaSpecificationExecutor<Facture> {

    @Query("select facture from Facture facture where facture.sasisseur.login = ?#{principal.username}")
    List<Facture> findBySasisseurIsCurrentUser();

    @Query("select facture from Facture facture where facture.verificateur.login = ?#{principal.username}")
    List<Facture> findByVerificateurIsCurrentUser();

    @Query(value = "select distinct facture from Facture facture left join fetch facture.corrections",
        countQuery = "select count(distinct facture) from Facture facture")
    Page<Facture> findAllWithEagerRelationships(Pageable pageable);

    List<Facture> findAllBySasisseur(User saisisseur);

    List<Facture> findAllBySasisseurAndLastModifiedAtBetween(User saisisseur, Instant publicationTimeStart,
            Instant publicationTimeEnd);

    List<Facture> findAllByEtat(String etat);

    Page<Facture> findAllByLastModifiedAtBetween(Instant publicationTimeStart, Instant publicationTimeEnd,
    Pageable pageable);

    @Query("select distinct facture from Facture facture left join fetch facture.corrections")
    List<Facture> findAllWithEagerRelationships();

    @Query("select facture from Facture facture left join fetch facture.corrections where facture.id =:id")
    Optional<Facture> findOneWithEagerRelationships(@Param("id") Long id);

    List<Facture> findBySasisseurIdAndEtat(Long id, String etat);
    List<Facture> findByVerificateurIdAndEtat(Long id, String etat);
}
