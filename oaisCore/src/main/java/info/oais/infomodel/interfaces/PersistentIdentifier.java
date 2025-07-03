package info.oais.infomodel.interfaces;


/**
 * A long-lasting Identifier. NOTE - Persistent Identifier resolution services
 * cannot be guaranteed to persist. The Archive should evaluate what the options
 * are if the resolver service ceases to operate. The archive may also consider
 * using multiple alternative Persistent Identifier systems for an object. [OAIS]
 * @author David
 * @version 1.0
 * @since 06-Sep-2021 15:59:46
 */
public interface PersistentIdentifier extends Identifier {

}