
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

/**
 * REST controller for handling generic adapter operations.
 */
@RestController
@PropertySource("classpath:genericadapter.properties")
@RequestMapping("/oaisif/v1")
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
        @ApiResponse(responseCode = "404", description = "Generic Adapter not found"),
        @ApiResponse(responseCode = "405", description = "Method Not Allowed")
    })
    @GetMapping(value = "/properties", produces = "application/json")
    public String getJsonAllProperties() {
        List<GenericAdapterEntry> ar = getAllProperties();
        StringBuilder arStr1 = new StringBuilder("[");

        for (int i = 0; i < ar.size(); i++) {
            GenericAdapterEntry ent = ar.get(i);
            arStr1.append("{\"propertyName\": \"").append(ent.getPropertyName()).append("\",\"propertyValue\": \"").append(ent.getPropertyValue()).append("\"}");
            if (i < ar.size() - 1) arStr1.append(",");
        }
        arStr1.append("]");
        String arStr = arStr1.toString().replace("\"", "\\\"");

        return "{\"InformationPackage\":{\"version\":\"1.0.0\",\"PackageType\":\"General\",\"IsDeclaredComplete\":false,\"PackageDescription\":\"This is a list of properties of this repository\",\"InformationObject\":{\"DataObject\":{\"EncodedObject\":{\"Encoding\":\"OTHER\", \"CustomEncoding\":\"JSON using JSON schema http://www.oais.info/oais-if/json-schema/gaproperties.schema.json\",\"EncodedContent\":\"" + arStr + "\"}},\"RepresentationInformation\":{\"RepInfoCategory\":\"Combined\",\"InformationObject\":{\"IdentifierObject\":{\"IdentifierType\":\"URI\",\"IdentifierString\":\"http://www.oais.info/oais-if/json-schema/GAPropertySemantics.txt\"}}}}}}";
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
	 * baseuri/GetProperty?name=xxx    where XXX is the name of the property
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
	  @ApiResponse(responseCode = "400", description = "Cannot find Generic Adapter", 
	    content = @Content), 
	  @ApiResponse(responseCode = "401", description = "Unauthorized access", 
	    content = @Content), 
	  @ApiResponse(responseCode = "403", description = "Forbidden request", 
	    content = @Content), 
	  @ApiResponse(responseCode = "404", description = "Generic Adapter not found", 
	    content = @Content),
	  @ApiResponse(responseCode = "405", description = "Method Not Allowed", content = @Content) })
	@GetMapping(value="/properties/{param}",  produces = "application/json")
	public String getJsonProperty(@PathVariable(value = "param") String param)  { //List<GenericAdapterEntry> getAllProperties() {

		List<GenericAdapterEntry> ar = getAllProperties();
		String arStr1 = null;

		System.out.println("ar size :"+ ar.size() + ","+ar.toString());
		arStr1 = "[";
		System.out.println(ar);
		GenericAdapterEntry ent = null;
		for (int i = 0; i< ar.size(); i++) {
			ent = ar.get(i);
			if (ent.getPropertyName().equals(param) ) {
				arStr1 = arStr1 + "{\"propertyName\": \"" + ent.getPropertyName() + "\",\"propertyValue\": \""+ ent.getPropertyValue() + "\"}";
			}
			
		}
		arStr1 = arStr1 + "]";
		String arStr = arStr1.replace("\"", "\\\"");
		System.out.println("arstr:" + arStr);
		
		String ret = "{\"InformationPackage\":{\"version\":\"1.0.0\",\"PackageType\":\"General\",\"IsDeclaredComplete\":false,";
		ret = ret + "\"PackageDescription\":\"This is the value of the selected property of this repository\",";
		ret = ret + "\"InformationObject\":{\"DataObject\":{\"EncodedObject\":{\"Encoding\":\"OTHER\", \"CustomEncoding\":\"JSON using JSON schema http://www.oais.info/oais-if/json-schema/gaproperties.schema.json\",\"EncodedContent\":\""+ arStr + "\"}},";
		ret = ret + "\"RepresentationInformation\":{\"RepInfoCategory\":\"Combined\",\"InformationObject\":{\"IdentifierObject\":{\"IdentifierType\":\"URI\",\"IdentifierString\":\"http://www.oais.info/oais-if/json-schema/GAPropertySemantics.txt\"}}";
		ret = ret + "}}}}";
		
		return ret;	
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
	  @ApiResponse(responseCode = "400", description = "Could not find the component in the IP", 
	    content = @Content), 
	  @ApiResponse(responseCode = "404", description = "IP not found", 
	    content = @Content) })
	@GetMapping(value="/information-packages/{id}/{component}", produces = "application/json")
	public String getComponentByIPIDByRequestParam( 
			@PathVariable(value = "id") String idStr, @PathVariable(value = "component") String compStr )  {

/**
 * Need to create AIP then get PDI then package that as Info Object in IP
 */
				
	    System.out.println("XXXYYYZZZSpecificAdapter is:" + specificAdapterUrl);
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity <String> entity = new HttpEntity<String>(headers);
	    RestTemplate restTemplate = new RestTemplate();   

	    String cStr = restTemplate.exchange(specificAdapterUrl+"/oaisif/v1/specific-adapter/information-packages/"+idStr+"/"+compStr, HttpMethod.GET, entity, String.class).getBody();
	    System.out.println(compStr + " is:"+cStr);
	    //return "{ \"InformationPackage\": {\"version\": \"1.0\", \"PackageType\": \"General\", \"PackageDescription\": \"This is the " + compStr + " of IP " + ipid + "\",  \"InformationObject\": {\"PDI\":" + pdi + "}}}";
	    return cStr;
	}
	
	/**
	 * baseuri/information-packages/{id}   where {id} is the identifier for the IP
	 * 
	 */
	@ResponseBody
	@Operation(summary = "Get the specified IP with the given id.")
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Found the IP", 
	    content = { @Content(mediaType = "application/json") }),
	  @ApiResponse(responseCode = "400", description = "Could not find the IP", 
	    content = @Content), 
	  @ApiResponse(responseCode = "404", description = "IP not found", 
	    content = @Content) })
	@GetMapping(value="/information-packages/{id}", produces = "application/json")
	public String getIPByIPIDByRequestParam( 
			@PathVariable(value = "id") String idStr)  {
		
		JsonNode node=null;
		ObjectMapper mapper = new ObjectMapper();
	    System.out.println("XXXYYYSpecificAdapter is:" + specificAdapterUrl);
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity <String> entity = new HttpEntity<String>(headers);
	    RestTemplate restTemplate = new RestTemplate();   

	    String cStr = restTemplate.exchange(specificAdapterUrl+"/oaisif/v1/specific-adapter/information-packages/"+idStr, HttpMethod.GET, entity, String.class).getBody();
	    System.out.println("IP is "+cStr);
	    try {
			node = mapper.readTree(cStr);
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    //return "{ \"InformationPackage\": {\"version\": \"1.0\", \"PackageType\": \"General\", \"PackageDescription\": \"This is the " + compStr + " of IP " + ipid + "\",  \"InformationObject\": {\"PDI\":" + pdi + "}}}";
	    

	    return cStr;
	}	
	
	/**
	 * baseuri/information-packages/xxx    where XXX is the identifier
	 * @param ipid The String to identify the IP
	 * @return The JSON for the IP
	 */
	@ResponseBody
	@Operation(summary = "Get list of all IPs from the associated Specific Adapter. Use ?query=String to send a query, consistent with MYQUERYMETHOD")
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Found the list of IPs", 
	    content = { @Content(mediaType = "application/json") }),
	  @ApiResponse(responseCode = "400", description = "Could not find list of IPs", 
	    content = @Content), 
	  @ApiResponse(responseCode = "404", description = "Specific Adapter not found", 
	    content = @Content) })
	@GetMapping(value="/information-packages", produces = "application/json")
	public String getAllIPs(
			@Parameter(description = "page=n The initial page to start listing, (first page is 0).")  @RequestParam(defaultValue="0" )int page,
    		@Parameter(description = "size=m The page size i.e. number of entries to list.")  @RequestParam(defaultValue="20")int size,
    		@Parameter(description = "sortBy= Sort entries by either IsDeclaredComplete, PackageType, PackageDescription or id") @RequestParam(defaultValue="id") String sortBy,
    		@Parameter(description = "sortDir= The sort direction asc (ascending) or desc (descending).")@RequestParam(defaultValue = "asc") String sortDir){
	    System.out.println("XXXXSpecificAdapter is:" + specificAdapterUrl);
	    
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity <String> entity = new HttpEntity<String>(headers);
	    RestTemplate restTemplate = new RestTemplate();  
	    
	    // Remove quotes around the query if necessary
	    
	    String aips;
	    System.out.println("******REQUEST IS:"+specificAdapterUrl+"/oaisif/v1/specific-adapter/information-packages"+"?page="+page+"&size="+size+"&sortBy="+sortBy+"&sortDir="+sortDir);
	    aips = restTemplate.exchange(specificAdapterUrl+"/oaisif/v1/specific-adapter/information-packages"+"?page="+page+"&size="+size+"&sortBy="+sortBy+"&sortDir="+sortDir, HttpMethod.GET, entity, String.class).getBody();
        System.out.println("********aips is:"+aips);
	    return aips;
	}
	
	
	/**
	 * SWITCHBOARD access
	 */
	
	@ResponseBody
	@Operation(summary = "Get a list of all the Repositories known from the associated SwitchBoard")
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Found the list of all known repositories", 
	    content = { @Content(mediaType = "application/json") }),
	  @ApiResponse(responseCode = "400", description = "Could not find the list of respositories", 
	    content = @Content), 
	  @ApiResponse(responseCode = "404", description = "SwitchBoard not found", 
	    content = @Content) })
	@GetMapping(value="/sources", produces = "application/json")
	public String getBySwitchboardAll() {
		System.out.println("/switchboard/sources  being used ");
	    System.out.println("Switchboard is:" + switchboardUrl);
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity <String> entity = new HttpEntity<String>(headers);
	    RestTemplate restTemplate = new RestTemplate();   

	    String aips = restTemplate.exchange(switchboardUrl+"/oaisif/v1/switchboard/sources", HttpMethod.GET, entity, String.class).getBody();
	    return aips;
	}
	
	@ResponseBody
	@Operation(summary = "Get the details of the named source, e.g. RRORI, from the associated SwitchBoard")
	@ApiResponses(value = { 
	  @ApiResponse(responseCode = "200", description = "Found the named repository", 
	    content = { @Content(mediaType = "application/json") }),
	  @ApiResponse(responseCode = "400", description = "Could not find the list of respositories", 
	    content = @Content), 
	  @ApiResponse(responseCode = "404", description = "SwitchBoard not found", 
	    content = @Content) })
	@GetMapping(value="/sources/{name}", produces = "application/json")
	public String getBySwitchboardName(
			@PathVariable(value = "name") String name) {
		System.out.println("/switchboard/sources/" + name + "  being used ");
	    System.out.println("Switchboard is:" + switchboardUrl);
		HttpHeaders headers = new HttpHeaders();
	    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	    HttpEntity <String> entity = new HttpEntity<String>(headers);
	    RestTemplate restTemplate = new RestTemplate();   

	    String aips = restTemplate.exchange(switchboardUrl+"/oaisif/v1/switchboard/sources/"+name, HttpMethod.GET, entity, String.class).getBody();
	    return aips;
	}

}
