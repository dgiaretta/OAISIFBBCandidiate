#!/bin/bash

echo "***TEST 01***"
echo "***Download an IP." 

rm *.json
rm *.jar*
IpIdUrl=$(urlencode $IpId)
cmd="curl -X GET $GaUrl/oaisif/v1/information-packages/$IpIdUrl -H accept:application/json"
echo "****************************************************************************"
echo "***executing***" "$cmd"
echo "****************************************************************************"
echo $cmd | bash > getaip1.json

echo "***Download the validator JAR file"
wget http://www.oais.info/oais-if/packagevalidator/packagevalidator-1.0-SNAPSHOT.jar

echo "***Display the JSON file for human checking"
cat getaip1.json | jq 

echo "***Verify that this is UTF-8 - should confirm that it is UTF-8"
file -bi getaip1.json

echo "***Download the schema for reference"
wget -O infopackage-v1.0.0.schema.json http://www.oais.info/oais-if/json-schema/infopackage-v1.0.0.schema.json

echo "***Validate the JSON file - should state VALID at the end"
java -jar packagevalidator-1.0-SNAPSHOT.jar  getaip1.json
