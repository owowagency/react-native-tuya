'use strict';

Object.defineProperty(exports, '__esModule', { value: true });

var tslib = require('tslib');
var reactNative = require('react-native');

var GROUPLISTENER = 'groupListener';
var HARDWAREUPGRADELISTENER = 'hardwareUpgradeListener';
var DEVLISTENER = 'devListener';
var SUBDEVLISTENER = 'subDevListener';
var HOMESTATUS = 'homeStatus';
var HOMECHANGE = 'homeChange';
var SINGLETRANSFER = 'SingleTransfer';
var eventEmitter = /*#__PURE__*/new reactNative.NativeEventEmitter(reactNative.NativeModules.TuyaRNEventEmitter);
function addEvent(eventName, callback) {
  return eventEmitter.addListener(eventName, callback);
}
var bridge = function bridge(key, id) {
  return "".concat(key, "//").concat(id);
};
/*
 * On iOS home devices list has differences in structure, soo need to make it same as on android
 * */
var prepareDeviceBean = function prepareDeviceBean(device) {
  var schema = JSON.parse(device.schema);
  var schemaMap = {};
  var dpCodeSchemaMap = {};
  schema.forEach(function (item) {
    schemaMap[item.id] = item;
    dpCodeSchemaMap[item.code] = item;
  });
  return tslib.__assign(tslib.__assign({}, device), {
    schemaMap: schemaMap,
    productBean: {
      schemaInfo: {
        dpCodeSchemaMap: dpCodeSchemaMap
      }
    }
  });
};

var tuya = reactNative.NativeModules.TuyaActivatorModule;
var tuyaBLEActivator = reactNative.NativeModules.TuyaBLEActivatorModule;
var tuyaBLEScanner = reactNative.NativeModules.TuyaBLEScannerModule;
function openNetworkSettings() {
  return tuya.openNetworkSettings({});
}
(function (TuyaActivationType) {
  TuyaActivationType["EZ"] = "TY_EZ";
  TuyaActivationType["AP"] = "TY_AP";
  TuyaActivationType["QR"] = "TY_QR";
})(exports.TuyaActivationType || (exports.TuyaActivationType = {}));
function initActivator(params) {
  return tslib.__awaiter(this, void 0, void 0, function () {
    var device;
    return tslib.__generator(this, function (_a) {
      switch (_a.label) {
        case 0:
          if (!(reactNative.Platform.OS === 'ios')) return [3 /*break*/, 2];
          return [4 /*yield*/, tuya.initActivator(params)];
        case 1:
          device = _a.sent();
          return [2 /*return*/, prepareDeviceBean(device)];
        case 2:
          return [2 /*return*/, tuya.initActivator(params)];
      }
    });
  });
}
function getActivatorToken(params) {
  return tuya.getActivatorToken(params);
}
function startQRActivator(params) {
  return tslib.__awaiter(this, void 0, void 0, function () {
    var device;
    return tslib.__generator(this, function (_a) {
      switch (_a.label) {
        case 0:
          if (!(reactNative.Platform.OS === 'ios')) return [3 /*break*/, 2];
          return [4 /*yield*/, tuya.initActivator(tslib.__assign(tslib.__assign({}, params), {
            type: 'TY_QR'
          }))];
        case 1:
          device = _a.sent();
          return [2 /*return*/, prepareDeviceBean(device)];
        case 2:
          return [2 /*return*/, tuya.startQRActivator(params)];
      }
    });
  });
}
function stopQRActivator() {
  if (reactNative.Platform.OS === 'ios') {
    return stopConfig();
  }
  return tuya.stopQRActivator();
}
function stopConfig() {
  return tuya.stopConfig();
}
function startBluetoothScan(params) {
  if (reactNative.Platform.OS === 'ios') {
    return tuyaBLEScanner.startBluetoothLEScan();
  }
  return tuya.startBluetoothScan(params);
}
function initBluetoothDualModeActivator(params) {
  return tslib.__awaiter(this, void 0, void 0, function () {
    var device;
    return tslib.__generator(this, function (_a) {
      switch (_a.label) {
        case 0:
          if (!(reactNative.Platform.OS === 'ios')) return [3 /*break*/, 2];
          return [4 /*yield*/, tuyaBLEActivator.initActivator(params)];
        case 1:
          device = _a.sent();
          return [2 /*return*/, prepareDeviceBean(device)];
        case 2:
          return [2 /*return*/, tuya.initBluetoothDualModeActivator(params)];
      }
    });
  });
}
function initBluetoothDualModeActivatorFromScanBean(params) {
  return tslib.__awaiter(this, void 0, void 0, function () {
    var device;
    return tslib.__generator(this, function (_a) {
      switch (_a.label) {
        case 0:
          if (!(reactNative.Platform.OS === 'ios')) return [3 /*break*/, 2];
          return [4 /*yield*/, tuyaBLEActivator.initActivator(params)];
        case 1:
          device = _a.sent();
          return [2 /*return*/, prepareDeviceBean(device)];
        case 2:
          return [2 /*return*/, tuya.initBluetoothDualModeActivatorFromScanBean(params)];
      }
    });
  });
}
function stopLeScan() {
  if (reactNative.Platform.OS === 'ios') {
    return tuyaBLEScanner.stopBluetoothScan();
  }
  return tuya.stopBluetoothScan();
}
function stopLePairing() {
  if (reactNative.Platform.OS === 'ios') {
    return tuyaBLEActivator.stopLePairing();
  }
  return tuya.stopLeActivation();
}
function getCurrentWifi(success, error) {
  // We need the Allow While Using App location permission to use this.
  return tuya.getCurrentWifi({}, success, error);
}

