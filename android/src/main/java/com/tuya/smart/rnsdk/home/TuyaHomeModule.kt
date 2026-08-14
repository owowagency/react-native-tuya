package com.tuya.smart.rnsdk.home

import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.api.IThingHome
import com.thingclips.smart.home.sdk.api.IThingHomeStatusListener
import com.thingclips.smart.home.sdk.bean.HomeBean
import com.thingclips.smart.home.sdk.bean.RoomBean
import com.thingclips.smart.home.sdk.callback.IThingGetRoomListCallback
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback
import com.thingclips.smart.home.sdk.callback.IThingResultCallback
import com.thingclips.smart.home.sdk.callback.IThingRoomResultCallback
import com.thingclips.smart.sdk.bean.GroupDeviceBean
import com.tuya.smart.rnsdk.NativeTuyaHomeModuleSpec
import com.tuya.smart.rnsdk.utils.*
import com.tuya.smart.rnsdk.utils.Constant.DEVIDLIST
import com.tuya.smart.rnsdk.utils.Constant.GEONAME
import com.tuya.smart.rnsdk.utils.Constant.HOMEID
import com.tuya.smart.rnsdk.utils.Constant.IDLIST
import com.tuya.smart.rnsdk.utils.Constant.LAT
import com.tuya.smart.rnsdk.utils.Constant.LON
import com.tuya.smart.rnsdk.utils.Constant.NAME
import com.tuya.smart.rnsdk.utils.Constant.PRODUCTID
import com.tuya.smart.rnsdk.utils.Constant.ROOMID
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

    /* 获取本地缓存中的数据信息 */
    override fun getHomeLocalCache(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID), params)) {
            getHomeInstance(params.getDouble(HOMEID))?.getHomeLocalCache(getITuyaHomeResultCallback(promise))
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

    /* 添加房间 */
    override fun addRoom(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID, NAME), params)) {
            getHomeInstance(params.getDouble(HOMEID))?.addRoom(params.getString(NAME), getITuyaRoomResultCallback(promise))
        }
    }

    /* 移除房间 */
    override fun removeRoom(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID, ROOMID), params)) {
            getHomeInstance(params.getDouble(HOMEID))?.removeRoom(params.getDouble(ROOMID).toLong(), getIResultCallback(promise))
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

    /* 排序房间 */
    override fun sortHome(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID, IDLIST), params)) {
            var list = ArrayList<Long>()
            var length = (params.getArray(IDLIST) as ReadableArray).size()
            for (index in 0..length - 1) {
                list.add((params.getArray(IDLIST) as ReadableArray).getDouble(index).toLong())
            }
            getHomeInstance(params.getDouble(HOMEID))?.sortHome(list, getIResultCallback(promise))
        }
    }

    /* 查询房间列表 */
    override fun queryRoomList(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID), params)) {
            getHomeInstance(params.getDouble(HOMEID))?.queryRoomList(ITuyaGetRoomListCallback(promise))
        }
    }

    /* 创建群组 */
    override fun createGroup(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID, PRODUCTID, NAME, DEVIDLIST), params)) {
            var list = ArrayList<String>()
            var length = (params.getArray(DEVIDLIST) as ReadableArray).size()
            for (index in 0..length) {
                list.add((params.getArray(DEVIDLIST) as ReadableArray).getString(index) as String)
            }
            getHomeInstance(params.getDouble(HOMEID))?.createGroup(
                    params.getString(PRODUCTID),
                    params.getString(NAME),
                    list,
                    object : IThingResultCallback<Long> {
                        override fun onSuccess(var1: Long) {
                            promise.resolve(var1)
                        }

                        override fun onError(var1: String, var2: String) {
                            promise.reject(var1, var2)
                        }
                    }
            )
        }
    }

    /* 监听家庭下面信息(设备的新增或者删除)变更的监听 */
    override fun registerHomeStatusListener(params: ReadableMap) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID), params)) {
            getHomeInstance(params.getDouble(HOMEID))?.registerHomeStatusListener(object : IThingHomeStatusListener {
                override fun onDeviceAdded(var1: String){
                    val map = Arguments.createMap()
                    map.putString("devId", var1)
                    map.putDouble("homeId", params.getDouble(HOMEID))
                    map.putString("type","onDeviceAdded");
                    BridgeUtils.homeStatus(reactApplicationContext,map,params.getString(HOMEID) as String)
                }

                override fun onDeviceRemoved(var1: String){
                    val map = Arguments.createMap()
                    map.putString("devId", var1)
                    map.putDouble("homeId", params.getDouble(HOMEID))
                    map.putString("type","onDeviceRemoved");
                    BridgeUtils.homeStatus(reactApplicationContext,map,params.getString(HOMEID) as String)
                }

                override fun onGroupAdded(var1: Long){
                    val map = Arguments.createMap()
                    map.putDouble("groupId", var1.toDouble())
                    map.putDouble("homeId", params.getDouble(HOMEID))
                    map.putString("type","onGroupAdded");
                    BridgeUtils.homeStatus(reactApplicationContext,map,params.getString(HOMEID) as String)
                }

                override fun onGroupRemoved(var1: Long){
                    val map = Arguments.createMap()
                    map.putDouble("groupId", var1.toDouble())
                    map.putDouble("homeId", params.getDouble(HOMEID))
                    map.putString("type","onGroupRemoved");
                    BridgeUtils.homeStatus(reactApplicationContext,map,params.getString(HOMEID) as String)
                }

                override fun onMeshAdded(var1: String){
                    val map = Arguments.createMap()
                    map.putDouble("meshId", var1.toDouble())
                    map.putDouble("homeId", params.getDouble(HOMEID))
                    map.putString("type","onMeshAdded");
                    BridgeUtils.homeStatus(reactApplicationContext,map,params.getString(HOMEID) as String)
                }
            })
        }
    }

    /* 注销家庭下面信息变更的监听 */
    override fun unRegisterHomeStatusListener(params: ReadableMap) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID), params)) {
            getHomeInstance(params.getDouble(HOMEID))
        }
    }

    /* 查询用户下面相同产品且支持群组的设备列表 */
    override fun queryDeviceListToAddGroup(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID, PRODUCTID), params)) {
            getHomeInstance(params.getDouble(HOMEID))?.queryDeviceListToAddGroup(params.getDouble(HOMEID).toLong(),
                    params.getString(PRODUCTID),
                    object : IThingResultCallback<List<GroupDeviceBean>> {
                        override fun onSuccess(var1: List<GroupDeviceBean>) {
                            promise.resolve(TuyaReactUtils.parseToWritableArray(JsonUtils.toJsonArray(var1)))
                        }

                        override fun onError(var1: String, var2: String) {
                            promise.reject(var1, var2)
                        }
                    }
            )
        }
    }

    /* 释放家庭相关资源 */
    override fun onDestroy(params: ReadableMap) {
        if (ReactParamsCheck.checkParams(arrayOf(HOMEID), params)) {
            getHomeInstance(params.getDouble(HOMEID))?.onDestroy()
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

    fun getITuyaRoomResultCallback(promise: Promise): IThingRoomResultCallback? {
        return object : IThingRoomResultCallback {
            override fun onSuccess(p0: RoomBean) {
                promise.resolve(TuyaReactUtils.parseToWritableMap(p0))
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
