package info.oais.oaisif.specificadapter;


import java.io.Serializable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import jakarta.persistence.Id;
import lombok.*;
	
	@Getter @Setter

	@Entity
	@Table(name = "oaisif-specificadapter")
	public class SpecificAdapterEntry implements Serializable {
		/**
		 * 
		 */
		@Id
		//@GeneratedValue(strategy = GenerationType.AUTO);
		//@GeneratedValue(strategy = GenerationType.AUTO)
		
		private Long id;
		

		private String idStr;
		
		
		private static final long serialVersionUID = 1L;
		
		/**
		 * Set the max length of the JSON String
		 */
		@Column(length=40960)
		private String jsonString=null;
		
		@Column(length=256)
		private String packageType = null;
		
		@Column(length=256)
		private String packageDescription = null;
		
		@Column(length=8)
		private String isDeclaredComplete = null;
		

		/**
		 * SpecificAdapterEntry constructor with  no args
		 * 
		 */
		public SpecificAdapterEntry() {
			super();
	    }
		/**
		 * SpecificAdapterEntry constructor with String arg
		 * @param js The string which is the JSON of the InformationPackage
		 * 
		 * The ID is constructed as the hashCode of the string.
		 */
		public SpecificAdapterEntry(String js) {
			//super();
			setJsonString( js);
			id = (long) js.hashCode(); 
			if (id < 0) id = -id;
			idStr = id.toString();
	    }
		/**
		 * SpecificAdapterEntry constructor 
		 * 
		 * @param id The identifier of the InfoPackage
		 * @param js The string which is the JSON of the InformationPackage
		 * 
		 */
		public SpecificAdapterEntry(Long id, String js) {
			//super();
			setJsonString(js);
			this.id = id; 
			idStr = id.toString();
	    }
		
		/**
		 * Get the Identifier for the entry
		 * 
		 * @return Identifier for the SwitchBoard entry
		 */
		public long getId() {
			return id;
		}
		

		/**
		 * Set the Identifier for the entry
		 * 
		 * @param id Identifier for the SA entry
		 */
		public void setId(long id) {
			this.id = id;
			this.idStr = Long.toString(this.id);
		}
		
		/**
		 * Set the jsonString for the entry
		 * 
		 * @param js The String for the AIP
		 */
		public void setJsonString(String js) {
			ObjectMapper mapper = new ObjectMapper();
			jsonString = js;
			try {
				JsonNode node = mapper.readTree(js);
                JsonNode comp = node.at("/InformationPackage/IsDeclaredComplete");
                System.out.println("IsDeclaredComplete as node: " + comp);
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
            //System.out.println(" Node is:" + node);
            
			
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
		/**
		 * Get the jsonString for the entry
		 * 
		 * @return The String for the AIP entry
		 */
		public String getJsonString() {
			System.out.println("getJsonString: " + jsonString);
			return this.jsonString;
		}
		
		@Override
		public String toString() {
			return  "Id:" + this.id + "    AIP : " + this.jsonString ;
		}
		/**
		 * @return the m_idStr
		 */
		public String getIdStr() {
			return this.idStr;
		}
		/**
		 * @param idStr the mIdStr to set
		 */
		public void setIdStr(String idStr) {
			this.idStr = idStr;
			this.id = Long.valueOf(idStr);
		}	
}
