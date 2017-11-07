#!/bin/bash

echo "Initialising..."

echo ""
echo "Setting up package names in scaffolds"

cd ..

find ./ -name '*.scala*'  -type f -exec sed -i'.bak' 's/\$package\$/uk.gov.hmrc.claimtaxrefundfrontend/g' {} \;
find ./ -name '*.sh'  -type f -exec sed -i'.bak' 's/\$package\$/uk.gov.hmrc.claimtaxrefundfrontend/g' {} \;
find ./ -name '*.md'  -type f -exec sed -i'.bak' 's/\$package\$/uk.gov.hmrc.claimtaxrefundfrontend/g' {} \;

echo ""
echo "Setting directory names in scaffolds"
find ./ -type d -iname '*appName*' -depth -exec bash -c 'mv "$1" "${1//\$appName__word\$/claimtaxrefundfrontend}"' -- {} \;

find ./ -name '*.sh'  -type f -exec sed -i'.bak' 's/\$package;format=\"packaged\"\$/uk\/gov\/hmrc\/claimtaxrefundfrontend/g' {} \;

find . -name '*.bak' -exec rm -r {} \;

echo ""
echo "Initialising git repository"
git init
git add .
git commit -m 'initial commit'
