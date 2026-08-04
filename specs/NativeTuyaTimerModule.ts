import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  initWithOptions(params: Object): void;
  onDestory(params: Object): void;
  addTimerWithTask(params: Object): Promise<Object>;
  getTimerTaskStatusWithDeviceId(params: Object): Promise<Object>;
  updateTimerTaskStatusWithTask(params: Object): Promise<Object>;
  updateTimerStatusWithTask(params: Object): Promise<Object>;
  removeTimerWithTask(params: Object): Promise<Object>;
  updateTimerWithTask(params: Object): Promise<Object>;
  getTimerWithTask(params: Object): Promise<Object>;
  getAllTimerWithDeviceId(params: Object): Promise<Object>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('TuyaTimerModule');
