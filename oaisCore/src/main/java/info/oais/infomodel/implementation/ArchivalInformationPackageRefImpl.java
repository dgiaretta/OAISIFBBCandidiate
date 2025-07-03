/**
 *
 */
package info.oais.infomodel.implementation;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import info.oais.infomodel.deserialise.ArchivalInformationPackageDeserialize;
import info.oais.infomodel.interfaces.*;
import info.oais.infomodel.interfaces.ArchivalInformationPackage;
import info.oais.infomodel.interfaces.InformationObject;
import info.oais.infomodel.interfaces.PackageDescription;
import info.oais.infomodel.interfaces.PreservationDescriptionInformation;

/**
 * Archival Information Package implementation
 *
 * This complements the basic InformationPackage this has:
 * <ul>
 * <li>PreservationDescriptionInformation</li>
 * <li>IsComplete - declares whether or not the AIP is complete</li>
 * </ul>
 *
 * @author david
 *
 */


// DG20240217 @JsonIgnoreProperties(value = { "contentInformation" })
@JsonPropertyOrder({"PackageVersion", "PackageType", "IsComplete", "PackageDescription", "PackagingInformation","InformationObject", "InformationObject", "PreservationDescriptionInformation"  })
// DG20240217 @JsonRootName(value = "InformationPackage" )
//@JsonProperty("ArchivalInformationPackageRefImpl")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArchivalInformationPackageRefImpl extends InformationPackageRefImpl implements ArchivalInformationPackage {

	/**
	 * IsComplete declares whether the AIP is complete
	 */
	Boolean m_IsComplete = false;
	/**
	 * The PDI of the AIP
	 */
	PreservationDescriptionInformation m_PDI = null;

	/**
	 * Create new AIP with no arguments
	 *
	 *
	 */
	public ArchivalInformationPackageRefImpl() {


		super( );
    	/**
    	 * Register the deserializer
    	 */
    	SimpleModule deserialization = new SimpleModule();
    	deserialization.addDeserializer(ArchivalInformationPackage.class, new ArchivalInformationPackageDeserialize());

    	ObjectMapper objectMapper = new ObjectMapper();
    	objectMapper.registerModule(deserialization);

	}

	/**
	 * Create new AIP
	 *
	 * @param io InformationObject for the AIP
	 * @param pdi	PDI for the AIP;
	 * @param pd    Package Description
	 * @param complete Is the package complete
	 * @param id	Identifier for the AIP
	 *
	 */
	public ArchivalInformationPackageRefImpl(InformationObject io, PreservationDescriptionInformationRefImpl pdi, PackageDescription pd, boolean complete, Identifier id) {


		super( io,  pd, "AIP", id);
		m_PDI = pdi;
		m_IsComplete = complete;

	}

	/**
	 * Get boolean showing whether IP has been declared complete
	 *
	 * @return boolean showing whether IP has been declared complete
	 */
	@JsonGetter("IsComplete")
	public Boolean isDeclaredComplete() {
		return m_IsComplete; //(Boolean) aip.get("IsComplete");
	}
	/**
	 * Set boolean showing whether AIP has been declared complete
	 *
	 * @param complete  boolean showing whether AIP has been declared complete
	 */
	@JsonSetter("IsComplete")
	public void setIsDeclaredComplete(Boolean complete) {
		m_IsComplete = complete;
	}
	/**
	 * Get PDI of AIP
	 *
	 * @return PDI of AIP
	 */
	@JsonGetter("PreservationDescriptionInformation")
	public PreservationDescriptionInformation getPDI() {
		return m_PDI;
	}
	/**
	 * Set PDI of AIP
	 *
	 * @param pdi  PDI of AIP
	 */
	@JsonSetter("PreservationDescriptionInformation")
	public void setPDI(PreservationDescriptionInformation pdi) {
		m_PDI = pdi;
	}

	public InformationObject getContentInformation() {
		return super.getInformationObject();
	}

	public void setContentInformation(InformationObject ci) {
		super.setInformationObject(ci);
	}


}
