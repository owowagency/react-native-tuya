const fs = require('fs');
const path = require('path');
const {
  withDangerousMod,
  withInfoPlist,
  withAndroidManifest,
  withProjectBuildGradle,
  withAppBuildGradle,
  withAppDelegate,
  withMainApplication,
} = require('@expo/config-plugins');

// These Maven repos are needed to resolve Tuya's Android SDK and its transitive
// dependencies - see submodules/react-native-tuya/android/build.gradle for the
// wrapper's own (self-contained) copy; the app-level build.gradle needs its own
// because the closed-source security-algorithm .aar is consumed at that level.
const MAVEN_REPO_URLS = [
  'https://maven-other.tuya.com/repository/maven-releases/',
  'https://maven-other.tuya.com/repository/maven-commercial-releases/',
  'https://maven.aliyun.com/repository/public',
  'https://oss.sonatype.org/content/repositories/snapshots/',
  'https://developer.huawei.com/repo/',
  'https://jitpack.io',
];

const MARKER = '@owowagency/react-native-tuya';

function withTuyaPodfile(config, { iosXcframeworkPath }) {
  return withDangerousMod(config, [
    'ios',
    async (config) => {
      const projectRoot = config.modRequest.projectRoot;
      const platformProjectRoot = config.modRequest.platformProjectRoot;
      const podfilePath = path.join(platformProjectRoot, 'Podfile');
      let contents = fs.readFileSync(podfilePath, 'utf8');

      const sourceMarker = `# ${MARKER}: custom CocoaPods sources`;
      if (!contents.includes(sourceMarker)) {
        contents = `${sourceMarker}\nsource 'https://github.com/CocoaPods/Specs'\nsource 'https://github.com/tuya/TuyaPublicSpecs.git'\nsource 'https://github.com/tuya/tuya-pod-specs.git'\n\n${contents}`;
      }

      const envMarker = `# ${MARKER}: force RCT_NEW_ARCH_ENABLED before autolinking evaluates podspecs`;
      if (!contents.includes(envMarker)) {
        contents = contents.replace(
          /^(platform :ios)/m,
          `${envMarker}\nENV['RCT_NEW_ARCH_ENABLED'] = '1'\n\n$1`
        );
      }

      const podMarker = `# ${MARKER}: closed-source crypto pod`;
      if (!contents.includes(podMarker)) {
        // Anchored to `use_expo_modules!`/`use_native_modules!` as a standalone
        // line start, not a bare token match - the modern Expo/RN template
        // Podfile has `config = use_native_modules!(...)` as one statement, and
        // matching mid-line there would split the assignment and comment out
        // the rest of the line (Ruby then silently sets config = nil).
        if (/^(\s*use_expo_modules!)/m.test(contents)) {
          contents = contents.replace(
            /^(\s*use_expo_modules!.*)$/m,
            `$1\n\n  ${podMarker}\n  pod 'ThingSmartCryption', :path => './tuya_ios_core_sdk'`
          );
        } else {
          contents = contents.replace(
            /^(\s*)(config\s*=\s*use_native_modules!.*)$/m,
            `$1${podMarker}\n$1pod 'ThingSmartCryption', :path => './tuya_ios_core_sdk'\n\n$1$2`
          );
        }
      }

      fs.writeFileSync(podfilePath, contents);

      // Copy the (non-proprietary) podspec bundled with this plugin, plus the
      // closed-source xcframework binary the consuming app supplies a path to.
      const destDir = path.join(platformProjectRoot, 'tuya_ios_core_sdk');
      fs.mkdirSync(destDir, { recursive: true });
      fs.copyFileSync(
        path.join(__dirname, 'ThingSmartCryption.podspec'),
        path.join(destDir, 'ThingSmartCryption.podspec')
      );

      const xcframeworkSrc = path.isAbsolute(iosXcframeworkPath)
        ? iosXcframeworkPath
        : path.join(projectRoot, iosXcframeworkPath);
      const xcframeworkDest = path.join(destDir, 'Build', 'ThingSmartCryption.xcframework');
      fs.mkdirSync(path.dirname(xcframeworkDest), { recursive: true });
      fs.rmSync(xcframeworkDest, { recursive: true, force: true });
      fs.cpSync(xcframeworkSrc, xcframeworkDest, { recursive: true });

      return config;
    },
  ]);
}

