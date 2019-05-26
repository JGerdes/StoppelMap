FROM openjdk:8-jdk

# Just matched `app/build.gradle`
ENV ANDROID_COMPILE_SDK "28"
# Just matched `app/build.gradle`
ENV ANDROID_BUILD_TOOLS "28.0.3"
# Version from https://developer.android.com/studio#command-tools
ENV ANDROID_SDK_TOOLS "4333796"

ENV ANDROID_HOME /android-sdk-linux
ENV PATH="${PATH}:/android-sdk-linux/platform-tools/"

# install OS packages
RUN apt-get --quiet update --yes
RUN apt-get --quiet install --yes wget tar unzip lib32stdc++6 lib32z1 build-essential ruby ruby-dev
# We use this for xxd hex->binary
RUN apt-get --quiet install --yes vim-common
# install Android SDK
RUN wget --quiet --output-document=android-sdk.zip https://dl.google.com/android/repository/sdk-tools-linux-${ANDROID_SDK_TOOLS}.zip
RUN unzip -d android-sdk-linux android-sdk.zip
RUN echo y | android-sdk-linux/tools/bin/sdkmanager "platforms;android-${ANDROID_COMPILE_SDK}" >/dev/null
RUN echo y | android-sdk-linux/tools/bin/sdkmanager "platform-tools" >/dev/null
RUN echo y | android-sdk-linux/tools/bin/sdkmanager "build-tools;${ANDROID_BUILD_TOOLS}" >/dev/null
RUN echo y | android-sdk-linux/tools/bin/sdkmanager "extra-google-google_play_services" >/dev/null