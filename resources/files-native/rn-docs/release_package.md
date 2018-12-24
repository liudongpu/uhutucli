## 打包发布

* android打包 cd ./react[@config:project.projectName]/android && ./gradlew assembleRelease



cd ~/react/[@config:project.projectName]/react[@config:project.projectName]/ && mkdir -p build/android && mkdir -p build/ios && mkdir -p build/abc

android更新编译：


rm -rf build/android/*

react-native bundle  --platform android --dev false --reset-cache --entry-file index.js  --bundle-output ./build/android/index.android.jsbundle  --assets-dest ./build/android/

cd build/android/ 
zip -r '../abc/android-'`date +%y%m%d` ./


ios更新编译：

rm -rf build/ios/*

react-native bundle  --platform ios --dev false --reset-cache --entry-file index.js  --bundle-output ./build/ios/index.ios.jsbundle  --assets-dest ./build/ios/


cd build/ios/ 
zip -r '../abc/ios-'`date +%y%m%d` ./


### 初始化

# iOS 
  
* 更改 Project > Build Settings > Enable bitCode 改为NO
* 修改 General > Deployment Info > Devices 调整为Universel   
* 修改 General > Deployment Info > Requires full screen 勾上选择
* 修改 Build Settings > PRODUCT_BUNDLE_IDENTIFIER 调整为[@config:project.domainSpace]
* 添加Ginkgo系列文件夹进项目目录
* Info.plist 增加ITSAppUsesNonExemptEncryption  false

# iOS推送配置

* 添加根目录下的tac_services_configurations.plist文件进项目目录
* Target > Capabilities > Background Modes > Remote notifications 勾选
* Target > Capabilities > Push Notifications 选择

# Android

* AndroidManifest > manifest添加属性 xmlns:tools="http://schemas.android.com/tools"
* AndroidManifest > application 添加属性 tools:replace="android:allowBackup"




