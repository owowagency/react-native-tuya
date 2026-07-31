//
//  TuyaRNHomeDataManagerModule.m
//  TuyaRnDemo
//
//  Created by 浩天 on 2019/3/2.
//  Copyright © 2019年 Facebook. All rights reserved.
//

#import "TuyaRNHomeDataManagerModule.h"
#import <ThingSmartDeviceKit/ThingSmartDeviceKit.h>
#import "TuyaRNUtils+Cache.h"
#import "TuyaRNUtils+DeviceParser.h"

#define kTuyaRNHomeDataManagerModuleHomeId @"homeId"
#define kTuyaRNHomeDataManagerModuleRoomId @"roomId"

@interface TuyaRNHomeDataManagerModule()

@property (strong, nonatomic) ThingSmartRoom *smartRoom;

@end

@implementation TuyaRNHomeDataManagerModule

RCT_EXPORT_MODULE(TuyaHomeDataManagerModule)

RCT_EXPORT_METHOD(getDeviceRoomBean:(NSDictionary *)params resolver:(RCTPromiseResolveBlock)resolver rejecter:(RCTPromiseRejectBlock)rejecter) {

}

RCT_EXPORT_METHOD(getRoomDeviceList:(NSDictionary *)params resolver:(RCTPromiseResolveBlock)resolver rejecter:(RCTPromiseRejectBlock)rejecter) {

  //获取room下的Device
  NSNumber *homeId = params[kTuyaRNHomeDataManagerModuleHomeId];
  if(!homeId) {
    homeId = [TuyaRNUtils currentHomeId];
  }
  NSNumber *roomId = params[kTuyaRNHomeDataManagerModuleRoomId];

  //获取room下的设备
  self.smartRoom = [ThingSmartRoom roomWithRoomId:roomId.longLongValue homeId:homeId.longLongValue];
  if(resolver) {
    NSMutableDictionary *roomDic = [[NSMutableDictionary alloc] initWithCapacity:2];
    [roomDic setObject:getValidDataForDeviceModel(self.smartRoom.deviceList) forKey:@"deviceList"];
    [roomDic setObject:getValidDataForGroupModel(self.smartRoom.groupList) forKey:@"groupList"];
    resolver(roomDic);
  }
}

#if RCT_NEW_ARCH_ENABLED

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params {
  return std::make_shared<facebook::react::NativeTuyaHomeDataManagerModuleSpecJSI>(params);
}

#endif

@end
