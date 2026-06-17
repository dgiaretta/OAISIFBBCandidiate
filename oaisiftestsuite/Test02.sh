#!/bin/bash
echo "******************************************************************"
echo "*** TEST 02 Request list of Info Packages from Generic Adapter ***"
echo "******************************************************************"
cmd="curl -X GET $GaUrl/oaisif/v1/information-packages -H accept:application/json"
echo "****************************************************************************"
echo "***executing***" "$cmd"
echo "****************************************************************************"
echo $cmd | bash > getiplist.json

# Validate that this is a valid OAIS-IF IP
java -jar packagevalidator-1.0-SNAPSHOT.jar  getiplist.json

# Examine the encoded content to check that it looks right
cat getiplist.json | jq '.InformationPackage.InformationObject.DataObject.EncodedObject.EncodedContent' | sed 's/\\//g' | awk '{print substr($0, 2, length($0) - 2)}' | jq

#Check optional features - check visually
cmd="curl -X GET $GaUrl/oaisif/v1/information-packages?$GaQuery"
echo "****************************************************************************"
echo "***executing***" "$cmd"
echo "****************************************************************************"
echo $cmd | bash > getselectediplist.json


echo "Examine the encoded content to check that it looks right"
sleep 1
cat getselectediplist.json | jq '.InformationPackage.InformationObject.DataObject.EncodedObject.EncodedContent' | sed 's/\\//g' | awk '{print substr($0, 2, length($0) - 2)}' | jq

