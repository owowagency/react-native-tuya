//
//  TuyaBLERNScannerModule.m
//  TuyaRnDemo
//
//  Created by 浩天 on 2019/2/28.
//  Copyright © 2019年 Facebook. All rights reserved.
//

#import "TuyaBLERNScannerModule.h"
#import <ThingSmartActivatorKit/ThingSmartActivatorKit.h>
#import <ThingSmartBaseKit/ThingSmartBaseKit.h>
#import <ThingSmartDeviceKit/ThingSmartDeviceKit.h>
#import <ThingBluetooth/ThingBluetooth.h>
#import <ThingSmartBLECoreKit/ThingSmartBLECoreKit.h>
#import <ThingSmartBLEKit/ThingSmartBLEManager+Biz.h>
#import "TuyaRNUtils+Network.h"
#import "YYModel.h"

// Bluetooth Pairing
static TuyaBLERNScannerModule * scannerInstance = nil;

@interface TuyaBLERNScannerModule()<ThingSmartBLEManagerDelegate>

@property(copy, nonatomic) RCTPromiseResolveBlock promiseResolveBlock;
@property(copy, nonatomic) RCTPromiseRejectBlock promiseRejectBlock;

@end

@implementation TuyaBLERNScannerModule

RCT_EXPORT_MODULE(TuyaBLEScannerModule)

RCT_EXPORT_METHOD(startBluetoothScan:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {
  if (scannerInstance == nil) {
    scannerInstance = [TuyaBLERNScannerModule new];
  }

  [ThingSmartBLEManager sharedInstance].delegate = scannerInstance;
  scannerInstance.promiseResolveBlock = resolver;
  scannerInstance.promiseRejectBlock = rejecter;

  [[ThingSmartBLEManager sharedInstance] startListening:YES];
}

- (void)didDiscoveryDeviceWithDeviceInfo:(ThingBLEAdvModel *)deviceInfo {
  // This delegate callback fires once per discovered device, but promiseResolveBlock
  // is a one-shot Promise resolver - invoking it more than once crashes under the
  // New Architecture's TurboModule promise handling. Clear it after first use.
  if (scannerInstance.promiseResolveBlock) {
    RCTPromiseResolveBlock resolve = scannerInstance.promiseResolveBlock;
    scannerInstance.promiseResolveBlock = nil;
    resolve([deviceInfo yy_modelToJSONObject]);
  }
}

#if RCT_NEW_ARCH_ENABLED

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params {
  return std::make_shared<facebook::react::NativeTuyaBLEScannerModuleSpecJSI>(params);
}

#endif

@end
