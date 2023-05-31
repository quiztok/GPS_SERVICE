package com.quiztok.gps.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiztok.gps.client.Impl.GpsApiMapper;
import com.quiztok.gps.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.*;

@Service
public class GpsServiceImpl implements  GpsService {

    @Autowired
    RestService restService;

    @Autowired
    GpsApiMapper gpsApiMapper;


    @Value("${point.term}")
    private int POINT_TERM;

    @Value("${sso.url}")
     private String SSO_URL;


    @Value("${point.url}")
    private String POINT_URL;


    private int[] bonusMember = {3,3,3,3,3,3,2,1};

    private String[] setBonusMember(int memCount){

        String[] bonusMemberName = new String[0];

        int memcount = bonusMember[memCount];
        System.out.println(memcount);
        switch(memcount) {
            case 3:
                bonusMemberName = new String[]{"bonus", "surprise", "quiztok"};
                break;
            case 2:
                bonusMemberName = new String[]{"bonus", "surprise"};
                break;
            case 1:
                bonusMemberName = new String[]{"bonus"};
                break;
        }

        return bonusMemberName;
    }

    public Map<String , Object> getGpsFindMember(String authorization , UserVo userGps){

        Map nResult = new HashMap();

        try {
            String url = SSO_URL+"/users";
            HashMap<String, String> headerInfo = new HashMap<String, String>();
            MultiValueMap params = new LinkedMultiValueMap<>();
            HashMap<String, Object> getUserDataMap = new HashMap<String, Object>();
            headerInfo.put("Authorization", authorization);
            ResponseEntity userInfo =  restService.getRestGetHeaderService(url , params, headerInfo);
            Map<String , Object> userData = (Map<String, Object>) userInfo.getBody();


            UserVo userVo = new UserVo();
            userVo.setId(userData.get("id").toString());
            if(!userData.get("id").toString().isBlank()){
                boolean renewal = true;
                int userCntLog = gpsApiMapper.getCntGSpotTermTime(userVo);
                if(userCntLog>0){

                    SpotTermCheckVo getPointTerm = gpsApiMapper.getGSpotTermTime(userVo);
                    System.out.println("getPointTerm:"+ getPointTerm);
                    if(getPointTerm.getTime()==null && getPointTerm.getCnt()<=0){ // 최초 값이 없어서 null  인 경우

                        renewal=true;
                        nResult.put("waitMinute", 0);
                    } else if (getPointTerm.getTime()==null && getPointTerm.getCnt()>0){ // 값은 있으나 null 인 경우
                        renewal=false;
                        nResult.put("waitMinute", 0);
                        System.out.println("f 1");
                    } else {
                        if(Integer.parseInt(getPointTerm.getTime()) >=POINT_TERM) {
                            renewal=true;
                            nResult.put("waitMinute", 0);
                        } else {
                            renewal=false;
                            nResult.put("waitMinute", (POINT_TERM-Integer.parseInt(getPointTerm.getTime())));
                            System.out.println("f 2");
                        }
                    }
                }
                System.out.println("renewal:"+ renewal);
                UserVo userDetailInfo = gpsApiMapper.getUserDetailInfo(userVo);
               // nResult.put("totalPoint", Integer.parseInt( userDetailInfo.getPoint()));
                nResult.put("profileImgPath", "https://quiztok.s3.ap-northeast-2.amazonaws.com/"+userDetailInfo.getImgKey());
                userVo.setLatitude(userGps.getLatitude());
                userVo.setLongitude(userGps.getLongitude());


                CountqSpotVo spotCount = gpsApiMapper.getCountqSpot(userVo);
                ArrayList locationPoints = new ArrayList();

                nResult.put("qSpotId", spotCount.getIdx());
                System.out.println("cnt:"+ spotCount.getCount());

                if(renewal==false){ // 저장된 인근 사용자 조회

                    CountqSpotVo countqSpotVo = gpsApiMapper.getCountqSpot(userVo);

                    List<QSpotHistoryVo> qSpotHistory =  gpsApiMapper.getQSpotMember(countqSpotVo);

                    for(QSpotHistoryVo qSpotHistoryVo : qSpotHistory){
                        Map addData = new HashMap();
                        addData.put("id", qSpotHistoryVo.getNum());
                        addData.put("lat", qSpotHistoryVo.getLat());
                        addData.put("lng", qSpotHistoryVo.getLng());
                        addData.put("nickName",qSpotHistoryVo.getNickName() );
                        addData.put("point", qSpotHistoryVo.getPoint());
                        addData.put("type", qSpotHistoryVo.getType());
                        //addData.put("id", qSpotHistoryVo.getUserId());
                        addData.put("distance", qSpotHistoryVo.getDistance());
                        addData.put("isReceived", qSpotHistoryVo.getReceived());
                        locationPoints.add(addData);
                    }
                } else { // 인근 사용자 호출 및 저장
                    ArrayList findMember = gpsApiMapper.getGpsFindQuizTokMember(userVo);
                    System.out.println("cnt2:"+findMember.size());
                    if(findMember.size()<8){
                        addFindMember(findMember);
                    }
                    System.out.println("cnt3:"+findMember.size());
                    System.out.println(findMember);
                    QSpotVo qSpotVo  = new QSpotVo();
                    qSpotVo.setUserId(userVo.getId());
                    qSpotVo.setSpotCnt(findMember.size());
                    qSpotVo.setSpotStatus("N");
                    gpsApiMapper.saveQSpot(qSpotVo);
                    System.out.println("qSpotId:"+ qSpotVo.getIdx());
                    nResult.put("qSpotId", qSpotVo.getIdx());
                    ObjectMapper objectMapper = new ObjectMapper();
                    Collections.shuffle(findMember);
                    int i = 1;
                    for(Object obj : findMember){
                        GpsFindMemberVo gpsFindMemberVo = objectMapper.convertValue(obj, GpsFindMemberVo.class);
                        QSpotHistoryVo qSpotHistoryVo = new QSpotHistoryVo();
                        qSpotHistoryVo.setqSpotIdx(qSpotVo.getIdx());
                        qSpotHistoryVo.setNum(i);
                        qSpotHistoryVo.setLat(gpsFindMemberVo.getLat());
                        qSpotHistoryVo.setLng(gpsFindMemberVo.getLng());
                        qSpotHistoryVo.setNickName(gpsFindMemberVo.getNickName());
                        qSpotHistoryVo.setPoint(gpsFindMemberVo.getPoint());
                        qSpotHistoryVo.setType(gpsFindMemberVo.getType());
                        qSpotHistoryVo.setUserId(gpsFindMemberVo.getId());
                        qSpotHistoryVo.setDistance(gpsFindMemberVo.getDistance());
                        qSpotHistoryVo.setReceived(false);
                        gpsApiMapper.saveQSpotHistory(qSpotHistoryVo);

                        Map addData = new HashMap();
                        addData.put("id", i);
                        addData.put("lat", qSpotHistoryVo.getLat());
                        addData.put("lng", qSpotHistoryVo.getLng());
                        addData.put("nickName",qSpotHistoryVo.getNickName() );
                        addData.put("point", qSpotHistoryVo.getPoint());
                        addData.put("type", qSpotHistoryVo.getType());
                        //addData.put("id", qSpotHistoryVo.getUserId());
                        addData.put("distance", qSpotHistoryVo.getDistance());
                        addData.put("isReceived", qSpotHistoryVo.getReceived());
                        locationPoints.add(addData);
                        i++;
                    }
                }
                GivePointVo givePointVo = new GivePointVo();
                givePointVo.setqSpotId(Integer.parseInt(nResult.get("qSpotId").toString()));
                //int accumulatedPoint =  gpsApiMapper.getAccumulatedPoint(givePointVo);
                int accumulatedPoint =  gpsApiMapper.getQSpotUserTotalPoint(userData.get("id").toString());
                int givePoint = accumulatedPoint;

                nResult.put("totalPoint", givePoint);
                //nResult.put("accumulatedPoint",givePoint);
                nResult.put("locationPoints", locationPoints);
                //nResult.put("code",200);
                //nResult.put("msg", "조회 성공");
            } else {
                //nResult.put("code",400);
                //nResult.put("mag", "회원 정보 호출 오류");
            }
        } catch (Exception e){
           // nResult.put("code", 500);
           // nResult.put("msg", e.getMessage());

        }

        return  nResult;
    }

