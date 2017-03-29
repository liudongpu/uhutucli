"use strict";
var Log = require("log");
var UtilsJson = require("../../base/utils/json");
var UtilsIo = require("../../base/utils/io");
var UtilsString = require("../../base/utils/string");
var log = new Log("info");
var McommonRoot = (function () {
    function McommonRoot() {
    }
    McommonRoot.prototype.logDebug = function (iLogCode) {
        var aArgs = [];
        for (var _i = 1; _i < arguments.length; _i++) {
            aArgs[_i - 1] = arguments[_i];
        }
        log.debug(logLoad.upLogInfo(iLogCode, aArgs));
    };
    McommonRoot.prototype.logInfo = function (iLogCode) {
        var aArgs = [];
        for (var _i = 1; _i < arguments.length; _i++) {
            aArgs[_i - 1] = arguments[_i];
        }
        log.info(logLoad.upLogInfo(iLogCode, aArgs));
    };
    McommonRoot.prototype.logWarn = function (iLogCode) {
        var aArgs = [];
        for (var _i = 1; _i < arguments.length; _i++) {
            aArgs[_i - 1] = arguments[_i];
        }
        log.warn(logLoad.upLogInfo(iLogCode, aArgs));
    };
    McommonRoot.prototype.logError = function (iLogCode) {
        var aArgs = [];
        for (var _i = 1; _i < arguments.length; _i++) {
            aArgs[_i - 1] = arguments[_i];
        }
        log.error(logLoad.upLogInfo(iLogCode, aArgs));
    };
    return McommonRoot;
}());
var LogLoad = (function () {
    function LogLoad() {
        this._currentMap = new Map();
        this._flagInitMap = false;
        var sCliRoot = UtilsIo.parentTop(__dirname, 3);
        var sContent = UtilsIo.readFile(UtilsIo.pathJoin(sCliRoot, "resources/files-local/log-info/cli_log.json"));
        var aJson = UtilsJson.parse(sContent);
        for (var p in aJson) {
            this._currentMap.set(p, aJson[p]);
        }
    }
    LogLoad.prototype.upLogInfo = function (iLogCode) {
        var aArgs = [];
        for (var _i = 1; _i < arguments.length; _i++) {
            aArgs[_i - 1] = arguments[_i];
        }
        return iLogCode + ' ' + UtilsString.formatString(this.upLogInfoByCode(iLogCode), aArgs);
    };
    LogLoad.prototype.upLogInfoByCode = function (iLogCode) {
        return this._currentMap.get(iLogCode.toString());
    };
    return LogLoad;
}());
var logLoad = new LogLoad();
module.exports = new McommonRoot();