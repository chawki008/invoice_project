package com.cheikh.invoice.repository;

import com.cheikh.invoice.domain.TempsTravail;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the TempsTravail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TempsTravailRepository extends JpaRepository<TempsTravail, Long> {

    @Query("select tempsTravail from TempsTravail tempsTravail where tempsTravail.user.login = ?#{principal.username}")
    List<TempsTravail> findByUserIsCurrentUser();

}
