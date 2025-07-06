
package info.oais.oaisif.rrori;

import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
    
	@Column(length=256)
	private String packageType = null;
	
	@Column(length=256)
	private String packageDescription = null;
	
	@Column(length=8)
	private String isDeclaredComplete = null;

    // Default constructor
    public RroriEntry() {
    }

    // Constructor with JSON string, ID is generated from hash code of the string
    public RroriEntry(String js) {
    	setJsonString(js);
        this.id = (long) js.hashCode();
        if (id < 0) id = -id;
        this.idStr = id.toString();
    }

    // Constructor with ID and JSON string
    public RroriEntry(Long id, String js) {
        setJsonString(js);
        this.id = id;
        this.idStr = id.toString();
    }
    private void setJsonString(String js) {
		ObjectMapper mapper = new ObjectMapper();
		jsonString = js;
		try {
			JsonNode node = mapper.readTree(js);
            JsonNode comp = node.at("/InformationPackage/IsDeclaredComplete");
            //System.out.println("IsDeclaredComplete as node: " + comp);
            setIsDeclaredComplete(comp.asText());
            
            comp = node.at("/InformationPackage/PackageType");
            setPackageType(comp.asText());
            
            comp = node.at("/InformationPackage/PackageDescription");
            setPackageDescription(comp.asText());
            
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
	}

	/**
	 * @return the m_idStr
	 */
	public String getIdStr() {
		return idStr;
	}
	
	/**
	 * Set the String for the PackageType
	 * 
	 * @param pt The PackageType for the IP
	 */
	public void setPackageType(String pt) {
		this.packageType = pt;
	}

	/**
	 * Get the String for the PackageType
	 * 
	 * @return The String for the PackageType
	 */
	public String getPackageType() {
		return this.packageType;
	}
	/**
	 * Set the String for the PackageDescription
	 * 
	 * @param pd The PackageDescription for the IP
	 */
	public void setPackageDescription(String pd) {
		this.packageDescription = pd;
	}

	/**
	 * Get the String for the PackageDescription
	 * 
	 * @return The String for the PackageDescription
	 */
	public String getPackageDescription() {
		return this.packageDescription;
	}
	
	
	/**
	 * Set the Boolean for the IsDeclaredComplete
	 * 
	 * @param idc The IsDeclaredComplete for the IP
	 */
	public void setIsDeclaredComplete(String idc) {
		this.isDeclaredComplete = idc;
	}

	/**
	 * Get the String for the IsDeclaredComplete
	 * 
	 * @return The Boolean for the IsDeclaredComplete
	 */
	public String getIsDeclaredComplete() {
		return this.isDeclaredComplete;
	}
    @Override
    public String toString() {
        return "Id:" + idStr + " AIP : " + jsonString;
    }

	public String getJsonString() {
		return jsonString;
	}
}
