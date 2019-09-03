#!/bin/bash

# get the GLOBAL ACCOUNT NAME FROM PORT 9200 of ES
GAN=$(curl -s localhost:9200/appdynamics_accounts___search/_search?pretty=true -d '{ "query": {  "wildcard" : { "accountName": "customer1_*" } } }' | grep "_id")
GAN=`echo $GAN | cut -d, -f1`
GAN=`echo $GAN | cut -d: -f2`
GAN=`echo $GAN | cut -d '"' -f2`
echo $GAN
ACCESS_KEY=SJ5b2m7d1\$354
API_KEY=4a5c3998-8914-4a57-a2f2-920cd76c08ae
echo $API_KEY

# create EVENTS SERVICE API KEY
curl -v -X POST -u $GAN:$ACCESS_KEY -H 'Content-Type: application/json' http://localhost:9080/v1/apiKey/$GAN -d '{
    "globalAccountName": "$output",
    "name": "Key 8",
    "description": "Key Description",
    "key": "4a5c3998-8914-4a57-a2f2-920cd76c08ae",
    "suffix": "08ae",
    "enabled": true,
    "createdTime": 1447654377118,
    "lastModifiedTime": 1447654377118,
    "apiTypeEventAccessPermissions": {

    	"MANAGE_SCHEMA": {
            "accessToAllInternalEventTypes": true,
            "accessToAllCustomEventTypes": true,
            "eventAccessFilters": {
            }

        },

        "QUERY": {
            "accessToAllInternalEventTypes": true,
            "accessToAllCustomEventTypes": true,
            "eventAccessFilters": {
            }

        },
        "PUBLISH": {
            "accessToAllInternalEventTypes": true,
            "accessToAllCustomEventTypes": true,
            "eventAccessFilters": {
            }
        }

    }
}'