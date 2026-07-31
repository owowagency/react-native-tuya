import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  addMember(params: Object): Promise<Object>;
  removeMember(params: Object): Promise<Object>;
  queryMemberList(params: Object): Promise<Object>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('TuyaHomeMemberModule');
