/**
 * Hand-written fallback matching what react-native-codegen would generate for
 * specs/NativeTuyaHomeModule.ts. Used only when New Architecture is
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

public abstract class NativeTuyaHomeModuleSpec extends ReactContextBaseJavaModule implements ReactModuleWithSpec, TurboModule {
  public NativeTuyaHomeModuleSpec(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @ReactMethod
  @DoNotStrip
  public abstract void getHomeDetail(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void updateHome(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void dismissHome(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void sortRoom(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void queryRoomList(ReadableMap params, Promise promise);
}
