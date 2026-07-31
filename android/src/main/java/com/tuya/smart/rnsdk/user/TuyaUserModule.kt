package com.tuya.smart.rnsdk.user

import com.facebook.react.bridge.*
import com.facebook.react.module.annotations.ReactModule
import com.thingclips.smart.android.user.api.ILoginCallback
import com.thingclips.smart.android.user.api.ILogoutCallback
import com.thingclips.smart.android.user.api.IRegisterCallback
import com.thingclips.smart.android.user.bean.User
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.tuya.smart.rnsdk.NativeTuyaUserModuleSpec
import com.tuya.smart.rnsdk.utils.Constant
import com.tuya.smart.rnsdk.utils.Constant.COUNTRYCODE
import com.tuya.smart.rnsdk.utils.Constant.EMAIL
import com.tuya.smart.rnsdk.utils.Constant.NEWPASSWORD
import com.tuya.smart.rnsdk.utils.Constant.PASSWORD
import com.tuya.smart.rnsdk.utils.Constant.VALIDATECODE
import com.tuya.smart.rnsdk.utils.Constant.getIResultCallback
import com.tuya.smart.rnsdk.utils.ReactParamsCheck
import com.tuya.smart.rnsdk.utils.TuyaReactUtils

@ReactModule(name = TuyaUserModule.NAME)
class TuyaUserModule(reactContext: ReactApplicationContext) : NativeTuyaUserModuleSpec(reactContext) {

    companion object {
        const val NAME = "TuyaUserModule"
    }

    override fun getName(): String {
        return NAME
    }

    /*注册获取邮箱验证码。*/
    override fun getRegisterEmailValidateCode(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(COUNTRYCODE, EMAIL), params)) {
            ThingHomeSdk.getUserInstance().getRegisterEmailValidateCode(
                    params.getString(COUNTRYCODE),
                    params.getString(EMAIL),
                    getIResultCallback(promise)
            )
        }
    }

    /* 邮箱密码注册 */
    override fun registerAccountWithEmail(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(COUNTRYCODE, EMAIL, PASSWORD, VALIDATECODE), params)) {
            ThingHomeSdk.getUserInstance().registerAccountWithEmail(
                    params.getString(COUNTRYCODE),
                    params.getString(EMAIL),
                    params.getString(PASSWORD),
                    params.getString(VALIDATECODE),
                    getRegisterCallback(promise)
            )
        }
    }

    /* 邮箱密码登陆 */
    override fun loginWithEmail(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(COUNTRYCODE, EMAIL, PASSWORD), params)) {
            ThingHomeSdk.getUserInstance().loginWithEmail(
                    params.getString(COUNTRYCODE),
                    params.getString(EMAIL),
                    params.getString(PASSWORD),
                    getLoginCallback(promise)
            )
        }
    }

    /* 邮箱获取验证码 找密码 */
    override fun getEmailValidateCode(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(COUNTRYCODE, EMAIL), params)) {
            ThingHomeSdk.getUserInstance().getEmailValidateCode(
                    params.getString(COUNTRYCODE),
                    params.getString(EMAIL),
                    getValidateCodeCallback(promise)
            )
        }
    }

    /* 邮箱重置密码 */
    override fun resetEmailPassword(params: ReadableMap, promise: Promise) {
        if (ReactParamsCheck.checkParams(arrayOf(COUNTRYCODE, EMAIL, VALIDATECODE, NEWPASSWORD), params)) {
            ThingHomeSdk.getUserInstance().resetEmailPassword(
                    params.getString(COUNTRYCODE),
                    params.getString(EMAIL),
                    params.getString(VALIDATECODE),
                    params.getString(NEWPASSWORD),
                    getResetPasswdCallback(promise)
            )
        }
    }

    /* logout */
    override fun logout(promise: Promise) {
        ThingHomeSdk.getUserInstance().logout(object : ILogoutCallback {
            override fun onSuccess() {
                promise.resolve(Constant.SUCCESS)
            }

            override fun onError(code: String?, error: String?) {
                promise.reject(code, error)
            }

        })
    }

    /* 注销账户 */
    override fun cancelAccount(promise: Promise) {
        ThingHomeSdk.getUserInstance().cancelAccount(getIResultCallback(promise))
    }

    override fun getCurrentUser(promise: Promise) {
        if (ThingHomeSdk.getUserInstance().user != null) {
            promise.resolve(TuyaReactUtils.parseToWritableMap(ThingHomeSdk.getUserInstance().user))
        } else {
            promise.resolve(null)
        }
    }

    fun getLoginCallback(promise: Promise): ILoginCallback? {
        val callback = object : ILoginCallback {
            override fun onSuccess(user: User?) {
                promise.resolve(TuyaReactUtils.parseToWritableMap(user))
            }

            override fun onError(code: String?, error: String?) {
                promise.reject(code, error)
            }

        }
        return callback
    }

    fun getRegisterCallback(promise: Promise): IRegisterCallback? {
        return object : IRegisterCallback {
            override fun onSuccess(user: User?) {
                promise.resolve(TuyaReactUtils.parseToWritableMap(user))
            }

            override fun onError(code: String?, error: String?) {
                promise.reject(code, error)
            }

        }
    }

    fun getResetPasswdCallback(promise: Promise): com.thingclips.smart.android.user.api.IResetPasswordCallback? {
        return object : com.thingclips.smart.android.user.api.IResetPasswordCallback {
            override fun onSuccess() {
                promise.resolve(Constant.SUCCESS)
            }

            override fun onError(code: String?, error: String?) {
                promise.reject(code, error)
            }

        }
    }

    fun getValidateCodeCallback(promise: Promise): com.thingclips.smart.android.user.api.IValidateCallback {
        return object : com.thingclips.smart.android.user.api.IValidateCallback {
            override fun onSuccess() {
                promise.resolve(Constant.SUCCESS)
            }

            override fun onError(code: String?, error: String?) {
                promise.reject(code, error)
            }
        }
    }
}
