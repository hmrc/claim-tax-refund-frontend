#!/bin/bash

echo "Initialising..."

cd ..

echo ""
echo "Setting directory names in scaffolds"
find ./ -type d -iname '*appName*' -depth -exec bash -c 'mv "$1" "${1//\$appName__word\$/claimtaxrefundfrontend}"' -- {} \;

find . -name '*.bak' -exec rm -r {} \;

echo ""
echo "Initialising git repository"
git init
git add .
git commit -m 'initial commit'
