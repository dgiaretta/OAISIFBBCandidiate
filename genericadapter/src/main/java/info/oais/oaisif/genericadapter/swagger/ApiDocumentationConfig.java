package info.oais.oaisif.genericadapter.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
/**
 * Reference : <a href="https://support.intershop.com/kb/index.php/Display/2914L4">...</a>
 */
@OpenAPIDefinition(
        info = @Info(
                description = "GenericAdapter",
                version = "1",
                title = "Generic Adapter API",
                contact = @Contact(
                        name = "David Giaretta",
                        email = "david@giaretta.org",
                        url = "http://www.giaretta.org"
                ),
                license = @License(
                        name = "Apache 2.0",
                        url = "http://www.apache.org/licenses/LICENSE-2.0"
                )
        ),
        servers = {@Server(url = "http://localhost")}
)
public interface ApiDocumentationConfig {

}