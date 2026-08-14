import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  apiRequest(params: Object): Promise<Object>;
  initWithoutOptions(): void;
  initWithOptions(params: Object): void;
  setOnNeedLoginListener(): void;
  exitApp(): void;
  openNetworkSettings(params: Object): void;
  onDestory(params: Object): void;
  setLocation(params: Object): void;
  getLocationData(): Promise<Object>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('TuyaCoreModule');
