import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  getDeviceRoomBean(params: Object): Promise<Object>;
  getRoomDeviceList(params: Object): Promise<Object>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('TuyaHomeDataManagerModule');
