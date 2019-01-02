package zjtech.coolie.handler;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.mongodb.client.result.DeleteResult;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import zjtech.cinema.dto.VideoTypeDto;
import zjtech.coolie.handler.appcms.AppCmsVideoParser;
import zjtech.cinema.dto.VideoDto;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.otter.canal.protocol.CanalEntry.EventType.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static zjtech.coolie.common.Constant.VIDEO_COLLECTION;

@Slf4j
@CanalEventListener
public class CoolieCanalEventListener {
  private final MongoTemplate mongoTemplate;

  private final AppCmsVideoParser parser;


  @Autowired
  public CoolieCanalEventListener(MongoTemplate mongoTemplate, AppCmsVideoParser parser) {
    this.mongoTemplate = mongoTemplate;
    this.parser = parser;
  }

  @ListenPoint(destination = "example", schema = "movie",
     table = {"mv1_vod"}, eventType = {UPDATE, INSERT, DELETE})
  public void onOperateVideo(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
    List<CanalEntry.Column> columnList = rowData.getAfterColumnsList();
    if (eventType.equals(DELETE)) {
      columnList = rowData.getBeforeColumnsList();
    }

    Map<String, Object> map = new HashMap<>();
    columnList.forEach(column -> {
      map.put(column.getName(), column.getValue());
    });
    VideoDto videoDto = parser.parse(map);

    handleVideoEvent(eventType, videoDto);
  }

  @ListenPoint(destination = "example", schema = "movie",
     table = {"mv1_type"}, eventType = {UPDATE, INSERT, DELETE})
  public void onOperateVideoType(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
    List<CanalEntry.Column> columnList = rowData.getAfterColumnsList();
    if (eventType.equals(DELETE)) {
      columnList = rowData.getBeforeColumnsList();
    }

    Map<String, Object> map = new HashMap<>();
    columnList.forEach(column -> {
      map.put(column.getName(), column.getValue());
    });

    VideoTypeDto videoTypeDto = new VideoTypeDto();
    videoTypeDto.setName(map.get("type_name").toString());
    videoTypeDto.setParentId(map.get("type_pid").toString());
    videoTypeDto.setDbId(map.get("type_id").toString());
    if (eventType.equals(INSERT)) {
      mongoTemplate.insert(videoTypeDto, VideoTypeDto.COLLECTION_NAME);
      log.info("inserted a videoType '{}'", videoTypeDto.getName());
    }

    if (eventType.equals(DELETE)) {
      Query query = new Query(where("dbId").is(videoTypeDto.getDbId()));
      DeleteResult result = mongoTemplate.remove(query, VideoTypeDto.class, VideoTypeDto.COLLECTION_NAME);
      if (result.wasAcknowledged()) {
        log.info("deleted a videoType '{}'", videoTypeDto.getDbId());
      } else {
        log.warn("Failed to deleted a videoType '{}'", videoTypeDto.getDbId());
      }
    }

    if (eventType.equals(UPDATE)) {
      Document document = new Document();
      mongoTemplate.getConverter().write(videoTypeDto, document);
      Query query = new Query(where("dbId").is(videoTypeDto.getDbId()));
      document = mongoTemplate.findAndReplace(query, document, VideoTypeDto.COLLECTION_NAME);
      if (document == null) {
        log.warn("Failed to update videoType 'name={}, dbId={}': not found in mongodb",
           videoTypeDto.getName(), videoTypeDto.getDbId());
      }
      log.info("updated videoType 'name={},dbId={}'", videoTypeDto.getName(), videoTypeDto.getDbId());
    }
  }

  public void handleVideoEvent(CanalEntry.EventType eventType, VideoDto videoDto) {
    if (eventType.equals(INSERT)) {
      mongoTemplate.insert(videoDto, VIDEO_COLLECTION);
      log.info("inserted a video '{}'", videoDto.getDbId());
    }

    if (eventType.equals(DELETE)) {
      Query query = new Query(where("name").is(videoDto.getName()));
      DeleteResult result = mongoTemplate.remove(query, VideoDto.class, VIDEO_COLLECTION);
      if (result.wasAcknowledged()) {
        log.info("deleted a video '{}'", videoDto.getDbId());
      } else {
        log.warn("Failed to deleted a video '{}'", videoDto.getDbId());
      }
    }

    if (eventType.equals(UPDATE)) {
      Document document = new Document();
      mongoTemplate.getConverter().write(videoDto, document);
//      var update = Update.fromDocument(document);
      Query query = new Query(where("name").is(videoDto.getName()));
      document = mongoTemplate.findAndReplace(query, document, VIDEO_COLLECTION);
      if (document == null) {
        log.warn("Failed to update video 'name={}, dbId={}': not found in mongodb",
           videoDto.getName(), videoDto.getDbId());
      }
      log.info("updated video 'name={},dbId={}'", videoDto.getName(), videoDto.getDbId());
    }
  }
}
