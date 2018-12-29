package zjtech.coolie.transfer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import zjtech.coolie.handler.appcms.AppCmsVideoParser;
import zjtech.cinema.dto.VideoDto;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static zjtech.coolie.common.Constant.VIDEO_COLLECTION;

@Service
@Slf4j
public class VideoTransferService {

  @Autowired
  private VideoJpaRepository videoJpaRepository;

  @Autowired
  private AppCmsVideoParser parser;

  @Autowired
  private MongoOperations mongoOperations;

  @Async
  public void transfer() {
    int pageNumber = 0;
    int size = 10;
    Page<Video> page = null;
    do {
      Pageable pageable = PageRequest.of(pageNumber, size);
      page = videoJpaRepository.findAll(pageable);

      List<VideoDto> list = page.get().map(video -> {
        HashMap<String, Object> map = new HashMap();
        map.put("vod_id", video.getVodId());
        map.put("vod_name", video.getVodName());
        map.put("vod_pic", video.getVodPic());
        map.put("vod_en", video.getVodLang());
        map.put("vod_actor", video.getVodActor());
        map.put("vod_director", video.getVodDirector());
        map.put("vod_area", video.getVodArea());
        map.put("vod_content", video.getVodContent());
        map.put("vod_year", video.getVodYear());
        map.put("vod_play_from", video.getVodPlayFrom());
        map.put("vod_play_url", video.getVodPlayUrl());
        map.put("vod_time", video.getVodTime());
        map.put("vod_time_add", video.getVodTimeAdd());
        map.put("type_id", video.getTypeId());
        map.put("type_id_1", video.getTypeId1());
        return map;
      }).map(parser::parse).collect(Collectors.toList());
      mongoOperations.insert(list, VIDEO_COLLECTION);
      pageNumber++;
      log.info("Totally inserted {} rows into mongodb now.", pageNumber * size);
    } while (page != null && !page.isEmpty());
  }
}
