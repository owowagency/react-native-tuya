/**
 * Hand-written fallback matching what react-native-codegen would generate for
 * specs/NativeTuyaHomeMemberModule.ts. Used only when New Architecture is
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

public abstract class NativeTuyaHomeMemberModuleSpec extends ReactContextBaseJavaModule implements ReactModuleWithSpec, TurboModule {
  public NativeTuyaHomeMemberModuleSpec(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @ReactMethod
  @DoNotStrip
  public abstract void addMember(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void removeMember(ReadableMap params, Promise promise);

  @ReactMethod
  @DoNotStrip
  public abstract void queryMemberList(ReadableMap params, Promise promise);
}
