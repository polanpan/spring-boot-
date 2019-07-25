package com.yq.configclient.repository;

import com.yq.configclient.db.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <p> 场所</p>
 * @author youq  2019/4/2 19:13
 */
@Repository
public interface LocationRepository extends JpaRepository<Location, String> {
}
