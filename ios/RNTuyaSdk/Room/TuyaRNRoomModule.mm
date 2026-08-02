//
//  TuyaRNRoomModule.m
//  TuyaRnDemo
//
//  Created by 浩天 on 2019/2/28.
//  Copyright © 2019年 Facebook. All rights reserved.
//

#import "TuyaRNRoomModule.h"
#import <ThingSmartDeviceKit/ThingSmartRoom.h>
#import "TuyaRNUtils.h"

#define kTuyaRNRoomModuleName @"name"
#define kTuyaRNRoomModuleGroupId @"groupId"
#define kTuyaRNRoomModuleDevId @"devId"
#define kTuyaRNRoomModuleRoomId @"roomId"
#define kTuyaRNRoomModuleHomeId @"homeId"

@interface TuyaRNRoomModule()

@property (strong, nonatomic) ThingSmartRoom *smartRoom;

@end

@implementation TuyaRNRoomModule

RCT_EXPORT_MODULE(TuyaRoomModule)

/**
 * 更新房间名称
 *
 * @param name     新房间名称
 */
RCT_EXPORT_METHOD(updateRoom:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {
  
  self.smartRoom = [self smartRoomWithParams:params];
  NSString *name = params[kTuyaRNRoomModuleName];
  
  [self.smartRoom updateRoomName:name success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];
  
}

RCT_EXPORT_METHOD(addDevice:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {
  
  self.smartRoom = [self smartRoomWithParams:params];
  NSString *deviceId = params[kTuyaRNRoomModuleDevId];
  
  [self.smartRoom addDeviceWithDeviceId:deviceId success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];
}


RCT_EXPORT_METHOD(removeDevice:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  self.smartRoom = [self smartRoomWithParams:params];
  
  NSString *deviceId = params[kTuyaRNRoomModuleDevId];
  
  [self.smartRoom removeDeviceWithDeviceId:deviceId success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];
  
}

#pragma mark -
#pragma mark - init
- (ThingSmartRoom *)smartRoomWithParams:(NSDictionary *)params {

  NSNumber *homeId = params[kTuyaRNRoomModuleHomeId];
  NSNumber *roomId = params[kTuyaRNRoomModuleRoomId];
  return [ThingSmartRoom roomWithRoomId:roomId.longLongValue homeId:homeId.longLongValue];
}

#if RCT_NEW_ARCH_ENABLED

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params {
  return std::make_shared<facebook::react::NativeTuyaRoomModuleSpecJSI>(params);
}

#endif

@end
