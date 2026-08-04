import { NativeModules, Platform } from 'react-native';

const tuya = NativeModules.TuyaHomeMemberModule;

export type QueryMemberListParams = { homeId: number };
export type MemberListItem = {
  admin: boolean;
  username: string;
  id: number;
  dealStatus: number;
};
export type QueryMemberListResponse = MemberListItem[];

export async function queryMemberList(
  params: QueryMemberListParams
): Promise<QueryMemberListResponse> {
  let members = await tuya.queryMemberList(params);
  // Tuya's Android SDK uses different property names than the iOS SDK...
  if (Platform.OS === 'android') {
    members = members.map((m: any) => ({
      admin: m.admin,
      username: m.account,
      id: m.memberId,
      dealStatus: m.memberStatus,
    }));
  }
  return members;
}

export type AddMemberParams = {
  homeId: number;
  userAccount: string; // email
  countryCode: string;
  name: string;
  admin: boolean;
};

export function addMember(params: AddMemberParams): Promise<any> {
  return tuya.addMember(params);
}

export type RemoveMemberParams = {
  memberId: number;
};

export function removeMember(params: RemoveMemberParams): Promise<any> {
  return tuya.removeMember(params);
}

export type UpdateMemberParams = {
  memberId: number;
  name: string;
  admin: boolean;
};

export function updateMember(params: UpdateMemberParams): Promise<any> {
  return tuya.updateMember(params);
}
