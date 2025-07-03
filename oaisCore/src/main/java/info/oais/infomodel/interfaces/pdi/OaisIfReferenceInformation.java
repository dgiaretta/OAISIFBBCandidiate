package info.oais.infomodel.interfaces.pdi;

import info.oais.infomodel.interfaces.Identifier;
import info.oais.infomodel.interfaces.ReferenceInformation;

/**
 * Interface for accessing Reference Information
 *
 */
public interface OaisIfReferenceInformation extends ReferenceInformation {
	/**
	 * Get the array of identifiers which make up the Reference Information
	 *
	 * @return An array of identifiers which are alternatives of the Reference Information
	 *
	 */
	public Identifier[] getReferenceIds();

	/**
	 * Set the array of identifiers which make up the Reference Information
	 *
	 * @param ids An array of identifiers which are alternatives of the Reference Information
	 *
	 */
	public void setReferenceIds(Identifier[] ids);

}
