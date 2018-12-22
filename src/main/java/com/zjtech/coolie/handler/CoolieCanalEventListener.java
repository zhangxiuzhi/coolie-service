package com.zjtech.coolie.handler;

import com.alibaba.fastjson.JSON;
import com.alibaba.otter.canal.protocol.CanalEntry;
import com.xpand.starter.canal.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.alibaba.otter.canal.protocol.CanalEntry.EventType.*;

@CanalEventListener
public class CoolieCanalEventListener {
  @Autowired
  private MongoTemplate mongoTemplate;

  @ListenPoint(destination = "example", schema = "movie",
      table = {"test"}, eventType = {UPDATE, INSERT, DELETE})
  public void onOperateVideo(CanalEntry.EventType eventType, CanalEntry.RowData rowData) {
    if (eventType.equals(INSERT)) {
      List<CanalEntry.Column> columnList = rowData.getAfterColumnsList();
      Map<String, Object> map = new HashMap<>();
      columnList.forEach(column -> {
        map.put(column.getName(), column.getValue());
      });
    }

//    mongoTemplate.insertAll();
    System.out.println("onEvent4");
    //do something...
  }
}
