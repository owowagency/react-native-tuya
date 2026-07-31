import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  cancelAccount(): Promise<Object>;
  getCurrentUser(): Promise<Object>;
  getEmailValidateCode(params: Object): Promise<Object>;
  getRegisterEmailValidateCode(params: Object): Promise<Object>;
  loginWithEmail(params: Object): Promise<Object>;
  logout(): Promise<Object>;
  registerAccountWithEmail(params: Object): Promise<Object>;
  resetEmailPassword(params: Object): Promise<Object>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('TuyaUserModule');
