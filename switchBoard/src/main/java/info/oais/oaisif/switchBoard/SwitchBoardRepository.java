
package info.oais.oaisif.switchBoard;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface SwitchBoardRepository extends CrudRepository<SwitchBoardEntry, Long> {
    List<SwitchBoardEntry> findByArchiveName(String name);
    List<SwitchBoardEntry> findByArchiveNameLike(String name);
}
