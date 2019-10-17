package com.hunre.phinp.repository;
import com.hunre.phinp.domain.ShopMain;
import com.hunre.phinp.domain.ShopSub;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the ShopMain entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopMainRepository extends JpaRepository<ShopMain, Long> {



}
