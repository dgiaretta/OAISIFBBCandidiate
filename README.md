Create executable jar files

mvn build package

- WINDOWS: javaw -jar xxxxx.jar
  - jps - to get list of processes
  - taskkill /F /PID nnn
- UBUNTU:  $JAVA_HOME/bin/java -jar xxxx.jar &
 - oaisifga.service 
 - oaisifrrori.service
 - oaisifsa.service 
 - oaisifsb.service

On command line for running JAR one can set parameter e.g. add --server.port=xxxx

# DEFAULTS:
- GA: 8765
- RRORI: 8083
- SWITCHBOARD: 8085
- SPECIFIC ADAPTER: 8510


# GENERIC ADAPTER
http://www.oais.info:8765/oaisif/v1/properties
- shows the properties needed to communicate with the server

http://www.oais.info:8765/oaisif/v1/properties/MYDESCRIPTION
- to select the value of one of the properties

http://www.oais.info:8765/oaisif/v1/information-packages		
- returns table listing the AIPs and their Package Descriptions.
- The table is in the form of an array (i.e. the rows) of arrays (containing the values in the columns). The first row gives the names of the columns.
- Note that JSON arrays maintain the order of the elements see https://datatracker.ietf.org/doc/html/rfc8259#page-7

http://www.oais.info:8765/oaisif/v1/information-packages/XXXX
- e.g. http://www.oais.info:8765/oaisif/v1/information-packages/596245458 - returns JSON AIP for a FITS file with links to "reasonable" RepInfo etc.

http://www.oais.info:8765/oaisif/v1/information-packages/XXXX/IO
- returns JSON containing the Information Object of that IP

http://www.oais.info:8765/oaisif/v1/information-packages/XXXX/DO
- returns JSON containing the (Content) Data Object of that IP

http://www.oais.info:8765/oaisif/v1/information-packages/XXXX/PDI
- returns JSON containing the PDI of that IP - only guaranteed to exist in an AIP

http://www.oais.info:8765/v3/api-docs.yaml
- returns the YAML
- 
http://www.oais.info:8765/swagger-ui/index.html
- SWAGGER page which can ge used to test the interfaces


# SWITCHBOARD
http://www.oais.info:8085/oaisif/v1/switchboard/sources
- note the one with archiveURL http://www.oais.info:8765
- note the one with archiveName RRORI i.e. http://www.oais.info:8085/oaisif/v1/switchboard/sources/RRORI

# RRORI
http://www.oais.info:8083/oaisif/v1/representation-info-repository/information-packages
- list all the pieces of RepInfo
- the RI is in Information Packages, which could be an AIP

http://www.oais.info:8083/oaisif/v1/representation-info-repository/information-packages/457598613
- lists a particular Information Package with RepInfo with given ID



# FOR INTERNAL use in this implementation
## SPECIFIC ADAPTER
http://www.oais.info:8510/oaisif/v1/specific-adapter/information-packages
- list all the IPs - including AIPs

