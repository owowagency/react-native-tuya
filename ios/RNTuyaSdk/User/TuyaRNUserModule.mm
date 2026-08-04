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
#define kTuyaRNUserModulePhoneNumber @"phoneNumber"
#define kTuyaRNUserModulePhone @"phone"
#define kTuyaRNUserModuleValidateType @"validateType"
#define kTuyaRNUserModuleValidateCode @"validateCode"
#define kTuyaRNUserModulePassword @"password"
#define kTuyaRNUserModuleNewPassword @"newPassword"
#define kTuyaRNUserModuleEmail @"email"
#define kTuyaRNUserModuleUid @"uid"

#define kTuyaRNUserModuleTwitterKey @"key"
#define kTuyaRNUserModuleTwitterSecret @"secret"
#define kTuyaRNUserModuleQQUserId @"userId"
#define kTuyaRNUserModuleQQAccessToken @"accessToken"
#define kTuyaRNUserModuleWechatkCode @"code"
#define kTuyaRNUserModuleFacebookCode @"code"

#define kTuyaRNUserModuleImageFile @"file"
#define kTuyaRNUserModuleUnit @"unit"

@implementation TuyaRNUserModule

RCT_EXPORT_MODULE(TuyaUserModule)

/* 版本检测 */
RCT_EXPORT_METHOD(checkVersionUpgrade:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  if (resolver) {
    resolver([NSNumber numberWithBool:[[ThingSmartSDK sharedInstance] checkVersionUpgrade]]);
  }
}

/* 版本升级 */
RCT_EXPORT_METHOD(upgradeVersion:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  [[ThingSmartSDK sharedInstance] upgradeVersion:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];

}

/*获取手机验证码
* @param countryCode   国家区号
* @param phoneNumber   手机号码
* @param validateType  验证码类型
*/
RCT_EXPORT_METHOD(getValidateCode:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSString *countryCode = params[kTuyaRNUserModuleCountryCode];
  NSString *phoneNumber = params[kTuyaRNUserModulePhoneNumber];
  NSInteger aType = 1;
  NSString *validateType = params[kTuyaRNUserModuleValidateType];
  if (validateType) {
    if ([validateType isKindOfClass:[NSString class]] ||
        [validateType isKindOfClass:[NSNumber class]]) {
      aType = validateType.integerValue;
    }
  }
  [[ThingSmartUser sharedInstance] sendVerifyCode:countryCode phoneNumber:phoneNumber type:aType success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];
}

/* 手机验证码登陆
* @param countryCode 国家区号
* @param phone       电话
* @param code        验证码
*/
RCT_EXPORT_METHOD(loginWithValidateCode:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSString *countryCode = params[kTuyaRNUserModuleCountryCode];
  NSString *phoneNumber = params[kTuyaRNUserModulePhoneNumber];
  NSString *phone = params[kTuyaRNUserModulePhone];

  if(phone.length > 0) {
    phoneNumber = phone;
  }
  NSString *validateCode = params[kTuyaRNUserModuleValidateCode];
  [[ThingSmartUser sharedInstance] login:countryCode phoneNumber:phoneNumber code:validateCode success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];

}

/*
* 注册手机密码账户
* @param countryCode 国家区号
* @param phone       手机密码
* @param passwd      登陆密码
*/
RCT_EXPORT_METHOD(registerAccountWithPhone:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSString *countryCode = params[kTuyaRNUserModuleCountryCode];
  NSString *phoneNumber = params[kTuyaRNUserModulePhoneNumber];
  NSString *phone = params[kTuyaRNUserModulePhone];
  if(phone.length > 0) {
    phoneNumber = phone;
  }
  NSString *password = params[kTuyaRNUserModulePassword];

  //验证码  可以为空
  NSString *validateCode = params[kTuyaRNUserModuleValidateCode];
  if (validateCode.length == 0) {
    validateCode = @"";
  }

  [[ThingSmartUser sharedInstance] registerByPhone:countryCode phoneNumber:phoneNumber password:password code:validateCode success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];

}

/*手机密码登陆
* @param countryCode 国家区号
* @param phone       手机密码
* @param passwd      登陆密码
 */
RCT_EXPORT_METHOD(loginWithPhonePassword:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSString *countryCode = params[kTuyaRNUserModuleCountryCode];
  NSString *phoneNumber = params[kTuyaRNUserModulePhoneNumber];
  NSString *phone = params[kTuyaRNUserModulePhone];
  if(phone.length > 0) {
    phoneNumber = phone;
  }
  NSString *password = params[kTuyaRNUserModulePassword];

  [[ThingSmartUser sharedInstance] loginByPhone:countryCode phoneNumber:phoneNumber password:password success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];

}

/* 重置密码
* @param countryCode 国家区号
* @param phone       手机号码
* @param code        手机验证码
* @param newPasswd   新密码
*/
RCT_EXPORT_METHOD(resetPhonePassword:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSString *countryCode = params[kTuyaRNUserModuleCountryCode];
  NSString *phoneNumber = params[kTuyaRNUserModulePhoneNumber];
  NSString *phone = params[kTuyaRNUserModulePhone];
  if(phone.length > 0) {
    phoneNumber = phone;
  }
  NSString *password = params[kTuyaRNUserModuleNewPassword];
  NSString *validateCode = params[kTuyaRNUserModuleValidateCode];

  [[ThingSmartUser sharedInstance] resetPasswordByPhone:countryCode phoneNumber:phoneNumber newPassword:password code:validateCode success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];

}

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

