package com.tuya.smart.rnsdk.home

import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.tuya.smart.rnsdk.NativeTuyaHomeDataManagerModuleSpec
import com.tuya.smart.rnsdk.utils.Constant.DEVID
import com.tuya.smart.rnsdk.utils.Constant.GROUPID
import com.tuya.smart.rnsdk.utils.Constant.HOMEID
import com.tuya.smart.rnsdk.utils.Constant.MESHID
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

    /*  家庭下面的房间列表 */
    override fun getHomeRoomList(params: ReadableMap, promise: Promise) {
        promise.resolve(TuyaReactUtils.parseToWritableArray(
                JsonUtils.toJsonArray(ThingHomeSdk.getDataInstance().getHomeRoomList(params.getDouble(HOMEID).toLong()))))
    }

    /* 获取家庭下面的设备列表 */
    override fun getHomeDeviceList(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID), params)) {
            promise.resolve(TuyaReactUtils.parseToWritableArray(
                    JsonUtils.toJsonArray(ThingHomeSdk.getDataInstance().getHomeDeviceList(params.getDouble(HOMEID).toLong()))))
        }
    }

    /* 获取家庭下面的群组列表 */
    override fun getHomeGroupList(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID), params)) {
            promise.resolve(TuyaReactUtils.parseToWritableArray(
                    JsonUtils.toJsonArray(ThingHomeSdk.getDataInstance().getHomeGroupList(params.getDouble(HOMEID).toLong()))))
        }
    }

    /* 获取群组 */
    override fun getGroupBean(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(GROUPID), params)) {
            promise.resolve(TuyaReactUtils.parseToWritableMap(ThingHomeSdk.getDataInstance().getGroupBean(params.getDouble(GROUPID).toLong())))
        }
    }

    /* 获取设备 */
    override fun getDeviceBean(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(DEVID), params)) {
            promise.resolve(TuyaReactUtils.parseToWritableMap(ThingHomeSdk.getDataInstance().getDeviceBean(params.getString(DEVID))))
        }
    }

    /* 获取设备 */
    override fun getGroupRoomBean(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(GROUPID), params)) {
            promise.resolve(TuyaReactUtils.parseToWritableMap(ThingHomeSdk.getDataInstance().getGroupRoomBean(params.getDouble(GROUPID).toLong())))
        }
    }

    /* 获取房间 */
    override fun getRoomBean(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(ROOMID), params)) {
            promise.resolve(TuyaReactUtils.parseToWritableMap(ThingHomeSdk.getDataInstance().getRoomBean(params.getDouble(ROOMID).toLong())))
        }
    }

    /* 根据设备获取房间信息 */
    override fun getDeviceRoomBean(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(DEVID), params)) {
            promise.resolve(TuyaReactUtils.parseToWritableMap(ThingHomeSdk.getDataInstance().getDeviceRoomBean(params.getString(DEVID))))
        }
    }

    /* 获取群组下面的设备列表 */
    override fun getGroupDeviceList(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(GROUPID), params)) {
            promise.resolve(TuyaReactUtils.parseToWritableArray(
                    JsonUtils.toJsonArray(ThingHomeSdk.getDataInstance().getGroupDeviceList(params.getDouble(GROUPID).toLong()))))
        }
    }

    /* 获取mesh下面的群组列表 */
    override fun getMeshGroupList(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(MESHID), params)) {
            promise.resolve(TuyaReactUtils.parseToWritableArray(
                    JsonUtils.toJsonArray(ThingHomeSdk.getDataInstance().getMeshGroupList(params.getString(MESHID)))))
        }
    }

    override fun getMeshDeviceList(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(MESHID), params)) {
            promise.resolve(TuyaReactUtils.parseToWritableArray(
                    JsonUtils.toJsonArray(ThingHomeSdk.getDataInstance().getMeshDeviceList(params.getString(MESHID)))))
        }
    }

    /* 根据房间ID获取房间下面的设备列表 */
    override fun getRoomDeviceList(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(ROOMID), params)) {
            promise.resolve(TuyaReactUtils.parseToWritableArray(
                    JsonUtils.toJsonArray(ThingHomeSdk.getDataInstance().getRoomDeviceList(params.getDouble(ROOMID).toLong()))))
        }
    }

    /* 根据房间ID获取房间下面的群组列表 */
    override fun getRoomGroupList(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(ROOMID), params)) {
            promise.resolve(TuyaReactUtils.parseToWritableArray(
                    JsonUtils.toJsonArray(ThingHomeSdk.getDataInstance().getRoomGroupList(params.getDouble(ROOMID).toLong()))))
        }
    }

    /* 根据房间ID获取房间下面的群组列表 */
    override fun getHomeBean(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID), params)) {
            promise.resolve(TuyaReactUtils.parseToWritableMap(ThingHomeSdk.getDataInstance().getHomeBean(params.getDouble(HOMEID).toLong())))
        }
    }

}
