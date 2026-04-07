
package info.oais.oaisif.genericadapter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.ResponseEntity; 

/**
 * REST controller for handling generic adapter operations.
 */
@SuppressWarnings("deprecation")
@RestController
@PropertySource("classpath:genericadapter.properties")
@RequestMapping("/oaisif/v1")// (value="/oaisif/v1", produces = "application/json; charset=UTF-8")
public class GenericAdapterController {
    private static final Logger log = LoggerFactory.getLogger(GenericAdapterController.class);

    @Value("${SPECIFICADAPTER}")
    private String specificAdapterUrl;

    @Value("${SWITCHBOARD}")
    private String switchboardUrl;

    @Autowired
    private GenericAdapterRepository genericAdapterRepository;

    /**
     * Get all configuration parameters needed to use this instance of the Generic Adapter.
     * @return JSON string containing all properties.
     */
    @Operation(summary = "Get the configuration parameters needed to use this instance of the Generic Adapter")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found the configuration parameters"),
        @ApiResponse(responseCode = "400", description = "Cannot find Generic Adapter"),
        @ApiResponse(responseCode = "401", description = "Unauthorized access"),
        @ApiResponse(responseCode = "403", description = "Forbidden request"),
        @ApiResponse(responseCode = "404", description = "Parameters not found"),
        @ApiResponse(responseCode = "405", description = "Method (e.g. PUT, DELETE) Not Allowed")
    })
    @GetMapping(value = "/properties", produces = "application/json")
    public ResponseEntity<String> getJsonAllProperties() {
        List<GenericAdapterEntry> ar = getAllProperties();
        StringBuilder arStr1 = new StringBuilder("[");

        if (ar.size() >= 1 ) {
	        for (int i = 0; i < ar.size(); i++) {
	            GenericAdapterEntry ent = ar.get(i);
	            arStr1.append("{\"propertyName\": \"").append(ent.getPropertyName()).append("\",\"propertyValue\": \"").append(ent.getPropertyValue()).append("\"}");
	            if (i < ar.size() - 1) arStr1.append(",");
	        }
	        arStr1.append("]");
	        String arStr = arStr1.toString().replace("\"", "\\\"");
	
	        String retStr = "{\"InformationPackage\":{\"version\":\"1.0.0\",\"PackageType\":\"General\",\"IsDeclaredComplete\":false,\"PackageDescription\":\"This is a list of properties of this repository\",\"InformationObject\":{\"DataObject\":{\"EncodedObject\":{\"Encoding\":\"OTHER\", \"CustomEncoding\":\"JSON using JSON schema http://www.oais.info/oais-if/json-schema/gaproperties.schema.json\",\"EncodedContent\":\"" + arStr + "\"}},\"RepresentationInformation\":{\"RepInfoCategory\":\"Combined\",\"InformationObject\":{\"IdentifierObject\":{\"IdentifierType\":\"URI\",\"IdentifierString\":\"http://www.oais.info/oais-if/json-schema/GAPropertySemantics.txt\"}}}}}}";
	        
	        return ResponseEntity.ok(retStr);
	   } else {
		   return ResponseEntity.status(404).body("{\"error\":\"Property with this name was not found\"}");
	   }
    }

    /**
     * Retrieve all properties from the repository.
     * @return List of GenericAdapterEntry objects.
     */
    private List<GenericAdapterEntry> getAllProperties() {
        List<GenericAdapterEntry> ar = genericAdapterRepository.findByPropertyName("MYDESCRIPTION");
        ar = Stream.concat(ar.stream(), genericAdapterRepository.findByPropertyName("MYAUTHENTCATIONMETHOD").stream()).toList();
        ar = Stream.concat(ar.stream(), genericAdapterRepository.findByPropertyName("MYSERIALISATIONMETHOD").stream()).toList();
        ar = Stream.concat(ar.stream(), genericAdapterRepository.findByPropertyName("MYCOMMUNICATIONMETHOD").stream()).toList();
        ar = Stream.concat(ar.stream(), genericAdapterRepository.findByPropertyName("MYQUERYMETHOD").stream()).toList();
        return ar;
    }
    
	/**
	 * baseuri/properties/XXX    where XXX is the name of the property
	 * 
	 * @param name   name of the property wanted
	 * @returns The String value associated with that property
	 * 
	 */
	@ResponseBody
	@Operation(summary = "Get the named parameter for the Generic Adapter")
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Found the named configuration parameters", 
	    content = { @Content(mediaType = "application/json" 
	      ) }),
      @ApiResponse(responseCode = "400", description = "Cannot find Generic Adapter"),
      @ApiResponse(responseCode = "401", description = "Unauthorized access"),
      @ApiResponse(responseCode = "403", description = "Forbidden request"),
      @ApiResponse(responseCode = "404", description = "Named parameter not found"),
      @ApiResponse(responseCode = "405", description = "Method (e.g. PUT, DELETE) Not Allowed")
	  })
	@GetMapping(value="/properties/{param}",  produces = "application/json")
	public ResponseEntity<String> getJsonProperty(@PathVariable(value = "param") String param)  { //List<GenericAdapterEntry> getAllProperties() {

		List<GenericAdapterEntry> ar = getAllProperties();
		StringBuilder arStr1 = new StringBuilder("[");
	    boolean found = false;
	    for (GenericAdapterEntry ent : ar) {
	        if (ent.getPropertyName().equals(param)) {
	            arStr1.append("{\"propertyName\": \"").append(ent.getPropertyName())
	                  .append("\",\"propertyValue\": \"").append(ent.getPropertyValue()).append("\"}");
	            found = true;
	        }
	    }
	    arStr1.append("]");
	    if (!found) {
	        return ResponseEntity.status(404).body("{\"error\":\"Property not found\"}");
	    }
	    String arStr = arStr1.toString().replace("\"", "\\\"");
	    String ret = "{\"InformationPackage\":{\"version\":\"1.0.0\",\"PackageType\":\"General\",\"IsDeclaredComplete\":false,"
	        + "\"PackageDescription\":\"This is the value of the selected property of this repository\","
	        + "\"InformationObject\":{\"DataObject\":{\"EncodedObject\":{\"Encoding\":\"OTHER\", \"CustomEncoding\":\"JSON using JSON schema http://www.oais.info/oais-if/json-schema/gaproperties.schema.json\",\"EncodedContent\":\""
	        + arStr + "\"}},"
	        + "\"RepresentationInformation\":{\"RepInfoCategory\":\"Combined\",\"InformationObject\":{\"IdentifierObject\":{\"IdentifierType\":\"URI\",\"IdentifierString\":\"http://www.oais.info/oais-if/json-schema/GAPropertySemantics.txt\"}}"
	        + "}}}}";
	    return ResponseEntity.ok(ret);
	}	

		
	/**
	 * baseuri/information-packages/{id}   where {id} is the identifier for the IP
	 * 
	 */
	@ResponseBody
	@Operation(summary = "Get the specified IP with the given id.")
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Found the Information Package with this id", 
	    content = { @Content(mediaType = "application/json") }),
      @ApiResponse(responseCode = "400", description = "Cannot find Generic Adapter"),
      @ApiResponse(responseCode = "401", description = "Unauthorized access"),
      @ApiResponse(responseCode = "403", description = "Forbidden request"),
      @ApiResponse(responseCode = "404", description = "Specified Information Package not found"),
      @ApiResponse(responseCode = "405", description = "Method (e.g. PUT, DELETE) Not Allowed")
	  })
	@GetMapping(value="/information-packages/{id}", produces = "application/json")
	public ResponseEntity<String> getIPByIPIDByRequestParam( 
			@PathVariable(value = "id") String idStr)  {
		
		JsonNode node=null;
		ObjectMapper mapper = new ObjectMapper();
	    
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity <String> entity = new HttpEntity<String>(headers);
	    RestTemplate restTemplate = new RestTemplate();   

	    //String cStr = restTemplate.exchange(specificAdapterUrl+"/oaisif/v1/specific-adapter/information-packages/"+idStr, HttpMethod.GET, entity, String.class).getBody();
	    //System.out.println("IP is "+cStr);
	    try {
	        ResponseEntity<String> response = restTemplate.exchange(
	            specificAdapterUrl + "/oaisif/v1/specific-adapter/information-packages/" + idStr,
	            HttpMethod.GET, entity, String.class);

	        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && !response.getBody().isEmpty()) {
	            return ResponseEntity.ok(response.getBody());
	        } else if (response.getStatusCode().is4xxClientError()) {
	            return ResponseEntity.status(404).body("{\"error\":\"IP with this Id was not found\"}");
	        } else {
	            return ResponseEntity.status(500).body("{\"error\":\"Unexpected error from Specific Adapter\"}");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(404).body("{\"error\":\"InfoPackage with this Id was not found\"}");
	    }
	}	
	
	/**
	 * baseuri/information-packages/{id}/{component}    where {id} is the identifier for the IP
	 * 
	 */
	@ResponseBody
	@Operation(summary = "Get the specified component from the IP with the given id. The component may be IO (InformationObject), DO (DataObject), RI (RepresentationInformation) or PDI")
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Found the ", 
	    content = { @Content(mediaType = "application/json") }),
      @ApiResponse(responseCode = "400", description = "Cannot find Generic Adapter"),
      @ApiResponse(responseCode = "401", description = "Unauthorized access"),
      @ApiResponse(responseCode = "403", description = "Forbidden request"),
      @ApiResponse(responseCode = "404", description = "Specified component from the IP with the given id not found"),
      @ApiResponse(responseCode = "405", description = "Method (e.g. PUT, DELETE) Not Allowed") 
	  })
	@GetMapping(value="/information-packages/{id}/{component}", produces = "application/json")
	public ResponseEntity<String>  getComponentByIPIDByRequestParam( 
			@PathVariable(value = "id") String idStr, @PathVariable(value = "component") String compStr )  {

/**
 * Need to get component then package that as Info Object in IP
 */
				
	    
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity <String> entity = new HttpEntity<String>(headers);
	    RestTemplate restTemplate = new RestTemplate();   
	    
	    try {
	        ResponseEntity<String> response = restTemplate.exchange(
	            specificAdapterUrl + "/oaisif/v1/specific-adapter/information-packages/" + idStr + "/" + compStr,
	            HttpMethod.GET, entity, String.class);

	        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && !response.getBody().isEmpty()) {
	            return ResponseEntity.ok(response.getBody());
	        } else if (response.getStatusCode().is4xxClientError()) {
	            return ResponseEntity.status(404).body("{\"error\":\"IP or IP component not found\"}");
	        } else {
	            return ResponseEntity.status(500).body("{\"error\":\"Unexpected error from Specific Adapter\"}");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(404).body("{\"error\":\"IP or IP component not found\"}");
	    }
	    //String cStr = restTemplate.exchange(specificAdapterUrl+"/oaisif/v1/specific-adapter/information-packages/"+idStr+"/"+compStr, HttpMethod.GET, entity, String.class).getBody();
	    
	    //return cStr;
	}
	

	
	/**
	 * baseuri/information-packages
	 * @param queryd The String to use as a query
	 * @return The JSON for the list of IPs
	 */
	@ResponseBody
	@Operation(summary = "Get list of all IPs from the associated Specific Adapter. Use ?query=String to send a query, consistent with MYQUERYMETHOD")
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Found the list of IPs", content = { @Content(mediaType = "application/json") }),
	  @ApiResponse(responseCode = "400", description = "Cannot find Generic Adapter"),
      @ApiResponse(responseCode = "401", description = "Unauthorized access"),
      @ApiResponse(responseCode = "403", description = "Forbidden request"),
      @ApiResponse(responseCode = "404", description = "List of Information Packages not found"),
      @ApiResponse(responseCode = "405", description = "Method (e.g. PUT, DELETE) Not Allowed"), 
	  @ApiResponse(responseCode = "404", description = "Specific Adapter not found", content = @Content) })
	@GetMapping(value="/information-packages", produces = "application/json")
	public ResponseEntity<String> getAllIPs(
			@Parameter(description = "page=n The initial page to start listing, (first page is 0). If n<0 then n is set to 0")  @RequestParam(defaultValue="0" )int page,
    		@Parameter(description = "size=m The page size i.e. number of entries to list. If m<1 then m set to 10")  @RequestParam(defaultValue="20")int size,
    		@Parameter(description = "sortBy= Sort entries by either IsDeclaredComplete, PackageType, PackageDescription or id") @RequestParam(defaultValue="id") String sortBy,
    		@Parameter(description = "sortDir= The sort direction asc (ascending) or desc (descending).")@RequestParam(defaultValue = "asc") String sortDir,
    		@Parameter(description = "query= The query string to filter the results, conforming to the information in MYQUERYMETHOD property, with a default to return details of IPs where the Package Description contains the string") @RequestParam(defaultValue = "") String query  ){
	    System.out.println("XXXXSpecificAdapter is:" + specificAdapterUrl);
	    
	    
	    if (page < 0 || size < 1) {
	        return ResponseEntity.badRequest().body("{\"error\":\"Invalid page\"," + page+ " or size parameter\", " + size + " }");
	    }
	    if (!sortBy.equals("IsDeclaredComplete") && !sortBy.equals("PackageType")
	        && !sortBy.equals("PackageDescription") && !sortBy.equals("id")) {
	        return ResponseEntity.badRequest().body("{\"error\":\"Invalid sortBy parameter\", " + sortBy + " }");
	    }
	    if (!sortDir.equals("asc") && !sortDir.equals("desc")) {
	        return ResponseEntity.badRequest().body("{\"error\":\"Invalid sortDir parameter\", " + sortDir + " }");
	    }
	    HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity<String> entity = new HttpEntity<>(headers);
	    RestTemplate restTemplate = new RestTemplate();
	    String restStr = specificAdapterUrl + "/oaisif/v1/specific-adapter/information-packages"
	            + "?page=" + page + "&size=" + size + "&sortBy=" + sortBy + "&sortDir=" + sortDir + "&query=" + query;
	    try {
	        ResponseEntity<String> response = restTemplate.exchange(restStr, HttpMethod.GET, entity, String.class);

	        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
	            return ResponseEntity.ok(response.getBody());
	        } else if (response.getStatusCode().is4xxClientError()) {
	            return ResponseEntity.status(404).body("{\"error\":\"IP or IP component not found or Specific Adapter not found\", "+ "\"details\":\"" + restStr + "\"}");
	        } else {
	            return ResponseEntity.status(500).body("{\"error\":\"Unexpected error from Specific Adapter\", \"details\":\" " + restStr + "\"}");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(404).body("{\"error\":\"IP or IP component not found or Specific Adapter not found\"," + restStr + "\"}");
	    }
	    
	    
	    
	    // Validate the parameters
	    
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
//		System.out.println("XXXXSpecificAdapter is:" + specificAdapterUrl);
//		System.out.println(
//				"XXXXSpecificAdapter is:" + specificAdapterUrl + "/oaisif/v1/specific-adapter/information-packages"
//						+ "?page=" + page + "&size=" + size + "&sortBy=" + sortBy + "&sortDir=" + sortDir+"&query"+ query);
//		// Create the headers and entity for the request
//		HttpHeaders headers = new HttpHeaders();
//	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//	    HttpEntity <String> entity = new HttpEntity<String>(headers);
//	    RestTemplate restTemplate = new RestTemplate();  
//	    
//	    // Remove quotes around the query if necessary
//	    
//	    String aips;
//	    //System.out.println("******REQUEST IS:"+specificAdapterUrl+"/oaisif/v1/specific-adapter/information-packages"+"?page="+page+"&size="+size+"&sortBy="+sortBy+"&sortDir="+sortDir+"&query"+ query);
//	    aips = restTemplate.exchange(specificAdapterUrl+"/oaisif/v1/specific-adapter/information-packages"+"?page="+page+"&size="+size+"&sortBy="+sortBy+"&sortDir="+sortDir+"&query="+ query, HttpMethod.GET, entity, String.class).getBody();
//        //System.out.println("********aips is:"+aips);
//	    return aips;
	}
	
	
	/**
	 * SWITCHBOARD access
	 */
	
	@ResponseBody
	@Operation(summary = "Get a list of all the Repositories known from the associated SwitchBoard")
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Found the list of all known repositories", 
	    content = { @Content(mediaType = "application/json") }),
	  @ApiResponse(responseCode = "400", description = "Cannot find Generic Adapter or SwitchBoard"),
      @ApiResponse(responseCode = "401", description = "Unauthorized access"),
      @ApiResponse(responseCode = "403", description = "Forbidden request"),
      @ApiResponse(responseCode = "404", description = "List of all the Repositories known from the associated SwitchBoard not found"),
      @ApiResponse(responseCode = "405", description = "Method (e.g. PUT, DELETE) Not Allowed")  
	  })
	@GetMapping(value="/sources", produces = "application/json")
	public ResponseEntity<String> getBySwitchboardAll(
    		@Parameter(description = "page=n The initial page to start listing, (first page is 0). If n<0 then n is set to 0")  @RequestParam(defaultValue="0" )int page,
    		@Parameter(description = "size=m The page size i.e. number of entries to list. If m<1 then m set to 20")  @RequestParam(defaultValue="20")int size,
    		@Parameter(description = "sortBy= Sort entries by either ArchiveName, ArchiveURL, ArchiveDescription or id") @RequestParam(defaultValue="id") String sortBy,
    		@Parameter(description = "sortDir= The sort direction asc (ascending) or desc (descending).")@RequestParam(defaultValue = "asc") String sortDir,
    		@Parameter(description = "query= The query string to filter the results, conforming to the information in MYQUERYMETHOD property, with a default to return details of IPs where the Archive Description contains the string") @RequestParam(defaultValue = "") String query) {
		
		
	    // Validate the parameters
	    
		if (page < 0) {
			page = 0;
		}
		if (size < 1) {
			size = 20;
		}
		if (!sortBy.equals("ArchiveName") && !sortBy.equals("ArchiveURL")
				&& !sortBy.equals("ArchiveDescription") && !sortBy.equals("id")) {
			sortBy = "id";
		}
		if (!sortDir.equals("asc") && !sortDir.equals("desc")) {
			sortDir = "asc";
		}
		
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity<String> entity = new HttpEntity<>(headers);
	    RestTemplate restTemplate = new RestTemplate();
		
		try {
		    ResponseEntity<String> response = restTemplate.exchange(
		        switchboardUrl + "/oaisif/v1/switchboard/sources"
		        + "?page=" + page + "&size=" + size + "&sortBy=" + sortBy + "&sortDir=" + sortDir + "&query=" + query,
		        HttpMethod.GET, entity, String.class);

		    if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && !response.getBody().isEmpty()) {
		        return ResponseEntity.ok(response.getBody());
		    } else if (response.getStatusCode().is4xxClientError()) {
		        return ResponseEntity.status(404).body("{\"error\":\"SwitchBoard sources not found\"}");
		    } else {
		        return ResponseEntity.status(500).body("{\"error\":\"Unexpected error from SwitchBoard\"}");
		    }
		} catch (Exception e) {
		    return ResponseEntity.status(404).body("{\"error\":\"SwitchBoard sources not found\"}");
		}
    	
		//System.out.println("/switchboard/sources  being used ");
	    //System.out.println("Switchboard is:" + switchboardUrl);
