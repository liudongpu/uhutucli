//
//  DeviceReact.h
//  reacthospital
//
//  Created by srnpr on 2017/6/14.
//  Copyright © 2017年 Facebook. All rights reserved.
//
#import <React/RCTBridgeModule.h>
#import <React/RCTLog.h>
#import <CoreBluetooth/CoreBluetooth.h>
#import <React/RCTEventEmitter.h>
#import <React/RCTConvert.h>
@interface DeviceReact : RCTEventEmitter <RCTBridgeModule,CBCentralManagerDelegate>
@end


