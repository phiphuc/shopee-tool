package com.hunre.phinp.repository;
import com.hunre.phinp.domain.ShopSub;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the ShopSub entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopSubRepository extends JpaRepository<ShopSub, Long> {
    Optional<ShopSub> findByUsername(String username);
}
