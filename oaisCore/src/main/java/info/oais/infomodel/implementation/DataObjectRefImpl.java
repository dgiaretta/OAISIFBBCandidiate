/**
 *
 */
package info.oais.infomodel.implementation;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

import info.oais.infomodel.interfaces.DataObject;
import info.oais.infomodel.interfaces.Identifier;
import info.oais.infomodel.interfaces.ObjVersion;

/**
 * Reference implementation of DataObject.
 *
 * The DataObject has an Identifier, so that it can be found.
 *
 * The DataObject content is an Object. This Object may be
 * hold the bytes or it may be an instance of an Identifier,
 * in which can it may point to the location of the bytes,
 * or may point to a physical object.
 *
 * @author david
 *
 */
@JsonPropertyOrder({"Object", "Identifier", "IdType" } )
public class DataObjectRefImpl implements DataObject {
	/**
	 * Internal value of Data Object
	 */
	@JsonIgnore
	Object m_DATA = null;
	/**
	 * Internal value of the Identifier
	 */
	@JsonIgnore
	Identifier m_ID = null;
	/**
	 * Internal value of Version
	 */
	@JsonIgnore
	ObjVersion m_ObjVer = new ObjVersionRefImpl();

	/**
	 * Create new DataObject
	 *
	 */
	public DataObjectRefImpl() {
		super();
		/**
		 * Generate an initial UUID
		 */
		//m_ID = new IdentifierRefImpl((UUID.randomUUID()).toString(), "UUID");
	}
	/**
	 * Create new DataObject
	 *
	 * @param obj Object for the DataObject
	 *
	 */
	public DataObjectRefImpl(Object obj) {

		m_DATA = obj;
		/**
		 * Generate an initial UUID
		 */
		//m_ID = new IdentifierRefImpl((UUID.randomUUID()).toString(), "UUID");

	}

	public Object getObject() {
		return m_DATA;
	}

	/**
	 * The following methods are for JACKSON serialisation
	 * If the Object is an instance of Identifier then we want the
	 * JSON to show an Identifier rather than an Object
	 */
	/**
	 * The Data Object may in fact be an Identifier which points to the actual Data Object
	 * @return the Data Object OR else return NULL if this is an Identifier
	 */
	@JsonGetter("Object")
	public Object j_getObject() {
		if (m_DATA instanceof Identifier) {
			return null;
		} else {
			return m_DATA;
		}
	}

	/**
	 * Set the Object of the DataObject.
	 *
	 * @param obj The Object of the DataObject;
	 */
	@JsonSetter("Object")
	public void setObject(Object obj) {
		m_DATA = obj;
	}

	/**
	 * Get the Identifier of this DataObject.
	 *
	 * @return Identifier of this DataObject.
	 *
	 * @author david
	 *
	 */
	@JsonGetter("Identifier")
	public Identifier getIdentifier() {
		return m_ID;
	}
	/**
	 * Set the Identifier of this DataObject.
	 *
	 * @param id Identifier of this DataObject.
	 *
	 * @author david
	 *
	 */
	@JsonSetter("Identifier")
	public void setIdentifier(Identifier id) {
		m_ID = id;
	}
	@Override
	@JsonIgnore
	public String toString() {
		return (String)m_DATA;
	}

	/**
	 * Return the version of the IO
	 *
	 * @return version
	 */
	@JsonGetter("Version")
	public ObjVersion getObjVersion() {

		return m_ObjVer;
	}

	/**
	 * Set the version for the IO
	 *
	 * @param ver The version to set
	 */
	@JsonSetter("Version")
	public void setObjVersion(ObjVersion ver) {
		m_ObjVer = ver;
	}
}
