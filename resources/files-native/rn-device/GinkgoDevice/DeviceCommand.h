//
//  DeviceCommand.h
//  DeviceCommand
//
//  Created by contec on 14-8-29.
//  Copyright (c) 2014年 contec. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreBluetooth/CoreBluetooth.h>

@protocol DeviceCommandDelegate <NSObject>

@required
-(void)getDeviceData:(NSDictionary *)dicDeviceData;
-(void)getOperateResult:(NSDictionary *)dicOperateResult;
-(void)getError:(NSDictionary *)dicError;

@optional
-(void)receivePM10Param:(NSDictionary *)dicParam;//15.1.20
-(void)receive50KVersion:(NSDictionary *)dicVersion;//15.8.27

@end


@interface DeviceCommand : NSObject
@property(weak, nonatomic) id<DeviceCommandDelegate> delegate;

//nParam的值
//电子秤，尿机，耳温计,0 接收全部，1 接收最新一条
//50D血氧
//心电计，0 接收全部，1 接收最新一条，2 接收未上传
//50K,0 血氧脉率,1 全天步数,2 5分钟步数,3 心电,4 脉搏,5 连续脉率,6 连续血氧,7 连续体动
-(void)peripheral:(CBPeripheral *)peripheral receiveData:(NSInteger)nParam;

-(void)peripheral:(CBPeripheral *)peripheral deleteData:(NSInteger)nParam;//ABPM50，50D自动删除数据  pm10、50k用到nParam

-(void)markData;

-(void)peripheral:(CBPeripheral *)peripheral height:(NSInteger)nHeight weight:(float)fWeight startHour:(NSInteger)nStartHour endHour:(NSInteger)nEndHour targetCalories:(NSInteger)nTargetCalories saveDay:(NSInteger)nSaveDay sensitivity:(NSInteger)nSensitivity;//只有50D血氧用到了,设置参数

/////////////PM10 获取和设置参数
-(void)getPM10Param:(CBPeripheral *)peripheral;
-(void)peripheral:(CBPeripheral *)peripheral language:(NSInteger)nLanguage saveTime:(NSInteger)nSaveTime heartSound:(NSInteger)nHeartSound;

/////////////////////CMS50K 多功能表设置体重、时间
-(void)peripheral:(CBPeripheral *)peripheral setWeight:(NSInteger)nWeight;
-(void)peripheral:(CBPeripheral *)peripheral setHeight:(NSInteger)nHeight;

-(void)peripheral:(CBPeripheral *)peripheral setTarCalories:(NSInteger)nTarCalories setDays:(NSInteger)nDays setSensitivity:(NSInteger)nSensitivity;

-(void)peripheral:(CBPeripheral *)peripheral valueData:(NSDictionary *)dicValueData;

-(void)peripheral:(CBPeripheral *)peripheral startTime:(NSInteger)nStartTime endTime:(NSInteger)nEndTime;

-(void)getDeviceVersion:(CBPeripheral *)peripheral;
/////////////////////

-(void)setSoundDirPath:(NSString *)strSoundDirPath;//胎心仪，获取声音数据前，需要设置保存路径

@end
