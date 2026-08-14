import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  checkVersionUpgrade(): Promise<Object>;
  upgradeVersion(): Promise<Object>;
  getValidateCode(params: Object): Promise<Object>;
  loginWithValidateCode(params: Object): Promise<Object>;
  registerAccountWithPhone(params: Object): Promise<Object>;
  loginWithPhonePassword(params: Object): Promise<Object>;
  resetPhonePassword(params: Object): Promise<Object>;
  getRegisterEmailValidateCode(params: Object): Promise<Object>;
  registerAccountWithEmail(params: Object): Promise<Object>;
  loginWithEmail(params: Object): Promise<Object>;
  getEmailValidateCode(params: Object): Promise<Object>;
  resetEmailPassword(params: Object): Promise<Object>;
  logout(): Promise<Object>;
  cancelAccount(): Promise<Object>;
  registerAccountWithUid(params: Object): Promise<Object>;
  loginWithUid(params: Object): Promise<Object>;
  loginOrRegisterWithUid(params: Object): Promise<Object>;
  loginByTwitter(params: Object): Promise<Object>;
  loginByQQ(params: Object): Promise<Object>;
  loginByWechat(params: Object): Promise<Object>;
  loginByFacebook(params: Object): Promise<Object>;
  getCurrentUser(): Promise<Object>;
  uploadUserAvatar(params: Object): Promise<Object>;
  setTempUnit(params: Object): Promise<Object>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('TuyaUserModule');
