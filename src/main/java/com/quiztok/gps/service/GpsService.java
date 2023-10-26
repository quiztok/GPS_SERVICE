package com.quiztok.gps.service;

import com.quiztok.gps.vo.GivePointVo;
import com.quiztok.gps.vo.UserVo;

import java.math.BigDecimal;
import java.util.Map;

public interface GpsService {
    public Map<String , Object> getGpsFindMember(String authorization , UserVo userVo);
    public  Map<String , Object> saveUserPosition(String authorization ,  UserVo userVo);

    public Map<String, Object> userGivePoint(String authorization , GivePointVo givePointVo );

    public Map<String, Object> getSpotHistory(String authorization);

    public void test(int n);

    public  int randomFigure();
    public Map<String , Object> getGpsFindMemberTest(String uidx , UserVo userGps);



}
