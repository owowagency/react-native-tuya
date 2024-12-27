import { NativeModules, Platform } from 'react-native';
import { addEvent, bridge, HARDWAREUPGRADELISTENER } from './bridgeUtils';

const tuya = Platform.OS === 'ios' ? NativeModules.TuyaDeviceModule : NativeModules.TuyaOTAModule;

export type StartOtaParams = {
  devId: string;
};

export function startOta(
  params: StartOtaParams,
  onSuccess: (data: any) => void,
  onFailure: (data: any) => void,
  onProgress: (data: any) => void
) {
  tuya.startOta(params);
  return addEvent(bridge(HARDWAREUPGRADELISTENER, params.devId), data => {
    if (data.type === 'onSuccess') {
      onSuccess(data);
    } else if (data.type === 'onFailure') {
      onFailure(data);
    } else if (data.type === 'onProgress') {
      onProgress(data);
    }
  });
}

export enum TuyaUpgradeStatus {
  noUpdate,
  updateAvailable,
  updating,
  waitForDevice = 5,
}
export type ThingSmartFirmwareUpgradeModel = {
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

export function getOtaInfo(params: { devId: string }): Promise<ThingSmartFirmwareUpgradeModel[]> {
  return tuya.getOtaInfo(params);
}

export function startFirmwareUpgrade(params: { devId: string; firmwares: ThingSmartFirmwareUpgradeModel[] }): Promise<boolean> {
  return tuya.startFirmwareUpgrade(params);
}