    private List<Map<String, Object>> addFindMember(ArrayList findMember){
        System.out.println("findMember Size:"+ findMember.size());


        String[] typeNamesArr = setBonusMember(findMember.size());
        for (int i = 0; i < typeNamesArr.length; i++) {
            Map addData = new HashMap();
            addData.put("lat", 0.0);
            addData.put("lng", 0.0);
            addData.put("nickName","큐몽 "+randomFigure() );
            addData.put("point", randomFigure());
            addData.put("type", typeNamesArr[i]);
            addData.put("distance", "");
            addData.put("received", false);
            findMember.add(addData);
        }
        return findMember;
    }



    private int randomFigure(){
        double min = 150;
        double max = 1000;
        int random = (int) ((Math.random() * (max - min)) + min);
        return random;
    }


    public  Map<String , Object> saveUserPosition(String authorization , UserVo userVo){
        Map nResult = new HashMap();

        try {
            Map<String , Object> userData = getUserData(authorization);
            if(userData.get("id").toString().isBlank()){
                nResult.put("code",400);
                nResult.put("mag", "회원 정보 호출 오류");

            } else {
                nResult.put("code",200);
                nResult.put("msg", "업데이트 성공");

                userVo.setId(userData.get("id").toString());
                gpsApiMapper.updateUserGpsData(userVo);
            }
        } catch(Exception e){
            nResult.put("code", 500);
            nResult.put("msg", "서버 오류 발생");
        }

        return nResult;
    }


