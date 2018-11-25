//
//  DeviceReact.m
//  reacthospital
//
//  Created by srnpr on 2017/6/14.
//  Copyright © 2017年 Facebook. All rights reserved.
//
#import "DeviceReact.h"
#import <Foundation/Foundation.h>
#import "DeviceCommand.h"



@interface DeviceReact()

@end

@implementation DeviceReact
{
  
  
  DeviceCommand *device;
  
  CBCentralManager *manager;
  CBPeripheral *peripheralFind;
  
  Boolean bReSend;
  
  NSString *findType;
  
}
RCT_EXPORT_MODULE();


- (NSArray<NSString *> *)supportedEvents
{
  return @[@"deviceDataCallBack"];//有几个就写几个
}



- (NSString*)convertToJSONData:(NSDictionary*)infoDict
{
  NSError *error;
  NSData *jsonData = [NSJSONSerialization dataWithJSONObject:infoDict
                                                     options:NSJSONWritingPrettyPrinted // Pass 0 if you don't care about the readability of the generated string
                                                       error:&error];
  
  NSString *jsonString = @"";
  
  if (! jsonData)
  {
    NSLog(@"Got an error: %@", error);
  }else
  {
    jsonString = [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
  }
  
  jsonString = [jsonString stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];  //去除掉首尾的空白字符和换行字符
  
  [jsonString stringByReplacingOccurrencesOfString:@"\n" withString:@""];
  
  return jsonString;
}


-(void)deviceDataCallBack:(NSString*)code result:(NSDictionary *) result
{
  NSString *json=[self convertToJSONData:@{
                                           @"code": peripheralFind.name,
                                           @"result": result,
                                           }];
  NSLog(@"send event: %@", json);
  [self sendEventWithName:@"deviceDataCallBack"
                     body:json];
}



RCT_EXPORT_METHOD(initDevice:(NSString *)appKey)
{
  
  if(device==nil){
    device = [[DeviceCommand alloc] init];
    device.delegate = self;
    bReSend = YES;
    
    NSLog(@"device connect");
    
  }
  else{
    
    NSLog(@"device connected");
    
  }
  
  

}



#pragma mark ----<DeviceCommandDelegate>
-(void)getDeviceData:(NSDictionary *)dicDeviceData {
  
  NSLog(@"getDeviceData %@", dicDeviceData);
  
  //NSString *location = [RCTConvert NSString:dicDeviceData];
  
  [self deviceDataCallBack:@"" result:dicDeviceData];
  
  //pm10  error
  if ([dicDeviceData objectForKey:@"Error"] != nil)
  {
    //       NSLog(@"re receiveData");
    //       [device peripheral:peripheralFind receiveData:0];
  }
  
  //   if (bReSend)
  //    {
  //        bReSend = NO;
  
  //       NSLog(@"re receiveData");
  //       [device peripheral:peripheralFind receiveData:0];
  
  //    [device peripheral:peripheralFind deleteData:0];
  
  //    }
  
}

-(void)getOperateResult:(NSDictionary *)dicDeleteResult
{
  NSLog(@"getOperateResult %@", dicDeleteResult);
  
}

-(void)getError:(NSDictionary *)dicError
{
  NSLog(@"getError %@", dicError);
}


-(void)receivePM10Param:(NSDictionary *)dicParam//15.1.20
{
  NSLog(@"getPM10Param %@", dicParam);
}


#pragma mark ----
RCT_EXPORT_METHOD(commandFine:(NSString *)frindUserId)
{
  NSLog(@"findDevice %@",frindUserId);
  findType=frindUserId;
  
  if (manager != nil)
  {
    if (peripheralFind != nil)
    {
      [manager cancelPeripheralConnection:peripheralFind];
    }
    
    NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSNumber numberWithBool:false], CBCentralManagerScanOptionAllowDuplicatesKey, nil];
    
    [manager scanForPeripheralsWithServices:nil options:dic];
  }
  else
  {
    CBCentralManager *managerFind = [[CBCentralManager alloc] initWithDelegate:self queue:nil];
    
    manager = managerFind;
  }
}

RCT_EXPORT_METHOD(commandConnect)
{
  if ((manager != nil) && (peripheralFind != nil))
  {
    [manager connectPeripheral:peripheralFind options:nil];
  }
}

RCT_EXPORT_METHOD(commandDisCon)
{
  if ((manager != nil) && (peripheralFind != nil))
  {
    [manager cancelPeripheralConnection:peripheralFind];
  }
}

#pragma mark ----  device sdk
RCT_EXPORT_METHOD(commandRecive)
{
  [device peripheral:peripheralFind receiveData:0];
}
RCT_EXPORT_METHOD(commandReciveOne)
{
  [device peripheral:peripheralFind receiveData:1];
}

RCT_EXPORT_METHOD(commandSetParam)
{
  [device peripheral:peripheralFind height:175 weight:75 startHour:0 endHour:24 targetCalories:3100 saveDay:4 sensitivity:2];
}

