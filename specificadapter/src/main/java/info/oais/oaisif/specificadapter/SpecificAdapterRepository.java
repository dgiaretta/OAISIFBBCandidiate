package info.oais.oaisif.specificadapter;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface SpecificAdapterRepository extends PagingAndSortingRepository<SpecificAdapterEntry, Long>,JpaRepository<SpecificAdapterEntry, Long> {
	//@Query("SELECT u FROM oaisif-switchboard u WHERE u.archiveName = name")
	//List<SpecificAdapterEntry> findByAipDoid(String name);
	List<SpecificAdapterEntry> findByIdStrLike(String name);

	//List<SpecificAdapterEntry> findByPdiDoid(String name);
	List<SpecificAdapterEntry> findByIdStr(String idStr);

	//List<SpecificAdapterEntry> findByIoDoid(String name);

	//List<SpecificAdapterEntry> findByDoDoid(String name);
	
	//String getBySAAll();
	
	List<SpecificAdapterEntry> findByJsonStringLike(String text);

	Page<SpecificAdapterEntry> findByPackageDescriptionContains(String query, Pageable pageable);
}
