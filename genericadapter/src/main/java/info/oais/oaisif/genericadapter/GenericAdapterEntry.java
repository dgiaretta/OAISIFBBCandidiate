
package info.oais.oaisif.genericadapter;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

/**
 * Entity class representing a generic adapter entry.
 */
@Getter
@Setter
@Entity
@Table(name = "oaisif_genericadapter")
public class GenericAdapterEntry implements Serializable {
    private static final long serialVersionUID = 2L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String propertyName;

    @Column
    private String propertyValue;

    /**
     * Default constructor.
     */
    public GenericAdapterEntry() {
        super();
    }

	public String getPropertyName() {
		// TODO Auto-generated method stub
		return propertyName;
	}
	public String getPropertyValue() {
		// TODO Auto-generated method stub
		return propertyValue;
	}
	public void setPropertyName(String propName) {
		// TODO Auto-generated method stub
		propertyName = propName;
	}
	public void setPropertyValue(String propValue) {
		// TODO Auto-generated method stub
		propertyValue = propValue;
	}
	public void setId(long idVal) {
		// TODO Auto-generated method stub
		id = idVal;
	}
}
