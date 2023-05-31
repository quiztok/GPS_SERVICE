package com.quiztok.gps.vo;

public class QSpotVo {
    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getSpotCnt() {
        return spotCnt;
    }

    public void setSpotCnt(int spotCnt) {
        this.spotCnt = spotCnt;
    }

    public String getSpotStatus() {
        return spotStatus;
    }

    public void setSpotStatus(String spotStatus) {
        this.spotStatus = spotStatus;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }

    public int getGivePoint() {
        return givePoint;
    }

    public void setGivePoint(int givePoint) {
        this.givePoint = givePoint;
    }

    private int idx;
    private String userId;
    private int spotCnt;
    private String spotStatus;
    private String startTime;
    private String endTime;
    private String regDate;



    private int givePoint;
}
