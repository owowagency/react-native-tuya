import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  startBluetoothScan(): Promise<Object>;
  stopBluetoothScan(): void;
  initBluetoothDualModeActivator(params: Object): Promise<Object>;
  getCurrentWifi(
    params: Object,
    success: (ssid: string) => void,
    error: () => void
  ): void;
  openNetworkSettings(params: Object): void;
  initActivator(params: Object): Promise<Object>;
  stopConfig(): void;
  newGwSubDevActivator(params: Object): Promise<Object>;
  stopNewGwSubDevActivatorConfig(params: Object): void;
  onDestory(): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('TuyaActivatorModule');
