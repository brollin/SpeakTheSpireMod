#!/bin/sh

# package the mod
mvn package -f ./pom.xml

# copy the mod to the steam mod directory
mkdir -p ./steam/content
cp ./target/SpeakTheSpire.jar ./steam/content/SpeakTheSpire.jar

# (brittle) upload the mod to the steam workshop
echo "Uploading mod to steam workshop..."
cp -r ./steam/* '/Users/ben.rollin/Library/Application Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources/speakthespire/'
cd '/Users/ben.rollin/Library/Application Support/Steam/steamapps/common/SlayTheSpire/SlayTheSpire.app/Contents/Resources'
./jre/bin/java -jar mod-uploader.jar upload -w speakthespire

# Note: before the above upload command will work, you need to have created a workspace with the following command:
# ./jre/bin/java -jar mod-uploader.jar new -w speakthespire
