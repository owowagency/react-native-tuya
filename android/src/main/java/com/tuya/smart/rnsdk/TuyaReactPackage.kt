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

class TuyaReactPackage : TurboReactPackage() {

    // Modules converted to TurboModules. As more modules are converted, move
    // their construction here and add a matching entry to MODULE_NAMES below.
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

    // Not yet converted to TurboModules. RN 0.73's interop layer covers these
    // under New Architecture; each will move to getModule()/MODULE_NAMES above
    // as it's converted.
    override fun createNativeModules(reactContext: ReactApplicationContext): MutableList<NativeModule> {
        val module: ArrayList<NativeModule> = ArrayList()
        module.add(TuyaDeviceModule(reactContext))
        module.add(TuyaUserModule(reactContext))
        return module
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
            TuyaActivatorModule.NAME to TuyaActivatorModule::class.java
        )
    }
}
