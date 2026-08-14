import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  // Note: not implemented on iOS today (pre-existing limitation, see
  // src/api/device.ts's TODO comment) - stubbed there to reject cleanly.
  getDevice(params: Object): Promise<Object>;
  getDeviceData(params: Object): Promise<Object>;
  onDestroy(params: Object): void;
  getDp(params: Object): Promise<Object>;
  registerDevListener(params: Object): void;
  unRegisterDevListener(params: Object): void;
  send(params: Object): Promise<Object>;
  renameDevice(params: Object): Promise<Object>;
  getDataPointStat(params: Object): Promise<Object>;
  removeDevice(params: Object): Promise<Object>;
  // Note: not implemented on Android today (pre-existing bug - the real
  // implementation lives in a separate, differently-named native module,
  // TuyaOTAModule, that JS never actually calls). Stubbed here to reject
  // cleanly instead of throwing "not a function".
  getOtaInfo(params: Object): Promise<Object>;
  startOta(params: Object): void;
}

export default TurboModuleRegistry.getEnforcing<Spec>('TuyaDeviceModule');
