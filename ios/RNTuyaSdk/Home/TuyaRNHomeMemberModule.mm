//
//  TuyaRNHomeMemberModule.m
//  TuyaRnDemo
//
//  Created by 浩天 on 2019/3/1.
//  Copyright © 2019年 Facebook. All rights reserved.
//

#import "TuyaRNHomeMemberModule.h"
#import "TuyaRNUtils.h"
#import "YYModel.h"
#import <ThingSmartUtil/ThingSmartUtil.h>
#import <ThingSmartDeviceKit/ThingSmartHomeMember.h>
#import <ThingSmartDeviceKit/ThingSmartHome.h>

#define kTuyaRNHomeMemberModuleHomeId @"homeId"
#define kTuyaRNHomeMemberModuleCountryCode @"countryCode"
#define kTuyaRNHomeMemberModuleUserAccount @"userAccount"
#define kTuyaRNHomeMemberModuleName @"name"
#define kTuyaRNHomeMemberModuleAdmin @"admin"
#define kTuyaRNHomeMemberModuleMemberId @"memberId"
//#define kTuyaRNHomeMemberModule

@interface TuyaRNHomeMemberModule()

@property (nonatomic, strong) ThingSmartHomeMember *homeMember;
@property (nonatomic, strong) ThingSmartHome *smartHome;

@end

@implementation TuyaRNHomeMemberModule

RCT_EXPORT_MODULE(TuyaHomeMemberModule)

/**
 * 给这个Home下面添加成员
 *
 * @param countryCode 国家码
 * @param userAccount 用户名
 * @param name        昵称
 * @param admin       是否拥有管理员权限
 * @param callback
 */

RCT_EXPORT_METHOD(addMember:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  ThingSmartHome *smartHome = [self smartHomeWithParams:params];

  ThingSmartHomeAddMemberRequestModel *requestModel = [[ThingSmartHomeAddMemberRequestModel alloc] init];

  NSString *name = params[kTuyaRNHomeMemberModuleName];
  NSString *userAccount = params[kTuyaRNHomeMemberModuleUserAccount];
  NSString *countryCode = params[kTuyaRNHomeMemberModuleCountryCode];
  NSString *admin = params[kTuyaRNHomeMemberModuleAdmin];

  requestModel.name = name;
  requestModel.account = userAccount;
  requestModel.countryCode = countryCode;
  requestModel.autoAccept = NO;
  requestModel.role = admin.boolValue ? ThingHomeRoleType_Admin : ThingHomeRoleType_Member;

  [smartHome addHomeMemberWithAddMemeberRequestModel:requestModel success:^(NSDictionary *dict) {
    if (resolver) {
      resolver(dict);
    }
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];
}

/**
 * 移除Home下面的成员
 *
 * @param id
 * @param callback
 */
RCT_EXPORT_METHOD(removeMember:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSNumber *memberId = params[kTuyaRNHomeMemberModuleMemberId];
  [self.homeMember removeHomeMemberWithMemberId:memberId.longLongValue success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];
}

/**
 * 更新成员备注名和权限
 * @param name 备注名 如果不更改备注名，传入从memberBean获取的nickName
 * @param admin  是否是管理员
 * @param callback
 */
RCT_EXPORT_METHOD(updateMember:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSNumber *memberId = params[kTuyaRNHomeMemberModuleMemberId];
  NSString *admin = params[kTuyaRNHomeMemberModuleAdmin];

  ThingSmartHomeMemberRequestModel *requestModel = [[ThingSmartHomeMemberRequestModel alloc] init];
  requestModel.memberId = memberId.longLongValue;
  requestModel.name = params[kTuyaRNHomeMemberModuleName];
  requestModel.role = admin.boolValue ? ThingHomeRoleType_Admin : ThingHomeRoleType_Member;

  [self.homeMember updateHomeMemberInfoWithMemberRequestModel:requestModel success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];
}

/**
 * 查询Home下面的成员列表
 *
 */
RCT_EXPORT_METHOD(queryMemberList:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  self.smartHome = [self smartHomeWithParams:params];
  [self.smartHome getHomeMemberListWithSuccess:^(NSArray<ThingSmartHomeMemberModel *> *memberList) {
    if (memberList.count == 0) {
      if (resolver) {
        resolver(@[]);
      }
      return;
    }
    NSMutableArray *memberDicList = [NSMutableArray array];
    for (ThingSmartHomeMemberModel *memberModel in memberList) {
      NSDictionary *dic = [memberModel yy_modelToJSONObject];
      if (dic) {
        [memberDicList addObject:dic];
      }
    }
    if (resolver) {
      resolver(memberDicList);
    }
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];

}

#pragma mark -
#pragma mark - init
- (ThingSmartHome *)smartHomeWithParams:(NSDictionary *)params {
  long long homeId = ((NSNumber *)params[kTuyaRNHomeMemberModuleHomeId]).longLongValue;
  return [ThingSmartHome homeWithHomeId:homeId];
}

- (ThingSmartHomeMember *)homeMember {
  if (!_homeMember) {
    _homeMember = [[ThingSmartHomeMember alloc] init];
  }
  return _homeMember;
}

#if RCT_NEW_ARCH_ENABLED

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params {
  return std::make_shared<facebook::react::NativeTuyaHomeMemberModuleSpecJSI>(params);
}

#endif

@end
