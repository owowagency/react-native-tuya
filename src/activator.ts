import { DeviceBean } from 'device';
import { NativeModules, Platform } from 'react-native';
import { DeviceDetailResponse } from 'home';

const tuya = NativeModules.TuyaActivatorModule;
const tuyaBLEActivator = NativeModules.TuyaBLEActivatorModule;
const tuyaBLEScanner = NativeModules.TuyaBLEScannerModule;

export function openNetworkSettings() {
  return tuya.openNetworkSettings({});
}

export type InitActivatorParams = {
  homeId: number;
  ssid: string;
  password: string;
  time: number;
  type: 'TY_EZ' | 'TY_AP' | 'TY_QR';
};

export interface InitBluetoothActivatorParams {
  deviceId?: string;
  homeId: number;
  ssid: string;
  password: string;
}

export interface StartLeScanParams {
  timeout: number;
  scanType: 'SINGLE' | 'SINGLE_QR' | 'MESH' | 'SIG_MESH' | 'NORMAL' | 'TY_BEACON';
}

export interface InitBluetoothActivatorFromScanBeanParams {
  homeId: number;
  ssid: string;
  password: string;
  uuid: string;
  deviceType: number;
  mac: string;
  address: string;
  token: string;
}

export function initActivator(
  params: InitActivatorParams
): Promise<DeviceDetailResponse> {
  return tuya.initActivator(params);
}

export type GetActivatorTokenParams = {
  homeId: number;
}

export function getActivatorToken(params: GetActivatorTokenParams) {
  return tuya.getActivatorToken(params);
}

export type StartQRActivatorParams = {
  token: string;
  time: number;
}

export function startQRActivator(params: StartQRActivatorParams) {
  return tuya.startQRActivator(params);
}

export function stopQRActivator() {
  return tuya.stopQRActivator();
}

export function stopConfig() {
  return tuya.stopConfig();
}

export function startBluetoothScan(params: StartLeScanParams) {
  if (Platform.OS === 'ios') {
    return tuyaBLEScanner.startBluetoothScan();
  }
  return tuya.startBluetoothScan(params);
}

export function initBluetoothDualModeActivator(
  params: InitBluetoothActivatorParams
): Promise<DeviceBean> {
  if (Platform.OS === 'ios') {
    return tuyaBLEActivator.initActivator(params);
  }
  return tuya.initBluetoothDualModeActivator(params);
}

export function initBluetoothDualModeActivatorFromScanBean(
  params: InitBluetoothActivatorFromScanBeanParams
): Promise<DeviceBean> {
  if (Platform.OS === 'ios') {
    // TODO
    return tuyaBLEActivator.initActivator(params);
  }
  return tuya.initBluetoothDualModeActivatorFromScanBean(params);
}

export function stopLeScan() {
  if (Platform.OS === 'ios') {
    // TODO
    return;
  }
  return tuya.stopBluetoothScan();
}

export function stopLePairing() {
  if (Platform.OS === 'ios') {
    // TODO
    return;
  }
  return tuya.stopLeActivation();
}

export function getCurrentWifi(
  success: (ssid: string) => void,
  error: () => void
) {
  // We need the Allow While Using App location permission to use this.
  return tuya.getCurrentWifi({}, success, error);
}
