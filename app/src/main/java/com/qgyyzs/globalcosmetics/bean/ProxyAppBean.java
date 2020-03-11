package com.qgyyzs.globalcosmetics.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/26.
 */

public class ProxyAppBean {

    private int Result;
    private String Msg;
    private List<JsonData> jsonData;

    public void setResult(int Result) {
        this.Result = Result;
    }

    public int getResult() {
        return Result;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public String getMsg() {
        return Msg;
    }

    public void setJsonData(List<JsonData> jsonData) {
        this.jsonData = jsonData;
    }

    public List<JsonData> getJsonData() {
        return jsonData;
    }

    public class JsonData {

        private List<_hqyy_caigou_bid> _hqyy_caigou_bid;

        public String getHeadImg() {
            return HeadImg;
        }

        public void setHeadImg(String headImg) {
            HeadImg = headImg;
        }

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public String getProid() {
            return proid;
        }

        public void setProid(String proid) {
            this.proid = proid;
        }

        public String getdType() {
            return dType;
        }

        public void setdType(String dType) {
            this.dType = dType;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public boolean isdel() {
            return isdel;
        }

        public void setIsdel(boolean isdel) {
            this.isdel = isdel;
        }

        public String getLinkTel() {
            return LinkTel;
        }

        public void setLinkTel(String linkTel) {
            LinkTel = linkTel;
        }

        public String getNickName() {
            return NickName;
        }

        public void setNickName(String nickName) {
            NickName = nickName;
        }

        public int getUserId() {
            return UserId;
        }

        public void setUserId(int userId) {
            UserId = userId;
        }

        public String getNimID() {
            return NimID;
        }

        public void setNimID(String nimID) {
            NimID = nimID;
        }

        public String getRealName() {
            return RealName;
        }

        public void setRealName(String realName) {
            RealName = realName;
        }

        public boolean isOpen() {
            return IsOpen;
        }

        public void setOpen(boolean open) {
            IsOpen = open;
        }

        private String HeadImg;

        private int Id;

        private String proid;

        private String dType;

        private String content;

        private String area;

        private String addtime;

        private int state;

        private boolean isdel;

        private String LinkTel;

        private String NickName;

        private int UserId;

        private String NimID;

        private String RealName;

        private boolean IsOpen;

        private String Title;

        public String getTitle() {
            return Title;
        }

        public void setTitle(String title) {
            Title = title;
        }

        public void set_hqyy_caigou_bid(List<_hqyy_caigou_bid> _hqyy_caigou_bid) {
            this._hqyy_caigou_bid = _hqyy_caigou_bid;
        }

        public List<_hqyy_caigou_bid> get_hqyy_caigou_bid() {
            return _hqyy_caigou_bid;
        }
        public class _hqyy_caigou_bid {

            private int id;
            private int CaiGouId;
            private String username;
            private String content;
            private String addtime;
            public void setId(int id) {
                this.id = id;
            }
            public int getId() {
                return id;
            }

            public void setCaiGouId(int CaiGouId) {
                this.CaiGouId = CaiGouId;
            }
            public int getCaiGouId() {
                return CaiGouId;
            }

            public void setUsername(String username) {
                this.username = username;
            }
            public String getUsername() {
                return username;
            }

            public void setContent(String content) {
                this.content = content;
            }
            public String getContent() {
                return content;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }
            public String getAddtime() {
                return addtime;
            }

        }
    }
}
