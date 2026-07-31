/**
 * Hand-written fallback matching what react-native-codegen would generate for
 * specs/NativeTuyaRoomModule.ts. Used only when New Architecture is disabled,
 * since Codegen doesn't generate Java/Kotlin spec classes in that mode. Keep
 * in sync with the TS spec.
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

public abstract class NativeTuyaRoomModuleSpec extends ReactContextBaseJavaModule implements ReactModuleWithSpec, TurboModule {
  public NativeTuyaRoomModuleSpec(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @ReactMethod
  @DoNotStrip
  public abstract void updateRoom(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void addDevice(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void removeDevice(ReadableMap params, Promise promise);
}
