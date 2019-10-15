package com.hunre.phinp.repository;
import com.hunre.phinp.domain.ShopSub;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ShopSub entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopSubRepository extends JpaRepository<ShopSub, Long> {

}
