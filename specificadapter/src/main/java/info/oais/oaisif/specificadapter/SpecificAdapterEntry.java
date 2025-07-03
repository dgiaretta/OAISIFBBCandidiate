package info.oais.oaisif.specificadapter;


import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
			jsonString = js;
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
			jsonString = js;
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
			jsonString = js;
		}
		/**
		 * Get the jsonString for the entry
		 * 
		 * @return The String for the AIP entry
		 */
		public String getJsonString() {
			System.out.println("getJsonString: " + jsonString);
			return jsonString;
		}
		
		@Override
		public String toString() {
			return  "Id:" + id + "    AIP : " + jsonString ;
		}
		/**
		 * @return the m_idStr
		 */
		public String getIdStr() {
			return idStr;
		}
		/**
		 * @param idStr the mIdStr to set
		 */
		public void setIdStr(String idStr) {
			this.idStr = idStr;
			id = Long.valueOf(idStr);
		}	
}
