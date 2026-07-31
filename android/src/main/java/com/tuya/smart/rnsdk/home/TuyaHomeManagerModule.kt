package com.tuya.smart.rnsdk.home

import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.bean.HomeBean
import com.thingclips.smart.home.sdk.callback.IThingGetHomeListCallback
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback
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
