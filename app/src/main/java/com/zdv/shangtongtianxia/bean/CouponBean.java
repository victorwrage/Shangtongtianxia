package com.zdv.shangtongtianxia.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/6/24/024.
 */
public class CouponBean implements Serializable {
    private  int icon;
    private  String name;
    private  String type;
    private  String price;
    private  int count;
    private  String discrib;
    private  String available;
    private  int cur_count;
    private  Boolean select;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCur_count() {
        return cur_count;
    }

    public void setCur_count(int cur_count) {
        this.cur_count = cur_count;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }


    public Boolean getSelect() {
        return select;
    }

    public void setSelect(Boolean select) {
        this.select = select;
    }




    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDiscrib() {
        return discrib;
    }

    public void setDiscrib(String discrib) {
        this.discrib = discrib;
    }

    public String getAvailable() {
        return available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }



    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private  String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



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