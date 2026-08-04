import { NativeModules } from 'react-native';

const tuya = NativeModules.TuyaHomeDataManagerModule;

export type GetRoomDeviceListParams = {
  homeId?: number;
  roomId: number;
};

export type GetRoomDeviceListResponse = {
  deviceList: {}[];
  groupList: {}[];
};

export function getRoomDeviceList(
  params: GetRoomDeviceListParams
): Promise<GetRoomDeviceListResponse> {
  return tuya.getRoomDeviceList(params);
}

export function getHomeRoomList(params: { homeId: number }): Promise<any[]> {
  return tuya.getHomeRoomList(params);
}

export function getHomeDeviceList(params: { homeId: number }): Promise<any[]> {
  return tuya.getHomeDeviceList(params);
}

export function getHomeGroupList(params: { homeId: number }): Promise<any[]> {
  return tuya.getHomeGroupList(params);
}

export function getGroupBean(params: { groupId: number }): Promise<any> {
  return tuya.getGroupBean(params);
}

export function getDeviceBean(params: { devId: string }): Promise<any> {
  return tuya.getDeviceBean(params);
}

export function getGroupRoomBean(params: { groupId: number }): Promise<any> {
  return tuya.getGroupRoomBean(params);
}

export function getRoomBean(params: { roomId: number }): Promise<any> {
  return tuya.getRoomBean(params);
}

export function getGroupDeviceList(params: {
  groupId: number;
}): Promise<any[]> {
  return tuya.getGroupDeviceList(params);
}

export function getMeshGroupList(params: { meshId: string }): Promise<any[]> {
  return tuya.getMeshGroupList(params);
}

export function getMeshDeviceList(params: {
  meshId: string;
}): Promise<any[]> {
  return tuya.getMeshDeviceList(params);
}

export function getRoomGroupList(params: { roomId: number }): Promise<any[]> {
  return tuya.getRoomGroupList(params);
}

export function getHomeBean(params: { homeId: number }): Promise<any> {
  return tuya.getHomeBean(params);
}

export function getDeviceRoomBean(params: { devId: string }): Promise<any> {
  return tuya.getDeviceRoomBean(params);
}
