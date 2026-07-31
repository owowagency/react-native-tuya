//
//  TuyaRNDeviceModule.m
//  TuyaRnDemo
//
//  Created by 浩天 on 2019/2/28.
//  Copyright © 2019年 Facebook. All rights reserved.
//

#import "TuyaRNDeviceModule.h"
#import "TuyaRNDeviceListener.h"
#import <ThingSmartDeviceKit/ThingSmartDeviceKit.h>
#import "TuyaRNUtils.h"
#import "YYModel.h"


#define kTuyaDeviceModuleDevId @"devId"
#define kTuyaDeviceModuleCommand @"command"
#define kTuyaDeviceModuleDeviceName @"name"

@interface TuyaRNDeviceModule()

@property (strong, nonatomic) ThingSmartDevice *smartDevice;

@end

@implementation TuyaRNDeviceModule

RCT_EXPORT_MODULE(TuyaDeviceModule)

// Pre-existing limitation: iOS has no comparable API to look up a device by
// id the way Android's getDevice does (see src/api/device.ts's own TODO
// comment). Stubbed here so this at least fails cleanly instead of
// throwing "tuya.getDevice is not a function".
RCT_EXPORT_METHOD(getDevice:(NSDictionary *)params resolver:(RCTPromiseResolveBlock)resolver rejecter:(RCTPromiseRejectBlock)rejecter) {
  if (rejecter) {
    rejecter(@"TuyaDeviceModule.getDevice", @"getDevice is not implemented on iOS", nil);
  }
}

/**
 设备监听开启
 */
RCT_EXPORT_METHOD(registerDevListener:(NSDictionary *)params) {

  self.smartDevice  = [self smartDeviceWithParams:params];
  //监听设备
  [TuyaRNDeviceListener registerDevice:self.smartDevice type:TuyaRNDeviceListenType_DeviceInfo];
}

/**
 设备监听删除

 */
RCT_EXPORT_METHOD(unRegisterDevListener:(NSDictionary *)params) {
  NSString *deviceId = params[kTuyaDeviceModuleDevId];
  if(deviceId.length == 0) {
    return;
  }

  ThingSmartDevice *device = [ThingSmartDevice deviceWithDeviceId:deviceId];

  // 移除监听设备
  [TuyaRNDeviceListener removeDevice:device type:TuyaRNDeviceListenType_DeviceInfo];

  self.smartDevice  = [self smartDeviceWithParams:params];
  //取消设备监听
  [TuyaRNDeviceListener removeDevice:self.smartDevice type:TuyaRNDeviceListenType_DeviceInfo];
}


/*
 * 通过局域网或者云端这两种方式发送控制指令给设备。send(通过局域网或者云端这两种方式发送控制指令给设备。)
 command的格式应符合{key:value} 例如 {"1":true}
 */
RCT_EXPORT_METHOD(send:(NSDictionary *)params resolver:(RCTPromiseResolveBlock)resolver rejecter:(RCTPromiseRejectBlock)rejecter) {
  //设备发送消息
  self.smartDevice  = [self smartDeviceWithParams:params];
  NSDictionary *command = params[kTuyaDeviceModuleCommand];
  [self.smartDevice publishDps:command success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];
}

/**
 设备重命名
 */
RCT_EXPORT_METHOD(renameDevice:(NSDictionary *)params resolver:(RCTPromiseResolveBlock)resolver rejecter:(RCTPromiseRejectBlock)rejecter) {

  self.smartDevice  = [self smartDeviceWithParams:params];
  NSString *deviceName = params[kTuyaDeviceModuleDeviceName];
  [self.smartDevice updateName:deviceName success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];
}

RCT_EXPORT_METHOD(getDataPointStat:(NSDictionary *)params resolver:(RCTPromiseResolveBlock)resolver rejecter:(RCTPromiseRejectBlock)rejecter) {
  self.smartDevice  = [self smartDeviceWithParams:params];
}

/**
 删除设备
 */
RCT_EXPORT_METHOD(removeDevice:(NSDictionary *)params resolver:(RCTPromiseResolveBlock)resolver rejecter:(RCTPromiseRejectBlock)rejecter) {

  self.smartDevice  = [self smartDeviceWithParams:params];
  [self.smartDevice remove:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];
}

// 下发升级指令：
RCT_EXPORT_METHOD(startOta:(NSDictionary *)params) {
    ThingSmartDevice *device = [ThingSmartDevice deviceWithDeviceId:params[@"devId"]];
    [device upgradeFirmware:[params[@"type"] integerValue] success:^{
    } failure:^(NSError *error) {
    }];
}

// 查询固件升级信息：
RCT_EXPORT_METHOD(getOtaInfo:(NSDictionary *)params resolver:(RCTPromiseResolveBlock)resolver rejecter:(RCTPromiseRejectBlock)rejecter) {

    ThingSmartDevice *device = [ThingSmartDevice deviceWithDeviceId:params[@"devId"]];
    [device getFirmwareUpgradeInfo:^(NSArray<ThingSmartFirmwareUpgradeModel *> *upgradeModelList) {

        NSMutableArray *res = [NSMutableArray array];
        for (ThingSmartFirmwareUpgradeModel *item in upgradeModelList) {
          NSDictionary *dic = [item yy_modelToJSONObject];
          [res addObject:dic];
        }
        if (resolver) {
          resolver(res);
        }

        NSLog(@"getFirmwareUpgradeInfo success");
    } failure:^(NSError *error) {
        [TuyaRNUtils rejecterWithError:error handler:rejecter];
    }];

}


#pragma mark -
- (ThingSmartDevice *)smartDeviceWithParams:(NSDictionary *)params {
  NSString *deviceId = params[kTuyaDeviceModuleDevId];
  if(deviceId.length == 0) {
    return nil;
  }
  return [ThingSmartDevice deviceWithDeviceId:deviceId];
}

#if RCT_NEW_ARCH_ENABLED

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params {
  return std::make_shared<facebook::react::NativeTuyaDeviceModuleSpecJSI>(params);
}

#endif

@end
