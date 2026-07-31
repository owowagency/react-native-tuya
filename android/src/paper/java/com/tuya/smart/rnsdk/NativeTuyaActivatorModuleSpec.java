/**
 * Hand-written fallback matching what react-native-codegen would generate for
 * specs/NativeTuyaActivatorModule.ts. Used only when New Architecture is
 * disabled. Keep in sync with the TS spec.
 *
 * @nolint
 */

package com.tuya.smart.rnsdk;

import com.facebook.proguard.annotations.DoNotStrip;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReactModuleWithSpec;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.turbomodule.core.interfaces.TurboModule;

public abstract class NativeTuyaActivatorModuleSpec extends ReactContextBaseJavaModule implements ReactModuleWithSpec, TurboModule {
  public NativeTuyaActivatorModuleSpec(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @ReactMethod
  @DoNotStrip
  public abstract void startBluetoothScan(Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void initBluetoothDualModeActivator(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void getCurrentWifi(ReadableMap params, Callback success, Callback error);

  @ReactMethod
  @DoNotStrip
  public abstract void openNetworkSettings(ReadableMap params);

  @ReactMethod
  @DoNotStrip
  public abstract void initActivator(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void stopConfig();
}
