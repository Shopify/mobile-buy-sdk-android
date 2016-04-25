#!/usr/bin/env bash

export TERM=${TERM:-dumb}

echo ${PORT}
echo ${SERIAL}
echo ${EMULATOR_NAME}

# change to our project directory, it is not the root in git
cd MobileBuy;

echo "--- Updating modules"
git submodule update --init --recursive

echo "--- Downloading sdk updates"
echo y | ./gradlew --no-color --refresh-dependencies

echo "--- Restarting ADB server"
${ANDROID_HOME}/platform-tools/adb kill-server
${ANDROID_HOME}/platform-tools/adb start-server

echo "--- Shutting down emulator"
${ANDROID_HOME}/platform-tools/adb emu kill || true

echo "--- Booting emulator"
${ANDROID_HOME}/tools/emulator -port ${PORT} -avd ${EMULATOR_NAME} -no-audio &

OUT=""
while [[ ${OUT:0:7}  != 'stopped' ]]; do
		OUT=`${ANDROID_HOME}/platform-tools/adb -s ${SERIAL} shell getprop init.svc.bootanim 2>&1`
		echo -n '.'
		sleep 1
done

echo ""
echo "Emulator booted!"

echo "--- Setting up emulator"
# ${ANDROID_HOME}/platform-tools/adb -s ${SERIAL} install -r scripts/AnimationDisabler.apk
# ${ANDROID_HOME}/platform-tools/adb -s ${SERIAL} shell pm grant com.shopify.AnimationDisabler android.permission.SET_ANIMATION_SCALE
# ${ANDROID_HOME}/platform-tools/adb -s ${SERIAL} shell am start -n com.shopify.AnimationDisabler/com.shopify.AnimationDisabler.DisableAnimationActivity

echo "--- Removing previous installs"
${ANDROID_HOME}/platform-tools/adb -s ${SERIAL} shell pm uninstall com.shopify.buy

echo "--- Setting up artifacts"
export BUILDKITE_ARTIFACTS="../BuildKiteArtifacts"
rm -rf ${BUILDKITE_ARTIFACTS}
mkdir ${BUILDKITE_ARTIFACTS}
${ANDROID_HOME}/platform-tools/adb -s ${SERIAL} logcat -v threadtime > ${BUILDKITE_ARTIFACTS}/logcat.txt &

echo "--- Running tests"
./gradlew :buy:connectedAndroidTest -PdisablePreDex

# Capture the exit status of the last command in a variable
ORIGINAL_STATUS=$?

if [ $ORIGINAL_STATUS -eq 0 ]
then
	echo "--- Building release package"
	./gradlew :buy:archiveReleasePackage
	ORIGINAL_STATUS=$?
else
	echo "--- Skipping release package"
fi

echo "--- Copying artifacts"
pwd
ls buy/build/outputs/androidTest-results/connected/
cp -r buy/build/outputs/androidTest-results/connected/* ${BUILDKITE_ARTIFACTS}
cp buy/build/distributions/* ${BUILDKITE_ARTIFACTS}

# Finish with the original status
exit ${ORIGINAL_STATUS}
