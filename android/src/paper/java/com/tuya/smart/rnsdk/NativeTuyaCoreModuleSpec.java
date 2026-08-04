/**
 * Hand-written fallback matching what react-native-codegen would generate for
 * specs/NativeTuyaCoreModule.ts. Used only when New Architecture is disabled,
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

public abstract class NativeTuyaCoreModuleSpec extends ReactContextBaseJavaModule implements ReactModuleWithSpec, TurboModule {
  public NativeTuyaCoreModuleSpec(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @ReactMethod
  @DoNotStrip
  public abstract void apiRequest(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void initWithoutOptions();

  @ReactMethod
  @DoNotStrip
  public abstract void initWithOptions(ReadableMap params);

  @ReactMethod
  @DoNotStrip
  public abstract void setOnNeedLoginListener();

  @ReactMethod
  @DoNotStrip
  public abstract void exitApp();

  @ReactMethod
  @DoNotStrip
  public abstract void openNetworkSettings(ReadableMap params);

  @ReactMethod
  @DoNotStrip
  public abstract void onDestory(ReadableMap params);

  @ReactMethod
  @DoNotStrip
  public abstract void setLocation(ReadableMap params);

  @ReactMethod
  @DoNotStrip
  public abstract void getLocationData(Promise promise);
}
