#!/bin/bash

rm infopackage-v1.0.0.schema.json

echo "************************************************************************"
echo "*** TEST 08 Request the JSON schema and check it is the one expected ***"
echo "************************************************************************"

cmd="curl -X GET http://www.oais.info/oais-if/json-schema/infopackage-v1.0.0.schema.json  | jq"
echo "****************************************************************************"
echo "***executing***" "$cmd"
echo "****************************************************************************"
echo $cmd | bash 
