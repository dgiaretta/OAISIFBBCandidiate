#!/bin/bash

rm getri.json

echo "*****************************************************************"
echo "*** TEST 07 Request a specific RepInfo package and inspect it ***"
echo "*****************************************************************"

cmd="curl -X GET $RroriUrl/oaisif/v1/representation-info-repository/information-packages/$RiId -H accept:application/json"
echo "****************************************************************************"
echo "***executing***" "$cmd"
echo "****************************************************************************"
echo $cmd | bash > getri.json

# Validate that this is a valid OAIS-IF IP
java -jar packagevalidator-1.0-SNAPSHOT.jar  getri.json

# Pretty print it
cat getri.json | jq
