import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  getHomeRoomList(params: Object): Promise<Object>;
  getHomeDeviceList(params: Object): Promise<Object>;
  getHomeGroupList(params: Object): Promise<Object>;
  getGroupBean(params: Object): Promise<Object>;
  getDeviceBean(params: Object): Promise<Object>;
  getGroupRoomBean(params: Object): Promise<Object>;
  getRoomBean(params: Object): Promise<Object>;
  getDeviceRoomBean(params: Object): Promise<Object>;
  getGroupDeviceList(params: Object): Promise<Object>;
  getMeshGroupList(params: Object): Promise<Object>;
  getMeshDeviceList(params: Object): Promise<Object>;
  getRoomDeviceList(params: Object): Promise<Object>;
  getRoomGroupList(params: Object): Promise<Object>;
  getHomeBean(params: Object): Promise<Object>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('TuyaHomeDataManagerModule');
