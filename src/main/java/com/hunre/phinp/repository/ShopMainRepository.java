package com.hunre.phinp.repository;
import com.hunre.phinp.domain.ShopMain;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the ShopMain entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopMainRepository extends JpaRepository<ShopMain, Long> {

    Optional<List<ShopMain>> findByLinkShop(String linkShop);

}
