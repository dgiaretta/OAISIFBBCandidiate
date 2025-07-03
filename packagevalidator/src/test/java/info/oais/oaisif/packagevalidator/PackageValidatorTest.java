package info.oais.oaisif.packagevalidator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class PackageValidatorTest {
    private PackageValidator validator;
    
    @BeforeEach
    public void setUp() {
        validator = new PackageValidator();
    }
    
    @org.junit.jupiter.api.Test
    public void testValidateFile_EmptyFile() throws IOException {
        String emptyJson = "{}";
        boolean isValid = validator.validateFile(createTempFile(emptyJson));
        assertFalse(isValid);
        //assertEquals(1, validator.getErrors().size());
        assertEquals("$.InformationPackage", validator.getErrors().get(0).getPath());
    }
    
    @org.junit.jupiter.api.Test
    public void testValidateFile_MissingRequiredFields() throws IOException {
        String missingFieldsJson = "{ \"InformationPackage\": { \"PackageType\": \"AIP\", \"version\": \"1.0.0\" } }";
        boolean isValid = validator.validateFile(createTempFile(missingFieldsJson));
        assertFalse(isValid);
        assertEquals(3, validator.getErrors().size());
    }
    
    @org.junit.jupiter.api.Test
    public void testValidateFile_InvalidVersionFormat() throws IOException {
        String invalidVersionJson = "{ \"InformationPackage\": { \"InformationObject\": {}, \"IsDeclaredComplete\": true, \"PackageDescription\": \"desc\", \"PackageType\": \"AIP\", \"version\": \"1.0\" } }";
        boolean isValid = validator.validateFile(createTempFile(invalidVersionJson));
        assertFalse(isValid);
        //assertEquals(1, validator.getErrors().size());
        assertEquals("$.InformationPackage.version", validator.getErrors().get(0).getPath());
    }
    
    @org.junit.jupiter.api.Test
    public void testValidateFile_EmptyPackageType() throws IOException {
        String emptyPackageTypeJson = "{ \"InformationPackage\": { \"InformationObject\": {}, \"IsDeclaredComplete\": true, \"PackageDescription\": \"desc\", \"PackageType\": \"\", \"version\": \"1.0.0\" } }";
        boolean isValid = validator.validateFile(createTempFile(emptyPackageTypeJson));
        assertFalse(isValid);
        //assertEquals(1, validator.getErrors().size());
        assertEquals("$.InformationPackage.PackageType", validator.getErrors().get(0).getPath());
    }
    
    @org.junit.jupiter.api.Test
    public void testValidateFile_EmptyInfoObjectID() throws IOException {
        String emptyInfoObjectIDJson = "{ \"InformationPackage\": { \"InformationObject\": { \"InfoObjectID\": \"\" }, \"IsDeclaredComplete\": false, \"PackageDescription\": \"desc\", \"PackageType\": \"AIP\", \"version\": \"1.0.0\" } }";
        boolean isValid = validator.validateFile(createTempFile(emptyInfoObjectIDJson));
        assertFalse(isValid);
        //assertEquals(1, validator.getErrors().size());
        assertEquals("$.InformationPackage.InformationObject.InfoObjectID", validator.getErrors().get(0).getPath());
    }
    
    private String createTempFile(String content) throws IOException {
        java.nio.file.Path tempFile = java.nio.file.Files.createTempFile("test", ".json");
        java.nio.file.Files.write(tempFile, content.getBytes());
        return tempFile.toString();
    }
}
