"use strict";
var CommonRoot = require("../../base/common/root");
var MprocessItem = (function () {
    function MprocessItem() {
    }
    MprocessItem.prototype.upPropValue = function (oItem, sPropName) {
        return this.zeroUpPropValue(oItem, sPropName, CommonRoot.upProperty().dataAttrProp);
    };
    MprocessItem.prototype.upEventValue = function (oItem, sPropName) {
        return this.zeroUpPropValue(oItem, sPropName, CommonRoot.upProperty().dataAttrEvent);
    };
    MprocessItem.prototype.upXaryValue = function (oItem, sPropName) {
        return this.zeroUpPropValue(oItem, sPropName, CommonRoot.upProperty().dataAttrXary);
    };
    MprocessItem.prototype.zeroUpPropValue = function (oItem, sPropName, sAttr) {
        return oItem.sourceAttr.get(sAttr + sPropName);
    };
    /**
     * 使用引号修改属性
     */
    MprocessItem.prototype.checkPropWithQuotes = function (oItem, sSource, sTarget) {
        this.checkPropFull(oItem, sSource, sTarget, "", "", "\"");
    };
    MprocessItem.prototype.checkPropWithBrace = function (oItem, sSource, sTarget) {
        this.checkPropFull(oItem, sSource, sTarget, "{", "}", "");
    };
    /**
     * 直接属性  不增加标记
     */
    MprocessItem.prototype.checkPropWithEmpty = function (oItem, sSource, sTarget) {
        this.checkPropFull(oItem, sSource, sTarget, "", "", "");
    };
    MprocessItem.prototype.checkPropFull = function (oItem, sSource, sTarget, sLeft, sRight, sSign) {
        this.zeroFieldCheck(oItem, sSource, sTarget, CommonRoot.upProperty().dataAttrProp, sLeft, sRight, sSign);
    };
    MprocessItem.prototype.checkXaryFull = function (oItem, sSource, sTarget, sLeft, sRight, sSign) {
        this.zeroFieldCheck(oItem, sSource, sTarget, CommonRoot.upProperty().dataAttrXary, sLeft, sRight, sSign);
    };
    MprocessItem.prototype.checkEventFull = function (oItem, sSource, sTarget, sLeft, sRight, sSign) {
        this.zeroFieldCheck(oItem, sSource, sTarget, CommonRoot.upProperty().dataAttrEvent, sLeft, sRight, sSign);
    };
    MprocessItem.prototype.checkStateFull = function (oItem, sSource, sTarget, sLeft, sRight, sSign) {
        this.zeroFieldCheck(oItem, sSource, sTarget, CommonRoot.upProperty().dataAttrState, sLeft, sRight, sSign);
    };
    MprocessItem.prototype.checkStyle = function (oItem, sSource, sTarget) {
        this.zeroFieldCheck(oItem, sSource, sTarget, CommonRoot.upProperty().dataAttrStyle, "{styles.", "}", "");
    };
    /**
     * 直接属性  不增加标记
     */
    MprocessItem.prototype.zeroFieldCheck = function (oItem, sSource, sTarget, sAttr, sLeft, sRight, sSign) {
        if (oItem.sourceAttr.has(sAttr + sSource)) {
            var sVal = oItem.sourceAttr.get(sAttr + sSource);
            if (!sVal.startsWith('@')) {
                sVal = sSign + sVal + sSign;
            }
            else {
                sVal = sVal.substr(1);
            }
            oItem.targetAttr.set(sTarget, sLeft + sVal + sRight);
        }
    };
    MprocessItem.prototype.formBaseAuto = function (oItem) {
        //数据列表源编号
        this.checkPropWithQuotes(oItem, "form-source-items", "formSourceItems");
        //默认值
        this.checkPropWithQuotes(oItem, "form-default-value", "formDefaultValue");
        //最小长度
        this.checkPropWithQuotes(oItem, "form-min-size", "formMinSize");
        //最大长度
        this.checkPropWithQuotes(oItem, "form-max-size", "formMaxSize");
        //正则表达式编号
        this.checkPropWithQuotes(oItem, "form-regex-code", "formRegexCode");
        //展示类型
        this.checkPropWithQuotes(oItem, "form-show-type", "formShowType");
        //扩展显示字符串
        this.checkPropWithQuotes(oItem, "form-extend-display", "formExtendDisplay");
        //扩展查询字符串
        this.checkPropWithQuotes(oItem, "form-extend-query", "formExtendQuery");
    };
    MprocessItem.prototype.styleBaseAuto = function (oItem) {
        this.checkStyle(oItem, "item-touch", "styleItemTouch");
        this.checkStyle(oItem, "item-box", "styleItemBox");
        this.checkStyle(oItem, "item-text", "styleItemText");
        this.checkStyle(oItem, "item-icon", "styleItemIcon");
        this.checkStyle(oItem, "item-active", "styleItemActive");
        this.checkStyle(oItem, 'main-touch', 'styleMainTouch');
        this.checkStyle(oItem, 'main-view', 'styleMainView');
        this.checkStyle(oItem, "main-icon", "styleMainIcon");
        this.checkStyle(oItem, "main-text", "styleMainText");
    };
    /**
     * 基本属性检测
     *
     * @param {CTF.ItransformItemInfo} oItem
     *
     * @memberof MprocessItem
     */
    MprocessItem.prototype.propertyBaseAuto = function (oItem) {
        this.checkPropWithQuotes(oItem, "name", "pName");
        this.checkPropWithQuotes(oItem, "color", "pColor");
        this.checkPropWithQuotes(oItem, "show", "pShow");
        this.checkPropWithQuotes(oItem, "family", "pFamily");
        this.checkPropWithQuotes(oItem, "text", "pText");
        this.checkPropWithQuotes(oItem, "subscribe", "pSubscribe");
    };
    MprocessItem.prototype.propertyEventAuto = function (oItem) {
        this.checkEventFull(oItem, "press", "onPress", "{(event)=>{", "}}", "");
        this.checkEventFull(oItem, "link", "onPress", "{(event)=>{top_support.pageNav(", ",this)}}", "'");
        this.checkEventFull(oItem, "value-change", "onValueChange", "{(item)=>{", "}}", "");
    };
    MprocessItem.prototype.VueEventAuto = function (oItem) {
        this.checkEventFull(oItem, "press", "onClick", "", "", "");
        this.checkEventFull(oItem, "link", CommonRoot.upProperty().vueBind + "href", "'javascript:top_support.pageNav(\\''+", "+'\\',this)'", "'");
        this.checkEventFull(oItem, "value-change", "onValueChange", "{(item)=>{", "}}", "");
    };
    MprocessItem.prototype.VueFormAuto = function (oItem) {
        if (oItem.targetAttr.has(CommonRoot.upProperty().formBaseAttr)) {
            oItem.targetAttr.set("v-model", 'formdata_' + oItem.targetAttr.get(CommonRoot.upProperty().formBaseAttr).replace(CommonRoot.upProperty().formNameSplit, "."));
        }
        if (oItem.sourceAttr.has(CommonRoot.upProperty().formBaseAttr)) {
            oItem.formField.fieldName = oItem.sourceAttr.get(CommonRoot.upProperty().formBaseAttr);
            oItem.formField.fieldType = oItem.sourceName;
        }
    };
    MprocessItem.prototype.VuePropFormat = function (sVal) {
        return sVal.replace('@', '').replace(/\\{/, '').replace(/\\}/, '');
    };
    return MprocessItem;
}());
module.exports = new MprocessItem();
