package com.tuya.smart.rnsdk.home

import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.api.IThingHome
import com.thingclips.smart.home.sdk.bean.HomeBean
import com.thingclips.smart.home.sdk.bean.RoomBean
import com.thingclips.smart.home.sdk.callback.IThingGetRoomListCallback
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback
import com.tuya.smart.rnsdk.NativeTuyaHomeModuleSpec
import com.tuya.smart.rnsdk.utils.*
import com.tuya.smart.rnsdk.utils.Constant.GEONAME
import com.tuya.smart.rnsdk.utils.Constant.HOMEID
import com.tuya.smart.rnsdk.utils.Constant.IDLIST
import com.tuya.smart.rnsdk.utils.Constant.LAT
import com.tuya.smart.rnsdk.utils.Constant.LON
import com.tuya.smart.rnsdk.utils.Constant.NAME
import com.tuya.smart.rnsdk.utils.Constant.getIResultCallback

@ReactModule(name = TuyaHomeModule.NAME)
class TuyaHomeModule(reactContext: ReactApplicationContext) : NativeTuyaHomeModuleSpec(reactContext) {

    companion object {
        const val NAME = "TuyaHomeModule"
    }

    override fun getName(): String {
        return NAME
    }

    /* 初始化家庭下的所有数据 */
    override fun getHomeDetail(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID), params)) {
            getHomeInstance(params.getDouble(HOMEID))?.getHomeDetail(getITuyaHomeResultCallback(promise))
        }
    }

    /* 更新家庭信息 */
    override fun updateHome(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID, NAME, LON, LAT, GEONAME), params)) {
            getHomeInstance(params.getDouble(HOMEID))?.updateHome(params.getString(NAME), params.getDouble(LON), params.getDouble(LAT), params.getString(GEONAME), getIResultCallback(promise))
        }
    }

    /* 解散家庭 */
    override fun dismissHome(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID), params)) {
            getHomeInstance(params.getDouble(HOMEID))?.dismissHome(getIResultCallback(promise))
        }
    }

    override fun sortRoom(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID, IDLIST), params)) {
            var list = ArrayList<Long>()
            var length = (params.getArray(IDLIST) as ReadableArray).size()
            for (index in 0..length - 1) {
                list.add((params.getArray(IDLIST) as ReadableArray).getDouble(index).toLong())
            }
            getHomeInstance(params.getDouble(HOMEID))?.sortRoom(list, getIResultCallback(promise))
        }
    }

    /* 查询房间列表 */
    override fun queryRoomList(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID), params)) {
            getHomeInstance(params.getDouble(HOMEID))?.queryRoomList(ITuyaGetRoomListCallback(promise))
        }
    }

    fun getHomeInstance(homeId: Double): IThingHome? {
        return ThingHomeSdk.newHomeInstance(homeId.toLong())
    }

    fun ITuyaGetRoomListCallback(promise: Promise): IThingGetRoomListCallback? {
        return object : IThingGetRoomListCallback {
            override fun onSuccess(var1: List<RoomBean>) {
                promise.resolve(TuyaReactUtils.parseToWritableArray(JsonUtils.toJsonArray(var1)))
            }

            override fun onError(code: String?, error: String?) {
                promise.reject(code, error)
            }
        }
    }

    fun getITuyaHomeResultCallback(promise: Promise): IThingHomeResultCallback? {
        return object : IThingHomeResultCallback {
            override fun onSuccess(p0: HomeBean?) {
                promise.resolve(TYCommonUtls.parseToWritableMap(p0))
            }

            override fun onError(code: String?, error: String?) {
                promise.reject(code, error)
            }
        }
    }
}
