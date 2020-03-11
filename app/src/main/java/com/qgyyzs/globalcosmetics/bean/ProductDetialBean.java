package com.qgyyzs.globalcosmetics.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/18 0018.
 */

public class ProductDetialBean {
    private int Result;

    private String Msg;

    private JsonData jsonData;

    public void setResult(int Result){
        this.Result = Result;
    }
    public int getResult(){
        return this.Result;
    }
    public void setMsg(String Msg){
        this.Msg = Msg;
    }
    public String getMsg(){
        return this.Msg;
    }
    public void setJsonData(JsonData jsonData){
        this.jsonData = jsonData;
    }
    public JsonData getJsonData(){
        return this.jsonData;
    }
    public class JsonData {
        private List<ProAreaList> ProAreaList ;

        private int id;

        private String PcUsername;

        private String Subject;

        private String Tags;

        private int SubjectId;

        private int ClassID;

        private String ClassName;

        private int nClassID;

        private String nClassName;

        private String SaleType;

        private String danwei;

        private String dianhua;

        private String lianxiren;

        private String image;

        private String x_gg;

        private String ZsArea;

        private String YongTu;

        private String ppai_name;

        private String Qudao;

        private String zc;

        private int IsJinKou;

        private String ProducingArea;

        private int ShenHe;

        private int newhit;

        private int shua;

        private String time;

        private String fbtime;

        private String Content;

        private int flag;

        public void setId(int id){
            this.id = id;
        }
        public int getId(){
            return this.id;
        }
        public void setPcUsername(String PcUsername){
            this.PcUsername = PcUsername;
        }
        public String getPcUsername(){
            return this.PcUsername;
        }
        public void setSubject(String Subject){
            this.Subject = Subject;
        }
        public String getSubject(){
            return this.Subject;
        }
        public void setTags(String Tags){
            this.Tags = Tags;
        }
        public String getTags(){
            return this.Tags;
        }
        public void setSubjectId(int SubjectId){
            this.SubjectId = SubjectId;
        }
        public int getSubjectId(){
            return this.SubjectId;
        }
        public void setClassID(int ClassID){
            this.ClassID = ClassID;
        }
        public int getClassID(){
            return this.ClassID;
        }
        public void setClassName(String ClassName){
            this.ClassName = ClassName;
        }
        public String getClassName(){
            return this.ClassName;
        }
        public void setNClassID(int nClassID){
            this.nClassID = nClassID;
        }
        public int getNClassID(){
            return this.nClassID;
        }
        public void setNClassName(String nClassName){
            this.nClassName = nClassName;
        }
        public String getNClassName(){
            return this.nClassName;
        }
        public void setSaleType(String SaleType){
            this.SaleType = SaleType;
        }
        public String getSaleType(){
            return this.SaleType;
        }
        public void setDanwei(String danwei){
            this.danwei = danwei;
        }
        public String getDanwei(){
            return this.danwei;
        }
        public void setDianhua(String dianhua){
            this.dianhua = dianhua;
        }
        public String getDianhua(){
            return this.dianhua;
        }
        public void setLianxiren(String lianxiren){
            this.lianxiren = lianxiren;
        }
        public String getLianxiren(){
            return this.lianxiren;
        }
        public void setImage(String image){
            this.image = image;
        }
        public String getImage(){
            return this.image;
        }
        public void setX_gg(String x_gg){
            this.x_gg = x_gg;
        }
        public String getX_gg(){
            return this.x_gg;
        }
        public void setZsArea(String ZsArea){
            this.ZsArea = ZsArea;
        }
        public String getZsArea(){
            return this.ZsArea;
        }
        public void setYongTu(String YongTu){
            this.YongTu = YongTu;
        }
        public String getYongTu(){
            return this.YongTu;
        }
        public void setPpai_name(String ppai_name){
            this.ppai_name = ppai_name;
        }
        public String getPpai_name(){
            return this.ppai_name;
        }
        public void setQudao(String Qudao){
            this.Qudao = Qudao;
        }
        public String getQudao(){
            return this.Qudao;
        }
        public void setZc(String zc){
            this.zc = zc;
        }
        public String getZc(){
            return this.zc;
        }
        public void setIsJinKou(int IsJinKou){
            this.IsJinKou = IsJinKou;
        }
        public int getIsJinKou(){
            return this.IsJinKou;
        }
        public void setProducingArea(String ProducingArea){
            this.ProducingArea = ProducingArea;
        }
        public String getProducingArea(){
            return this.ProducingArea;
        }
        public void setShenHe(int ShenHe){
            this.ShenHe = ShenHe;
        }
        public int getShenHe(){
            return this.ShenHe;
        }
        public void setNewhit(int newhit){
            this.newhit = newhit;
        }
        public int getNewhit(){
            return this.newhit;
        }
        public void setShua(int shua){
            this.shua = shua;
        }
        public int getShua(){
            return this.shua;
        }
        public void setTime(String time){
            this.time = time;
        }
        public String getTime(){
            return this.time;
        }
        public void setFbtime(String fbtime){
            this.fbtime = fbtime;
        }
        public String getFbtime(){
            return this.fbtime;
        }
        public void setContent(String Content){
            this.Content = Content;
        }
        public String getContent(){
            return this.Content;
        }
        public void setFlag(int flag){
            this.flag = flag;
        }
        public int getFlag(){
            return this.flag;
        }

        public void setProAreaList(List<ProAreaList> ProAreaList){
            this.ProAreaList = ProAreaList;
        }
        public List<ProAreaList> getProAreaList(){
            return this.ProAreaList;
        }

        public class ProAreaList {
            private int Id;

            private int keyid;

            private String province;

            private String city;

            private double jiage;

            private int FieldType;

            private int UserId;

            public void setId(int Id){
                this.Id = Id;
            }
            public int getId(){
                return this.Id;
            }
            public void setKeyid(int keyid){
                this.keyid = keyid;
            }
            public int getKeyid(){
                return this.keyid;
            }
            public void setProvince(String province){
                this.province = province;
            }
            public String getProvince(){
                return this.province;
            }
            public void setCity(String city){
                this.city = city;
            }
            public String getCity(){
                return this.city;
            }
            public void setJiage(double jiage){
                this.jiage = jiage;
            }
            public double getJiage(){
                return this.jiage;
            }
            public void setFieldType(int FieldType){
                this.FieldType = FieldType;
            }
            public int getFieldType(){
                return this.FieldType;
            }
            public void setUserId(int UserId){
                this.UserId = UserId;
            }
            public int getUserId(){
                return this.UserId;
            }

        }

    }

}
