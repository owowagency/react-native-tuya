import { DeviceBean } from './device';
import { NativeModules, Platform } from 'react-native';
import { DeviceDetailResponse } from './home';

const tuya = NativeModules.TuyaActivatorModule;
const tuyaBLEActivator = NativeModules.TuyaBLEActivatorModule;
const tuyaBLEScanner = NativeModules.TuyaBLEScannerModule;

export function openNetworkSettings() {
  return tuya.openNetworkSettings({});
}

export enum ActivatorType {
  AP = 'THING_AP',
  EZ = 'THING_EZ',
  AP_4G_GATEWAY = 'THING_4G_GATEWAY',
  QR = 'THING_QR',
}

export type InitActivatorParams = {
  homeId: number;
  ssid: string;
  password: string;
  time: number;
  type: ActivatorType;
};

export interface InitBluetoothActivatorParams {
  deviceId?: string;
  homeId: number;
  ssid: string;
  password: string;
}

export function initActivator(
  params: InitActivatorParams
): Promise<DeviceDetailResponse> {
  return tuya.initActivator(params);
}

export function stopConfig() {
  return tuya.stopConfig();
}

export function onDestory() {
  return tuya.onDestory();
}

export interface GwSubDevActivatorParams {
  devId: string;
  time: number;
}

export function newGwSubDevActivator(
  params: GwSubDevActivatorParams
): Promise<DeviceBean> {
  return tuya.newGwSubDevActivator(params);
}

export function stopNewGwSubDevActivatorConfig(devId: string) {
  return tuya.stopNewGwSubDevActivatorConfig({ devId });
}

export function startBluetoothScan() {
  if (Platform.OS === 'ios') {
    return tuyaBLEScanner.startBluetoothScan();
  }
  return tuya.startBluetoothScan();
}

export function stopBluetoothScan() {
  return tuya.stopBluetoothScan();
}

export function initBluetoothDualModeActivator(
  params: InitBluetoothActivatorParams
): Promise<DeviceBean> {
  if (Platform.OS === 'ios') {
    return tuyaBLEActivator.initActivator(params);
  }
  return tuya.initBluetoothDualModeActivator(params);
}

export function getCurrentWifi(
  success: (ssid: string) => void,
  error: () => void
) {
  // We need the Allow While Using App location permission to use this.
  return tuya.getCurrentWifi({}, success, error);
}
