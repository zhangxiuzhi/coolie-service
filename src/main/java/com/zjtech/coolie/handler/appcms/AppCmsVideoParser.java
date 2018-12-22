package com.zjtech.coolie.handler.appcms;

import com.zjtech.dto.cinema.VideoDto;
import com.zjtech.dto.cinema.VideoVendorDto;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class AppCmsVideoParser {

  public void parse(Map<String, String> data) {
    VideoDto videoDto = new VideoDto();

    videoDto.setName(data.get("vod_name"));//name
    videoDto.setLanguage(data.get("vod_en"));//语言
    videoDto.setActors(data.get("vod_actor"));//演员列表
    videoDto.setDirector(data.get("vod_director")); //导演
    videoDto.setRegion(data.get("vod_area")); //地区
    videoDto.setIntroduction(data.get("vod_content"));//详细描述
    videoDto.setYear(data.get("vod_year"));//上映年份

    //parse vod_play_from : 视频提供方代称
    //format: sohu$$$letv$$$mgtv$$$pptv, youku,
    String vendors = data.get("vod_play_from");
    List<VideoVendorDto> vendorList = null;
    if (!StringUtils.isEmpty(vendors)) {
      if (vendors.contains("$$$")) {
        vendorList = Stream.of(vendors)
            .flatMap(val -> Arrays.stream(val.split("$$$"))).map(val -> {
              VideoVendorDto vendorDto = new VideoVendorDto();
              vendorDto.setName(val);
              vendorDto.setDisplayName(null);
              return vendorDto;
            }).collect(Collectors.toList());
      }
    }

    if (vendorList != null && !vendorList.isEmpty()) {
      videoDto.setVendors(vendorList);
    }

    //add play info
    /*
     * 蓝光$http://www.le.com/ptv/vplay/31470021.html$$$蓝光$https://v.qq.com/x/cover/x8e13lfnovxz2rk.html$$$蓝光$http://www.iqiyi.com/v_19rrc1bweg.html$$$蓝光$http://v.youku.com/v_show/id_XMzkwMTQ0MDAzNg==.html$$$高清$27pan94E96F9E890CDCBF$$$蓝光$https://www.mgtv.com/b/322332/4729291.html$$$BD$https://cdn-1.haku99.com/hls/2018/11/28/s4IPdm9B/playlist.m3u8$$$BD1280国语中字$https://v6.438vip.com/2018/11/07/qLNGl6oqb44luO2L/playlist.m3u8
     */

  }
}