function withTuyaInfoPlist(config, props) {
  return withInfoPlist(config, (config) => {
    config.modResults.NSBluetoothAlwaysUsageDescription =
      props.bluetoothAlwaysUsageDescription ??
      config.modResults.NSBluetoothAlwaysUsageDescription ??
      'Bluetooth is used to access your device.';
    config.modResults.NSBluetoothPeripheralUsageDescription =
      props.bluetoothPeripheralUsageDescription ??
      config.modResults.NSBluetoothPeripheralUsageDescription ??
      'Bluetooth is used to access your device.';
    config.modResults.NSLocationAlwaysUsageDescription =
      props.locationAlwaysUsageDescription ??
      config.modResults.NSLocationAlwaysUsageDescription ??
      'Your location is used to set up your device.';
    config.modResults.NSLocationWhenInUseUsageDescription =
      props.locationWhenInUseUsageDescription ??
      config.modResults.NSLocationWhenInUseUsageDescription ??
      'Your location is used to set up your device.';

    const backgroundModes = new Set(config.modResults.UIBackgroundModes ?? []);
    backgroundModes.add('bluetooth-central');
    config.modResults.UIBackgroundModes = Array.from(backgroundModes);

    return config;
  });
}

function withTuyaAppDelegate(config, { appKey, secretKey }) {
  return withAppDelegate(config, (config) => {
    const marker = `${MARKER}: Tuya SDK init`;
    if (config.modResults.contents.includes(marker)) {
      return config;
    }

    if (config.modResults.language === 'swift') {
      config.modResults.contents = config.modResults.contents.replace(
        /^(import React)$/m,
        `$1\nimport ThingSmartHomeKit`
      );

      config.modResults.contents = config.modResults.contents.replace(
        /(didFinishLaunchingWithOptions launchOptions:[^\n]*\n\s*\) -> Bool \{\n)/,
        `$1    // ${marker}\n    #if DEBUG\n    ThingSmartSDK.sharedInstance().debugMode = true\n    #endif\n    ThingSmartSDK.sharedInstance().start(withAppKey: "${appKey}", secretKey: "${secretKey}")\n\n`
      );
    } else {
      config.modResults.contents = config.modResults.contents.replace(
        '#import "AppDelegate.h"',
        `#import "AppDelegate.h"\n#import <ThingSmartHomeKit/ThingSmartKit.h>`
      );

      config.modResults.contents = config.modResults.contents.replace(
        /(self\.moduleName = @"[^"]+";)/,
        `$1\n\n  // ${marker}\n  #ifdef DEBUG\n    [[ThingSmartSDK sharedInstance] setDebugMode:YES];\n  #endif\n  [[ThingSmartSDK sharedInstance] startWithAppKey:@"${appKey}" secretKey:@"${secretKey}"];`
      );
    }

    return config;
  });
}

function withTuyaAndroidManifest(config) {
  return withAndroidManifest(config, (config) => {
    const manifest = config.modResults;
    const permissions = [
      'SYSTEM_ALERT_WINDOW',
      'ACCESS_COARSE_LOCATION',
      'BLUETOOTH',
      'BLUETOOTH_ADMIN',
      'BLUETOOTH_SCAN',
      'BLUETOOTH_CONNECT',
      'ACCESS_FINE_LOCATION',
      'CHANGE_NETWORK_STATE',
      'CHANGE_WIFI_STATE',
      'ACCESS_WIFI_STATE',
      'ACCESS_NETWORK_STATE',
    ];

    manifest.manifest['uses-permission'] = manifest.manifest['uses-permission'] || [];
    const existing = new Set(
      manifest.manifest['uses-permission'].map((p) => p.$['android:name'])
    );
    for (const permission of permissions) {
      const name = `android.permission.${permission}`;
      if (!existing.has(name)) {
        const entry = { $: { 'android:name': name } };
        if (permission === 'BLUETOOTH' || permission === 'BLUETOOTH_ADMIN') {
          entry.$['android:maxSdkVersion'] = '30';
        }
        manifest.manifest['uses-permission'].push(entry);
      }
    }

    manifest.manifest['permission'] = manifest.manifest['permission'] || [];
    const existingPermissionDecls = new Set(
      manifest.manifest['permission'].map((p) => p.$['android:name'])
    );
    for (const name of [
      'android.permission.ACCESS_COARSE_LOCATION',
      'android.permission.ACCESS_FINE_LOCATION',
    ]) {
      if (!existingPermissionDecls.has(name)) {
        manifest.manifest['permission'].push({
          $: { 'android:name': name, 'android:protectionLevel': 'signature' },
        });
      }
    }

    return config;
  });
}

function withTuyaProjectBuildGradle(config) {
  return withProjectBuildGradle(config, (config) => {
    const marker = `// ${MARKER}: Maven repos`;
    if (config.modResults.contents.includes(marker)) {
      return config;
    }

    const repoLines = MAVEN_REPO_URLS.map((url) => `        maven { url '${url}' }`).join('\n');
    const block = `\n${marker}\nallprojects {\n    repositories {\n${repoLines}\n    }\n}\n`;

    config.modResults.contents += block;
    return config;
  });
}

