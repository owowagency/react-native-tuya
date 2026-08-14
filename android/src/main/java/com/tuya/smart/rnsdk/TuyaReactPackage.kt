package com.tuya.smart.rnsdk


import com.facebook.react.TurboReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.module.model.ReactModuleInfo
import com.facebook.react.module.model.ReactModuleInfoProvider
import com.tuya.smart.rnsdk.activator.TuyaActivatorModule
import com.tuya.smart.rnsdk.core.TuyaCoreModule
import com.tuya.smart.rnsdk.device.TuyaDeviceModule
import com.tuya.smart.rnsdk.home.TuyaHomeDataManagerModule
import com.tuya.smart.rnsdk.home.TuyaHomeManagerModule
import com.tuya.smart.rnsdk.home.TuyaHomeMemberModule
import com.tuya.smart.rnsdk.home.TuyaHomeModule
import com.tuya.smart.rnsdk.home.TuyaRoomModule
import com.tuya.smart.rnsdk.timer.TuyaTimerModule
import com.tuya.smart.rnsdk.user.TuyaUserModule

import java.util.*

// All 12 JS-reachable native modules have been converted to TurboModules.
// See specs/*.ts for the Codegen source of truth.
class TuyaReactPackage : TurboReactPackage() {

    override fun getModule(name: String, reactContext: ReactApplicationContext): NativeModule? {
        return when (name) {
            TuyaCoreModule.NAME -> TuyaCoreModule(reactContext)
            TuyaRoomModule.NAME -> TuyaRoomModule(reactContext)
            TuyaHomeManagerModule.NAME -> TuyaHomeManagerModule(reactContext)
            TuyaHomeMemberModule.NAME -> TuyaHomeMemberModule(reactContext)
            TuyaHomeDataManagerModule.NAME -> TuyaHomeDataManagerModule(reactContext)
            TuyaTimerModule.NAME -> TuyaTimerModule(reactContext)
            TuyaHomeModule.NAME -> TuyaHomeModule(reactContext)
            TuyaActivatorModule.NAME -> TuyaActivatorModule(reactContext)
            TuyaDeviceModule.NAME -> TuyaDeviceModule(reactContext)
            TuyaUserModule.NAME -> TuyaUserModule(reactContext)
            else -> null
        }
    }

    override fun getReactModuleInfoProvider(): ReactModuleInfoProvider {
        return ReactModuleInfoProvider {
            val moduleInfos: MutableMap<String, ReactModuleInfo> = HashMap()
            for ((name, clazz) in TURBO_MODULE_CLASSES) {
                moduleInfos[name] = ReactModuleInfo(
                    name,
                    clazz.name,
                    false, // canOverrideExistingModule
                    false, // needsEagerInit
                    false, // hasConstants
                    false, // isCxxModule
                    true // isTurboModule
                )
            }
            moduleInfos
        }
    }

    override fun createNativeModules(reactContext: ReactApplicationContext): MutableList<NativeModule> {
        return ArrayList()
    }

    override fun createViewManagers(reactContext: ReactApplicationContext) = emptyList<Nothing>()

    companion object {
        private val TURBO_MODULE_CLASSES = listOf(
            TuyaCoreModule.NAME to TuyaCoreModule::class.java,
            TuyaRoomModule.NAME to TuyaRoomModule::class.java,
            TuyaHomeManagerModule.NAME to TuyaHomeManagerModule::class.java,
            TuyaHomeMemberModule.NAME to TuyaHomeMemberModule::class.java,
            TuyaHomeDataManagerModule.NAME to TuyaHomeDataManagerModule::class.java,
            TuyaTimerModule.NAME to TuyaTimerModule::class.java,
            TuyaHomeModule.NAME to TuyaHomeModule::class.java,
            TuyaActivatorModule.NAME to TuyaActivatorModule::class.java,
            TuyaDeviceModule.NAME to TuyaDeviceModule::class.java,
            TuyaUserModule.NAME to TuyaUserModule::class.java
        )
    }
}
