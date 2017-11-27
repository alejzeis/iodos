#!/usr/bin/env bash
echo "** This Script will generate keys for the server and client"
echo "** Please note that it will CLEAR ALL KEYS IN THE ./keys FOLDER!"

read -n 1 -s -p "~~ Press any key to continue"
echo ""
rm -rf ./keys
mkdir ./keys

openssl ecparam -genkey -name secp384r1 -noout -out ./keys/private-legacy.pem
openssl ec -in ./keys/private-legacy.pem -pubout -out ./keys/public.pem
echo "| Generated Server keys"

openssl pkey -in ./keys/private-legacy.pem -out ./keys/private.pem
echo "| Converted private-legacy.pem to new format private.pem"

echo "| Done!"