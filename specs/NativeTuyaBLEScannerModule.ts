import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  startBluetoothScan(): Promise<Object>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('TuyaBLEScannerModule');
