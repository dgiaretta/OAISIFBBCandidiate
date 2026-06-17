#!/bin/bash

rm getrilist.json

echo "****************************************************************************"
echo "*** TEST 06 Request the list of RepInfo packages. Extract it and display ***"
echo "****************************************************************************"

cmd="curl -X GET $RroriUrl/oaisif/v1/representation-info-repository/information-packages -H accept:application/json"
echo "****************************************************************************"
echo "***executing***" "$cmd"
echo "****************************************************************************"
echo $cmd | bash > getrilist.json

# Validate that this is a valid OAIS-IF IP
java -jar packagevalidator-1.0-SNAPSHOT.jar  getrilist.json

# Extract the list and print it
cat getrilist.json | jq  '.InformationPackage.InformationObject.DataObject.EncodedObject.EncodedContent' | sed 's/\\//g' | awk '{print substr($0, 2, length($0) - 2)}' | jq
