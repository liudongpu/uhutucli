

export class MprocessParseFile {

    parseType: string

    fileContent: string

    fileBasename: string

}


export class MtransformPageOut {
    content: string[] = []
    pageConfig: IbasePageConfig
    sourceFile: MprocessParseFile
}


/** 定义元素转义接口 */
export interface ItransformElmentProcess {
    upProcess(oTransform: ItransformParse, oItem: ItransformItemInfo): ItransformElmentInfo;
}



/**
 * 处理转换接口
 */
export interface ItransformParse {

    elms: ItransformElmentProcess
    /**
     * 预设定义
     */
    inc: {

        /**
         * 属性替换字符串标记
         */
        attr_replace: string
    }
    /**
     * 转换处理
     */
    parses: ItransformSubExtend
    /**
     * 所有的元素标记
     */
    mould: any

    pageConfig: IbasePageConfig

    /**
     * 最终返回前对oOut进行内容格式化替换操作
     */
    outFormat: ItransFormatOut

}

export interface ItransFormatOut {


    contentFormat(oOut: MtransformPageOut): MtransformPageOut

}



/**
 * 处理过程中的临时对象信息
 */

export interface ItransformItemInfo {

    /**
     * 定义元素类型   
     * 0:为未定义  
     * 1:为普通直接输出   
     * 2:为模板元素
     * 3:为json配置数据
     */
    elmType: number
    /**
     * 元素名称
     */
    elmName: string
    /**
     * 元素的信息
     */
    elmentInfo: ItransformElmentInfo
    /**
     * 源文件中的名称
     */
    sourceName: string
    /**
     * 源文件中的属性
     */
    sourceAttr: Map<string, string>

    /**
     * 源文件中的内容
     */
    sourceContent: string

    /**
     * 目标属性  经过转换后的属性配置
     */
    targetAttr: Map<string, string>
    /**
     * 子系统处理
     */
    transSub: ItransformSubExtend

}

/**
 * 子系统扩展处理接口
 */
export interface ItransformSubExtend {
    attrParse(oElm: ItransformItemInfo)

}


/**
 * 元素基本信息
 */
export interface ItransformElmentInfo {
    /**
     * 生成的对象的名称
     */
    tagName: string;
    /**
     * 元素的样式
     */
    styleName: string[];

    expandFile: string;


    primeAttr: Map<string, string>;
}


export interface ItransformExpandItem {
    expandOpen(oItem: ItransformItemInfo, oOut: MtransformPageOut): ItransformItemInfo
}


export interface IbasePageConfig {

    //页面标题
    pageTitle: string

    //模板文件夹路径
    masterPath: string

    //模板文件
    tplFile: string

    //页面初始化时加载的脚本
    scriptInit: string



}



export class MbasePageConfig implements IbasePageConfig {

    pageTitle = ""
    masterPath = ""
    tplFile = "tpl/default.ejs"
    scriptInit = ""
}



/**
 * 转换帮助类
 */
export class MtransformParseHelper {
    /**
     * 获取基本元素的转换处理定义
     */
    upBaseElm(oTransform: ItransformParse, oItem: ItransformItemInfo) {

        if (oItem.sourceName in oTransform.mould.elms) {

            let tp = oTransform.mould.elms[oItem.sourceName];

            return tp;
        }
        else {
            return null;
        }

    }

}