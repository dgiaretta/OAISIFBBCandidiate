package info.oais.infomodel.interfaces;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import info.oais.infomodel.implementation.InformationPackageRefImpl;

/**
 * A logical container composed of optional Information Object(s). Associated with
 * this Information Package is Packaging Information used to delimit and identify
 * the Information Object and optional Package Description information used to
 * facilitate searches for the Information Object. [OAIS]
 * @author David
 * @version 1.0
 * @since 06-Sep-2021 15:59:46
 */
@JsonDeserialize(as = InformationPackageRefImpl.class)
public interface InformationPackage extends DataObject {

	/**
	 * Return the InformationObject in the package.
	 *
	 * @return The InformationObject which the InformationPackage contains.
	 */
	public InformationObject getInformationObject();

	/**
	 * Set the InformationObject in the InformationPackage.
	 *
	 * @param infoObj The InformationObject for the InformationPackage.
	 */
	public void setInformationObject(InformationObject infoObj);

	/**
	 * Return the PackageDescription associated with the InformationPackage
	 *
	 * @return the PackageDescription associated with the InformationPackage
	 */
	public PackageDescription getPackageDescription();

	/**
	 * Set the PackageDescription to associate with the InformationPackage.
	 *
	 * @param pd The PackageDescription to associate with the InformationPackage
	 */
	public void setPackageDescription(PackageDescription pd);

	/**
	 * Return the PackagingInformation associated with the InformationPackage
	 *
	 * @return the PackagingInformation associated with the InformationPackage
	 */
	public PackagingInformation getPackagingInformation();

	/**
	 * Set the PackagingInformation to associate with the InformationPackage.
	 *
	 * @param pi The PackagingInformation to associate with the InformationPackage
	 */
	public void setPackagingInformation(PackagingInformation pi);

	/**
	 * Get the Identifier of the Information Package
	 *
	 * @return Identifier for the Info Package
	 */
	@Override
	public Identifier getIdentifier();

	/**
	 * Set the Identifier for the Info Package
	 *
	 * @param id Identifier for the Info Package
	 */
	public void setIdentifier(Identifier id);

	/**
	 * Get the Identifier of the Object
	 *
	 * @return Identifier for the Object
	 */
	@Override
	public ObjVersion getObjVersion();

	/**
	 * Set the version for the Object
	 *
	 * @param v version for the Object
	 */
	@Override
	public void setObjVersion(ObjVersion v);
}