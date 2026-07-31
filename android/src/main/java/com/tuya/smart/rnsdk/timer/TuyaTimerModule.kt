package com.tuya.smart.rnsdk.timer

import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.sdk.api.IGetAllTimerWithDevIdCallback
import com.thingclips.smart.sdk.api.IGetDeviceTimerStatusCallback
import com.thingclips.smart.sdk.api.IResultStatusCallback
import com.thingclips.smart.sdk.bean.TimerTask
import com.thingclips.smart.sdk.bean.TimerTaskStatus
import com.tuya.smart.rnsdk.NativeTuyaTimerModuleSpec
import com.tuya.smart.rnsdk.utils.Constant.DEVID
import com.tuya.smart.rnsdk.utils.Constant.DPS
import com.tuya.smart.rnsdk.utils.Constant.ISOPEN
import com.tuya.smart.rnsdk.utils.Constant.LOOPS
import com.tuya.smart.rnsdk.utils.Constant.STATUS
import com.tuya.smart.rnsdk.utils.Constant.TASKNAME
import com.tuya.smart.rnsdk.utils.Constant.TIME
import com.tuya.smart.rnsdk.utils.Constant.TIMERID
import com.tuya.smart.rnsdk.utils.JsonUtils
import com.tuya.smart.rnsdk.utils.ReactParamsCheck
import com.tuya.smart.rnsdk.utils.TuyaReactUtils
import java.util.ArrayList

@ReactModule(name = TuyaTimerModule.NAME)
class TuyaTimerModule(reactContext: ReactApplicationContext) : NativeTuyaTimerModuleSpec(reactContext) {

    companion object {
        const val NAME = "TuyaTimerModule"
    }

    override fun getName(): String {
        return NAME
    }

    /**
     *   增加定时器 单dp点 默认置为true  支持子设备
     *  @param taskName     定时任务名称
     *  @param loops        循环次数 "0000000", 每一位 0:关闭,1:开启, 从左至右依次表示: 周日 周一 周二 周三 周四 周五 周六
     *  @param devId        设备Id或群组Id
     *  @param dps          dp点键值对，key是dpId，value是dpValue,仅支持单dp点
     *  @param time         定时任务下的定时钟
     *  @param callback     回调
     */
    override fun addTimerWithTask(params: ReadableMap,promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(TASKNAME, LOOPS,DEVID, DPS, TIME), params)) {
            ThingHomeSdk.getTimerManagerInstance().addTimerWithTask(
                    params.getString(TASKNAME),
                    params.getString(DEVID),
                    params.getString(LOOPS),
                    TuyaReactUtils.parseToMap(params.getMap(DPS) as ReadableMap),
                    params.getString(TIME),
                    getIResultStatusCallback(promise)
            )
        }
    }

    /*获取某设备下的所有定时任务状态*/
    override fun getTimerTaskStatusWithDeviceId(params: ReadableMap,promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(DEVID), params)) {
            ThingHomeSdk.getTimerManagerInstance().getTimerTaskStatusWithDeviceId(
                    params.getString(DEVID),
                    getIGetDeviceTimerStatusCallback(promise)
            )
        }
    }

    /*控制某个定时器的开关状态*/
    override fun updateTimerStatusWithTask(params: ReadableMap,promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(TASKNAME,DEVID,TIMERID, ISOPEN), params)) {
            ThingHomeSdk.getTimerManagerInstance().updateTimerStatusWithTask(
                    params.getString(TASKNAME),
                    params.getString(DEVID),
                    params.getString(TIMERID),
                    params.getBoolean(ISOPEN),
                    getIResultStatusCallback(promise)
            )
        }
    }


    /*删除定时器*/
    override fun removeTimerWithTask(params: ReadableMap,promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(TASKNAME,DEVID, TIMERID), params)) {
            ThingHomeSdk.getTimerManagerInstance().removeTimerWithTask(
                    params.getString(TASKNAME),
                    params.getString(DEVID),
                    params.getString(TIMERID),
                    getIResultStatusCallback(promise)
            )
        }
    }

    /**
     * * 更新定时器的状态
     * @param taskName 定时任务名称
     * @param loops    循环次数 如每周每天传”1111111”
     * @param devId    设备Id或群组Id
     * @param timerId  定时钟Id
     * @param time     定时时间
     * @param isOpen	  是否开启
     * @param callback 回调
     */
    override fun updateTimerWithTask(params: ReadableMap,promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(TASKNAME, LOOPS, DEVID, TIMERID, TIME, ISOPEN), params)) {
            ThingHomeSdk.getTimerManagerInstance().updateTimerWithTask(
                    params.getString(TASKNAME),
                    params.getString(LOOPS),
                    params.getString(DEVID),
                    params.getString(TIMERID),
                    // Unfortunately we cannot update dps values, we can only give a single dp id but we also want to control the dp value
                    null,
                    params.getString(TIME),
                    params.getBoolean(ISOPEN),
                    getIResultStatusCallback(promise))
        }
    }

    /*获取设备所有定时任务下所有定时器*/
    override fun getAllTimerWithDeviceId(params: ReadableMap,promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(DEVID), params)) {
            ThingHomeSdk.getTimerManagerInstance().getAllTimerWithDeviceId(
                    params.getString(DEVID),
                    getIGetAllTimerWithDevIdCallback(promise))
        }
    }

    fun getIGetAllTimerWithDevIdCallback(promise: Promise): IGetAllTimerWithDevIdCallback {
        return object : IGetAllTimerWithDevIdCallback {
            override fun onSuccess(p0: ArrayList<TimerTask>?) {
                promise.resolve(TuyaReactUtils.parseToWritableArray(JsonUtils.toJsonArray(p0!!)))
            }

            override fun onError(code: String?, error: String?) {
                promise.reject(code, error)
            }
        }
    }
    fun getIGetDeviceTimerStatusCallback(promise: Promise): IGetDeviceTimerStatusCallback {
        return object : IGetDeviceTimerStatusCallback {
            override fun onSuccess(p0: ArrayList<TimerTaskStatus>?) {
                promise.resolve(TuyaReactUtils.parseToWritableArray(JsonUtils.toJsonArray(p0!!)))
            }

            override fun onError(code: String?, error: String?) {
                promise.reject(code, error)
            }
        }
    }

    fun getIResultStatusCallback(promise: Promise): IResultStatusCallback {
        return object : IResultStatusCallback {
            override fun onSuccess() {
                promise.resolve(com.tuya.smart.rnsdk.utils.Constant.SUCCESS)
            }

            override fun onError(code: String?, error: String?) {
                promise.reject(code, error)
            }
        }
    }
}
