import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  updateRoom(params: Object): Promise<Object>;
  addDevice(params: Object): Promise<Object>;
  removeDevice(params: Object): Promise<Object>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('TuyaRoomModule');
