
package info.oais.oaisif.packagevalidator;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Validator for OAIS-IF Information Package JSON files.
 * 
 * This class provides validation according to the OAIS-IF Information Package schema
 * using the Jackson library for JSON parsing rather than standard JSON Schema validation.
 */
public class PackageValidator {
    // The class has a number of private fields for storing validation errors and known object IDs.
    /**
     * The errors field is a list of ValidationError objects    
     */
    private List<ValidationError> errors = new ArrayList<>();
    
    /**
     *  The knownInfoObjectIDs field is used to keep track of InfoObjectID values
     *  to ensure that they are unique within the package.
     */
    private Set<String> knownInfoObjectIDs = new HashSet<>();
    /**
     *  The knownRepInfoIDs field is used to keep track of RepInfoID values
     *  to ensure that they are unique within the package.
     */
    private Set<String> knownRepInfoIDs = new HashSet<>();

    /**
     *  The VALID_PACKAGE_TYPES array
     */
    private static final String[] VALID_PACKAGE_TYPES = {
        "AIP", "General", "InfoObjectRequest", "Query", "QueryResponse",
        "ObjectRequestResponse", "ErrorResponse"
    };

    /**
     *  The VALID_IDENTIFIER_TYPES array
     */
    private static final String[] VALID_IDENTIFIER_TYPES = {
        "URI", "URL", "URN", "DOI", "ARK", "HANDLE", "PURL", "UUID",
        "JSONPath", "PhysicalLocation", "OTHER"
    };

    /**
     *  The VALID_REFERENCE_TYPES array
     */
    private static final String[] VALID_REFERENCE_TYPES = {
        "INTERNAL_INFOOBJECT", "INTERNAL_REPINFO",
        "REMOTE_INFOOBJECT", "REMOTE_REPINFO"
    };

    /**
     *  The VALID_ENCODING_TYPES array
     */
    private static final String[] VALID_ENCODING_TYPES = {
        "ASCII7", "UTF8", "UTF16", "BASE64", "BASE32", "HEX", "OTHER"
    };

    /**
     *  The VERSION_PATTERN field is used to validate the version fields
     */
    private static final Pattern VERSION_PATTERN = Pattern.compile("^\\d+\\.\\d+\\.\\d+$");
    
    /**
     * The REPINFO_CATEGORY_PATTERN field is used to validate the RepInfoCategory
     */
    private static final Pattern REPINFO_CATEGORY_PATTERN =
        Pattern.compile("^(Structure|Semantic|Other|Combined)(/[A-Za-z]+)*$");

    /**
     * Exception class for validation errors.
     */
    public static class ValidationException extends Exception {
        /**
         * Constructor for a validation exception.
         * @param message The error message
         */
        public ValidationException(String message) {
            super(message);
        }
    }

    /**
     * Class representing a validation error.
     * The class has fields for the path to the error, the error message, the JSON fragment that caused the error, and a detail message.
     * The class has a constructor that takes the path, message, JSON fragment, and detail message as arguments.
     * The class has getter methods for the path, message, JSON fragment, and detail message.
     * The class has a toString method that returns a string representation of the error.    
     * The class has a toStringBrief method that returns a brief string representation of the error.
     * The toStringBrief method truncates the JSON fragment to a maximum of 80 characters.
     * 
     */
    static class ValidationError {
        /**
         * path to the error
         */
        private final String path;
        /**
         * error message
         */
        private final String message;
        /**
         * JSON fragment that caused the error
         */
        private final JsonNode jsonFragment;
        /**
         * detail message
         */
        private final String detail;
         
        /**
         * Constructor for a validation error.
         * 
         * @param path         the path to the error
         * @param message      the error message
         * @param jsonFragment the JSON fragment that caused the error
         * @param detail       a detail message
         */
        public ValidationError(String path, String message, JsonNode jsonFragment, String detail) {
            this.path = path;
            this.message = message;
            this.jsonFragment = jsonFragment;
            this.detail = detail;
        }