RCT_EXPORT_METHOD(commandDelete)
{
  [device peripheral:peripheralFind deleteData:0];//0 2 1
}

RCT_EXPORT_METHOD(commandMark)
{
  [device markData];
}

RCT_EXPORT_METHOD(commandUnUpData)
{
  [device peripheral:peripheralFind receiveData:2];
}

RCT_EXPORT_METHOD(commandGetParamPM10)
{
  [device getPM10Param:peripheralFind];
}

RCT_EXPORT_METHOD(commandSetParamPM10)
{
  [device peripheral:peripheralFind language:1 saveTime:1 heartSound:1];
}

///////////CMS50K
RCT_EXPORT_METHOD(commandReciveCMS50K:(id)sender)
{
  UIButton *btnRecive = (UIButton *)sender;
  
  [device peripheral:peripheralFind receiveData:btnRecive.tag];
}

RCT_EXPORT_METHOD(commandDeleteCMS50K:(id)sender)
{
  UIButton *btnDelete = (UIButton *)sender;
  
  [device peripheral:peripheralFind deleteData:btnDelete.tag];
}

RCT_EXPORT_METHOD(commandSetWeight)
{
  [device peripheral:peripheralFind setWeight:100];
}

RCT_EXPORT_METHOD(commandSetTime)
{
  [device peripheral:peripheralFind startTime:3 endTime:24];
}

#pragma mark ----  <CBCentralManagerDelegate>
-(void)centralManagerDidUpdateState:(CBCentralManager *)central
{
  NSMutableString *nsmstring = [NSMutableString stringWithString:@"UpdateState:"];
  switch (central.state)
  {
    case CBCentralManagerStateUnknown:
    {
      [nsmstring appendString:@"Unknown\n"];
      break;
    }
    case CBCentralManagerStateUnsupported:
    {
      [nsmstring appendString:@"Unsupported\n"];
      break;
    }
    case CBCentralManagerStateUnauthorized:
    {
      [nsmstring appendString:@"Unauthorized\n"];
      break;
    }
    case CBCentralManagerStateResetting:
    {
      [nsmstring appendString:@"Resetting\n"];
      break;
    }
    case CBCentralManagerStatePoweredOff:
    {
      [nsmstring appendString:@"PoweredOff\n"];
      if(peripheralFind != NULL)
      {
        [manager cancelPeripheralConnection:peripheralFind];
      }
      break;
    }
    case CBCentralManagerStatePoweredOn:
    {
      [nsmstring appendString:@"PoweredOn\n"];
      
      NSDictionary *dic = [NSDictionary dictionaryWithObjectsAndKeys:[NSNumber numberWithBool:false], CBCentralManagerScanOptionAllowDuplicatesKey, nil];
      
      [manager scanForPeripheralsWithServices:nil options:dic];
      
      break;
    }
      
    default:
    {
      [nsmstring appendString:@"none\n"];
      break;
    }
  }
  
  NSLog(@"%@", nsmstring);
}


//发现设备
- (void)centralManager:(CBCentralManager *)central didDiscoverPeripheral:(CBPeripheral *)peripheral advertisementData:(NSDictionary *)advertisementData RSSI:(NSNumber *)RSSI
{
  NSLog(@"discover %@",peripheral.name);
  
  //这里是总控扫描
  if ([peripheral.name hasPrefix:@"NIBP03"]
      ||[peripheral.name hasPrefix:@"NIBP04"]
      || [peripheral.name hasPrefix:@"NIBP01"]
      || [peripheral.name hasPrefix:@"SpO201"]
      || [peripheral.name hasPrefix:@"PULMO01"]
      || [peripheral.name hasPrefix:@"WT01"]
      || [peripheral.name hasPrefix:@"BG01"]
      || [peripheral.name hasPrefix:@"BC01"]
      || [peripheral.name hasPrefix:@"SpO208"]
      || [peripheral.name hasPrefix:@"PM10"]
      || [peripheral.name hasPrefix:@"TEMP"]
      || [peripheral.name hasPrefix:@"SpO209"])
  {
    //判断传入的名称是否含有
    if([peripheral.name hasPrefix:findType])
    {
      [manager stopScan];
      
      peripheralFind = peripheral;
      [self commandConnect];
    
    }
    
    
    
  }
  
}

- (void)centralManager:(CBCentralManager *)central didConnectPeripheral:(CBPeripheral *)peripheral
{
  NSLog(@"connect %@",peripheral.name);
  [self commandRecive];
}

- (void)centralManager:(CBCentralManager *)central didFailToConnectPeripheral:(CBPeripheral *)peripheral error:(NSError *)error
{
  NSLog(@"fail to connect %@",peripheral.name);
  NSLog(@"%@", error);
}

- (void)centralManager:(CBCentralManager *)central didDisconnectPeripheral:(CBPeripheral *)peripheral error:(NSError *)error
{
  //   peripheralFind = nil;
  
  NSLog(@"disconnect %@, error %@",peripheral.name, error);
}



@end
