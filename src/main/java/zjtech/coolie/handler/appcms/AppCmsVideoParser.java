package zjtech.coolie.handler.appcms;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import zjtech.cinema.dto.PlayInfo;
import zjtech.cinema.dto.VideoDto;
import zjtech.cinema.dto.VideoVendorDto;

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
    VideoDto videoDto = new VideoDto();

    videoDto.setDbId(getValue(data, "vod_id"));//name
    videoDto.setName(getValue(data, "vod_name"));//name
    videoDto.setImageUrl(getValue(data, "vod_pic"));//name
    videoDto.setLanguage(getValue(data, "vod_lang"));//语言
    videoDto.setActors(getValue(data, "vod_actor"));//演员列表
    videoDto.setDirector(getValue(data, "vod_director")); //导演
    videoDto.setRegion(getValue(data, "vod_area")); //地区
    videoDto.setIntroduction(getValue(data, "vod_content"));//详细描述
    videoDto.setYear(getValue(data, "vod_year"));//上映年份
    videoDto.setUpdateTime(Long.parseLong(getValue(data, "vod_time")));//update time
    videoDto.setCreateTime(Long.parseLong(getValue(data, "vod_time_add")));//create time
    videoDto.setVideoType(getValue(data, "type_id"));//create time
    videoDto.setVideoCatalog(getValue(data, "type_id_1"));//create time

    //parse vod_play_from : 视频提供方代称
    //format: sohu$$$letv$$$mgtv$$$pptv, youku,
    String vendors = getValue(data, "vod_play_from");
    List<VideoVendorDto> vendorList = null;
    if (!StringUtils.isEmpty(vendors)) {
      if (vendors.contains("$$$")) {
        vendorList = new ArrayList<>();
        String[] vendorArray = vendors.split(VENDOR_SEPARATOR);
        for (String item : vendorArray) {
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
    String playInfo = getValue(data, "vod_play_url");
    if (StringUtils.isEmpty(playInfo)) {
      return videoDto;
    }

    if (playInfo.contains(TV_SEPARATOR)) {
      //for teleplay
      addPlayInfo(vendorList, playInfo, TV_SEPARATOR);
    } else {
      //for movie
      addPlayInfo(vendorList, playInfo, MOVIE_SEPARATOR);
    }


    return videoDto;
  }

  private void addPlayInfo(List<VideoVendorDto> vendorList, String playInfo, String separator) {
    String[] playInfoArray = playInfo.split(separator);
    String previousUrl = null;
    int i = 0;
    for (String playUrlInfo : playInfoArray) {
      String[] items = playUrlInfo.split("\\$");
      PlayInfo specificPlayInfo = new PlayInfo();
      String realUrl = null;
      if (items.length == 1) {
        specificPlayInfo.setName("NA");
        realUrl = items[0];
      } else {
        specificPlayInfo.setName(items[0]);
        realUrl = StringUtils.isEmpty(items[1]) ? "" : items[1];
      }
      specificPlayInfo.setPlayUrl(realUrl);

      if (previousUrl != null && !Objects.equals(getHost(realUrl), getHost(previousUrl))
         && i + 1 != vendorList.size()) {
        //not same host
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
    Object val = map.get(key);
    if (val != null) {
      return val.toString();
    }
    return null;
  }
}
