package com.tuya.smart.rnsdk.activator

import android.content.Intent
import android.provider.Settings
import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.thingclips.smart.android.ble.api.ScanType
import com.thingclips.smart.android.common.utils.WiFiUtil
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.builder.ActivatorBuilder
import com.thingclips.smart.sdk.api.IMultiModeActivatorListener
import com.thingclips.smart.sdk.api.IThingActivator
import com.thingclips.smart.sdk.api.IThingActivatorGetToken
import com.thingclips.smart.sdk.api.IThingSmartActivatorListener
import com.thingclips.smart.sdk.bean.DeviceBean
import com.thingclips.smart.sdk.bean.MultiModeActivatorBean
import com.thingclips.smart.sdk.enums.ActivatorModelEnum
import com.tuya.smart.rnsdk.NativeTuyaActivatorModuleSpec
import com.tuya.smart.rnsdk.utils.*
import com.tuya.smart.rnsdk.utils.Constant.HOMEID
import com.tuya.smart.rnsdk.utils.Constant.PASSWORD
import com.tuya.smart.rnsdk.utils.Constant.SSID
import com.tuya.smart.rnsdk.utils.Constant.TIME
import com.tuya.smart.rnsdk.utils.Constant.TYPE

@ReactModule(name = TuyaActivatorModule.NAME)
class TuyaActivatorModule(reactContext: ReactApplicationContext) : NativeTuyaActivatorModuleSpec(reactContext) {

  companion object {
    const val NAME = "TuyaActivatorModule"
  }

  var mITuyaActivator: IThingActivator? = null
  var mTuyaGWActivator: IThingActivator? = null

  override fun getName(): String {
    return NAME
  }

  override fun startBluetoothScan(promise: Promise) {
    ThingHomeSdk.getBleOperator().startLeScan(60000, ScanType.SINGLE
    ) { bean -> promise.resolve(TuyaReactUtils.parseToWritableMap(bean)) };
  }

  override fun initBluetoothDualModeActivator(params: ReadableMap, promise: Promise) {
    if (ReactParamsCheck.checkParams(arrayOf(HOMEID, SSID, PASSWORD), params)) {

      ThingHomeSdk.getBleOperator().startLeScan(60000, ScanType.SINGLE
      ) { bean ->
        params.getDouble(HOMEID).toLong().let {
          ThingHomeSdk.getActivatorInstance()
            .getActivatorToken(it, object : IThingActivatorGetToken {
              override fun onSuccess(token: String) {
                val multiModeActivatorBean = MultiModeActivatorBean();
                multiModeActivatorBean.ssid = params.getString(SSID);
                multiModeActivatorBean.pwd = params.getString(PASSWORD);

                multiModeActivatorBean.uuid = bean.getUuid();
                multiModeActivatorBean.deviceType = bean.getDeviceType();
                multiModeActivatorBean.mac = bean.getMac();
                multiModeActivatorBean.address = bean.getAddress();


                multiModeActivatorBean.homeId = params.getDouble(HOMEID).toLong();
                multiModeActivatorBean.token = token;
                multiModeActivatorBean.timeout = 180000;
                multiModeActivatorBean.phase1Timeout = 60000;

                ThingHomeSdk.getActivator().newMultiModeActivator()
                  .startActivator(multiModeActivatorBean, object : IMultiModeActivatorListener {
                    override fun onSuccess(bean: DeviceBean) {
                      promise.resolve(TuyaReactUtils.parseToWritableMap(bean));
                    }

                    override fun onFailure(code: Int, msg: String?, handle: Any?) {
                      promise.reject(code.toString(), msg);
                    }
                  });
              }

              override fun onFailure(s: String, s1: String) {
                promise.reject(s, s1);
              }
            })
        }
      };
    }
  }

  override fun getCurrentWifi(params: ReadableMap, successCallback: Callback,
                     errorCallback: Callback) {
    successCallback.invoke(WiFiUtil.getCurrentSSID(reactApplicationContext.applicationContext));
  }

  override fun openNetworkSettings(params: ReadableMap) {
    val currentActivity = currentActivity
    if (currentActivity == null) {
      return
    }
    try {
      currentActivity.startActivity(Intent(Settings.ACTION_SETTINGS))
    } catch (e: Exception) {
    }

  }

  /**
   * Maps string value to ActivatorModelEnum
   * Returns null on unknown case
   */
  private fun getActivatorModelEnumByString(value: String): ActivatorModelEnum? {
    return when (value) {
      "THING_AP" -> ActivatorModelEnum.THING_AP
      "THING_EZ" -> ActivatorModelEnum.THING_EZ
      "THING_4G_GATEWAY" -> ActivatorModelEnum.THING_4G_GATEWAY
      "THING_QR" -> ActivatorModelEnum.THING_QR
      else -> null
    }
  }

  override fun initActivator(params: ReadableMap, promise: Promise) {
    if (ReactParamsCheck.checkParams(arrayOf(HOMEID, SSID, PASSWORD, TIME, TYPE), params)) {
      ThingHomeSdk.getActivatorInstance().getActivatorToken(params.getDouble(HOMEID).toLong(), object : IThingActivatorGetToken {
        override fun onSuccess(token: String) {
          val modeValue = params.getString(TYPE) as String
          val mode = getActivatorModelEnumByString(modeValue) ?: ActivatorModelEnum.THING_EZ
          mITuyaActivator = ThingHomeSdk.getActivatorInstance().newActivator(
            ActivatorBuilder()
            .setSsid(params.getString(SSID))
            .setContext(reactApplicationContext.applicationContext)
            .setPassword(params.getString(PASSWORD))
            .setActivatorModel(mode)
            .setTimeOut(params.getInt(TIME).toLong())
            .setToken(token).setListener(getITuyaSmartActivatorListener(promise)))
          mITuyaActivator?.start()
        }


        override fun onFailure(s: String, s1: String) {
          promise.reject(s, s1)
        }
      })
    }

  }

  override fun stopConfig() {
    mITuyaActivator?.stop()
    mTuyaGWActivator?.stop()
  }

  fun getITuyaSmartActivatorListener(promise: Promise): IThingSmartActivatorListener {
    return object : IThingSmartActivatorListener {
      /**
       * 1001        网络错误
      1002        配网设备激活接口调用失败，接口调用不成功
      1003        配网设备激活失败，设备找不到。
      1004        token 获取失败
      1005        设备没有上线
      1006        配网超时
       */
      override fun onError(var1: String, var2: String) {
        promise.reject(var1, var2)
      }

      /**
       * 设备配网成功,且设备上线（手机可以直接控制），可以通过
       */
      override fun onActiveSuccess(var1: DeviceBean) {
        promise.resolve(TuyaReactUtils.parseToWritableMap(var1))
      }

      /**
       * device_find 发现设备
      device_bind_success 设备绑定成功，但还未上线，此时设备处于离线状态，无法控制设备。
       */
      override fun onStep(var1: String, var2: Any) {
        // IOS 没有onStep保持一致
        //promise.reject(var1,"")
      }
    }
  }
}
