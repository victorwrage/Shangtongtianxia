package com.zdv.shangtongtianxia.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/24/024.
 */
public class HistoryBean implements Serializable {
    private String cost_type;
    private String cost_state;
    private String money;
    private String code;
    private String mcode;
    private String gift_amount;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMcode() {
        return mcode;
    }

    public void setMcode(String mcode) {
        this.mcode = mcode;
    }

    public String getGift_amount() {
        return gift_amount;
    }

    public void setGift_amount(String gift_amount) {
        this.gift_amount = gift_amount;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    private String company_id;
    private String operator;


    private String createtime;

    public String getCost_type() {
        return cost_type;
    }

    public void setCost_type(String cost_type) {
        this.cost_type = cost_type;
    }

    public String getCost_state() {
        return cost_state;
    }

    public void setCost_state(String cost_state) {
        this.cost_state = cost_state;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getOpacount_type() {
        return opacount_type;
    }

    public void setOpacount_type(String opacount_type) {
        this.opacount_type = opacount_type;
    }

    public String getOpacount_state() {
        return opacount_state;
    }

    public void setOpacount_state(String opacount_state) {
        this.opacount_state = opacount_state;
    }

    public String getOpacount_name() {
        return opacount_name;
    }

    public void setOpacount_name(String opacount_name) {
        this.opacount_name = opacount_name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    private String opacount_type;
    private String opacount_state;
    private String opacount_name;
    private String action;
    private List<OutputsBean> outputs;

    public List<OutputsBean> getOutputs() {
        return outputs;
    }

    public void setOutputs(List<OutputsBean> outputs) {
        this.outputs = outputs;
    }

    public static class OutputsBean {

        private String outputLabel;
        private OutputMultiBean outputMulti;
        private OutputValueBean outputValue;

        public String getOutputLabel() {
            return outputLabel;
        }

        public void setOutputLabel(String outputLabel) {
            this.outputLabel = outputLabel;
        }

        public OutputMultiBean getOutputMulti() {
            return outputMulti;
        }

        public void setOutputMulti(OutputMultiBean outputMulti) {
            this.outputMulti = outputMulti;
        }

        public OutputValueBean getOutputValue() {
            return outputValue;
        }

        public void setOutputValue(OutputValueBean outputValue) {
            this.outputValue = outputValue;
        }

        public static class OutputMultiBean {
        }

        public static class OutputValueBean {


            private int dataType;
            private String dataValue;

            public int getDataType() {
                return dataType;
            }

            public void setDataType(int dataType) {
                this.dataType = dataType;
            }

            public String getDataValue() {
                return dataValue;
            }

            public void setDataValue(String dataValue) {
                this.dataValue = dataValue;
            }
        }
    }
}