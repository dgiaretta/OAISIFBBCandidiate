
package info.oais.oaisif.rrori;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;


public interface RroriRepository extends PagingAndSortingRepository<RroriEntry, Long>,JpaRepository<RroriEntry, Long> {

    // Find entries with ID string like the given name
    List<RroriEntry> findByIdStrLike(String name);

    // Find entries with the exact ID string
    List<RroriEntry> findByIdStr(String idStr);
	Page<RroriEntry> findByPackageDescriptionContains(String query, Pageable pageable);
}
