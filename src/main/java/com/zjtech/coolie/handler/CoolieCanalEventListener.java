package com.zjtech.coolie.handler;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.CanalEventListener;
import com.xpand.starter.canal.annotation.ListenPoint;
import com.zjtech.coolie.handler.appcms.AppCmsVideoParser;
import com.zjtech.dto.cinema.VideoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.otter.canal.protocol.CanalEntry.EventType.*;

@Slf4j
@CanalEventListener
public class CoolieCanalEventListener {
  private final MongoTemplate mongoTemplate;

  private final AppCmsVideoParser parser;

  private static final String VIDEO_COLLECTION = "video";

  @Autowired
  public CoolieCanalEventListener(MongoTemplate mongoTemplate, AppCmsVideoParser parser) {
    this.mongoTemplate = mongoTemplate;
    this.parser = parser;
  }

  @ListenPoint(destination = "example", schema = "movie",
     table = {"test"}, eventType = {UPDATE, INSERT, DELETE})
  public void onOperateVideo(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
    List<CanalEntry.Column> columnList = rowData.getAfterColumnsList();
    Map<String, Object> map = new HashMap<>();
    columnList.forEach(column -> {
      map.put(column.getName(), column.getValue());
    });
    VideoDto videoDto = parser.parse(map);

    if (eventType.equals(INSERT)) {
      mongoTemplate.save(videoDto, "video");
      log.info("inserted a video '{}'", videoDto.getName());
    }

    if (eventType.equals(DELETE)) {
//      mongoTemplate.findAndRemove()
      log.info("deleted a video '{}'", videoDto.getName());
    }
//    mongoTemplate.findAndModify(new Query().)

    //do something...
  }
}
