/**
 * Hand-written fallback matching what react-native-codegen would generate for
 * specs/NativeTuyaTimerModule.ts. Used only when New Architecture is
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

public abstract class NativeTuyaTimerModuleSpec extends ReactContextBaseJavaModule implements ReactModuleWithSpec, TurboModule {
  public NativeTuyaTimerModuleSpec(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @ReactMethod
  @DoNotStrip
  public abstract void addTimerWithTask(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void getTimerTaskStatusWithDeviceId(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void updateTimerStatusWithTask(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void removeTimerWithTask(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void updateTimerWithTask(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void getAllTimerWithDeviceId(ReadableMap params, Promise promise);
}
