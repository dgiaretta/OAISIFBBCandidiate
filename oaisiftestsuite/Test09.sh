#!/bin/bash

rm getsources.json
echo "********************************************************************"
echo "*** TEST 09   get the list of known sources from the switchboard ***"
echo "********************************************************************"

cmd="curl -X GET $SbUrl/oaisif/v1/switchboard/sources -H accept:application/json"
echo "****************************************************************************"
echo "***executing***" "$cmd"
echo "****************************************************************************"
echo $cmd | bash > getsources.json

cat getsources.json | jq

# Validate that this is a valid OAIS-IF IP
java -jar packagevalidator-1.0-SNAPSHOT.jar  getsources.json

# Extract the list
cat getsources.json | jq '.InformationPackage.InformationObject.DataObject.EncodedObject.EncodedContent' | sed 's/\\//g' | awk '{print substr($0, 2, length($0) - 2)}' | jq

#Check the optional parameters
curl -X GET "http://www.oais.info:8085/oaisif/v1/switchboard/sources?page=0&size=30&sortBy=archiveName&sortDir=desc&query=PDS"   >  getselectedsources.json 

# Extract the list
cat getselectedsources.json | jq '.InformationPackage.InformationObject.DataObject.EncodedObject.EncodedContent' | sed 's/\\//g' | awk '{print substr($0, 2, length($0) - 2)}' | jq
