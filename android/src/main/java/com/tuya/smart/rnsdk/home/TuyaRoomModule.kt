package com.tuya.smart.rnsdk.home

import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.tuya.smart.rnsdk.NativeTuyaRoomModuleSpec
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.api.IThingRoom
import com.tuya.smart.rnsdk.utils.Constant.NAME
import com.tuya.smart.rnsdk.utils.Constant.DEVID
import com.tuya.smart.rnsdk.utils.Constant.ROOMID
import com.tuya.smart.rnsdk.utils.Constant.getIResultCallback
import com.tuya.smart.rnsdk.utils.ReactParamsCheck

@ReactModule(name = TuyaRoomModule.NAME)
class TuyaRoomModule(reactContext: ReactApplicationContext) : NativeTuyaRoomModuleSpec(reactContext) {

    companion object {
        const val NAME = "TuyaRoomModule"
    }

    override fun getName(): String {
        return NAME
    }

    /* 更新房间名称 */
    override fun updateRoom(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(ROOMID, NAME), params)) {
            getRoomInstance(params.getDouble(ROOMID)).updateRoom(params.getString(NAME), getIResultCallback(promise))
        }
    }

    /* 添加设备 */
    override fun addDevice(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(ROOMID, DEVID), params)) {
            getRoomInstance(params.getDouble(ROOMID)).addDevice(params.getString(DEVID), getIResultCallback(promise))
        }
    }

    /* 删除设备  */
    override fun removeDevice(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(ROOMID, DEVID), params)) {
            getRoomInstance(params.getDouble(ROOMID)).removeDevice(params.getString(DEVID), getIResultCallback(promise))
        }
    }

    fun getRoomInstance(roomId: Double): IThingRoom {
        return ThingHomeSdk.newRoomInstance(roomId.toLong())
    }

}
