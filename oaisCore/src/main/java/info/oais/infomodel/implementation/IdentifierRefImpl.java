/**
 *
 */
package info.oais.infomodel.implementation;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonSetter;

import info.oais.infomodel.interfaces.Identifier;

/**
 * @author david
 *
 */
//DG XXXX
//@JsonIgnoreType
@JsonPropertyOrder({"IdName", "IdType" } )
public class IdentifierRefImpl implements Identifier {
	@JsonIgnore
	String m_idName = null;
	@JsonIgnore
	String m_idType = null;
	/**
	 * Create new Identifier
	 */
	public IdentifierRefImpl() {
		super();
	}
	/**
	 * Create new Identifier
	 *
	 *
	 * @param idType	Type of the identifier e.g. \"URI\"
	 * @param idStr	Character string of the Identifier;
	 *
	 */
	public IdentifierRefImpl(String idType, String idStr) {

		m_idName = idType;
		m_idType = idStr;
	}


	/**
	 * Get Identifier name
	 *
	 * @return Character string of the Identifier;
	 *
	 */
	@JsonGetter("IdName")
	public String getIdName() {
		return m_idName;  //(String)ident.get("IdName");
	}
	/**
	 * Get Type of the Identifier
	 *
	 * @return Type of the Identifier e.g. \"URI\"
	 *
	 */
	@JsonGetter("IdType")
	public String getIdType() {
		return m_idType; //(String)ident.get("IdType");
	}
	/**
	 * Set Identifier name
	 *
	 * @param name Name of the Identifier
	 *
	 */
	@JsonSetter("IdName")
	public void setIdName(String name) {
		m_idName = name;
	}
	/**
	 * Set Type of Identifier
	 *
	 * @param type Type of the Identifier e.g. \"URI\";
	 *
	 */
	@JsonSetter("IdType")
	public void setIdType(String type) {
		m_idType = type;
	}

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
			return true;
		}
        if ((obj == null) || (getClass() != obj.getClass())) {
			return false;
		}
        if(this.getIdName().equals(((IdentifierRefImpl)obj).getIdName()) && this.getIdType().equals(((IdentifierRefImpl)obj).getIdType())) {
			return true;
		}
        return false;
    }

    @Override
    public int hashCode() {
        return getIdName().hashCode()  + getIdType().hashCode();
    }

    @Override
    public String toString() {
    	return "Id = "+ m_idName + " Type = " + m_idType;
    }
}
