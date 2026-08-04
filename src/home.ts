import { NativeModules } from 'react-native';
import { DeviceDps } from './device';

const tuya = NativeModules.TuyaHomeModule;

export type QueryRoomListParams = {
  homeId?: number;
};
export type QueryRoomListResponse = {
  name: string;
  displayOrder: number;
  id: number;
  roomId: number;
}[];

export function queryRoomList(
  params: QueryRoomListParams
): Promise<QueryRoomListResponse> {
  return tuya.queryRoomList(params);
}

export type GetHomeDetailParams = {
  homeId: number;
};
export type DeviceDetailResponse = {
  homeId: number;
  isOnline: boolean;
  productId: string;
  devId: string;
  verSw: string;
  name: string;
  dps: DeviceDps;
  homeDisplayOrder: number;
  roomId: number;
};
export type GetHomeDetailResponse = {
  deviceList: DeviceDetailResponse[];
  groupList: any[];
  meshList: any[];
  sharedDeviceList: any[];
  sharedGroupList: any[];
};

export function getHomeDetail(
  params: GetHomeDetailParams
): Promise<GetHomeDetailResponse> {
  return tuya.getHomeDetail(params);
}

export type UpdateHomeParams = {
  homeId: number;
  name: string;
  geoName: string;
  lon: number;
  lat: number;
};

export function updateHome(params: UpdateHomeParams): Promise<string> {
  return tuya.updateHome(params);
}

export type DismissHomeParams = {
  homeId: number;
};

export function dismissHome(params: DismissHomeParams): Promise<string> {
  return tuya.dismissHome(params);
}

export type SortRoomsParams = {
  idList: number[];
  homeId: number;
};

export function sortRoom(params: SortRoomsParams): Promise<string> {
  return tuya.sortRoom(params);
}

export type AddRoomParams = {
  homeId: number;
  name: string;
};

export function addRoom(params: AddRoomParams): Promise<string> {
  return tuya.addRoom(params);
}

export type RemoveRoomParams = {
  homeId: number;
  roomId: number;
};

export function removeRoom(params: RemoveRoomParams): Promise<string> {
  return tuya.removeRoom(params);
}

export type GetHomeLocalCacheParams = {
  homeId: number;
};

export function getHomeLocalCache(
  params: GetHomeLocalCacheParams
): Promise<GetHomeDetailResponse> {
  return tuya.getHomeLocalCache(params);
}

export type SortHomeParams = {
  idList: number[];
  homeId: number;
};

export function sortHome(params: SortHomeParams): Promise<string> {
  return tuya.sortHome(params);
}

export type CreateGroupParams = {
  homeId: number;
  productId: string;
  name: string;
  devIdList: string[];
};

export function createGroup(params: CreateGroupParams): Promise<number> {
  return tuya.createGroup(params);
}

export type RegisterHomeStatusListenerParams = {
  homeId: number;
};

export function registerHomeStatusListener(
  params: RegisterHomeStatusListenerParams
): void {
  tuya.registerHomeStatusListener(params);
}

export type UnRegisterHomeStatusListenerParams = {
  homeId: number;
};

export function unRegisterHomeStatusListener(
  params: UnRegisterHomeStatusListenerParams
): void {
  tuya.unRegisterHomeStatusListener(params);
}

export type QueryDeviceListToAddGroupParams = {
  homeId: number;
  productId: string;
};

export function queryDeviceListToAddGroup(
  params: QueryDeviceListToAddGroupParams
): Promise<any[]> {
  return tuya.queryDeviceListToAddGroup(params);
}

export type OnDestroyHomeParams = {
  homeId: number;
};

export function onDestroyHome(params: OnDestroyHomeParams): void {
  tuya.onDestroy(params);
}