        /**
         * Getter for the path to the error.
         * @return the error path
         */
        public String getPath() {
            return path;
        }

        /**
         * Getter for the error message.
         * 
         * @return the error message
         */
        public String getMessage() {
            return message;
        }

        /**
         * Getter for the JSON fragment.
         * 
         * @return the JSON fragment
         */
        public JsonNode getJsonFragment() {
            return jsonFragment;
        }

        /**
         * Getter for the detail message.
         * 
         * @return the detail message
         */
        public String getDetail() {
            return detail;
        }

        @Override
        public String toString() {
            return "ValidationError{" +
                    "path='" + path + '\'' +
                    ",\n message='" + message + '\'' +
                    ",\n jsonFragment=" + jsonFragment +
                    ",\n detail='" + detail + '\'' +
                    '}';
        }
        
        /**
         * Returns a brief string representation of the error.
         * The JSON fragment is truncated to a maximum of 80 characters.
         * 
         * @return a brief string representation of the error
         */
        public String toStringBrief() {
            String jsonFragStr = jsonFragment != null ? jsonFragment.toString() : "null";
            int jsonFragStrLen = jsonFragStr.length();
            String fragToPrint = "";
            if (jsonFragStrLen < 41) {
                fragToPrint = jsonFragStr;
            } else if (jsonFragStrLen < 81) {
                fragToPrint = jsonFragStr.substring(0, Math.min(jsonFragStrLen-1, 40)) + ".....";
            } else {
                fragToPrint = jsonFragStr.substring(0, Math.min(jsonFragStrLen-1, 40)) + "....." + jsonFragStr.substring(jsonFragStrLen-40, jsonFragStrLen-1);
            }
            return "ValidationError{" +
                    "\n path='\t" + path + '\'' +
                    ",\n message='\t" + message + '\'' +
                    ",\n jsonFragment=" + fragToPrint +
                    ",\n detail='\t" + detail + '\'' +
                    '}';
        }
    }

