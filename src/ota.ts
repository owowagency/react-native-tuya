import { NativeModules } from 'react-native';
import { addEvent, bridge, HARDWAREUPGRADELISTENER } from './bridgeUtils';

const tuya = NativeModules.TuyaOTAModule;

export type StartOtaParams = {
  devId: string;
};

export function startOta(
  params: StartOtaParams,
  onSuccess: (data: any) => void,
  onFailure: (data: any) => void,
  onProgress: (data: any) => void
) {
  tuya.startOta(params);
  return addEvent(bridge(HARDWAREUPGRADELISTENER, params.devId), data => {
    if (data.type === 'onSuccess') {
      onSuccess(data);
    } else if (data.type === 'onFailure') {
      onFailure(data);
    } else if (data.type === 'onProgress') {
      onProgress(data);
    }
  });
}

export function getOtaInfo(params: StartOtaParams): Promise<any[]> {
  return tuya.getOtaInfo(params);
}
