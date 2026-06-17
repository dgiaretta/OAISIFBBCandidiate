#!/bin/bash

rm getip.json

echo "**************************************************************************"
echo "*** TEST 05 Request an object given its identifier and pretty print it ***"
echo "**************************************************************************"
# NOTE THAT THE IDENTIFIER MAY NEED TO BE URLENCODED 
# e.g. 
# urlencode  IO#360000003 which produces IO%23360000003
IpIdUrl=$(urlencode $IpId)

cmd="curl -X GET $GaUrl/oaisif/v1/information-packages/$IpIdUrl -H accept:application/json"
echo "****************************************************************************"
echo "***executing***" "$cmd"
echo "****************************************************************************"
echo $cmd | bash > getip.json

# Validate that this is a valid OAIS-IF IP
java -jar packagevalidator-1.0-SNAPSHOT.jar  getip.json

# Prettyprint it
cat getip.json | jq
