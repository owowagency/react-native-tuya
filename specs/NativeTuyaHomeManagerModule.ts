import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  queryHomeList(): Promise<Object>;
  createHome(params: Object): Promise<Object>;
  joinFamily(params: Object): Promise<Object>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('TuyaHomeManagerModule');
