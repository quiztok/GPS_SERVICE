package com.quiztok.gps.controller;

import com.quiztok.gps.service.GpsService;
import com.quiztok.gps.vo.GivePointVo;
import com.quiztok.gps.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/")
public class AppRestController {

    @Autowired
    GpsService gpsService;
    @PostMapping("/gps/findMember")
    public Map<String , Object> getGpsFindMember(@RequestHeader("Authorization") String authorization , @RequestBody UserVo userVo ){
        return  gpsService.getGpsFindMember(authorization , userVo);
    }

    @PostMapping("/user/updatePosition")
    public Map<String, Object> saveUserPosition(@RequestHeader("Authorization") String authorization, @RequestBody UserVo userVo){
      return  gpsService.saveUserPosition(authorization , userVo);
    }

    @PostMapping("/user/givePoint")
    public Map<String , Object> userGivePoint(@RequestHeader("Authorization") String authorization, @RequestBody GivePointVo givePointVo){
        return  gpsService.userGivePoint(authorization ,givePointVo );
    }


    @GetMapping("/user/getSpotHistory")
    public Map<String , Object> getSpotHistory(@RequestHeader("Authorization") String authorization){
        return gpsService.getSpotHistory(authorization);
    }


    @GetMapping("/test")
    public void  randomFigure(){

        System.out.println(gpsService.randomFigure());
    }

    @PostMapping("/gps/findMemberTest")
    public Map<String , Object> getGpsFindMemberTest(@RequestHeader("uidx") String uidx , @RequestBody UserVo userVo ){
        return  gpsService.getGpsFindMemberTest(uidx , userVo);
    }

}
