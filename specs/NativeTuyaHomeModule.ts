import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  getHomeDetail(params: Object): Promise<Object>;
  updateHome(params: Object): Promise<Object>;
  dismissHome(params: Object): Promise<Object>;
  sortRoom(params: Object): Promise<Object>;
  queryRoomList(params: Object): Promise<Object>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('TuyaHomeModule');
