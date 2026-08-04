package com.tuya.smart.rnsdk.home

import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.api.IThingHomeChangeListener
import com.thingclips.smart.home.sdk.bean.HomeBean
import com.thingclips.smart.home.sdk.callback.IThingGetHomeListCallback
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback
import com.thingclips.smart.sdk.bean.DeviceBean
import com.thingclips.smart.sdk.bean.GroupBean
import com.tuya.smart.rnsdk.NativeTuyaHomeManagerModuleSpec
import com.tuya.smart.rnsdk.utils.*

@ReactModule(name = TuyaHomeManagerModule.NAME)
class TuyaHomeManagerModule(reactContext: ReactApplicationContext) : NativeTuyaHomeManagerModuleSpec(reactContext) {

    companion object {
        const val NAME = "TuyaHomeManagerModule"
    }

    override fun getName(): String {
        return NAME
    }

    /* 获取家庭列表 */
    override fun queryHomeList(promise: Promise) {
        ThingHomeSdk.getHomeManagerInstance().queryHomeList(object : IThingGetHomeListCallback {
            override fun onSuccess(var1: List<HomeBean>) {
                promise.resolve(TuyaReactUtils.parseToWritableArray(JsonUtils.toJsonArray(var1!!)))
            }

            override fun onError(var1: String, var2: String) {
                promise.reject(var1, var2)
            }
        })
    }

    /* 创建家庭 */
    override fun createHome(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(Constant.NAME, Constant.LON, Constant.LAT, Constant.GEONAME, Constant.ROMMS), params)) {
            var list = ArrayList<String>()
            var length = (params.getArray(Constant.ROMMS) as ReadableArray).size()
            for (index in 0 until length) {
                list.add((params.getArray(Constant.ROMMS) as ReadableArray).getString(index) as String)
            }
            ThingHomeSdk.getHomeManagerInstance().createHome(
                    params.getString(Constant.NAME),
                    params.getDouble(Constant.LON),
                    params.getDouble(Constant.LAT),
                    params.getString(Constant.GEONAME),
                    list,
                    getITuyaHomeResultCallback(promise)
            )

        }
    }

    override fun joinFamily(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(Constant.HOMEID, Constant.ACTION), params)) {
            ThingHomeSdk.getMemberInstance().processInvitation(
                    params.getDouble(Constant.HOMEID).toLong(),
                    params.getBoolean(Constant.ACTION),
                    Constant.getIResultCallback(promise)
            )

        }
    }

    /* 注册家庭信息的变更
     * 有：家庭的增加、删除、信息变更、分享列表的变更和服务器连接成功的监听 */
    override fun registerTuyaHomeChangeListener(params: ReadableMap) {
        ThingHomeSdk.getHomeManagerInstance().registerThingHomeChangeListener(object : IThingHomeChangeListener {
            override fun onHomeInvite(p0: Long, p1: String?) {
                val map = Arguments.createMap()
                map.putDouble("homeId", p0.toDouble())
                map.putString("homeName", p1)
                BridgeUtils.homeChange(reactApplicationContext, map, params.getDouble("homeId"))
            }

            override fun onHomeAdded(var1: Long) {
                val map = Arguments.createMap()
                map.putDouble("homeId", var1.toDouble())
                map.putString("type", "onHomeAdded");
                BridgeUtils.homeChange(reactApplicationContext, map, params.getDouble("homeId"))
            }

            override fun onHomeRemoved(var1: Long) {
                val map = Arguments.createMap()
                map.putDouble("homeId", var1.toDouble())
                map.putString("type", "onHomeRemoved");
                BridgeUtils.homeChange(reactApplicationContext, map, params.getDouble("homeId"))
            }

            override fun onHomeInfoChanged(var1: Long) {
                val map = Arguments.createMap()
                map.putDouble("homeId", var1.toDouble())
                map.putString("type", "onHomeInfoChanged");
                BridgeUtils.homeChange(reactApplicationContext, map, params.getDouble("homeId"))
            }

            override fun onSharedDeviceList(var1: List<DeviceBean>) {
                val map = Arguments.createMap()
                map.putArray("deviceBeans", TuyaReactUtils.parseToWritableArray(JsonUtils.toJsonArray(var1)))
                map.putString("type", "onSharedDeviceList");
                BridgeUtils.homeChange(reactApplicationContext, map, params.getDouble("homeId"))
            }

            override fun onSharedGroupList(var1: List<GroupBean>) {
                val map = Arguments.createMap()
                map.putArray("groupBeans", TuyaReactUtils.parseToWritableArray(JsonUtils.toJsonArray(var1)))
                map.putString("type", "onSharedGroupList");
                BridgeUtils.homeChange(reactApplicationContext, map, params.getDouble("homeId"))
            }

            override fun onServerConnectSuccess() {
                val map = Arguments.createMap()
                map.putString("type", "onServerConnectSuccess");
                BridgeUtils.homeChange(reactApplicationContext, map, params.getDouble("homeId"))
            }
        })
    }

    /**
     * unregisterTuyaHomeChangeListener/onDestory only ever existed in this
     * package's iOS implementation (they used TuyaRNHomeManagerListener/
     * TuyaRNHomeListener singletons that have no Android equivalent) - the
     * old Android bridge module never had an unregister counterpart to
     * registerTuyaHomeChangeListener. Kept as no-ops here only because the
     * TurboModule spec is shared across both platforms and must be
     * implemented on both; there is nothing to actually unregister/destroy
     * on the Android side.
     */
    override fun unregisterTuyaHomeChangeListener(params: ReadableMap) {}

    override fun onDestory(params: ReadableMap) {}

    fun getITuyaHomeResultCallback(promise: Promise): IThingHomeResultCallback? {
        return object : IThingHomeResultCallback {
            override fun onSuccess(p0: HomeBean?) {
                promise.resolve(TuyaReactUtils.parseToWritableMap(p0))
            }

            override fun onError(code: String?, error: String?) {
                promise.reject(code, error)
            }
        }
    }
}
