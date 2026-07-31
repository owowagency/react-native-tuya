//
//  TuyaRNHomeManagerModule.h
//  TuyaRnDemo
//
//  Created by 浩天 on 2019/2/28.
//  Copyright © 2019年 Facebook. All rights reserved.
//

#import <Foundation/Foundation.h>

#ifdef RCT_NEW_ARCH_ENABLED
#import <RNTuyaSdkSpec/RNTuyaSdkSpec.h>
#else
#import <React/RCTBridgeModule.h>
#endif

NS_ASSUME_NONNULL_BEGIN

#ifdef RCT_NEW_ARCH_ENABLED
@interface TuyaRNHomeManagerModule : NSObject<NativeTuyaHomeManagerModuleSpec>
#else
@interface TuyaRNHomeManagerModule : NSObject<RCTBridgeModule>
#endif

@end

NS_ASSUME_NONNULL_END