    /**
     * Validates a JSON file against the OAIS Information Package schema.
     * @param filePath the path to the JSON file to validate
     * @return true if the file is valid, false otherwise
     * @throws IOException if there is an error reading the file
     */
    public boolean validateFile(String filePath) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode json = mapper.readTree(new File(filePath));
        knownInfoObjectIDs.clear();
        knownRepInfoIDs.clear();
        validateRoot(json);
        return errors.isEmpty();
    }

    /**
     * Validates the root of the JSON object.
     * @param json the JSON object to validate
     */
    private void validateRoot(JsonNode json) {
        if (!json.has("InformationPackage")) {
            errors.add(new ValidationError("$.InformationPackage", "Root object must have an 'InformationPackage' property", json, "Missing 'InformationPackage' property"));
        } else {
            validateInformationPackage(json.get("InformationPackage"), "$.InformationPackage");
        }
    }

    /**
     * Validates the InformationPackage object.
     * 
     * Checks that the InformationPackage object has the required properties and that the version field is in the correct format.
     * The required properties are:
     * "InformationObject", "IsDeclaredComplete", "PackageDescription", "PackageType", "version"
     * version must be of the form d.d.d where d is a digit
     * 
     * @param ip the InformationPackage JSON object
     * @param path the JSON path to the InformationPackage object
     */
    private void validateInformationPackage(JsonNode ip, String path) {
        String[] requiredFields = {
            "InformationObject", "IsDeclaredComplete", "PackageDescription",
            "PackageType", "version"
        };

        for (String field : requiredFields) {
            if (!ip.has(field)) {
                errors.add(new ValidationError(path + "." + field, "InformationPackage must have a '" + field + "' property", ip, "Missing '" + field + "' property"));
            }
        }

        if (ip.has("version")) {
            String version = ip.get("version").asText();
            if (!VERSION_PATTERN.matcher(version).matches()) {
                errors.add(new ValidationError(path + ".version", "Invalid version format: " + version, ip, "Invalid version format"));
            }
        }

        if (ip.has("PackageType")) {
            String packageType = ip.get("PackageType").asText();
            if (packageType.trim().isEmpty()) {
                errors.add(new ValidationError(path + ".PackageType", "PackageType cannot be empty", ip, "Empty PackageType"));
            } else {
                validateEnum(packageType, VALID_PACKAGE_TYPES, "PackageType", path + ".PackageType");
            }
        }

        if (ip.has("IsDeclaredComplete") && ip.has("PackageType")) {
            boolean isDeclaredComplete = ip.get("IsDeclaredComplete").asBoolean();
            if ("AIP".equals(ip.get("PackageType").asText()) && isDeclaredComplete && !ip.has("PDI")) {
                errors.add(new ValidationError(path + ".PDI", "An AIP with IsDeclaredComplete=true must have a PDI property", ip, "Missing PDI property"));
            }
        }

        if (ip.has("InformationObject")) {
            validateInformationObject(ip.get("InformationObject"), path + ".InformationObject");
        }
    }

    /**
     * Validates that a value is one of the valid values for a field.
     * 
     * @param value the value to validate
     * @param validValues the array of valid values
     * @param fieldName the name of the field
     * @param path the JSON path to the field
     */
    void validateEnum(String value, String[] validValues, String fieldName, String path) {
        for (String validValue : validValues) {
            if (validValue.equals(value)) {
                return;
            }
        }
        errors.add(new ValidationError(path, "Invalid " + fieldName + ": " + value, null, "Invalid value"));
    }

    /**
     * Validates the InformationObject object.
     * 
     * The InformationObject object must have either DataObject and RepresentationInformation or IdentifierObject.
     * It cannot have both DataObject and RepresentationInformation and IdentifierObject.
     * 
     * @param io the InformationObject JSON object
     * @param path the JSON path to the InformationObject object
     */
    private void validateInformationObject(JsonNode io, String path) {
    	if (io == null || !io.isObject()) {
			errors.add(new ValidationError(path, "InformationObject must be a JSON object", io,
					"Invalid InformationObject format"));
			return;
		}

//		if (!io.has("InfoObjectID")) {
//			errors.add(new ValidationError(path + ".InfoObjectID",
//					"InformationObject must have an 'InfoObjectID' property", io, "Missing 'InfoObjectID' property"));
//    	}
        if (io.has("InfoObjectID")) {
            String infoObjectID = io.get("InfoObjectID").asText();
            if (infoObjectID.trim().isEmpty()) {
                errors.add(new ValidationError(path + ".InfoObjectID", "InfoObjectID cannot be empty", io, "Empty InfoObjectID"));
            }
            knownInfoObjectIDs.add(infoObjectID);
        }

        boolean hasDataObjectAndRepInfo = io.has("DataObject") && io.has("RepresentationInformation");
        boolean hasIdentifierObject = io.has("IdentifierObject");

        if (!hasDataObjectAndRepInfo && !hasIdentifierObject) {
            errors.add(new ValidationError(path, "InformationObject must have either (DataObject and RepresentationInformation) or IdentifierObject", io, "Missing required properties"));
        }

        if (hasDataObjectAndRepInfo && hasIdentifierObject) {
            errors.add(new ValidationError(path, "InformationObject cannot have both (DataObject and RepresentationInformation) and IdentifierObject", io, "Conflicting properties"));
        }

        if (hasDataObjectAndRepInfo) {
            validateDataObject(io.get("DataObject"), path + ".DataObject");
            validateRepresentationInformation(io.get("RepresentationInformation"), path + ".RepresentationInformation");
        }

        if (hasIdentifierObject) {
            validateIdentifierObject(io.get("IdentifierObject"), path + ".IdentifierObject");
        }
    }

    /**
     * Validates the DataObject object.
     * 
     * The DataObject object must have either EncodedObject or IdentifierObject.
     * It cannot have both EncodedObject and IdentifierObject.
     * The size property must be a non-negative integer as a string.
     * 
     * @param dataObj the DataObject JSON object
     * @param path the JSON path to the DataObject object
     */
    private void validateDataObject(JsonNode dataObj, String path) {
		if (dataObj == null || !dataObj.isObject()) {
			errors.add(new ValidationError(path, "DataObject must be a JSON object", dataObj,
					"Invalid DataObject format"));
			return;
		}
        boolean hasEncodedObject = dataObj.has("EncodedObject");
        boolean hasIdentifierObject = dataObj.has("IdentifierObject");

        if (!hasEncodedObject && !hasIdentifierObject) {
            errors.add(new ValidationError(path, "DataObject must have either EncodedObject or IdentifierObject", dataObj, "Missing required properties"));
        }

        if (hasEncodedObject && hasIdentifierObject) {
            errors.add(new ValidationError(path, "DataObject cannot have both EncodedObject and IdentifierObject", dataObj, "Conflicting properties"));
        }

        if (hasEncodedObject) {
            validateEncodedObject(dataObj.get("EncodedObject"), path + ".EncodedObject");
        }

        if (hasIdentifierObject) {
            validateIdentifierObject(dataObj.get("IdentifierObject"), path + ".IdentifierObject");
        }

        if (dataObj.has("size")) {
            String size = dataObj.get("size").asText();
            if (!size.matches("^[0-9]+$")) {
                errors.add(new ValidationError(path + ".size", "DataObject size must be a non-negative integer as a string", dataObj, "Invalid size format"));
            }
        }
    }

    /**
     * Validates the EncodedObject object.
     * 
     * The EncodedObject object must have an Encoding property and an EncodedContent property.
     * If the Encoding property is "OTHER", the EncodedObject must have a CustomEncoding property.
     * 
     * @param encodedObj the EncodedObject JSON object
     * @param path the JSON path to the EncodedObject object
     */
    private void validateEncodedObject(JsonNode encodedObj, String path) {
		if (encodedObj == null || !encodedObj.isObject()) {
			errors.add(new ValidationError(path, "EncodedObject must be a JSON object", encodedObj,
					"Invalid EncodedObject format"));
			return;
		}
        if (!encodedObj.has("Encoding")) {
            errors.add(new ValidationError(path + ".Encoding", "EncodedObject must have an 'Encoding' property", encodedObj, "Missing 'Encoding' property"));
        } else {
            String encoding = encodedObj.get("Encoding").asText();
            validateEnum(encoding, VALID_ENCODING_TYPES, "Encoding", path + ".Encoding");

            if ("OTHER".equals(encoding) && !encodedObj.has("CustomEncoding")) {
                errors.add(new ValidationError(path + ".CustomEncoding", "EncodedObject with Encoding='OTHER' must have a 'CustomEncoding' property", encodedObj, "Missing 'CustomEncoding' property"));
            }
        }

        if (!encodedObj.has("EncodedContent")) {
            errors.add(new ValidationError(path + ".EncodedContent", "EncodedObject must have an 'EncodedContent' property", encodedObj, "Missing 'EncodedContent' property"));
        } else {
            String encodedContent = encodedObj.get("EncodedContent").asText();
            if (encodedContent.trim().isEmpty()) {
                errors.add(new ValidationError(path + ".EncodedContent", "EncodedContent cannot be empty", encodedObj, "Empty 'EncodedContent'"));
            }
        }
    }

    /**
     * Validates the IdentifierObject object.
     * 
     * The IdentifierObject object must have an IdentifierString and an IdentifierType property.
     * If the IdentifierType property is "OTHER", the IdentifierObject must have a CustomIdentifierType property.
     * If the object has a version property, it must be of the form d.d.d where d is a digit.
     * If the object has a Description property, it cannot be empty.
     * If the object has a ReferenceType property, it must be one of the valid reference types.
     * 
     * @param idObj the IdentifierObject JSON object
     * @param path the JSON path to the IdentifierObject object
     */
    private void validateIdentifierObject(JsonNode idObj, String path) {
		if (idObj == null || !idObj.isObject()) {
			errors.add(new ValidationError(path, "IdentifierObject must be a JSON object", idObj,
					"Invalid IdentifierObject format"));
			return;
		}
		
        if (!idObj.has("IdentifierString")) {
            errors.add(new ValidationError(path + ".IdentifierString", "IdentifierObject must have an 'IdentifierString' property", idObj, "Missing 'IdentifierString' property"));
        } else {
            String identifierString = idObj.get("IdentifierString").asText();
            if (identifierString.trim().isEmpty()) {
                errors.add(new ValidationError(path + ".IdentifierString", "IdentifierString cannot be empty", idObj, "Empty 'IdentifierString'"));
            }
        }

        if (!idObj.has("IdentifierType")) {
            errors.add(new ValidationError(path + ".IdentifierType", "IdentifierObject must have an 'IdentifierType' property", idObj, "Missing 'IdentifierType' property"));
        } else {
            String identifierType = idObj.get("IdentifierType").asText();
            validateEnum(identifierType, VALID_IDENTIFIER_TYPES, "IdentifierType", path + ".IdentifierType");

            if ("OTHER".equals(identifierType) && !idObj.has("CustomIdentifierType")) {
                errors.add(new ValidationError(path + ".CustomIdentifierType", "IdentifierObject with IdentifierType='OTHER' must have a 'CustomIdentifierType' property", idObj, "Missing 'CustomIdentifierType' property"));
            }
        }

        if (idObj.has("version")) {
            String version = idObj.get("version").asText();
            if (!VERSION_PATTERN.matcher(version).matches()) {
                errors.add(new ValidationError(path + ".version", "Invalid version format: " + version, idObj, "Invalid version format"));
            }
        }

        if (idObj.has("Description")) {
            String description = idObj.get("Description").asText();
            if (description.trim().isEmpty()) {
                errors.add(new ValidationError(path + ".Description", "Description cannot be empty", idObj, "Empty 'Description'"));
            }
        }

        if (idObj.has("ReferenceType")) {
            String referenceType = idObj.get("ReferenceType").asText();
            validateEnum(referenceType, VALID_REFERENCE_TYPES, "ReferenceType", path + ".ReferenceType");
        }
    }

    /**
     * Validates the RepresentationInformation object.
     * 
     * The RepresentationInformation object must have a RepInfoID and a RepInfoCategory property.
     * The RepInfoCategory property must match the regular expression pattern.
     * The object must have one of AndGroup, OrGroup, InformationObject, or RecursionHalt.
     * 
     * @param repInfo the RepresentationInformation JSON object
     * @param path the JSON path to the RepresentationInformation object
     */
    private void validateRepresentationInformation(JsonNode repInfo, String path) {
    	// System.out.println("Validating RepresentationInformation:" + repInfo.toString() + "\n at path: " + path);
		if (repInfo == null || !repInfo.isObject()) {
			errors.add(new ValidationError(path, "RepresentationInformation must be a JSON object", repInfo,
					"Invalid RepresentationInformation format"));
			return;
		}
		
    	if (repInfo.has("RepInfoID")) {
            String repInfoID = repInfo.get("RepInfoID").asText();
            if (repInfoID.trim().isEmpty()) {
                errors.add(new ValidationError(path + ".RepInfoID", "RepInfoID cannot be empty", repInfo, "Empty 'RepInfoID'"));
            }
            knownRepInfoIDs.add(repInfoID);
        }

        if (repInfo.has("RepInfoCategory")) {
            String repInfoCategory = repInfo.get("RepInfoCategory").asText();
            if (!REPINFO_CATEGORY_PATTERN.matcher(repInfoCategory).matches()) {
                errors.add(new ValidationError(path + ".RepInfoCategory", "Invalid RepInfoCategory format: " + repInfoCategory, repInfo, "Invalid 'RepInfoCategory' format"));
            }
        } else {
            errors.add(new ValidationError(path + ".RepInfoCategory", "RepresentationInformation must have a 'RepInfoCategory' property", repInfo, "Missing 'RepInfoCategory' property"));
        }

        boolean hasAndGroup = repInfo.has("AndGroup");
        boolean hasOrGroup = repInfo.has("OrGroup");
        boolean hasInformationObject = repInfo.has("InformationObject");
        boolean hasRecursionHalt = repInfo.has("RecursionHalt");

        if (!hasAndGroup && !hasOrGroup && !hasInformationObject && !hasRecursionHalt) {
            errors.add(new ValidationError(path, "RepresentationInformation must have one of 'AndGroup', 'OrGroup', 'InformationObject', or 'RecursionHalt'", repInfo, "Missing required properties"));
        }

        if (hasAndGroup) {
            validateAndGroup(repInfo.get("AndGroup"), path + ".AndGroup");
        }

        if (hasOrGroup) {
            validateOrGroup(repInfo.get("OrGroup"), path + ".OrGroup");
        }

        if (hasInformationObject) {
            validateInformationObject(repInfo.get("InformationObject"), path + ".InformationObject");
        }

        if (hasRecursionHalt) {
            String recursionHalt = repInfo.get("RecursionHalt").asText();
            if (recursionHalt.trim().isEmpty()) {
                errors.add(new ValidationError(path + ".RecursionHalt", "RecursionHalt cannot be empty", repInfo, "Empty 'RecursionHalt'"));
            }
        }
    }

    /**
     * Validates the AndGroup array.
     * 
     * The AndGroup array must contain at least one element.
     * Each element in the array must be a RepresentationInformation object.
     * 
     * @param andGroup the AndGroup JSON array
     * @param path the JSON path to the AndGroup array
     */
    private void validateAndGroup(JsonNode andGroup, String path) {
        if (!andGroup.isArray() || andGroup.size() < 1) {
            errors.add(new ValidationError(path, "AndGroup must contain at least one element", andGroup, "Empty 'AndGroup'"));
        }

        for (int i = 0; i < andGroup.size(); i++) {
            validateRepresentationInformation(andGroup.get(i), path + "[" + i + "]");
        }
    }

    /**
     * Validates the OrGroup array.
     * 
     * The OrGroup array must contain at least one element.
     * Each element in the array must be a RepresentationInformation object.
     * 
     * @param orGroup the OrGroup JSON array
     * @param path the JSON path to the OrGroup array
     */
    private void validateOrGroup(JsonNode orGroup, String path) {
        if (!orGroup.isArray() || orGroup.size() < 1) {
            errors.add(new ValidationError(path, "OrGroup must contain at least one element", orGroup, "Empty 'OrGroup'"));
        }

        for (int i = 0; i < orGroup.size(); i++) {
            validateRepresentationInformation(orGroup.get(i), path + "[" + i + "]");
        }
    }

    /**
     * Getter for the list of validation errors.
     * @return list of errors
     */
    public List<ValidationError> getErrors() {
        return errors;
    }

    /**
     * Main method to run the validator.
     * @param args command line arguments, expects the path to the JSON file to validate
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java PackageValidator <jsonFilePath>");
            System.exit(1);
        }

        PackageValidator validator = new PackageValidator();
        try {
            boolean isValid = validator.validateFile(args[0]);
            System.out.println("Validation result: " + (isValid ? "VALID" : "INVALID"));
            if (!isValid) {
                int i = 0;
                for (ValidationError error : validator.getErrors()) {
                    System.out.println("Error [" + i++ + "] at " + error.toStringBrief() + "\n");
                }
            }
        } catch (IOException e) {
            System.out.println("File error: " + e.getMessage());
        }
    }
}
