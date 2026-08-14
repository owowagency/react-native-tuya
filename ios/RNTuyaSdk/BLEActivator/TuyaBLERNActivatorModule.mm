//
//  TuyaBLERNActivatorModule.m
//  TuyaRnDemo
//
//  Created by 浩天 on 2019/2/28.
//  Copyright © 2019年 Facebook. All rights reserved.
//

#import "TuyaBLERNActivatorModule.h"
#import <React/RCTBridgeModule.h>
#import <ThingSmartActivatorKit/ThingSmartActivatorKit.h>
#import <ThingSmartBaseKit/ThingSmartBaseKit.h>
#import <ThingSmartDeviceKit/ThingSmartDeviceKit.h>
#import <ThingBluetooth/ThingBluetooth.h>
#import <ThingSmartBLECoreKit/ThingSmartBLECoreKit.h>
#import <ThingSmartBLEKit/ThingSmartBLEWifiActivator.h>
#import "TuyaRNUtils+Network.h"
#import "YYModel.h"

#define kTuyaRNActivatorModuleHomeId @"homeId"
#define kTuyaRNActivatorModuleDeviceId @"deviceId"
#define kTuyaRNActivatorModuleProductId @"productId"
#define kTuyaRNActivatorModuleSSID @"ssid"
#define kTuyaRNActivatorModulePassword @"password"

// Bluetooth Pairing
static TuyaBLERNActivatorModule * activatorInstance = nil;

@interface TuyaBLERNActivatorModule()<ThingSmartBLEWifiActivatorDelegate>

@property(copy, nonatomic) RCTPromiseResolveBlock promiseResolveBlock;
@property(copy, nonatomic) RCTPromiseRejectBlock promiseRejectBlock;

@end

@implementation TuyaBLERNActivatorModule

RCT_EXPORT_MODULE(TuyaBLEActivatorModule)

RCT_EXPORT_METHOD(initActivator:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {
  if (activatorInstance == nil) {
    activatorInstance = [TuyaBLERNActivatorModule new];
  }

  [ThingSmartBLEWifiActivator sharedInstance].bleWifiDelegate = activatorInstance;
  activatorInstance.promiseResolveBlock = resolver;
  activatorInstance.promiseRejectBlock = rejecter;

  NSNumber *homeId = params[kTuyaRNActivatorModuleHomeId];
  NSString *deviceId = params[kTuyaRNActivatorModuleDeviceId];
  NSString *productId = params[kTuyaRNActivatorModuleProductId];
  NSString *ssid = params[kTuyaRNActivatorModuleSSID];
  NSString *password = params[kTuyaRNActivatorModulePassword];
  long long int homeIdValue = [homeId longLongValue];

  [[ThingSmartBLEWifiActivator sharedInstance] startConfigBLEWifiDeviceWithUUID:deviceId homeId:homeIdValue productId:productId ssid:ssid password:password  timeout:180 success:^{
      // Wait for activation
    } failure:^ {
      if (activatorInstance.promiseRejectBlock) {
        RCTPromiseRejectBlock reject = activatorInstance.promiseRejectBlock;
        activatorInstance.promiseResolveBlock = nil;
        activatorInstance.promiseRejectBlock = nil;
        [TuyaRNUtils rejecterWithError:nil handler:reject];
      }
      return;
    }];
}

- (void)bleWifiActivator:(ThingSmartBLEWifiActivator *)activator didReceiveBLEWifiConfigDevice:(ThingSmartDeviceModel *)deviceModel error:(NSError *)error {
  // These callbacks can fire more than once (e.g. an error after a prior success/error),
  // but the resolve/reject blocks are one-shot Promise callbacks - invoking one more than
  // once crashes under the New Architecture's TurboModule promise handling. Clear both
  // after first use.
  if (!error && deviceModel) {
    if (activatorInstance.promiseResolveBlock) {
      RCTPromiseResolveBlock resolve = activatorInstance.promiseResolveBlock;
      activatorInstance.promiseResolveBlock = nil;
      activatorInstance.promiseRejectBlock = nil;
      resolve([deviceModel yy_modelToJSONObject]);
    }
  }
  if (error) {
    if (activatorInstance.promiseRejectBlock) {
      RCTPromiseRejectBlock reject = activatorInstance.promiseRejectBlock;
      activatorInstance.promiseResolveBlock = nil;
      activatorInstance.promiseRejectBlock = nil;
      [TuyaRNUtils rejecterWithError:error handler:reject];
    }
  }

}

#if RCT_NEW_ARCH_ENABLED

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params {
  return std::make_shared<facebook::react::NativeTuyaBLEActivatorModuleSpecJSI>(params);
}

#endif

@end
