
package info.oais.oaisif.switchBoard;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import io.swagger.v3.oas.annotations.Parameter;

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
    public String getBySBAll(
    		@Parameter(description = "page=n The initial page to start listing, (first page is 0). If n<0 then n is set to 0")  @RequestParam(defaultValue="0" )int page,
    		@Parameter(description = "size=m The page size i.e. number of entries to list. If m<1 then m set to 20")  @RequestParam(defaultValue="20")int size,
    		@Parameter(description = "sortBy= Sort entries by either archiveName, archiveURL, archiveDescription or id") @RequestParam(defaultValue="id") String sortBy,
    		@Parameter(description = "sortDir= The sort direction asc (ascending) or desc (descending).")@RequestParam(defaultValue = "asc") String sortDir,
    		@Parameter(description = "query= The query string to filter the results, conforming to the information in MYQUERYMETHOD property, with a default to return details of IPs where the Archive Description contains the string") @RequestParam(defaultValue = "") String query ) {
        
	    // Validate the parameters
	    
		if (page < 0) {
			page = 0;
		}
		if (size < 1) {
			size = 20;
		}
		if (!sortBy.equals("archiveName") && !sortBy.equals("archiveURL")
				&& !sortBy.equals("archiveDescription") && !sortBy.equals("id")) {
			sortBy = "id";
		}
		if (!sortDir.equals("asc") && !sortDir.equals("desc")) {
			sortDir = "asc";
		}
    	
    	Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
    	//System.out.println("********Paging is : start" + page + " size: " + size + " sortBy: " + sortBy + "sortDir: " + sortDir + " query: " + query);
    	long totalEntries = 0L;
    	
    	Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
    	
        @SuppressWarnings("unchecked")
		Page<SwitchBoardEntry> ar = (Page<SwitchBoardEntry>) switchBoardRepository.findByArchiveDescriptionContains(query, pageable);
        try {
			if (ar != null ) totalEntries = ar.getTotalElements();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//    	List<SwitchBoardEntry> ar = (List<SwitchBoardEntry>) switchBoardRepository.findAll();
//        if (ar == null) {
//            return "All Entries is NULL";
//        }

        String init = "{\"packages\":[";
        StringBuilder csvStr = new StringBuilder(init);
        for (SwitchBoardEntry sae : ar) {
            long idStr = sae.getId();
            String ident = Long.toString(idStr);
            String sName = sae.getArchiveName();
            String sDescription = sae.getArchiveDescription();
            String sUrl = sae.getArchiveName();

            if (csvStr.length() > init.length()) csvStr.append(",");
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
        csvStr.append("],\"totalEntries\":" + totalEntries + ",\"page\":" + page + ",\"size\":" + size + ",\"sortBy\":\"" + sortBy + "\",\"sortDir\":\"" + sortDir + "\",\"query\":\"" + query+"\"}");

        String escapedStr = csvStr.toString().replace("\"", "\\\"");
        return "{\"InformationPackage\":{\"version\":\"1.0.0\",\"PackageType\":\"General\",\"IsDeclaredComplete\":false," +
               "\"PackageDescription\":\"This is a list of sources known to the Switchboard repository\"," +
               "\"InformationObject\":{\"DataObject\":{\"EncodedObject\":{\"Encoding\":\"OTHER\",\"CustomEncoding\":\"JSON using schema http://www.oais.info/oais-if/json-schema/switchboard.schema.json\",\"EncodedContent\":\"" + escapedStr + "\"}}," +
               "\"RepresentationInformation\":{\"RepInfoCategory\":\"Combined\",\"InformationObject\":{\"IdentifierObject\":{\"IdentifierType\":\"URI\",\"IdentifierString\":\"http://www.oais.info/oais-if/switchboard/RepInfoForSwitchboardfile.json\"}}}}}}";
    }
}
