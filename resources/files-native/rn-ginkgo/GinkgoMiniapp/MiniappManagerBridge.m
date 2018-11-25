//
//  MiniappManagerBridge.m
//  iComeKernel
//
//  Created by 刘流 on 2018/7/24.
//  Copyright © 2018年 XZWL. All rights reserved.
//

#import "MiniappManagerBridge.h"
#import <ILiveSDK/ILiveSDK.h>

@implementation MiniappManagerBridge : NSObject
RCT_EXPORT_MODULE();

RCT_EXPORT_METHOD(sendNativeEvent:(NSString *)sType param:(NSString *)sJson option:(NSString *)sOption)
{
    
    
    
    
    
    
    
    RCTLogInfo(@"Pretending to create an event:%@ param:%@ option:%@", sType, sJson,sOption);
    
    
    
    NSArray *aName = [NSArray arrayWithObjects:@"nativeEventBack",@"nativeEventJump",@"nativeEventHidenav",@"nativeEventToast",nil];
    long index = [aName  indexOfObject:sType];
    
    
    NSData *jsonData = [sJson dataUsingEncoding:NSUTF8StringEncoding];
    
    NSError *err;
    
    NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:jsonData
                         
                                                        options:NSJSONReadingMutableContainers
                         
                                                          error:&err];
    
    if(err) {
        
        NSLog(@"json解析失败：%@",err);
        
    }
    else{
        
        switch (index) {
            case 0:
            case 1:
            case 2:
                case 3:
            {
              
              
              
                [[NSNotificationCenter defaultCenter] postNotificationName:@"MiniappNotificationEvent" object:dic ];
                
                
            }
                break;
                
        }
        
    }
    
    
    
   
    
    
    

    
    
}



RCT_REMAP_METHOD(sendNativePromise,type:(NSString *)sType param:(NSString *)sJson option:(NSString *)sOption
                 
                 findEventsWithResolver:(RCTPromiseResolveBlock)resolve
                 rejecter:(RCTPromiseRejectBlock)reject)
{
    
    
    
    
    NSArray *aName = [NSArray arrayWithObjects:@"nativePromiseToken",@"nativePromiseInfo",nil];
    
    
    long index = [aName  indexOfObject:sType];
    
    NSDictionary *dic;
    
    switch (index) {
             case 0:
        {
            //dic=[NSDictionary dictionaryWithObjectsAndKeys:ICK_TOKEN,@"token", nil];
        }
            
            break;
        case 1:{
            //NSString *deviceName = [[UIDevice currentDevice] name];  //获取设备名称 例如：梓辰的手机
            //NSString *sysName = [[UIDevice currentDevice] systemName]; //获取系统名称 例如：iPhone OS
            //NSString *sysVersion = [[UIDevice currentDevice] systemVersion]; //获取系统版本 例如：9.2
            //NSString *deviceUUID = [[[UIDevice currentDevice] identifierForVendor] UUIDString]; //获取设备唯一标识符 例如：FBF2306E-A0D8-4F4B-BDED-9333B627D3E6
            
            dic=[NSDictionary dictionaryWithObjectsAndKeys:@"ios",@"systemType",[[UIDevice currentDevice] systemVersion],@"systemVersion",    [[UIDevice currentDevice] systemName],@"systemModel",@"apple",@"deviceBrand",nil];
        }
            break;
        default:{
            dic=[NSDictionary dictionaryWithObjectsAndKeys:@"",@"",nil];
        }
            break;
    }
    
    
    NSError *parseError = nil;
    
    NSData *jsonData = [NSJSONSerialization dataWithJSONObject:dic options:NSJSONWritingPrettyPrinted error:&parseError];
    
    NSString *sReturn= [[NSString alloc] initWithData:jsonData encoding:NSUTF8StringEncoding];
    
    
    if ([sReturn length]>0) {
        resolve(sReturn);
    } else {
        
        reject(@"no_events", @"There were no events", parseError);
    }
}




@end