/* 用户uid注册
* @param countryCode 国家号码
* @param uid         用户uid
* @param password    用户密码
*/
RCT_EXPORT_METHOD(registerAccountWithUid:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSString *countryCode = params[kTuyaRNUserModuleCountryCode];
  NSString *uid = params[kTuyaRNUserModuleUid];
  NSString *password = params[kTuyaRNUserModulePassword];

  [[ThingSmartUser sharedInstance] registerByUid:uid password:password countryCode:countryCode success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];

}

/* uid 登陆
* @param countryCode 国家号码
* @param uid         用户uid
* @param passwd      用户密码
 */
RCT_EXPORT_METHOD(loginWithUid:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSString *countryCode = params[kTuyaRNUserModuleCountryCode];
  NSString *uid = params[kTuyaRNUserModuleUid];
  NSString *password = params[kTuyaRNUserModulePassword];

  [[ThingSmartUser sharedInstance] loginByUid:uid password:password countryCode:countryCode success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];

}

/* uid 登陆+注册
* @param countryCode 国家号码
* @param uid         用户uid
* @param passwd      用户密码
*/
RCT_EXPORT_METHOD(loginOrRegisterWithUid:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSString *countryCode = params[kTuyaRNUserModuleCountryCode];
  NSString *uid = params[kTuyaRNUserModuleUid];
  NSString *password = params[kTuyaRNUserModulePassword];

  [[ThingSmartUser sharedInstance] loginOrRegisterWithCountryCode:countryCode uid:uid password:password success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];
}

/* Twitter 登陆
* @param countryCode 国家区号
* @param key         twitter授权登录获取的key
* @param secret      twitter授权登录获取的secret
 */
RCT_EXPORT_METHOD(loginByTwitter:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSString *countryCode = params[kTuyaRNUserModuleCountryCode];
  NSString *key = params[kTuyaRNUserModuleTwitterKey];
  NSString *secret = params[kTuyaRNUserModuleTwitterSecret];

  [[ThingSmartUser sharedInstance] loginByTwitter:countryCode key:key secret:secret success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];
}

/* QQ登录
* @param countryCode 国家区号
* @param userId          QQ授权登录获取的userId
* @param accessToken      QQ授权登录获取的accessToken
*/
RCT_EXPORT_METHOD(loginByQQ:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSString *countryCode = params[kTuyaRNUserModuleCountryCode];
  NSString *userId = params[kTuyaRNUserModuleQQUserId];
  NSString *accountToken = params[kTuyaRNUserModuleQQAccessToken];

  [[ThingSmartUser sharedInstance] loginByQQ:countryCode userId:userId accessToken:accountToken success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];
}

/* 微信登录
* @param countryCode 国家区号
* @param code        微信授权登录获取的code
*/
RCT_EXPORT_METHOD(loginByWechat:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSString *countryCode = params[kTuyaRNUserModuleCountryCode];
  NSString *code = params[kTuyaRNUserModuleWechatkCode];

  [[ThingSmartUser sharedInstance] loginByWechat:countryCode code:code success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];

}

/* Facebook登录
* @param countryCode 国家区号
* @param code     token facebook授权登录获取的token
 */
RCT_EXPORT_METHOD(loginByFacebook:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSString *countryCode = params[kTuyaRNUserModuleCountryCode];
  NSString *code = params[kTuyaRNUserModuleFacebookCode];

  [[ThingSmartUser sharedInstance] loginByFacebook:countryCode token:code success:^{
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

RCT_EXPORT_METHOD(uploadUserAvatar:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  UIImage *image = params[kTuyaRNUserModuleImageFile];

  if (image == nil || ![image isKindOfClass:[UIImage class]]) {
    [TuyaRNUtils rejecterWithError:[NSError thingsdk_errorWithCodeString:@"999" errorMsg:@"error image info"] handler:rejecter];
    return;
  }

  [[ThingSmartUser sharedInstance] updateHeadIcon:image success:^{
    [TuyaRNUtils resolverWithHandler:resolver];
  } failure:^(NSError *error) {
    [TuyaRNUtils rejecterWithError:error handler:rejecter];
  }];
}

RCT_EXPORT_METHOD(setTempUnit:(NSDictionary *)params resolve:(RCTPromiseResolveBlock)resolver reject:(RCTPromiseRejectBlock)rejecter) {

  NSString *unit = params[kTuyaRNUserModuleUnit];
  if (unit) {
    [[ThingSmartUser sharedInstance] setTempUnit:unit.integerValue];
    if (resolver) {
      resolver(@"success");
    }
  } else {
     [TuyaRNUtils rejecterWithError:[NSError thingsdk_errorWithCodeString:@"999" errorMsg:@"error params"] handler:rejecter];
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
