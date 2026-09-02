//
//  TuyaCoreApi.m
//  TuyaSdkTest
//
//  Created by 浩天 on 2019/2/27.
//  Copyright © 2019年 Facebook. All rights reserved.
//

#import "TuyaRNCoreModule.h"
#import <ThingSmartBaseKit/ThingSmartBaseKit.h>
#import <CoreGraphics/CoreGraphics.h>
#import <UIKit/UIKit.h>
#import <CoreLocation/CoreLocation.h>
#import "TuyaRNUtils+Network.h"

#define kTuyaCoreModuleAppkey @""
#define kTuyaCoreModuleAppSecret @""
#define kTuyaCoreModuleParamLat @"lat"
#define kTuyaCoreModuleParamLon @"lon"

#define kTuyaCoreModuleUserDefaultLocation_lat @"ty_rn_lat"
#define kTuyaCoreModuleUserDefaultLocation_lon @"ty_rn_lon"

@interface TuyaRNCoreModule()<CLLocationManagerDelegate>

@property (nonatomic, strong) CLLocationManager *locationManager;

@end

@implementation TuyaRNCoreModule

RCT_EXPORT_MODULE(TuyaCoreModule)

RCT_EXPORT_METHOD(initWithOptions:(NSDictionary *)params) {

  NSString *appKey = params[kTuyaCoreModuleAppkey];
  NSString *appSecret = params[kTuyaCoreModuleAppSecret];

  dispatch_async(dispatch_get_main_queue(), ^{
//    [[TuyaSmartSDK sharedInstance] startWithAppKey:appKey secretKey:appSecret];
//#ifdef DEBUG
//    [TuyaSmartSDK sharedInstance].debugMode = YES;
//#endif

    if (!self.locationManager) {
      self.locationManager = [CLLocationManager new];
      self.locationManager.delegate = self;
    }
    if ([CLLocationManager authorizationStatus] == kCLAuthorizationStatusAuthorizedWhenInUse || [CLLocationManager authorizationStatus] == kCLAuthorizationStatusAuthorizedAlways) {
      [self.locationManager startUpdatingLocation];
    } else {
      if ([self.locationManager respondsToSelector:@selector(requestWhenInUseAuthorization)]) {
        [self.locationManager requestWhenInUseAuthorization];
      }
    }
  });
}

//判断网络
RCT_EXPORT_METHOD(openNetworkSettings:(NSDictionary *)params) {

  [TuyaRNUtils openNetworkSettings];

}

RCT_EXPORT_METHOD(exitApp) {

}

RCT_EXPORT_METHOD(onDestory:(NSDictionary *)params) {

}

RCT_EXPORT_METHOD(setLocation:(NSDictionary *)params) {
  NSString *lat = params[kTuyaCoreModuleParamLat];
  NSString *lon = params[kTuyaCoreModuleParamLon];
  if ([lat isKindOfClass:[NSString class]] && lat.length > 0
      && [lon isKindOfClass:[NSString class]] && lon.length > 0) {
    [[ThingSmartSDK sharedInstance] setValue:lat forKey:@"latitude"];
    [[ThingSmartSDK sharedInstance] setValue:lon forKey:@"longitude"];
    [self.locationManager stopUpdatingLocation];
  }
}

RCT_EXPORT_METHOD(getLocationData:(RCTPromiseResolveBlock)resolver
                  reject:(RCTPromiseRejectBlock)rejecter) {
  NSString *lat = [[NSUserDefaults standardUserDefaults] objectForKey:kTuyaCoreModuleUserDefaultLocation_lat];
  NSString *lon = [[NSUserDefaults standardUserDefaults] objectForKey:kTuyaCoreModuleUserDefaultLocation_lon];

  if (lat.length == 0) {
    lat = @"";
  }
  if (lon.length == 0) {
    lon = @"";
  }

  if (resolver) {
    resolver(@{
               kTuyaCoreModuleParamLat: [lat isKindOfClass:[NSString class]] ? lat : @"",
               kTuyaCoreModuleParamLon: [lat isKindOfClass:[NSString class]] ? lon : @""
               });
  }
}

//通用api
RCT_REMAP_METHOD(apiRequest,
                 apiRequest:(NSDictionary *)parameters
                 resolve:(RCTPromiseResolveBlock)resolver
                 reject:(RCTPromiseRejectBlock)rejecter) {

  NSString *apiName       = [parameters objectForKey:@"apiName"];
  NSDictionary *postData  = [parameters objectForKey:@"postData"];
  NSString *version       = [parameters objectForKey:@"version"];

  ThingSmartRequest *request = [ThingSmartRequest new];

  [request requestWithApiName:apiName postData:postData version:version success:^(id result) {
    if ([result isKindOfClass:[NSDictionary class]] || [result isKindOfClass:[NSArray class]]) {
      if (resolver) {
        resolver([result thingsdk_JSONString]);
      }
    } else {
      if (resolver) {
        resolver([result description]);
      }
    }
  } failure:^(NSError *error) {
    if (rejecter) {
      rejecter([NSString stringWithFormat:@"%ld", error.code], error.userInfo[NSLocalizedDescriptionKey], error);
    }
  }];
}

// initWithoutOptions/setOnNeedLoginListener only ever existed on Android
// (they wire up ThingHomeSdk's INeedLoginListener, which has no iOS
// equivalent in this module) - no-ops here so the shared TurboModule spec
// compiles on both platforms.
RCT_EXPORT_METHOD(initWithoutOptions) {

}

RCT_EXPORT_METHOD(setOnNeedLoginListener) {

}

#pragma mark - delegate
#pragma mark - <CLLocationManagerDelegate>

- (void)locationManager:(CLLocationManager *)manager
     didUpdateLocations:(NSArray<CLLocation *> *)locations {
  if (!([locations isKindOfClass:[NSArray class]] && locations.count > 0)) {
    return;
  }
  CLLocation *location = locations[0];

  [self.locationManager stopUpdatingLocation];

  NSString *latitude = [NSString stringWithFormat:@"%f", location.coordinate.latitude];
  NSString *longitude = [NSString stringWithFormat:@"%f", location.coordinate.longitude];

  [[NSUserDefaults standardUserDefaults] setObject:latitude forKey:kTuyaCoreModuleParamLat];
  [[NSUserDefaults standardUserDefaults] setObject:longitude forKey:kTuyaCoreModuleParamLon];
  [[NSUserDefaults standardUserDefaults] synchronize];
}

- (void)locationManager:(CLLocationManager *)manager didChangeAuthorizationStatus:(CLAuthorizationStatus)status {
  if ([CLLocationManager authorizationStatus] == kCLAuthorizationStatusAuthorizedWhenInUse || [CLLocationManager authorizationStatus] == kCLAuthorizationStatusAuthorizedAlways) {
    [self.locationManager startUpdatingLocation];
  }
}

#if RCT_NEW_ARCH_ENABLED

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params {
  return std::make_shared<facebook::react::NativeTuyaCoreModuleSpecJSI>(params);
}

#endif

@end
