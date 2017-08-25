"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
var MprocessParseFile = (function () {
    function MprocessParseFile() {
    }
    return MprocessParseFile;
}());
exports.MprocessParseFile = MprocessParseFile;
var MtransformTemplateInfo = (function () {
    function MtransformTemplateInfo() {
        this.templateName = '';
        this.templateSource = '';
        this.templateContent = [];
    }
    return MtransformTemplateInfo;
}());
exports.MtransformTemplateInfo = MtransformTemplateInfo;
var MtransformPageProperty = (function () {
    function MtransformPageProperty() {
        this.formNames = [];
    }
    return MtransformPageProperty;
}());
exports.MtransformPageProperty = MtransformPageProperty;
var MtransformCurrentParse = (function () {
    function MtransformCurrentParse() {
        /**
         * 过程中的form名称
         *
         * @type {string}
         * @memberOf MtransformCurrentParse
         */
        this.formName = '';
        /**
         * 过程中的文本内容
         *
         * @type {string[]}
         * @memberOf MtransformCurrentParse
         */
        this.textContents = [];
        /**
         * 过程中的元素数据  用于判断当前元素 将元素push或者pop来判断出当前元素的内容
         *
         * @type {ItransformItemInfo[]}
         * @memberOf MtransformCurrentParse
         */
        this.elmArrays = [];
        this.elmUqiue = new Map();
        /**
         * 模板的名称
         *
         * @type {string}
         * @memberOf MtransformCurrentParse
         */
        this.templateName = '';
    }
    return MtransformCurrentParse;
}());
exports.MtransformCurrentParse = MtransformCurrentParse;
var MtransformPageScript = (function () {
    function MtransformPageScript() {
        this.scriptType = '';
        this.scriptContent = '';
    }
    return MtransformPageScript;
}());
exports.MtransformPageScript = MtransformPageScript;
var MtransformPageOut = (function () {
    function MtransformPageOut() {
        this.content = [];
        this.templateInfos = [];
        /**
         * 页面直接输出的脚本内容
         */
        this.scriptInfos = [];
        /**
         * 页面加载完成时执行的脚本内容
         */
        this.scriptReady = [];
    }
    return MtransformPageOut;
}());
exports.MtransformPageOut = MtransformPageOut;
var MbasePageConfig = (function () {
    function MbasePageConfig() {
        this.pageTitle = "";
        this.masterPath = "";
        this.tplFile = "tpl/default.ejs";
        this.styleFile = "./index";
        this.asyncShow = "";
        this.scriptInit = "";
        this.scriptFiles = [];
    }
    return MbasePageConfig;
}());
exports.MbasePageConfig = MbasePageConfig;
/**
 * 转换帮助类
 */
var MtransformParseHelper = (function () {
    function MtransformParseHelper() {
    }
    /**
     * 获取基本元素的转换处理定义
     */
    MtransformParseHelper.prototype.upBaseElm = function (oTransform, oItem) {
        if (oItem.sourceName in oTransform.mould.elms) {
            var tp = oTransform.mould.elms[oItem.sourceName];
            return tp;
        }
        else {
            return null;
        }
    };
    return MtransformParseHelper;
}());
exports.MtransformParseHelper = MtransformParseHelper;
