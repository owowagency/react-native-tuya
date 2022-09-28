package com.tuya.smart.rnsdk.activator

import android.content.Intent
import android.provider.Settings
import com.facebook.react.bridge.*
import com.facebook.react.modules.core.DeviceEventManagerModule.RCTDeviceEventEmitter
import com.tuya.smart.android.ble.api.ScanType
import com.tuya.smart.android.common.utils.WiFiUtil
import com.tuya.smart.home.sdk.TuyaHomeSdk
import com.tuya.smart.home.sdk.builder.ActivatorBuilder
import com.tuya.smart.home.sdk.builder.TuyaCameraActivatorBuilder
import com.tuya.smart.home.sdk.builder.TuyaGwSubDevActivatorBuilder
import com.tuya.smart.rnsdk.utils.Constant.ADDRESS
import com.tuya.smart.rnsdk.utils.Constant.DEVICE_TYPE
import com.tuya.smart.rnsdk.utils.Constant.DEVID
import com.tuya.smart.rnsdk.utils.Constant.HOMEID
import com.tuya.smart.rnsdk.utils.Constant.MAC
import com.tuya.smart.rnsdk.utils.Constant.PASSWORD
import com.tuya.smart.rnsdk.utils.Constant.SCAN_TYPE
import com.tuya.smart.rnsdk.utils.Constant.SSID
import com.tuya.smart.rnsdk.utils.Constant.TIME
import com.tuya.smart.rnsdk.utils.Constant.TIMEOUT
import com.tuya.smart.rnsdk.utils.Constant.TOKEN
import com.tuya.smart.rnsdk.utils.Constant.TYPE
import com.tuya.smart.rnsdk.utils.Constant.UUID
import com.tuya.smart.rnsdk.utils.ReactParamsCheck
import com.tuya.smart.rnsdk.utils.TuyaReactUtils
import com.tuya.smart.sdk.api.*
import com.tuya.smart.sdk.bean.DeviceBean
import com.tuya.smart.sdk.bean.MultiModeActivatorBean
import com.tuya.smart.sdk.enums.ActivatorModelEnum
import android.util.Log


