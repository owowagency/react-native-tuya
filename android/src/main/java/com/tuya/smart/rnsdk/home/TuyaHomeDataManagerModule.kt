package com.tuya.smart.rnsdk.home

import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.tuya.smart.rnsdk.NativeTuyaHomeDataManagerModuleSpec
import com.tuya.smart.rnsdk.utils.Constant.DEVID
import com.tuya.smart.rnsdk.utils.Constant.ROOMID
import com.tuya.smart.rnsdk.utils.JsonUtils
import com.tuya.smart.rnsdk.utils.ReactParamsCheck
import com.tuya.smart.rnsdk.utils.TuyaReactUtils

@ReactModule(name = TuyaHomeDataManagerModule.NAME)
class TuyaHomeDataManagerModule(reactContext: ReactApplicationContext) : NativeTuyaHomeDataManagerModuleSpec(reactContext) {

    companion object {
        const val NAME = "TuyaHomeDataManagerModule"
    }

    override fun getName(): String {
        return NAME
    }

    /* 根据设备获取房间信息 */
    override fun getDeviceRoomBean(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(DEVID), params)) {
            promise.resolve(TuyaReactUtils.parseToWritableMap(ThingHomeSdk.getDataInstance().getDeviceRoomBean(params.getString(DEVID))))
        }
    }

    /* 根据房间ID获取房间下面的设备列表 */
    override fun getRoomDeviceList(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(ROOMID), params)) {
            promise.resolve(TuyaReactUtils.parseToWritableArray(
                    JsonUtils.toJsonArray(ThingHomeSdk.getDataInstance().getRoomDeviceList(params.getDouble(ROOMID).toLong()))))
        }
    }

}
