Pod::Spec.new do |s|
  s.name             = 'ThingSmartCryption'
  s.version          = '5.0.0'
  s.summary          = 'This is an encrypted SDK designed to ensure basic security.'
  s.description      = 'This is an encrypted SDK designed to ensure basic security.'
  s.homepage         = 'https://github.com/tuya'
  s.license          = { :type => 'MIT', :file => 'LICENSE' }
  s.author           = { 'ios' => 'developer@tuya.com' }
  s.source           = { :git => '', :tag => s.version.to_s }

  s.ios.deployment_target = '9.0'
  s.watchos.deployment_target = '2.0'

  s.ios.source_files = 'Build/ThingSmartCryption.xcframework/ios*simulator/ThingSmartCryption.framework/Headers/*'
  s.resources = ['Build/ThingSmartCryption.xcframework/ios*simulator/**/*.bundle', 'Build/ThingSmartCryption.xcframework/ios*simulator/**/t_cdc.tcfg']

  s.watchos.source_files = 'Build/ThingSmartCryption.xcframework/watchos*simulator/ThingSmartCryption.framework/Headers/*'

  s.vendored_frameworks = 'Build/ThingSmartCryption.xcframework'

  s.user_target_xcconfig = {
    'CLANG_ALLOW_NON_MODULAR_INCLUDES_IN_FRAMEWORK_MODULES' => 'YES',
  }
  s.pod_target_xcconfig = {
    'CLANG_ALLOW_NON_MODULAR_INCLUDES_IN_FRAMEWORK_MODULES' => 'YES',
    'DEFINES_MODULE' => 'YES',
    'EXCLUDED_ARCHS[sdk=iphonesimulator*]' => 'arm64',
  }
end