function withTuyaAndroidAar(config, { androidAarPath }) {
  return withDangerousMod(config, [
    'android',
    async (config) => {
      const projectRoot = config.modRequest.projectRoot;
      const platformProjectRoot = config.modRequest.platformProjectRoot;

      const aarSrc = path.isAbsolute(androidAarPath)
        ? androidAarPath
        : path.join(projectRoot, androidAarPath);
      const libsDir = path.join(platformProjectRoot, 'app', 'libs');
      fs.mkdirSync(libsDir, { recursive: true });
      fs.copyFileSync(aarSrc, path.join(libsDir, path.basename(aarSrc)));

      return config;
    },
  ]);
}

function withTuyaAndroidAarDependency(config) {
  return withAppBuildGradle(config, (config) => {
    const marker = `// ${MARKER}: closed-source aar dependency`;
    if (config.modResults.contents.includes(marker)) {
      return config;
    }

    // withTuyaAndroidAar only copies the .aar into app/libs/ - it still needs
    // to be declared as a dependency, or Gradle never compiles it in (the
    // native libthing_security_algorithm.so silently ends up missing from
    // the APK, and Tuya's JNI calls crash at runtime with UnsatisfiedLinkError).
    const dependencyLine = `    ${marker}\n    implementation fileTree(dir: "libs", include: ["*.aar"])\n`;
    config.modResults.contents = config.modResults.contents.replace(
      /(dependencies\s*\{\n)/,
      `$1${dependencyLine}`
    );

    return config;
  });
}

function withTuyaMainApplication(config, { appKey, secretKey }) {
  return withMainApplication(config, (config) => {
    const marker = `${MARKER}: Tuya SDK init`;
    if (config.modResults.contents.includes(marker)) {
      return config;
    }

    const isKotlin = config.modResults.language === 'kt';
    const importLine = 'import com.tuya.smart.rnsdk.core.TuyaCoreModule';
    if (!config.modResults.contents.includes(importLine)) {
      config.modResults.contents = config.modResults.contents.replace(
        /^(import .*\n)/m,
        `$1${importLine}\n`
      );
    }

    const initCall = isKotlin
      ? `    // ${marker}\n    TuyaCoreModule.initTuyaSDk("${appKey}", "${secretKey}", this)\n`
      : `    // ${marker}\n    TuyaCoreModule.initTuyaSDk("${appKey}", "${secretKey}", this);\n`;

    config.modResults.contents = config.modResults.contents.replace(
      /(override fun onCreate\(\) \{\n\s*super\.onCreate\(\)\n)/,
      `$1${initCall}`
    );
    // Java fallback, in case the template ever generates a Java MainApplication.
    config.modResults.contents = config.modResults.contents.replace(
      /(public void onCreate\(\) \{\n\s*super\.onCreate\(\);\n)/,
      `$1${initCall}`
    );

    return config;
  });
}

/**
 * Expo config plugin for @owowagency/react-native-tuya.
 *
 * The closed-source Tuya binaries (ThingSmartCryption.xcframework, the Android
 * security-algorithm .aar) are not published inside this package - the consuming
 * app supplies paths to its own copies, which this plugin copies into the
 * generated native project at `expo prebuild` time.
 */
function withTuya(config, props) {
  const {
    iosXcframeworkPath,
    androidAarPath,
    iosAppKey,
    iosSecretKey,
    androidAppKey,
    androidSecretKey,
    bluetoothAlwaysUsageDescription,
    bluetoothPeripheralUsageDescription,
    locationAlwaysUsageDescription,
    locationWhenInUseUsageDescription,
  } = props;

  config = withTuyaPodfile(config, { iosXcframeworkPath });
  config = withTuyaInfoPlist(config, {
    bluetoothAlwaysUsageDescription,
    bluetoothPeripheralUsageDescription,
    locationAlwaysUsageDescription,
    locationWhenInUseUsageDescription,
  });
  config = withTuyaAppDelegate(config, { appKey: iosAppKey, secretKey: iosSecretKey });

  config = withTuyaAndroidManifest(config);
  config = withTuyaProjectBuildGradle(config);
  config = withTuyaAndroidAar(config, { androidAarPath });
  config = withTuyaAndroidAarDependency(config);
  config = withTuyaMainApplication(config, { appKey: androidAppKey, secretKey: androidSecretKey });

  return config;
}

module.exports = withTuya;