var tuya$1 = reactNative.NativeModules.TuyaDeviceModule;
var devListenerSubs = {};
function registerDevListener(params, type, callback) {
  tuya$1.registerDevListener(params);
  var sub = addEvent(bridge(DEVLISTENER, params.devId), function (data) {
    if (data.type === type) {
      callback(data);
    }
  });
  devListenerSubs[params.devId] = sub;
}
function unRegisterAllDevListeners() {
  for (var devId in devListenerSubs) {
    var sub = devListenerSubs[devId];
    sub.remove();
    tuya$1.unRegisterDevListener({
      devId: devId
    });
  }
  devListenerSubs = {};
}
function send(params) {
  return tuya$1.send(params);
}
function removeDevice(params) {
  return tuya$1.removeDevice(params);
}
function renameDevice(params) {
  return tuya$1.renameDevice(params);
}
function getDataPointStat(params) {
  return tuya$1.getDataPointStat(params);
}

var tuya$2 = reactNative.NativeModules.TuyaDeviceModule;
(function (DeviceListenerType) {
  DeviceListenerType["onDpUpdate"] = "onDpUpdate";
  DeviceListenerType["onRemoved"] = "onRemoved";
  DeviceListenerType["onStatusChanged"] = "onStatusChanged";
  DeviceListenerType["onNetworkStatusChanged"] = "onNetworkStatusChanged";
  DeviceListenerType["onDevInfoUpdate"] = "onDevInfoUpdate";
  DeviceListenerType["onFirmwareUpgradeSuccess"] = "onFirmwareUpgradeSuccess";
  DeviceListenerType["onFirmwareUpgradeFailure"] = "onFirmwareUpgradeFailure";
  DeviceListenerType["onFirmwareUpgradeProgress"] = "onFirmwareUpgradeProgress";
})(exports.DeviceListenerType || (exports.DeviceListenerType = {}));
var subscriptions = {};
var addDeviceListener = function addDeviceListener(_a, cb, onError) {
  var devId = _a.devId,
    type = _a.type;
  if (!subscriptions[devId]) {
    try {
      tuya$2.registerDevListener({
        devId: devId
      });
    } catch (e) {
      onError === null || onError === void 0 ? void 0 : onError(e);
    }
    subscriptions[devId] = 0;
  }
  var sub = addEvent(bridge(DEVLISTENER, devId), function (data) {
    if (data.type === type) {
      cb(data);
    }
  });
  subscriptions[devId]++;
  return {
    remove: function remove() {
      sub.remove();
      if (subscriptions[devId] <= 1) {
        tuya$2.unRegisterDevListener({
          devId: devId
        });
      }
      subscriptions[devId]--;
    }
  };
};
var removeAllDeviceListeners = function removeAllDeviceListeners() {
  Object.keys(subscriptions).forEach(function (devId) {
    tuya$2.unRegisterDevListener({
      devId: devId
    });
    delete subscriptions[devId];
  });
};

var tuya$3 = reactNative.NativeModules.TuyaHomeModule;
function queryRoomList(params) {
  return tuya$3.queryRoomList(params);
}
function getHomeDetail(params) {
  var _a;
  return tslib.__awaiter(this, void 0, void 0, function () {
    var homeDetails, deviceList;
    return tslib.__generator(this, function (_b) {
      switch (_b.label) {
        case 0:
          return [4 /*yield*/, tuya$3.getHomeDetail(params)];
        case 1:
          homeDetails = _b.sent();
          /*
          * On iOS home devices list has differences in structure, soo need to make it same as on android
          * */
          if (reactNative.Platform.OS === 'ios' && homeDetails.deviceList) {
            deviceList = (_a = homeDetails.deviceList) === null || _a === void 0 ? void 0 : _a.map(function (i) {
              return prepareDeviceBean(i);
            });
            return [2 /*return*/, tslib.__assign(tslib.__assign({}, homeDetails), {
              deviceList: deviceList
            })];
          }
          return [2 /*return*/, homeDetails];
      }
    });
  });
}
function updateHome(params) {
  return tuya$3.updateHome(params);
}
function dismissHome(params) {
  return tuya$3.dismissHome(params);
}
function sortRoom(params) {
  return tuya$3.sortRoom(params);
}

