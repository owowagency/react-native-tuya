import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  startBluetoothScan(): Promise<Object>;
  initBluetoothDualModeActivator(params: Object): Promise<Object>;
  getCurrentWifi(
    params: Object,
    success: (ssid: string) => void,
    error: () => void
  ): void;
  openNetworkSettings(params: Object): void;
  initActivator(params: Object): Promise<Object>;
  stopConfig(): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('TuyaActivatorModule');
