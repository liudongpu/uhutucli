# 项目使用  

[![Circle CI](https://circleci.com/gh/uhutu/uhutucli.svg?style=shield)](https://circleci.com/gh/uhutu/uhutucli) [![npm version](https://badge.fury.io/js/uhutu-cli.svg)](https://badge.fury.io/js/uhutu-cli)

这是一个命令行项目，用于生成react-native代码。

## 安装  
* 安装 [Node.js](https://nodejs.org/)  
* 安装react-native `npm install -g react-native-cli`
* 然后执行：  

```bash
npm install -g uhutu-cli
```

* 如果启用voip等额外功能需要安装pod

## 使用方法  
* 在一个空文件夹，执行`uhutu-cli --config`,该操作将在当前目录创建一个config.json文件。编译该config.json文件，修改对应的值。
* 执行`uhutu-cli --install`,该操作会初始化的各个项目。分别为react,vue,weapp项目。
* 执行`uhutu-cli --build`，该操作会持续监听dev文件夹下的html文件和sass文件。实时生成对应的文件。
* 命令行参数参考`uhutu-cli --help`


## 项目文件夹目录

> *config.json*    配置文件  
> *dev*    开发代码文件夹  
>> *pages* 页面相关目录  
>> *master* 模板文件夹,子文件夹按照不同项目命名，如react,vue,weapp  
>> *statics* 静态资源，子文件夹按不同项目命名，里面的文件会在执行build命令时拷贝到对应的实际项目目录下。
>
> *disk*   cli生成的文件夹目录  
>> *disk_config.json* 系统生成的配置文件的最终版  
>
> *logs* 文件夹  
> *reactdemo*  React项目文件夹  



## 编写代码

执行build操作，该操作会持续监听dev文件夹下的html文件和sass文件。实时生成对应的文件。
```node
uhutu-cli --build
```




### 初始化

# iOS 
  
* 更改 Project > Build Settings > Enable bitCode 改为NO
* 修改 General > Deployment Info > Devices 调整为Universel   
* 修改 General > Deployment Info > Requires full screen 勾上选择
* 修改 Build Settings > PRODUCT_BUNDLE_IDENTIFIER 调整为com.uhutu.react.
* 添加Ginkgo系列文件夹进项目目录
* Info.plist 增加ITSAppUsesNonExemptEncryption  false

# iOS推送配置

* 添加根目录下的tac_services_configurations.plist文件进项目目录
* Target > Capabilities > Background Modes > Remote notifications 勾选
* Target > Capabilities > Push Notifications 选择

# Android

* AndroidManifest > manifest添加属性 xmlns:tools="http://schemas.android.com/tools"
* AndroidManifest > application 添加属性 tools:replace="android:allowBackup"


#增加支付支持

https://github.com/puti94/react-native-puti-pay