var tuya$4 = reactNative.NativeModules.TuyaHomeDataManagerModule;
function getRoomDeviceList(params) {
  return tuya$4.getRoomDeviceList(params);
}

var tuya$5 = reactNative.NativeModules.TuyaHomeManagerModule;
function createHome(params) {
  return tuya$5.createHome(params);
}
function queryHomeList() {
  return tslib.__awaiter(this, void 0, void 0, function () {
    var homes;
    return tslib.__generator(this, function (_a) {
      switch (_a.label) {
        case 0:
          return [4 /*yield*/, tuya$5.queryHomeList()];
        case 1:
          homes = _a.sent();
          // Tuya's Android SDK uses different property names than the iOS SDK...
          if (reactNative.Platform.OS === 'android') {
            homes = homes.map(function (m) {
              return tslib.__assign(tslib.__assign({}, m), {
                dealStatus: m.homeStatus
              });
            });
          }
          return [2 /*return*/, homes];
      }
    });
  });
}
function joinFamily(params) {
  return tuya$5.joinFamily(params);
}

var tuya$6 = reactNative.NativeModules.TuyaHomeMemberModule;
function queryMemberList(params) {
  return tslib.__awaiter(this, void 0, void 0, function () {
    var members;
    return tslib.__generator(this, function (_a) {
      switch (_a.label) {
        case 0:
          return [4 /*yield*/, tuya$6.queryMemberList(params)];
        case 1:
          members = _a.sent();
          // Tuya's Android SDK uses different property names than the iOS SDK...
          if (reactNative.Platform.OS === 'android') {
            members = members.map(function (m) {
              return {
                admin: m.admin,
                username: m.account,
                id: m.memberId,
                dealStatus: m.memberStatus
              };
            });
          }
          return [2 /*return*/, members];
      }
    });
  });
}
function addMember(params) {
  return tuya$6.addMember(params);
}
function removeMember(params) {
  return tuya$6.removeMember(params);
}

var tuya$7 = reactNative.Platform.OS === 'ios' ? reactNative.NativeModules.TuyaDeviceModule : reactNative.NativeModules.TuyaOTAModule;
function startOta(params, onSuccess, onFailure, onProgress) {
  tuya$7.startOta(params);
  return addEvent(bridge(HARDWAREUPGRADELISTENER, params.devId), function (data) {
    if (data.type === 'onSuccess') {
      onSuccess(data);
    } else if (data.type === 'onFailure') {
      onFailure(data);
    } else if (data.type === 'onProgress') {
      onProgress(data);
    }
  });
}
(function (TuyaUpgradeStatus) {
  TuyaUpgradeStatus[TuyaUpgradeStatus["noUpdate"] = 0] = "noUpdate";
  TuyaUpgradeStatus[TuyaUpgradeStatus["updateAvailable"] = 1] = "updateAvailable";
  TuyaUpgradeStatus[TuyaUpgradeStatus["updating"] = 2] = "updating";
  TuyaUpgradeStatus[TuyaUpgradeStatus["waitForDevice"] = 5] = "waitForDevice";
})(exports.TuyaUpgradeStatus || (exports.TuyaUpgradeStatus = {}));
function getOtaInfo(params) {
  return tuya$7.getOtaInfo(params);
}
function startFirmwareUpgrade(params) {
  return tuya$7.startFirmwareUpgrade(params);
}

var tuya$8 = reactNative.NativeModules.TuyaTimerModule;
function addTimerWithTask(params) {
  return tuya$8.addTimerWithTask(params);
}
function updateTimerWithTask(params) {
  return tuya$8.updateTimerWithTask(params);
}
function getTimerTaskStatusWithDeviceId(params) {
  return tuya$8.getTimerTaskStatusWithDeviceId(params);
}
function getAllTimerWithDeviceId(params) {
  return tslib.__awaiter(this, void 0, void 0, function () {
    var timers;
    return tslib.__generator(this, function (_a) {
      switch (_a.label) {
        case 0:
          return [4 /*yield*/, tuya$8.getAllTimerWithDeviceId(params)];
        case 1:
          timers = _a.sent();
          timers.forEach(function (t) {
            t.timerTaskStatus.open = !!t.timerTaskStatus.open;
          });
          return [2 /*return*/, timers];
      }
    });
  });
}
function removeTimerWithTask(params) {
  return tuya$8.removeTimerWithTask(params);
}
function updateTimerStatusWithTask(params) {
  return tuya$8.updateTimerStatusWithTask(params);
}

