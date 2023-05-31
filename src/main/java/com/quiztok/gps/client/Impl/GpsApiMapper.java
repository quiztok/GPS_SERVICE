package com.quiztok.gps.client.Impl;

import com.quiztok.gps.vo.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper
public interface GpsApiMapper {

//    int getCountFindQuizTokMember();

    UserVo getUserDetailInfo(UserVo userVo);

    ArrayList<GpsFindMemberVo> getGpsFindQuizTokMember(UserVo userVo);

    CountqSpotVo getCountqSpot(UserVo userVo);

    int saveQSpot(QSpotVo qSpotVo);

    void saveQSpotHistory(QSpotHistoryVo qSpotHistoryVo);

    List<QSpotHistoryVo> getQSpotMember(CountqSpotVo countqSpotVo);

    int getCntGSpotTermTime(UserVo userVo);
    SpotTermCheckVo getGSpotTermTime(UserVo userVo);

    void updateUserGpsData(UserVo userVo);

    void updateSpotHistory(GivePointVo givePointVo);

    int getCntSpotHistory(GivePointVo givePointVo);

    int getAccumulatedPoint(GivePointVo givePointVo);

    void updateQSpot(GivePointVo givePointVo);

    QSpotHistoryVo getQSpotHistoryPoint(GivePointVo givePointVo);

    List<QSpotPointUserHistoryVo> getQSpotUserData(String userId);


    int getQSpotUserTotalPoint(String userId);

    int getTotalCntSpotHistory(GivePointVo givePointVo);
}
