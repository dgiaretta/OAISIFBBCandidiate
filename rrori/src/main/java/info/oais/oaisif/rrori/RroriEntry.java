
package info.oais.oaisif.rrori;

import java.io.Serializable;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "oaisif-rrori")
public class RroriEntry implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;
    private String idStr;

    @Column(length = 4096)
    private String jsonString;

    // Default constructor
    public RroriEntry() {
    }

    // Constructor with JSON string, ID is generated from hash code of the string
    public RroriEntry(String js) {
        this.jsonString = js;
        this.id = (long) js.hashCode();
        if (id < 0) id = -id;
        this.idStr = id.toString();
    }

    // Constructor with ID and JSON string
    public RroriEntry(Long id, String js) {
        this.jsonString = js;
        this.id = id;
        this.idStr = id.toString();
    }
    /**
	 * @return the m_idStr
	 */
	public String getIdStr() {
		return idStr;
	}
    @Override
    public String toString() {
        return "Id:" + idStr + " AIP : " + jsonString;
    }

	public String getJsonString() {
		return jsonString;
	}
}
