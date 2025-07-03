package info.oais.oaisif.specificadapter;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface SpecificAdapterRepository extends CrudRepository<SpecificAdapterEntry, Long> {
	//@Query("SELECT u FROM oaisif-switchboard u WHERE u.archiveName = name")
	//List<SpecificAdapterEntry> findByAipDoid(String name);
	List<SpecificAdapterEntry> findByIdStrLike(String name);

	//List<SpecificAdapterEntry> findByPdiDoid(String name);
	List<SpecificAdapterEntry> findByIdStr(String idStr);

	//List<SpecificAdapterEntry> findByIoDoid(String name);

	//List<SpecificAdapterEntry> findByDoDoid(String name);
	
	//String getBySAAll();
	
	List<SpecificAdapterEntry> findByJsonStringLike(String text);
}
