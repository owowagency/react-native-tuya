//
//  TuyaCoreApi.m
//  TuyaSdkTest
//
//  Created by 浩天 on 2019/2/27.
//  Copyright © 2019年 Facebook. All rights reserved.
//

#import "TuyaRNCoreModule.h"
#import <ThingSmartBaseKit/ThingSmartBaseKit.h>

@implementation TuyaRNCoreModule

RCT_EXPORT_MODULE(TuyaCoreModule)

//通用api
RCT_REMAP_METHOD(apiRequest,
                 postData:(NSDictionary *)parameters
                 resolver:(RCTPromiseResolveBlock)resolver
                 rejecter:(RCTPromiseRejectBlock)rejecter) {

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

#if RCT_NEW_ARCH_ENABLED

- (std::shared_ptr<facebook::react::TurboModule>)getTurboModule:
    (const facebook::react::ObjCTurboModule::InitParams &)params {
  return std::make_shared<facebook::react::NativeTuyaCoreModuleSpecJSI>(params);
}

#endif

@end
