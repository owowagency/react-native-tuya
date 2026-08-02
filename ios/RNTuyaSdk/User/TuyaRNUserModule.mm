//
//  TuyaRNUserModule.m
//  TuyaRnDemo
//
//  Created by 浩天 on 2019/2/28.
//  Copyright © 2019年 Facebook. All rights reserved.
//

#import "TuyaRNUserModule.h"
#import <ThingSmartBaseKit/ThingSmartBaseKit.h>
#import "TuyaRNUtils.h"
#import "YYModel.h"

#define kTuyaRNUserModuleCountryCode @"countryCode"
#define kTuyaRNUserModuleValidateCode @"validateCode"
#define kTuyaRNUserModulePassword @"password"
#define kTuyaRNUserModuleNewPassword @"newPassword"
#define kTuyaRNUserModuleEmail @"email"

@implementation TuyaRNUserModule

RCT_EXPORT_MODULE(TuyaUserModule)

/* 邮箱注册获取验证码
* @param email  邮箱账户
* @param countryCode 国家区号
*/
RCT_EXPORT_METHOD(getRegisterEmailValidateCode:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSString *countryCode = params[kTuyaRNUserModuleCountryCode];
  NSString *email = params[kTuyaRNUserModuleEmail];

  [[ThingSmartUser sharedInstance] sendVerifyCodeByRegisterEmail:countryCode email:email success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];

}

/* 邮箱密码注册
* @param countryCode 国家区号
* @param email       邮箱账户
* @param passwd      登陆密码
*/
RCT_EXPORT_METHOD(registerAccountWithEmail:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSString *countryCode = params[kTuyaRNUserModuleCountryCode];
  NSString *email = params[kTuyaRNUserModuleEmail];
  NSString *password = params[kTuyaRNUserModulePassword];
  NSString *validateCode = params[kTuyaRNUserModuleValidateCode];

  [[ThingSmartUser sharedInstance] registerByEmail:countryCode email:email password:password code:validateCode success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];

}

/*
* 邮箱密码登陆
* @param email  邮箱账户
* @param passwd 登陆密码
*/
RCT_EXPORT_METHOD(loginWithEmail:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSString *countryCode = params[kTuyaRNUserModuleCountryCode];
  NSString *email = params[kTuyaRNUserModuleEmail];
  NSString *password = params[kTuyaRNUserModulePassword];

  [[ThingSmartUser sharedInstance] loginByEmail:countryCode email:email password:password success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];

}

/*
* 邮箱找回密码，获取验证码
* @param countryCode 国家区号
* @param email       邮箱账户
*/
RCT_EXPORT_METHOD(getEmailValidateCode:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSString *countryCode = params[kTuyaRNUserModuleCountryCode];
  NSString *email = params[kTuyaRNUserModuleEmail];

  [[ThingSmartUser sharedInstance] sendVerifyCodeByEmail:countryCode email:email success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];

}

/* 邮箱重置密码
* @param email     用户账户
* @param validateCode 邮箱验证码
* @param passwd    新密码
*/
RCT_EXPORT_METHOD(resetEmailPassword:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSString *countryCode = params[kTuyaRNUserModuleCountryCode];
  NSString *email = params[kTuyaRNUserModuleEmail];
  NSString *validateCode = params[kTuyaRNUserModuleValidateCode];
  NSString *password = params[kTuyaRNUserModuleNewPassword];

  [[ThingSmartUser sharedInstance] resetPasswordByEmail:countryCode email:email newPassword:password code:validateCode success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];
}

RCT_EXPORT_METHOD(logout:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  [[ThingSmartUser sharedInstance] loginOut:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];
}

RCT_EXPORT_METHOD(cancelAccount:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  [[ThingSmartUser sharedInstance] cancelAccount:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];

}

RCT_EXPORT_METHOD(getCurrentUser:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {
  ThingSmartUser *user = [ThingSmartUser sharedInstance];
  if (resolver) {
    NSDictionary *dic = [user yy_modelToJSONObject];
    NSMutableDictionary *userDic = [NSMutableDictionary dictionaryWithDictionary:dic];
    [userDic setObject:[self getValidStr:user.userName] forKey:@"username"];
    [userDic setObject:[self getValidStr:user.uid] forKey:@"uid"];
    [userDic setObject:[self getValidStr:user.headIconUrl] forKey:@"headPic"];
    [userDic setObject:[self getValidStr:user.countryCode] forKey:@"phoneCode"];
    [userDic setObject:[self getValidStr:user.phoneNumber] forKey:@"mobile"];
    [userDic setObject:[self getValidStr:user.email] forKey:@"email"];
    [userDic setObject:[self getValidStr:user.nickname] forKey:@"nickname"];
    [userDic setObject:[self getValidStr:user.timezoneId] forKey:@"timezoneId"];
    resolver(userDic);
  }
}

#pragma mark -
#pragma mark - api
- (NSString *)getValidStr:(NSString *)str {
  if (str.length == 0) {
    return @"";
  }
  return str;
}

#if RCT_NEW_ARCH_ENABLED

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params {
  return std::make_shared<facebook::react::NativeTuyaUserModuleSpecJSI>(params);
}

#endif

@end
