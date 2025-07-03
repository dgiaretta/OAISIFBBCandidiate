package info.oais.oaisif.genericadapter;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface GenericAdapterRepository extends CrudRepository<GenericAdapterEntry, Long> {
	//@Query("SELECT u FROM oaisif-switchboard u WHERE u.archiveName = name")
	List<GenericAdapterEntry> findByPropertyName(String name);
	//String findByPropertyName(String name);
	//List<GenericAdapterEntry> findByIdLike(String name);
}