var tuya$9 = reactNative.NativeModules.TuyaUserModule;
function registerAccountWithEmail(params) {
  return tuya$9.registerAccountWithEmail(params);
}
function getRegisterEmailValidateCode(params) {
  return tuya$9.getRegisterEmailValidateCode(params);
}
function sendVerifyCodeWithUserName(params) {
  return tuya$9.sendVerifyCodeWithUserName(params);
}
function loginWithEmail(params) {
  return tuya$9.loginWithEmail(params);
}
function getEmailValidateCode(params) {
  return tuya$9.getEmailValidateCode(params);
}
function resetEmailPassword(params) {
  return tuya$9.resetEmailPassword(params);
}
function logout() {
  return tuya$9.logout();
}
function getCurrentUser() {
  return tuya$9.getCurrentUser();
}
function getUser() {
  return tuya$9.getUser();
}
function cancelAccount() {
  return tuya$9.cancelAccount();
}
function loginWithUid(params) {
  return tslib.__awaiter(this, void 0, void 0, function () {
    return tslib.__generator(this, function (_a) {
      return [2 /*return*/, tuya$9.loginWithUid(params)];
    });
  });
}
function loginOrRegisterWithUid(params) {
  return tslib.__awaiter(this, void 0, void 0, function () {
    return tslib.__generator(this, function (_a) {
      return [2 /*return*/, tuya$9.loginOrRegisterWithUid(params)];
    });
  });
}

exports.DEVLISTENER = DEVLISTENER;
exports.GROUPLISTENER = GROUPLISTENER;
exports.HARDWAREUPGRADELISTENER = HARDWAREUPGRADELISTENER;
exports.HOMECHANGE = HOMECHANGE;
exports.HOMESTATUS = HOMESTATUS;
exports.SINGLETRANSFER = SINGLETRANSFER;
exports.SUBDEVLISTENER = SUBDEVLISTENER;
exports.addDeviceListener = addDeviceListener;
exports.addEvent = addEvent;
exports.addMember = addMember;
exports.addTimerWithTask = addTimerWithTask;
exports.bridge = bridge;
exports.cancelAccount = cancelAccount;
exports.createHome = createHome;
exports.dismissHome = dismissHome;
exports.getActivatorToken = getActivatorToken;
exports.getAllTimerWithDeviceId = getAllTimerWithDeviceId;
exports.getCurrentUser = getCurrentUser;
exports.getCurrentWifi = getCurrentWifi;
exports.getDataPointStat = getDataPointStat;
exports.getEmailValidateCode = getEmailValidateCode;
exports.getHomeDetail = getHomeDetail;
exports.getOtaInfo = getOtaInfo;
exports.getRegisterEmailValidateCode = getRegisterEmailValidateCode;
exports.getRoomDeviceList = getRoomDeviceList;
exports.getTimerTaskStatusWithDeviceId = getTimerTaskStatusWithDeviceId;
exports.getUser = getUser;
exports.initActivator = initActivator;
exports.initBluetoothDualModeActivator = initBluetoothDualModeActivator;
exports.initBluetoothDualModeActivatorFromScanBean = initBluetoothDualModeActivatorFromScanBean;
exports.joinFamily = joinFamily;
exports.loginOrRegisterWithUid = loginOrRegisterWithUid;
exports.loginWithEmail = loginWithEmail;
exports.loginWithUid = loginWithUid;
exports.logout = logout;
exports.openNetworkSettings = openNetworkSettings;
exports.prepareDeviceBean = prepareDeviceBean;
exports.queryHomeList = queryHomeList;
exports.queryMemberList = queryMemberList;
exports.queryRoomList = queryRoomList;
exports.registerAccountWithEmail = registerAccountWithEmail;
exports.registerDevListener = registerDevListener;
exports.removeAllDeviceListeners = removeAllDeviceListeners;
exports.removeDevice = removeDevice;
exports.removeMember = removeMember;
exports.removeTimerWithTask = removeTimerWithTask;
exports.renameDevice = renameDevice;
exports.resetEmailPassword = resetEmailPassword;
exports.send = send;
exports.sendVerifyCodeWithUserName = sendVerifyCodeWithUserName;
exports.sortRoom = sortRoom;
exports.startBluetoothScan = startBluetoothScan;
exports.startFirmwareUpgrade = startFirmwareUpgrade;
exports.startOta = startOta;
exports.startQRActivator = startQRActivator;
exports.stopConfig = stopConfig;
exports.stopLePairing = stopLePairing;
exports.stopLeScan = stopLeScan;
exports.stopQRActivator = stopQRActivator;
exports.unRegisterAllDevListeners = unRegisterAllDevListeners;
exports.updateHome = updateHome;
exports.updateTimerStatusWithTask = updateTimerStatusWithTask;
exports.updateTimerWithTask = updateTimerWithTask;
//# sourceMappingURL=react-native-tuya.cjs.development.js.map
