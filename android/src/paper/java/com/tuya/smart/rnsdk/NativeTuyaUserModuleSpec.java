/**
 * Hand-written fallback matching what react-native-codegen would generate for
 * specs/NativeTuyaUserModule.ts. Used only when New Architecture is
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

public abstract class NativeTuyaUserModuleSpec extends ReactContextBaseJavaModule implements ReactModuleWithSpec, TurboModule {
  public NativeTuyaUserModuleSpec(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @ReactMethod
  @DoNotStrip
  public abstract void cancelAccount(Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void getCurrentUser(Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void getEmailValidateCode(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void getRegisterEmailValidateCode(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void loginWithEmail(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void logout(Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void registerAccountWithEmail(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void resetEmailPassword(ReadableMap params, Promise promise);
}
