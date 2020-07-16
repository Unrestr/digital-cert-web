package com.greatmap.digital.base;

/**
 * 常量定义接口
 * @author gaorui
 * @create 2020-06-29 15:05
 */
public interface DcConstants {

    /**
     * 请求接受字段常量
     */
    class RequestParam{

        /**
         * 权利人集合
         */
        public static final String QLR_LIST = "qlrList";

        /**
         * 权利信息
         */
        public static final String QLXX = "qlxx";

        /**
         * 单元信息集合
         */
        public static final String DYXX_LIST = "dyxxList";

        /**
         * 证照基础信息
         */
        public static final String JCXX = "jcxx";

        /**
         * 证书打印信息
         */
        public static final String ZSDYXX = "zsDyxx";

        /**
         * 证明打印信息
         */
        public static final String ZMDYXX = "zmDyxx";
    }

    /**
     * CA文件传值信息
     */
    class CaFile{

        /**
         * 权利人集合
         */
        public static final String SEAL_FILE = "sealFile";

        /**
         * 权利信息
         */
        public static final String CERT_FILE = "certFile";

    }

    /**
     * CA文件传值信息
     */
    class CaStatus{

        public static final String RESULT_CODE = "errorCode";

        public static final String RESULT_MSG = "errorMsg";

        public static final String RESULT_CODE_SUCCESS = "ESS00000000";

        public static final String RESULT_CODE_SUCCESS_1 = "ESS0000010";

        public static final String RESULT_CODE_SUCCESS_2 = "操作成功!";

        public static final String RESULT_CODE_SUCCESS_3 = "ESS0000000";

        public static final String RESULT_DATA = "data";

    }

    /**
     * CA文件传值信息
     */
    class CaHttp{

        public static final String HTTP = "";
        /**
         * 生成P10
         */
        public static final String URL_CREATE_P10 = HTTP + "createP10";
        /**
         * 获取签章列表
         */
        public static final String URL_GET_SEAL_LIST = HTTP + "getSealList";
        /**
         * 获取签章详情
         */
        public static final String URL_GET_SEAL_DETAIL = HTTP + "getSealById";
        /**
         * 上传数据证书
         */
        public static final String URL_UPLOAD_CERT = HTTP + "uploadCert";
        /**
         * 上传数字签章
         */
        public static final String URL_UPLOAD_SEAL = HTTP + "uploadSeal";
        /**
         * 更新签章状态
         */
        public static final String URL_EDIT_SEALSTATUS = HTTP + "editSealStatus";
        /**
         * 添加签章模板
         */
        public static final String URL_ADD_SEAL_TEMPLATE = HTTP + "addSealTemplate";
        /**
         * 获取签章模板规则列表
         */
        public static final String URL_GET_SEAL_TEMPLATE_LIST = HTTP + "getTemplateList";

        /**
         * 获取签章模板规则详情
         */
        public static final String URL_GET_SEAL_TEMPLATE = HTTP + "getTemplateById";
        /**
         * 更新签章模板
         */
        public static final String URL_UPDATE_SEAL_TEMPLATE = HTTP + "updateTemplateById";
        /**
         * 注销证书模板
         */
        public static final String URL_OFF_SEAL_TEMPLATE = HTTP + "logOffTemplate";

        /**
         * 验证证照文件
         */
        public static final String URL_VERIFY_CERT = HTTP + "verifyPDF";
        /**
         * 添加签章
         */
        public static final String URL_SIGN_PDF = HTTP + "signPDFByTemplate";
        /**
         * 添加签章
         */
        public static final String URL_SIGN_PDF_EX = HTTP + "signPDFByTemplateAndImage";
        /**
         * 生成图片
         */
        public static final String CREATE_IMAGE_BASE64 = HTTP + "createImageBase64";

    }

    /**
     * 证照状态
     */
    class DcStatus{

        /**
         * 历史
         */
        public static final String HISTORY = "0";

        /**
         * 现势
         */
        public static final String VALID = "1";

        /**
         * 注销
         */
        public static final String CANCEL = "2";

    }

    /**
     * 证照类型
     */
    class DcType{

        /**
         * 证书
         */
        public static final String ZS = "1";

        /**
         * 证明
         */
        public static final String ZM = "2";


    }

}
