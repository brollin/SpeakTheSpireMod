#!/bin/sh

# package the mod
mvn package -f ./pom.xml

# copy the mod to the steam mod directory
mkdir -p ./steam/content
cp ./target/SayTheSpire.jar ./steam/content/SayTheSpire.jar

# (brittle) upload the mod to the steam workshop
echo "Uploading mod to steam workshop..."
cp -r ./steam/* '/Users/ben.rollin/Library/Application Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources/saythespire/'
cd '/Users/ben.rollin/Library/Application Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources'
./jre/bin/java -jar mod-uploader.jar upload -w saythespire
