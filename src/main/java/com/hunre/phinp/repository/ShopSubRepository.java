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
    Optional<List<ShopSub>> findByUsernameAndStatus(String username, String status);

    @Query(
        value = "SELECT * FROM shop_sub u WHERE u.shop_id_id = ?1 and u.status = ?2",
        nativeQuery = true)
    List<ShopSub> findByShopIdAndStatus(String shopId, String status);
}
