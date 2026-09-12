import {NativeModules} from 'react-native';

const tuya = NativeModules.TuyaRoomModule;

export declare type UpdateParams = {
  roomId: string;
  name: string;
};
export function updateRoom(params: UpdateParams): Promise<any> {
  return tuya.updateRoom(params);
}

export declare type AddDeviceParams = {
  roomId: number;
  devId: string;
};
export function addDevice(params: AddDeviceParams): Promise<any> {
  return tuya.addDevice(params);
}

export declare type RemoveDeviceParams = {
  roomId: string;
  devId: string;
};
export function removeDeviceRoom(params: RemoveDeviceParams): Promise<any> {
  return tuya.removeDevice(params);
}

export declare type RemoveGroupParams = {
  roomId: string;
  groupId: string;
};
export function removeGroup(params: RemoveGroupParams): Promise<any> {
  return tuya.removeGroup(params);
}

export declare type AddGroupParams = {
  roomId: string;
  groupId: string;
};
export function addGroup(params: AddGroupParams): Promise<any> {
  return tuya.addGroup(params);
}

export function getRoomInstance(roomId: number): Promise<any> {
  return tuya.getRoomInstance(roomId);
}
