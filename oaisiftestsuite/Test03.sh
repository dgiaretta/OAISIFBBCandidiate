#!/bin/bash

rm getproperties.json
echo "****************************************************************************"
echo "*** TEST 03 Request the Generic Adapter property details and display them nicely by extracting the EncodedContent as JSON, validate it and pretty printing the JSON. ***"
echo "****************************************************************************"

IpIdUrl=$(urlencode $IpId)
cmd="curl -X GET $GaUrl/oaisif/v1/properties -H accept:application/json"
echo "****************************************************************************"
echo "***executing***" "$cmd"
echo "****************************************************************************"
echo $cmd | bash > getproperties.json

# Validate that this is a valid OAIS-IF IP
java -jar packagevalidator-1.0-SNAPSHOT.jar  getproperties.json

# Extract the properties  and display
cat getproperties.json| jq '.InformationPackage.InformationObject.DataObject.EncodedObject.EncodedContent' | sed 's/\\//g' | awk '{print substr($0, 2, length($0) - 2)}' | jq
