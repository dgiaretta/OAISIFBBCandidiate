
package info.oais.oaisif.switchBoard;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/oaisif/v1/switchboard")
public class SwitchBoardController {

    @Autowired
    private SwitchBoardRepository switchBoardRepository;

    @ResponseBody
    @Operation(summary = "Get the details of the named source. In particular one can find the RRORI")
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Found the source", content = { @Content(mediaType = "application/json") }),
        @ApiResponse(responseCode = "400", description = "Cannot find switchboard", content = @Content), 
        @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content), 
        @ApiResponse(responseCode = "403", description = "Forbidden request", content = @Content), 
        @ApiResponse(responseCode = "404", description = "switchboard not found", content = @Content),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content) 
    })
    @GetMapping(value="/sources/{name}", produces = "application/json")
    public String getByArchiveNameLikeByRequestParam(@PathVariable(value = "name") String name) {
        List<SwitchBoardEntry> ar = switchBoardRepository.findByArchiveNameLike(name);
        if (ar == null) {
            return "Entry request for " + name + " is NULL";
        }

        StringBuilder csvStr = new StringBuilder();
        for (SwitchBoardEntry sae : ar) {
            long idStr = sae.getId();
            String ident = Long.toString(idStr);
            String sName = sae.getArchiveName();
            String sDescription = sae.getArchiveDescription();
            String sUrl = sae.getArchiveName();

            if (csvStr.length() > 0) csvStr.append(",");
            csvStr.append("{\"IdentifierObject\":{\"IdentifierType\":\"Local\",\"IdentifierString\":\"")
                  .append(ident)
                  .append("\"},\"archiveName\":\"")
                  .append(sName)
                  .append("\",\"archiveDescription\":\"")
                  .append(sDescription)
                  .append("\",\"archiveURL\":\"")
                  .append(sUrl)
                  .append("\"}");
        }

        String escapedStr = csvStr.toString().replace("\"", "\\\"");
        return "{\"InformationPackage\":{\"version\":\"1.0.0\",\"PackageType\":\"General\",\"IsDeclaredComplete\":false," +
               "\"PackageDescription\":\"This provides details for the requested source known to the Switchboard repository\"," +
               "\"InformationObject\":{\"DataObject\":{\"EncodedObject\":{\"Encoding\":\"OTHER\",\"CustomEncoding\":\"JSON using schema http://www.oais.info/oais-if/json-schema/switchboard.schema.json\",\"EncodedContent\":\"" + escapedStr + "\"}}," +
               "\"RepresentationInformation\":{\"RepInfoCategory\":\"Combined\",\"InformationObject\":{\"IdentifierObject\":{\"IdentifierType\":\"URI\",\"IdentifierString\":\"http://www.oais.info/oais-if/switchboard/RepInfoForSwitchboardfile.json\"}}}}}}";
    }

    @ResponseBody
    @GetMapping(value="/sources", produces = "application/json")
    public String getBySBAll() {
        List<SwitchBoardEntry> ar = (List<SwitchBoardEntry>) switchBoardRepository.findAll();
        if (ar == null) {
            return "All Entries is NULL";
        }

        StringBuilder csvStr = new StringBuilder("[");
        for (SwitchBoardEntry sae : ar) {
            long idStr = sae.getId();
            String ident = Long.toString(idStr);
            String sName = sae.getArchiveName();
            String sDescription = sae.getArchiveDescription();
            String sUrl = sae.getArchiveName();

            if (csvStr.length() > 1) csvStr.append(",");
            csvStr.append("{\"IdentifierObject\":{\"IdentifierType\":\"Local\",\"IdentifierString\":\"")
                  .append(ident)
                  .append("\"},\"archiveName\":\"")
                  .append(sName)
                  .append("\",\"archiveDescription\":\"")
                  .append(sDescription)
                  .append("\",\"archiveURL\":\"")
                  .append(sUrl)
                  .append("\"}");
        }
        csvStr.append("]");

        String escapedStr = csvStr.toString().replace("\"", "\\\"");
        return "{\"InformationPackage\":{\"version\":\"1.0.0\",\"PackageType\":\"General\",\"IsDeclaredComplete\":false," +
               "\"PackageDescription\":\"This is a list of sources known to the Switchboard repository\"," +
               "\"InformationObject\":{\"DataObject\":{\"EncodedObject\":{\"Encoding\":\"OTHER\",\"CustomEncoding\":\"JSON using schema http://www.oais.info/oais-if/json-schema/switchboard.schema.json\",\"EncodedContent\":\"" + escapedStr + "\"}}," +
               "\"RepresentationInformation\":{\"RepInfoCategory\":\"Combined\",\"InformationObject\":{\"IdentifierObject\":{\"IdentifierType\":\"URI\",\"IdentifierString\":\"http://www.oais.info/oais-if/switchboard/RepInfoForSwitchboardfile.json\"}}}}}}";
    }
}
