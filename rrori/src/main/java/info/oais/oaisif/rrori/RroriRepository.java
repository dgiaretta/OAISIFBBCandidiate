
package info.oais.oaisif.rrori;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface RroriRepository extends CrudRepository<RroriEntry, Long> {
    // Find entries with ID string like the given name
    List<RroriEntry> findByIdStrLike(String name);

    // Find entries with the exact ID string
    List<RroriEntry> findByIdStr(String idStr);
}
