package com.tuya.smart.rnsdk.home

import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.bean.MemberBean
import com.thingclips.smart.home.sdk.callback.IThingGetMemberListCallback
import com.thingclips.smart.home.sdk.callback.IThingMemberResultCallback
import com.tuya.smart.rnsdk.NativeTuyaHomeMemberModuleSpec
import com.tuya.smart.rnsdk.utils.Constant
import com.tuya.smart.rnsdk.utils.JsonUtils
import com.tuya.smart.rnsdk.utils.ReactParamsCheck
import com.tuya.smart.rnsdk.utils.TuyaReactUtils

@ReactModule(name = TuyaHomeMemberModule.NAME)
class TuyaHomeMemberModule(reactContext: ReactApplicationContext) : NativeTuyaHomeMemberModuleSpec(reactContext) {

    companion object {
        const val NAME = "TuyaHomeMemberModule"
    }

    override fun getName(): String {
        return NAME
    }

    /* 添加成员 */
    override fun addMember(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(Constant.HOMEID, Constant.COUNTRYCODE, Constant.USERACCOUNT, Constant.NAME, Constant.ADMIN), params)) {
            ThingHomeSdk.getMemberInstance().addMember(
                    params.getDouble(Constant.HOMEID).toLong(),
                    params.getString(Constant.COUNTRYCODE),
                    params.getString(Constant.USERACCOUNT),
                    params.getString(Constant.NAME),
                    params.getBoolean(Constant.ADMIN),
                    object : IThingMemberResultCallback {
                        override fun onSuccess(var1: MemberBean){
                            promise.resolve(TuyaReactUtils.parseToWritableMap(var1))
                        }

                        override fun onError(var1: String, var2: String){
                            promise.reject(var1,var2)
                        }
                    }
            )
        }
    }

    /* 移除Home下面的成员 */
    override fun removeMember(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(Constant.MEMBERID),params)) {
            ThingHomeSdk.getMemberInstance().removeMember(params.getDouble(Constant.MEMBERID).toLong(), Constant.getIResultCallback(promise))
        }
    }

    /* 更新成员备注名和权限 */
    override fun updateMember(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(Constant.MEMBERID, Constant.NAME, Constant.ADMIN),params)) {
            ThingHomeSdk.getMemberInstance().updateMember(params.getDouble(Constant.MEMBERID).toLong(),params.getString(Constant.NAME),params.getBoolean(Constant.ADMIN), Constant.getIResultCallback(promise))
        }
    }

    /* 查询Home下面的成员列表 */
    override fun queryMemberList(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(Constant.HOMEID),params)) {
            ThingHomeSdk.getMemberInstance().queryMemberList(params.getDouble(Constant.HOMEID).toLong(),object : IThingGetMemberListCallback {
                override fun onSuccess(var1: List<MemberBean>){
                    promise.resolve(TuyaReactUtils.parseToWritableArray(JsonUtils.toJsonArray(var1)))
                }

                override fun onError(var1: String, var2: String){
                    promise.reject(var1,var2)
                }
            })
        }
    }
}