abstract class TuyaActivatorModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  var mITuyaActivator: ITuyaActivator? = null
  var mTuyaGWActivator: ITuyaActivator? = null
  var mTuyaCameraActivator: ITuyaCameraDevActivator? = null
  var leActivatorUUID: String? = null

  override fun getName(): String {
    return "TuyaActivatorModule"
  }

  companion object {
    const val ON_SCAN_BEAN_EVENT = "ON_SCAN_BEAN_EVENT"
  }

  @ReactMethod
  fun startBluetoothScan(params: ReadableMap, promise: Promise) {
    if (ReactParamsCheck.checkParams(arrayOf(TIMEOUT, SCAN_TYPE), params)) {
      TuyaHomeSdk.getBleOperator().startLeScan(
        params.getInt(TIMEOUT), params.getString(SCAN_TYPE)?.let { ScanType.valueOf(it) }
      ) { bean ->
        this.reactApplicationContext.getJSModule(RCTDeviceEventEmitter::class.java).emit(
          ON_SCAN_BEAN_EVENT, TuyaReactUtils.parseToWritableMap(bean)
        )
      };
      promise.resolve(true)
    }
  }

  @ReactMethod
  fun stopBluetoothScan() {
    TuyaHomeSdk.getBleOperator().stopLeScan();
  }


  @ReactMethod
  fun initBluetoothDualModeActivator(params: ReadableMap, promise: Promise) {
    if (ReactParamsCheck.checkParams(arrayOf(HOMEID, SSID, PASSWORD), params)) {

      TuyaHomeSdk.getBleOperator().startLeScan(
        60000, ScanType.SINGLE
      ) { bean ->
        params.getDouble(HOMEID).toLong().let {
          TuyaHomeSdk.getActivatorInstance()
            .getActivatorToken(it, object : ITuyaActivatorGetToken {
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

                TuyaHomeSdk.getActivator().newMultiModeActivator()
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

  @ReactMethod
  fun initBluetoothDualModeActivatorFromScanBean(params: ReadableMap, promise: Promise) {

    if (ReactParamsCheck.checkParams(
        arrayOf(
          HOMEID,
          SSID,
          PASSWORD,
          TOKEN,
          UUID,
          ADDRESS,
          MAC,
          DEVICE_TYPE,
          TIMEOUT
        ), params
      )
    ) {

      val multiModeActivatorBean = MultiModeActivatorBean();
      multiModeActivatorBean.ssid = params.getString(SSID);
      multiModeActivatorBean.pwd = params.getString(PASSWORD);

      multiModeActivatorBean.uuid = params.getString(UUID)
      multiModeActivatorBean.deviceType = params.getInt(DEVICE_TYPE)
      multiModeActivatorBean.mac = params.getString(MAC)
      multiModeActivatorBean.address = params.getString(ADDRESS)


      multiModeActivatorBean.homeId = params.getDouble(HOMEID).toLong();
      multiModeActivatorBean.token = params.getString(TOKEN);
      multiModeActivatorBean.timeout = params.getInt(TIMEOUT).toLong();
      multiModeActivatorBean.phase1Timeout = 60000;

      leActivatorUUID = params.getString(UUID);
      TuyaHomeSdk.getActivator().newMultiModeActivator()
        .startActivator(multiModeActivatorBean, object : IMultiModeActivatorListener {
          override fun onSuccess(bean: DeviceBean) {
            promise.resolve(TuyaReactUtils.parseToWritableMap(bean));
          }

          override fun onFailure(code: Int, msg: String?, handle: Any?) {
            Log.e("TuyaActivatorModule", code.toString() + msg.toString());
            promise.reject(code.toString(), msg);
          }
        });
    }
  }

  @ReactMethod
  fun stopLeActivation() {
    if (leActivatorUUID != null) {
      TuyaHomeSdk.getBleManager().stopBleConfig(leActivatorUUID)
    }
  }

  @ReactMethod
  fun getCurrentWifi(
    params: ReadableMap, successCallback: Callback,
    errorCallback: Callback
  ) {
    successCallback.invoke(WiFiUtil.getCurrentSSID(reactApplicationContext.applicationContext));
  }

  @ReactMethod
  fun openNetworkSettings(params: ReadableMap) {
    val currentActivity = currentActivity
    if (currentActivity == null) {
      return
    }
    try {
      currentActivity.startActivity(Intent(Settings.ACTION_SETTINGS))
    } catch (e: Exception) {
    }

  }

  @ReactMethod
  fun getActivatorToken(params: ReadableMap, promise: Promise) {
    if (ReactParamsCheck.checkParams(arrayOf(HOMEID), params)) {
      TuyaHomeSdk.getActivatorInstance()
        .getActivatorToken(params.getDouble(HOMEID).toLong(), object : ITuyaActivatorGetToken {
          override fun onSuccess(token: String?) {
            promise.resolve(token);
          }

          override fun onFailure(errorCode: String?, errorMsg: String?) {
            promise.reject(errorCode, errorMsg);
          }
        })
    }
  }

  @ReactMethod
  fun startQRActivator(params: ReadableMap, promise: Promise) {
    if (ReactParamsCheck.checkParams(arrayOf(TOKEN, TIME), params)) {
      val cBuilder = TuyaCameraActivatorBuilder()
        .setToken(params.getString(TOKEN))
        .setTimeOut(params.getInt(TIME).toLong())
        .setContext(reactApplicationContext.applicationContext)
        .setListener(object : ITuyaSmartCameraActivatorListener {
          override fun onActiveSuccess(devResp: DeviceBean?) {
            promise.resolve(TuyaReactUtils.parseToWritableMap(devResp));
          }

          override fun onQRCodeSuccess(qrcodeUrl: String?) {}

          override fun onError(errorCode: String?, errorMsg: String?) {
            promise.reject(errorCode, errorMsg);
          }
        });

      mTuyaCameraActivator = TuyaHomeSdk.getActivatorInstance().newCameraDevActivator(cBuilder);
      mTuyaCameraActivator?.start();
    }
  }

  @ReactMethod
  fun stopQRActivator() {
    mTuyaCameraActivator?.stop();
  }

  @ReactMethod
  fun initActivator(params: ReadableMap, promise: Promise) {
    if (ReactParamsCheck.checkParams(arrayOf(HOMEID, SSID, PASSWORD, TIME, TYPE), params)) {
      TuyaHomeSdk.getActivatorInstance()
        .getActivatorToken(params.getDouble(HOMEID)?.toLong(), object : ITuyaActivatorGetToken {
          override fun onSuccess(token: String) {
            mITuyaActivator = TuyaHomeSdk.getActivatorInstance().newActivator(
              ActivatorBuilder()
                .setSsid(params.getString(SSID))
                .setContext(reactApplicationContext.applicationContext)
                .setPassword(params.getString(PASSWORD))
                .setActivatorModel(ActivatorModelEnum.valueOf(params.getString(TYPE) as String))
                .setTimeOut(params.getInt(TIME).toLong())
                .setToken(token).setListener(getITuyaSmartActivatorListener(promise))
            )
            mITuyaActivator?.start()
          }


          override fun onFailure(s: String, s1: String) {
            promise.reject(s, s1)
          }
        })
    }

  }

  /**
   * ZigBee子设备配网需要ZigBee网关设备云在线的情况下才能发起,且子设备处于配网状态。
   */
  @ReactMethod
  fun newGwSubDevActivator(params: ReadableMap, promise: Promise) {
    if (ReactParamsCheck.checkParams(arrayOf(DEVID, TIME), params)) {
      val builder = TuyaGwSubDevActivatorBuilder()
        //设置网关ID
        .setDevId(params.getString(DEVID))
        //设置配网超时时间
        .setTimeOut(params.getInt(TIME).toLong())
        .setListener(object : ITuyaSmartActivatorListener {
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
            // promise.reject(var1,"")
          }
        })

      mTuyaGWActivator = TuyaHomeSdk.getActivatorInstance().newGwSubDevActivator(builder)
    }
  }

  @ReactMethod
  fun stopConfig() {
    mITuyaActivator?.stop()
    mTuyaGWActivator?.stop()
  }

  @ReactMethod
  fun onDestory() {
    mITuyaActivator?.onDestroy()
    mTuyaGWActivator?.onDestroy()
    mTuyaCameraActivator?.onDestroy()
  }

  fun getITuyaSmartActivatorListener(promise: Promise): ITuyaSmartActivatorListener {
    return object : ITuyaSmartActivatorListener {
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
