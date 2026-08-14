/**
 * Hand-written fallback matching what react-native-codegen would generate for
 * specs/NativeTuyaDeviceModule.ts. Used only when New Architecture is
 * disabled. Keep in sync with the TS spec.
 *
 * @nolint
 */

package com.tuya.smart.rnsdk;

import com.facebook.proguard.annotations.DoNotStrip;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReactModuleWithSpec;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.turbomodule.core.interfaces.TurboModule;

public abstract class NativeTuyaDeviceModuleSpec extends ReactContextBaseJavaModule implements ReactModuleWithSpec, TurboModule {
  public NativeTuyaDeviceModuleSpec(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @ReactMethod
  @DoNotStrip
  public abstract void getDevice(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void registerDevListener(ReadableMap params);

  @ReactMethod
  @DoNotStrip
  public abstract void unRegisterDevListener(ReadableMap params);

  @ReactMethod
  @DoNotStrip
  public abstract void send(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void renameDevice(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void getDataPointStat(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void removeDevice(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void getOtaInfo(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void startOta(ReadableMap params);
}
