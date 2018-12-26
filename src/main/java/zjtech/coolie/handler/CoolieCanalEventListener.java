package zjtech.coolie.handler;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import zjtech.coolie.handler.appcms.AppCmsVideoParser;
import zjtech.dto.cinema.VideoDto;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.otter.canal.protocol.CanalEntry.EventType.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@Slf4j
@CanalEventListener
public class CoolieCanalEventListener {
  private final MongoTemplate mongoTemplate;

  private final AppCmsVideoParser parser;

  public static final String VIDEO_COLLECTION = "video";

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

  public void handleVideoEvent(CanalEntry.EventType eventType, VideoDto videoDto) {
    if (eventType.equals(INSERT)) {
      mongoTemplate.insert(videoDto, VIDEO_COLLECTION);
      log.info("inserted a video '{}'", videoDto.getName());
    }

    if (eventType.equals(DELETE)) {
      var query = new Query(where("name").is(videoDto.getName()));
      var result = mongoTemplate.remove(query, VideoDto.class, VIDEO_COLLECTION);
      if (result.wasAcknowledged()) {
        log.info("deleted a video '{}'", videoDto.getName());
      } else {
        log.warn("Failed to deleted a video '{}'", videoDto.getName());
      }
    }

    if (eventType.equals(UPDATE)) {
      Document document = new Document();
      mongoTemplate.getConverter().write(videoDto, document);
//      var update = Update.fromDocument(document);
      var query = new Query(where("name").is(videoDto.getName()));
      document = mongoTemplate.findAndReplace(query, document, VIDEO_COLLECTION);
      if (document == null) {
        log.warn("Failed to update video 'name={}, dbId={}': not found in mongodb",
           videoDto.getName(), videoDto.getDbId());
      }
      log.info("updated video 'name={},dbId={}'", videoDto.getName(), videoDto.getDbId());
    }
  }
}
