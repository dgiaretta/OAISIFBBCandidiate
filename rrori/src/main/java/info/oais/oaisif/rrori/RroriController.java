
package info.oais.oaisif.rrori;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Parameter;

//import info.oais.oaisif.specificadapter.SpecificAdapterEntry;
//import info.oais.oaisif.rrori.RroriEntry;


@RestController
@RequestMapping(value="/oaisif/v1/representation-info-repository", produces = "application/json; charset=UTF-8")
public class RroriController {

    @Autowired
    private RroriRepository rroriRepository;

    // Get an AIP by its identifier
    @GetMapping(value = "/information-packages/{ipid}", produces = "application/json")
    public ResponseEntity<String> getAIPByDOIDByRequestParam(@PathVariable("ipid") String ipid) {
    	if (ipid == null || ipid.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("{\"error\":\"Missing or empty ipid parameter\"}");
        }
        List<RroriEntry> entries = rroriRepository.findByIdStr(ipid);
        if (entries.isEmpty()) {
            return ResponseEntity.status(404).body("{\"error\":\"Entry not found for ipid: " + ipid + "\"}");
        }
        if (entries.isEmpty()) {
        	return ResponseEntity.status(404).body("{\"error\":\"Entry not found for ipid: " + ipid + "\"}");
        }
        return ResponseEntity.ok(entries.get(0).getJsonString());
    }