    public Map<String, Object> userGivePoint(String authorization ,GivePointVo givePointVo ){

        Map nResult = new HashMap();
        try {

            Map<String , Object> userData = getUserData(authorization);
            if(userData.get("id").toString().isBlank()){
                nResult.put("code",400);
                nResult.put("mag", "회원 정보 호출 오류");

            } else {
                String uidx = userData.get("id").toString();
                QSpotHistoryVo qSpotHistoryVo = gpsApiMapper.getQSpotHistoryPoint(givePointVo);

                if(qSpotHistoryVo.getReceived().equals(true)){
                    nResult.put("code",202);
                    nResult.put("msg", "이미 포인트 지급 완료된 항목");
                    return nResult;
                }

                String url = POINT_URL+"/point";
                JSONObject params = new JSONObject();
                params.put("targetId", uidx);
                params.put("requestPoint", qSpotHistoryVo.getPoint());
                params.put("pointType", "EVENT");

                String res = restService.getRestPostJsonService(url ,params );
                if (res.equals("0.0")){
                    nResult.put("givePoint", qSpotHistoryVo.getPoint());
                    System.out.println(givePointVo);
                    gpsApiMapper.updateSpotHistory(givePointVo);
                    int cnt = gpsApiMapper.getCntSpotHistory(givePointVo);
                    int totalCnt =  gpsApiMapper.getTotalCntSpotHistory(givePointVo);
                    System.out.println(cnt+"//"+totalCnt);
                    if(cnt==totalCnt){
                        int accumulatedPoint =  gpsApiMapper.getAccumulatedPoint(givePointVo);
                        int givePoint = accumulatedPoint;

                        givePointVo.setPoint(givePoint); // 누적 포인트 설정
                        gpsApiMapper.updateQSpot(givePointVo);
                    } // 모두 풀었을때
                    nResult.put("code",200);
                    nResult.put("msg", "포인트 적립 성공");
                }
            }
        } catch(Exception e){
            nResult.put("code", 500);
            nResult.put("msg", "서버 오류 발생");
        }

        return nResult;
    }


    public Map<String, Object> getSpotHistory(String authorization){
        Map nResult = new HashMap();

        try{
            Map<String , Object> userData = getUserData(authorization);
            if(userData.get("id").toString().isBlank()){
                nResult.put("code",400);
                nResult.put("mag", "회원 정보 호출 오류");

            } else {
                String uidx = userData.get("id").toString();

                int totalPoint = gpsApiMapper.getQSpotUserTotalPoint(uidx);
                List<QSpotPointUserHistoryVo>  spotHistoryData = gpsApiMapper.getQSpotUserData(uidx);
                nResult.put("totalPoint", totalPoint);
                nResult.put("qSpotPointHistory", spotHistoryData);
            }
        } catch (Exception e){
            nResult.put("code", 500);
            nResult.put("msg", "서버 오류 발생");
        }

        return  nResult;
    }

    private Map<String , Object> getUserData(String authorization){
        String url = SSO_URL+"/users";
        HashMap<String, String> headerInfo = new HashMap<String, String>();
        MultiValueMap params = new LinkedMultiValueMap<>();
        HashMap<String, Object> getUserDataMap = new HashMap<String, Object>();
        headerInfo.put("Authorization", authorization);
        ResponseEntity userInfo =  restService.getRestGetHeaderService(url , params, headerInfo);
        Map<String , Object> userData = (Map<String, Object>) userInfo.getBody();
        return userData;
    }

    public void test(int n){

        System.out.println();


    }

}
