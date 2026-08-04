//
//  TuyaRNHomeManagerModule.m
//  TuyaRnDemo
//
//  Created by 浩天 on 2019/2/28.
//  Copyright © 2019年 Facebook. All rights reserved.
//

#import "TuyaRNHomeManagerModule.h"
#import <ThingSmartDeviceKit/ThingSmartHome.h>
#import <ThingSmartDeviceKit/ThingSmartHomeManager.h>
#import "YYModel.h"
#import "TuyaRNUtils.h"
#import "TuyaRNHomeManagerListener.h"
#import "TuyaRNHomeListener.h"

#define kTuyaHomeManagerModuleName @"name"
#define kTuyaHomeManagerModuleLon @"lon"
#define kTuyaHomeManagerModuleLat @"lat"
#define kTuyaHomeManagerModuleGeoName @"geoName"
#define kTuyaHomeManagerModuleRooms @"rooms"
#define kTuyaHomeManagerModuleHomeId @"homeId"
#define kTuyaHomeManagerModuleAction @"action"


@interface TuyaRNHomeManagerModule()

@property (nonatomic, strong) ThingSmartHomeManager *homeManager;

@end

@implementation TuyaRNHomeManagerModule

RCT_EXPORT_MODULE(TuyaHomeManagerModule)

/**
 * 获取家庭列表
 *
 * @param listener
 */
RCT_EXPORT_METHOD(queryHomeList:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  [self.homeManager getHomeListWithSuccess:^(NSArray<ThingSmartHomeModel *> *homes) {

    if (homes.count == 0) {
      if (resolver) {
        resolver(@[]);
      }
      return;
    }

    NSMutableArray *list = [NSMutableArray array];
    for (ThingSmartHomeModel *homeModel in homes) {
      NSDictionary *dic = [homeModel yy_modelToJSONObject];
      NSMutableDictionary *homeDic = [NSMutableDictionary dictionaryWithDictionary:dic];
      [homeDic setObject:[NSNumber numberWithLongLong:homeModel.homeId] forKey:@"homeId"];
      [list addObject:homeDic];
    }

    if (resolver) {
      resolver(list);
    }
  } failure:^(NSError *error) {

  }];
}

/**
 * 创建家庭
 * @param name     家庭名称
 * @param lon      经度
 * @param lat      纬度
 * @param geoName  家庭地理位置名称
 * @param rooms    房间列表
 * @param callback
 */
RCT_EXPORT_METHOD(createHome:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSString *name = params[kTuyaHomeManagerModuleName];
  NSString *geoName = params[kTuyaHomeManagerModuleGeoName];
  NSNumber *lat = params[kTuyaHomeManagerModuleLat];
  NSNumber *lon = params[kTuyaHomeManagerModuleLon];
  NSArray *rooms = params[kTuyaHomeManagerModuleRooms];

  double latValue = lat.doubleValue;
  double lonValue = lon.doubleValue;

  [self.homeManager addHomeWithName:name geoName:geoName rooms:rooms latitude:latValue longitude:lonValue success:^(long long result) {
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];
}

RCT_EXPORT_METHOD(joinFamily:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {
  NSNumber *homeIdNum = params[kTuyaHomeManagerModuleHomeId];
  NSString *action = params[kTuyaHomeManagerModuleAction];
  ThingSmartHome *newHome = [ThingSmartHome homeWithHomeId:homeIdNum.longLongValue];

  [newHome joinFamilyWithAccept:action.boolValue success:^(BOOL result) {
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];
}

/**
 * 注册家庭信息的变更
 * 有：家庭的增加、删除、信息变更、分享列表的变更和服务器连接成功的监听
 *
 * @param listener
 */
RCT_EXPORT_METHOD(registerTuyaHomeChangeListener:(NSDictionary *)params) {

  NSNumber *homeIdNum = params[kTuyaHomeManagerModuleHomeId];
  if (!homeIdNum || homeIdNum.longLongValue <= 0) {
    return;
  }
  //开始监听家庭的情况
  [[TuyaRNHomeManagerListener sharedInstance] registerSmartHomeManager:[ThingSmartHomeManager new]];
  [[TuyaRNHomeListener shareInstance] registerHomeChangeWithSmartHome:[ThingSmartHome homeWithHomeId:homeIdNum.longLongValue]];
}

/**
 * 注销家庭信息的变更
 *
 * @param listener
 */
RCT_EXPORT_METHOD(unregisterTuyaHomeChangeListener:(NSDictionary *)params) {

  //结束家庭的监听情况
  [[TuyaRNHomeManagerListener sharedInstance] removeSmartHomeManager];
  [[TuyaRNHomeListener shareInstance] removeHomeChangeSmartHome];
}

RCT_EXPORT_METHOD(onDestory:(NSDictionary *)params) {

}

#pragma mark -
#pragma mark - init
- (ThingSmartHomeManager *)homeManager {
  if (!_homeManager) {
    _homeManager = [[ThingSmartHomeManager alloc] init];
  }
  return _homeManager;
}

#if RCT_NEW_ARCH_ENABLED

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params {
  return std::make_shared<facebook::react::NativeTuyaHomeManagerModuleSpecJSI>(params);
}

#endif

@end
