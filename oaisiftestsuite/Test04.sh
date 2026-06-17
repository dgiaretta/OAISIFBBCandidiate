#!/bin/bash

rm getmyproperty.json

echo "*********************************************************************************************************************"
echo "*** TEST 04 Request the MYCOMMUNICATION from the Generic Adapter, extract the EncodedContent as JSON and display. ***"
echo "*********************************************************************************************************************"

cmd="curl -X GET $GaUrl/oaisif/v1/properties/MYCOMMUNICATIONMETHOD  -H accept:application/json"
echo "****************************************************************************"
echo "***executing***" "$cmd"
echo "****************************************************************************"
echo $cmd | bash > getmyproperty.json

# Validate that this is a valid OAIS-IF IP
java -jar packagevalidator-1.0-SNAPSHOT.jar  getmyproperty.json

# Display the property
cat getmyproperty.json | jq '.InformationPackage.InformationObject.DataObject.EncodedObject.EncodedContent' | sed 's/\\//g' | awk '{print substr($0, 2, length($0) - 2)}' | jq
