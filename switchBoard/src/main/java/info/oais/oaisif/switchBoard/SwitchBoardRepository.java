
package info.oais.oaisif.switchBoard;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

//import info.oais.oaisif.specificadapter.SpecificAdapterEntry;


public interface SwitchBoardRepository extends PagingAndSortingRepository<SwitchBoardEntry, Long>,JpaRepository<SwitchBoardEntry, Long> {

    List<SwitchBoardEntry> findByArchiveName(String name);
    List<SwitchBoardEntry> findByArchiveNameLike(String name);
	Page<SwitchBoardEntry> findByArchiveDescriptionContains(String query, Pageable pageable);
}
