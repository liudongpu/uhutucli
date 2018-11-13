## 打包发布

* android打包 cd ./react[@config:project.projectName]/android && ./gradlew assembleRelease


android更新编译：

cd ~/react/[@config:project.projectName]/react[@config:project.projectName]/ && mkdir -p build/android
rm -rf ~/react/[@config:project.projectName]/react[@config:project.projectName]/build/android/*

react-native bundle  --platform android --dev false --reset-cache --entry-file index.js  --bundle-output ./build/android/index.android.jsbundle  --assets-dest ./build/android/

cd build/android/ && zip -r '../bundle_android_'`date +%y%m%d` ./


ios更新编译：
cd ~/react/[@config:project.projectName]/react[@config:project.projectName]/ && mkdir -p build/ios
rm -rf ~/react/[@config:project.projectName]/react[@config:project.projectName]/build/ios/*

react-native bundle  --platform ios --dev false --reset-cache --entry-file index.js  --bundle-output ./build/ios/index.ios.jsbundle  --assets-dest ./build/ios/


cd build/ios/ && zip -r '../bundle_ios_'`date +%y%m%d` ./


### 更新

* ios更新

    code-push release-react [@config:project.projectName]-ios ios --dev false --d Production

    code-push deployment history [@config:project.projectName]-ios Production

强制更新指定版本
code-push release-react [@config:project.projectName]-ios ios --dev false --d Production --targetBinaryVersion 1.0.0  -m
code-push release-react [@config:project.projectName]-android android --dev false --d Production --targetBinaryVersion 1.0.0  -m

* android更新

    code-push release-react [@config:project.projectName]-android android --dev false --d Production



    code-push deployment history [@config:project.projectName]-android Production


codepush version 1.12.9-beta
code-push login https://code-push.srnpr.com/
code-push deployment clear <appName> <deploymentName>

## 生成key
* android添加生成key
code-push app add [@config:project.projectName]-android
* ios生成key
code-push app add [@config:project.projectName]-ios