    // Get AIPs with identifiers like the given name
    @GetMapping(value = "/information-packages-like/{name}", produces = "application/json")
    public ResponseEntity<?>  getByAIPNameLikeByRequestParam(@PathVariable("name") String name) {
    	if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("{\"error\":\"Missing or empty name parameter\"}");
        }
        List<RroriEntry> entries = rroriRepository.findByIdStrLike(name);
        if (entries == null || entries.isEmpty() || entries.size() < 1) {
            return ResponseEntity.status(404).body("{\"error\":\"No entries found for name: " + name + "\"}");
        }
        return ResponseEntity.ok(entries);
        //return rroriRepository.findByIdStrLike(name);
    }

    // Get all AIPs
    @GetMapping(value = "/information-packages", produces = "application/json")
    public ResponseEntity<String>  getByRIAll(
    		@Parameter(description = "page=n The initial page to start listing, (first page is 0). If n<0 then n is set to 0")  @RequestParam(defaultValue="0" )int page,
    		@Parameter(description = "size=m The page size i.e. number of entries to list. If m<1 then m set to 20")  @RequestParam(defaultValue="20")int size,
    		@Parameter(description = "sortBy= Sort entries by either IsDeclaredComplete, PackageType, PackageDescription or id") @RequestParam(defaultValue="id") String sortBy,
    		@Parameter(description = "sortDir= The sort direction asc (ascending) or desc (descending).")@RequestParam(defaultValue = "asc") String sortDir,
    		@Parameter(description = "query= The query string to filter the results, conforming to the information in MYQUERYMETHOD property, with a default to return details of IPs where the Package Description contains the string") @RequestParam(defaultValue = "") String query  ) {
	    // Validate the parameters
    	
        if (page < 0) {
            return ResponseEntity.badRequest().body("{\"error\":\"Page must be >= 0\"}");
        }
        if (size < 1) {
            return ResponseEntity.badRequest().body("{\"error\":\"Size must be >= 1\"}");
        }
        if (!sortBy.equals("IsDeclaredComplete") && !sortBy.equals("PackageType")
            && !sortBy.equals("PackageDescription") && !sortBy.equals("id")) {
            return ResponseEntity.badRequest().body("{\"error\":\"Invalid sortBy parameter\"}");
        }
        if (!sortDir.equals("asc") && !sortDir.equals("desc")) {
            return ResponseEntity.badRequest().body("{\"error\":\"Invalid sortDir parameter\"}");
        }

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ?
                Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<RroriEntry> ar = rroriRepository.findByPackageDescriptionContains(query, pageable);

        long totalEntries = (ar != null) ? ar.getTotalElements() : 0L;
        if (ar == null || ar.isEmpty()) {
            return ResponseEntity.status(404).body("{\"error\":\"No entries found for the given query\"}");
        }
	    
//		if (page < 0) {
//			page = 0;
//		}
//		if (size < 1) {
//			size = 20;
//		}
//		if (!sortBy.equals("IsDeclaredComplete") && !sortBy.equals("PackageType")
//				&& !sortBy.equals("PackageDescription") && !sortBy.equals("id")) {
//			sortBy = "id";
//		}
//		if (!sortDir.equals("asc") && !sortDir.equals("desc")) {
//			sortDir = "asc";
//		}
//    	
//    	Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? 
//                Sort.Direction.DESC : Sort.Direction.ASC;
//    	//System.out.println("********Paging is : start" + page + " size: " + size + " sortBy: " + sortBy + "sortDir: " + sortDir + " query: " + query);
//    	long totalEntries = 0L;
//    	
//    	Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
//    	
//        Page<RroriEntry> ar = (Page<RroriEntry>) rroriRepository.findByPackageDescriptionContains(query, pageable);
//        try {
//        	if (ar != null ) totalEntries = ar.getTotalElements();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
        
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        String init = "{\"packages\":[";
        String csvStr = new String(init);
        String ret = "";
        
        int count = 0;
        if (ar != null) {
            Iterator<RroriEntry> iter = ar.iterator();
            while (iter.hasNext()) {
            	RroriEntry sae = iter.next();
                System.out.println("Entry is " + sae);
                String idStr = sae.getIdStr();
                String ident = "\"IdentifierObject\":{\"IdentifierType\":\"Local\",\"IdentifierString\":\"" + idStr + "\"}";

                String aipStr = sae.getJsonString();
                System.out.println(" JsonString is:" + aipStr);
                try {
                    node = mapper.readTree(aipStr);
                } catch (JsonMappingException e) {
                    e.printStackTrace();
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                //System.out.println(" Node is:" + node);

                JsonNode pd = node.at("/InformationPackage/PackageDescription");
               // System.out.println("PackageDescription as node: " + pd);
                String pdStr = pd.asText();

                //if (query != null && !pdStr.contains(query)) continue;

                //System.out.println("PD: " + pdStr);

                JsonNode comp = node.at("/InformationPackage/IsDeclaredComplete");
                System.out.println("IsDeclaredComplete as node: " + comp);
                String compStr = comp.asText();
                System.out.println("compStr: " + compStr);

                JsonNode typ = node.at("/InformationPackage/PackageType");
                //System.out.println("PackageType as node: " + typ);
                String typStr = typ.asText();
                //System.out.println("typStr: " + typStr);

                JsonNode siz = node.at("/InformationPackage/InformationObject/DataObject/size");
                //System.out.println("Size as node: " + siz);
                String sizStr = "";
                if (siz.isMissingNode()) {
                    sizStr = "0";
                } else {
                    sizStr = siz.asText();
                }
                //System.out.println("typStr: " + typStr);

                if (!csvStr.equals(init)) csvStr = csvStr + ",";
                csvStr = csvStr + "{" + ident + ",\"PackageType\":" + typ + "," + "\"IsDeclaredComplete\":\"" + compStr + "\"" + ",\"PackageDescription\":\"" + pdStr + "\"" + ",\"size\":\"" + sizStr + "\"}";
                //System.out.println("CSVSTR:\r\n" + csvStr);
            }
            csvStr = csvStr + "],\"totalEntries\":" + totalEntries + ",\"page\":" + page + ",\"size\":" + size + ",\"sortBy\":\"" + sortBy + "\",\"sortDir\":\"" + sortDir + "\",\"query\":\"" + query+"\"}";
            String escapedStr = csvStr.replace("\"", "\\\"");

            ret = "{\"InformationPackage\":{\"version\":\"1.0.0\",\"PackageType\":\"General\",\"IsDeclaredComplete\":false,";
            ret = ret + "\"PackageDescription\":\"This is a list of Representation Information in this repository\",";
            ret = ret + "\"InformationObject\":{\"DataObject\":{\"EncodedObject\":{\"Encoding\":\"OTHER\",\"CustomEncoding\":\"JSON using schema http://www.oais.info/oais-if/json-schema/specificadapter.schema.json\",\"EncodedContent\":\"" + escapedStr + "\"}},";
            ret = ret + "\"RepresentationInformation\":{\"RepInfoCategory\":\"Combined\",\"InformationObject\":{\"IdentifierObject\":{\"IdentifierType\":\"URI\",\"IdentifierString\":\"http://www.oais.info/oais-if/rrori/SpecificAdapterSemantics.txt\"}}}";
            ret = ret + "}}}";

            //System.out.println("InfoPackage is: " + ret);
        }

        return ResponseEntity.ok(ret);
        // return ret;
    }

    // Get PDI by ID and component
    @GetMapping(value = "/information-packages/{ipid}/{component}", produces = "application/json")
    public ResponseEntity<?> getPDIByIDByRequestParam(@PathVariable("ipid") String idStr, @PathVariable("component") String compStr) {
    	if (idStr == null || idStr.trim().isEmpty() || compStr == null || compStr.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("{\"error\":\"Missing or empty idStr or compStr parameter\"}");
        }
        List<RroriEntry> entries = rroriRepository.findByIdStr(idStr);
        if (entries.isEmpty()) {
            return ResponseEntity.status(404).body("{\"error\":\"Entry not found for id: " + idStr + "\"}");
        }
        List<String> result = new ArrayList<>();
        for (RroriEntry entry : entries) {
            result.add(entry.getJsonString());
        }
        return ResponseEntity.ok(result);
    	//        List<RroriEntry> entries = rroriRepository.findByIdStr(idStr);
//        List<String> result = new ArrayList<>();
//        for (RroriEntry entry : entries) {
//            result.add(entry.getJsonString());
//        }
//        return result;
    }

    // Get IO by ID
    @GetMapping(value = "/information-packages/{ipid}/IO", produces = "application/json")
    public ResponseEntity<?> getIOByIDByRequestParam(@RequestParam("ipid") String idStr) {
    	if (idStr == null || idStr.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("{\"error\":\"Missing or empty idStr parameter\"}");
        }
        List<RroriEntry> entries = rroriRepository.findByIdStr(idStr);
        if (entries.isEmpty()) {
            return ResponseEntity.status(404).body("{\"error\":\"Entry not found for id: " + idStr + "\"}");
        }
        List<String> result = new ArrayList<>();
        for (RroriEntry entry : entries) {
            result.add(entry.getJsonString());
        }
        return ResponseEntity.ok(result);
    	
//        List<RroriEntry> entries = rroriRepository.findByIdStr(idStr);
//        List<String> result = new ArrayList<>();
//        for (RroriEntry entry : entries) {
//            result.add(entry.getJsonString());
//        }
//        return result;
    }

    // Get DO by ID
    @GetMapping(value = "/information-packages/{ipid}/DO", produces = "application/json")
    public ResponseEntity<?> getDOByIDByRequestParam(@RequestParam("ipid") String idStr) {
    	if (idStr == null || idStr.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("{\"error\":\"Missing or empty idStr parameter\"}");
        }
        List<RroriEntry> entries = rroriRepository.findByIdStr(idStr);
        if (entries.isEmpty()) {
            return ResponseEntity.status(404).body("{\"error\":\"Entry not found for id: " + idStr + "\"}");
        }
        List<String> result = new ArrayList<>();
        for (RroriEntry entry : entries) {
            result.add(entry.getJsonString());
        }
        return ResponseEntity.ok(result);
        
//        List<RroriEntry> entries = rroriRepository.findByIdStr(idStr);
//        List<String> result = new ArrayList<>();
//        for (RroriEntry entry : entries) {
//            result.add(entry.getJsonString());
//        }
//        return result;
    }
}
