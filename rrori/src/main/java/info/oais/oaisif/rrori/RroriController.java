
package info.oais.oaisif.rrori;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

//import info.oais.oaisif.specificadapter.SpecificAdapterEntry;
//import info.oais.oaisif.rrori.RroriEntry;


@RestController
@RequestMapping("/oaisif/v1/representation-info-repository")
public class RroriController {

    @Autowired
    private RroriRepository rroriRepository;

    // Get an AIP by its identifier
    @GetMapping(value = "/information-packages/{ipid}", produces = "application/json")
    public String getAIPByDOIDByRequestParam(@PathVariable("ipid") String ipid) {
        List<RroriEntry> entries = rroriRepository.findByIdStr(ipid);
        if (entries.isEmpty()) {
            return "{}"; // Return empty JSON if no entries found
        }
        return entries.get(0).getJsonString(); // Return the JSON string of the first entry
    }

    // Get AIPs with identifiers like the given name
    @GetMapping(value = "/information-packages-like/{name}", produces = "application/json")
    public List<RroriEntry> getByAIPNameLikeByRequestParam(@PathVariable("name") String name) {
        return rroriRepository.findByIdStrLike(name);
    }

    // Get all AIPs
    @GetMapping(value = "/information-packages", produces = "application/json")
    public String getByRIAll() {
        List<RroriEntry> ar = (List<RroriEntry>)rroriRepository.findAll();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        String csvStr = "[";
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

                System.out.println(" Node is:" + node);

                JsonNode pd = node.at("/InformationPackage/PackageDescription");
                System.out.println("PackageDescription as node: " + pd);
                String pdStr = pd.asText();

                //if (query != null && !pdStr.contains(query)) continue;

                System.out.println("PD: " + pdStr);

                JsonNode comp = node.at("/InformationPackage/IsDeclaredComplete");
                System.out.println("IsDeclaredComplete as node: " + comp);
                String compStr = comp.asText();
                System.out.println("compStr: " + compStr);

                JsonNode typ = node.at("/InformationPackage/PackageType");
                System.out.println("PackageType as node: " + typ);
                String typStr = typ.asText();
                System.out.println("typStr: " + typStr);

                JsonNode siz = node.at("/InformationPackage/InformationObject/DataObject/size");
                System.out.println("Size as node: " + siz);
                String sizStr = "";
                if (siz.isMissingNode()) {
                    sizStr = "0";
                } else {
                    sizStr = siz.asText();
                }
                System.out.println("typStr: " + typStr);

                if (csvStr != "[") csvStr = csvStr + ",";
                csvStr = csvStr + "{" + ident + ",\"PackageType\":" + typ + "," + "\"IsDeclaredComplete\":\"" + compStr + "\"" + ",\"PackageDescription\":\"" + pdStr + "\"" + ",\"size\":\"" + sizStr + "\"}";
                System.out.println("CSVSTR:\r\n" + csvStr);
            }
            csvStr = csvStr + "]";
            String escapedStr = csvStr.replace("\"", "\\\"");

            ret = "{\"InformationPackage\":{\"version\":\"1.0.0\",\"PackageType\":\"General\",\"IsDeclaredComplete\":false,";
            ret = ret + "\"PackageDescription\":\"This is a list of Representation Information in this repository\",";
            ret = ret + "\"InformationObject\":{\"DataObject\":{\"EncodedObject\":{\"Encoding\":\"OTHER\",\"CustomEncoding\":\"JSON using schema http://www.oais.info/oais-if/json-schema/specificadapter.schema.json\",\"EncodedContent\":\"" + escapedStr + "\"}},";
            ret = ret + "\"RepresentationInformation\":{\"RepInfoCategory\":\"Combined\",\"InformationObject\":{\"IdentifierObject\":{\"IdentifierType\":\"URI\",\"IdentifierString\":\"http://www.oais.info/oais-if/rrori/SpecificAdapterSemantics.txt\"}}}";
            ret = ret + "}}}";

            System.out.println("InfoPackage is: " + ret);
        }

        return ret;
    }

    // Get PDI by ID and component
    @GetMapping(value = "/information-packages/{ipid}/{component}", produces = "application/json")
    public List<String> getPDIByIDByRequestParam(@PathVariable("ipid") String idStr, @PathVariable("component") String compStr) {
        List<RroriEntry> entries = rroriRepository.findByIdStr(idStr);
        List<String> result = new ArrayList<>();
        for (RroriEntry entry : entries) {
            result.add(entry.getJsonString());
        }
        return result;
    }

    // Get IO by ID
    @GetMapping(value = "/information-packages/{ipid}/IO", produces = "application/json")
    public List<String> getIOByIDByRequestParam(@RequestParam("ipid") String idStr) {
        List<RroriEntry> entries = rroriRepository.findByIdStr(idStr);
        List<String> result = new ArrayList<>();
        for (RroriEntry entry : entries) {
            result.add(entry.getJsonString());
        }
        return result;
    }

    // Get DO by ID
    @GetMapping(value = "/information-packages/{ipid}/DO", produces = "application/json")
    public List<String> getDOByIDByRequestParam(@RequestParam("ipid") String idStr) {
        List<RroriEntry> entries = rroriRepository.findByIdStr(idStr);
        List<String> result = new ArrayList<>();
        for (RroriEntry entry : entries) {
            result.add(entry.getJsonString());
        }
        return result;
    }
}
