import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  getHomeDetail(params: Object): Promise<Object>;
  getHomeLocalCache(params: Object): Promise<Object>;
  updateHome(params: Object): Promise<Object>;
  dismissHome(params: Object): Promise<Object>;
  addRoom(params: Object): Promise<Object>;
  removeRoom(params: Object): Promise<Object>;
  sortRoom(params: Object): Promise<Object>;
  sortHome(params: Object): Promise<Object>;
  queryRoomList(params: Object): Promise<Object>;
  createGroup(params: Object): Promise<Object>;
  registerHomeStatusListener(params: Object): void;
  unRegisterHomeStatusListener(params: Object): void;
  queryDeviceListToAddGroup(params: Object): Promise<Object>;
  onDestroy(params: Object): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('TuyaHomeModule');
