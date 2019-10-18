package com.hunre.phinp.repository;
import com.hunre.phinp.domain.ShopMain;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the ShopMain entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopMainRepository extends JpaRepository<ShopMain, Long> {

}
