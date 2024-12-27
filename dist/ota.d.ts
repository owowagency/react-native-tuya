export declare type StartOtaParams = {
    devId: string;
};
export declare function startOta(params: StartOtaParams, onSuccess: (data: any) => void, onFailure: (data: any) => void, onProgress: (data: any) => void): import("react-native").EmitterSubscription;
export declare enum TuyaUpgradeStatus {
    noUpdate = 0,
    updateAvailable = 1,
    updating = 2,
    waitForDevice = 5
}
export declare type ThingSmartFirmwareUpgradeModel = {
    timeout: number;
    controlType: boolean;
    currentVersion: string;
    devType: number;
    upgradedType: number;
    canUpgrade: boolean;
    autoSwitch: boolean;
    typeDesc: string;
    type: number;
    upgradeStatus: TuyaUpgradeStatus;
    diffOta: false;
    url?: string;
    lastUpgradeTime: number;
    firmwareDeployTime: number;
    upgradeMode?: number;
    desc?: string;
    md5?: string;
    upgradingDesc?: string;
    fileSize: string;
    version: string;
};
export declare function getOtaInfo(params: {
    devId: string;
}): Promise<ThingSmartFirmwareUpgradeModel[]>;
export declare function startFirmwareUpgrade(params: {
    devId: string;
    firmwares: ThingSmartFirmwareUpgradeModel[];
}): Promise<boolean>;
