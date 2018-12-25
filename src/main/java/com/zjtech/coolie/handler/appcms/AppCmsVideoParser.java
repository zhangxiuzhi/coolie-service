package com.zjtech.coolie.handler.appcms;

import com.zjtech.dto.cinema.PlayInfo;
import com.zjtech.dto.cinema.VideoDto;
import com.zjtech.dto.cinema.VideoVendorDto;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AppCmsVideoParser {
  private static final String MOVIE_SEPARATOR = "\\$\\$\\$";
  private static final String VENDOR_SEPARATOR = "\\$\\$\\$";
  private static final String TV_SEPARATOR = "#";

  private static final String REG = "(\\w*?://)?([^/]*).*";
  private static final Pattern PATTERN = Pattern.compile(REG);

  public VideoDto parse(Map<String, Object> data) {
    var videoDto = new VideoDto();

    videoDto.setName(getValue(data, "vod_name"));//name
    videoDto.setLanguage(getValue(data, "vod_en"));//语言
    videoDto.setActors(getValue(data, "vod_actor"));//演员列表
    videoDto.setDirector(getValue(data, "vod_director")); //导演
    videoDto.setRegion(getValue(data, "vod_area")); //地区
    videoDto.setIntroduction(getValue(data, "vod_content"));//详细描述
    videoDto.setYear(getValue(data, "vod_year"));//上映年份

    //parse vod_play_from : 视频提供方代称
    //format: sohu$$$letv$$$mgtv$$$pptv, youku,
    var vendors = getValue(data, "vod_play_from");
    List<VideoVendorDto> vendorList = null;
    if (!StringUtils.isEmpty(vendors)) {
      if (vendors.contains("$$$")) {
        vendorList = new ArrayList<>();
        String[] vendorArray = vendors.split(VENDOR_SEPARATOR);
        for (var item : vendorArray) {
          VideoVendorDto vendorDto = new VideoVendorDto();
          vendorDto.setName(item);
          vendorDto.setDisplayName(null);
          vendorList.add(vendorDto);
        }

      } else {
        VideoVendorDto vendorDto = new VideoVendorDto();
        vendorDto.setName(vendors);
        vendorDto.setDisplayName(null);
        vendorList = new ArrayList<>();
        vendorList.add(vendorDto);
      }
    }

    if (vendorList != null && !vendorList.isEmpty()) {
      videoDto.setVendors(vendorList);
    }

    //add play info
    /*
     * 蓝光$http://www.le.com/ptv/vplay/31470021.html$$$蓝光$https://v.qq.com/x/cover/x8e13lfnovxz2rk.html$$$蓝光$http://www.iqiyi.com/v_19rrc1bweg.html$$$蓝光$http://v.youku.com/v_show/id_XMzkwMTQ0MDAzNg==.html$$$高清$27pan94E96F9E890CDCBF$$$蓝光$https://www.mgtv.com/b/322332/4729291.html$$$BD$https://cdn-1.haku99.com/hls/2018/11/28/s4IPdm9B/playlist.m3u8$$$BD1280国语中字$https://v6.438vip.com/2018/11/07/qLNGl6oqb44luO2L/playlist.m3u8
     */
    var playInfo = getValue(data, "vod_play");
    if(StringUtils.isEmpty(playInfo)){
      return videoDto;
    }

    if (playInfo.contains(TV_SEPARATOR)) {
      //for teleplay
      /*
       * 第1集$http://v.qq.com/x/cover/boowq1mh1mp2owp/v00220seoak.html#第2集$http://xxx
       */
      addPlayInfo(vendorList, playInfo, TV_SEPARATOR);
    } else {

      //for movie
      addPlayInfo(vendorList, playInfo, MOVIE_SEPARATOR);
    }



    return videoDto;
  }

  private void addPlayInfo(List<VideoVendorDto> vendorList, String playInfo, String separator) {
    var playInfoArray = playInfo.split(separator);
    String previousUrl = null;
    int i = 0;
    for (var playUrlInfo : playInfoArray) {
      var items = playUrlInfo.split("\\$");
      var specificPlayInfo = new PlayInfo();
      specificPlayInfo.setName(items[0]);

      var realUrl = StringUtils.isEmpty(items[1]) ? "" : items[1];
      specificPlayInfo.setPlayUrl(items[1]);

      if (previousUrl != null && !Objects.equals(getHost(realUrl), getHost(previousUrl))) {
        //same host
        i++;
      }
      vendorList.get(i).getPlayList().add(specificPlayInfo);
      previousUrl = specificPlayInfo.getPlayUrl();
    }
  }

  private String getHost(String value) {
    Matcher matcher = PATTERN.matcher(value);
    if (matcher.matches()) {
      return matcher.group(2);
    }
    return null;
  }

  private String getValue(Map<String, Object> map, String key) {
    var val = map.get(key);
    if (val != null) {
      return val.toString();
    }
    return null;
  }
}