//		HttpHeaders headers = new HttpHeaders();
//	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
//	    HttpEntity <String> entity = new HttpEntity<String>(headers);
//	    RestTemplate restTemplate = new RestTemplate();   
//
//	    String aips = restTemplate.exchange(switchboardUrl+"/oaisif/v1/switchboard/sources"+"?page="+page+"&size="+size+"&sortBy="+sortBy+"&sortDir="+sortDir+"&query="+ query, HttpMethod.GET, entity, String.class).getBody();
//	    return aips;
	}
	
	@ResponseBody
	@Operation(summary = "Get the details of the named source, e.g. RRORI, from the associated SwitchBoard")
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Found the named repository", 
	    content = { @Content(mediaType = "application/json") }),
	  @ApiResponse(responseCode = "400", description = "Cannot find Generic Adapter or SwitchBoard"),
      @ApiResponse(responseCode = "401", description = "Unauthorized access"),
      @ApiResponse(responseCode = "403", description = "Forbidden request"),
      @ApiResponse(responseCode = "404", description = "Named Repository not found in SwitchBoard"),
      @ApiResponse(responseCode = "405", description = "Method (e.g. PUT, DELETE) Not Allowed") })
	@GetMapping(value="/sources/{name}", produces = "application/json")
	public ResponseEntity<String> getBySwitchboardName(
			@PathVariable(value = "name") String name) {
		//System.out.println("/switchboard/sources/" + name + "  being used ");
	    //System.out.println("Switchboard is:" + switchboardUrl);
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity <String> entity = new HttpEntity<String>(headers);
	    RestTemplate restTemplate = new RestTemplate();   
	    try {
	        ResponseEntity<String> response = restTemplate.exchange(
	            switchboardUrl + "/oaisif/v1/switchboard/sources/" + name,
	            HttpMethod.GET, entity, String.class);

	        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && !response.getBody().isEmpty()) {
	            return ResponseEntity.ok(response.getBody());
	        } else if (response.getStatusCode().is4xxClientError()) {
	            return ResponseEntity.status(404).body("{\"error\":\"Repository not found in SwitchBoard\"}");
	        } else {
	            return ResponseEntity.status(500).body("{\"error\":\"Unexpected error from SwitchBoard\"}");
	        }
	    } catch (Exception e) {
	        return ResponseEntity.status(404).body("{\"error\":\"Repository not found in SwitchBoard\"}");
	    }
//	    String aips = restTemplate.exchange(switchboardUrl+"/oaisif/v1/switchboard/sources/"+name, HttpMethod.GET, entity, String.class).getBody();
//	    return aips;
	}

}
