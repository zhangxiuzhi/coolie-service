package zjtech.coolie.handler.appcms;

import com.alibaba.otter.canal.protocol.CanalEntry;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ContextConfiguration;
import zjtech.CoolieServiceApplication;
import zjtech.coolie.handler.CoolieCanalEventListener;
import zjtech.dto.cinema.VideoDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.*;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static zjtech.coolie.handler.CoolieCanalEventListener.VIDEO_COLLECTION;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = CoolieServiceApplication.class)
@DataMongoTest  //enable an embedded mongo for test
//@DataMongoTest(excludeAutoConfiguration = EmbeddedMongoAutoConfiguration.class)  //disable the embedded mongo for using production mongo server
public class MongoVideoTest {
  @Autowired
  private MongoTemplate mongoTemplate;

  @Test
  public void testMongo() {
    var parser = new AppCmsVideoParser();
    var listener = new CoolieCanalEventListener(mongoTemplate, parser);

    var map = new HashMap<String, Object>();
    map.put("vod_id", "1111");
    map.put("vod_name", "咦!弄啥嘞 2016");
    map.put("vod_en", "yinongshalei2016");
    map.put("vod_actor", "未知");
    map.put("vod_director", "dir");
    map.put("vod_area", "大陆");
    map.put("vod_content", "《咦!弄啥嘞》爆笑来袭，给你带来不一样的快乐");
    map.put("vod_year", "2016");
    map.put("vod_play_from", "youku");
    map.put("vod_play_url", "第1集$http://v.youku.com" +
       "/v_show/id_XMTQ1MTkxOTM0OA==.html#第2集$http://" +
       "v.youku.com/v_show/id_XMTQ2NjQ2MTM3Ng==.html#" +
       "第3集$http://v.youku.com/v_show/id_XMTQ5OTU3NzUxNg==.html#" +
       "第4集$http://v.youku.com/v_show/id_XMTUxODk3MDg4OA==.html#" +
       "第5集$http://v.youku.com/v_show/id_XMTU0MjYzMzA2MA==.html" +
       "#第6集$http://v.youku.com/v_show/id_XMTU2OTA2NTk4OA==.html");

    var videoDto = parser.parse(map);
    var oldName = videoDto.getName();

    //insert a document
    listener.handleVideoEvent(CanalEntry.EventType.INSERT, videoDto);

    //find this document
    List<VideoDto> list = getVideo(videoDto.getDbId());
    Query query;

    assertEquals(list.size(), 1);
    assertEquals(list.get(0).getName(), videoDto.getName());
    assertNotNull(videoDto.getId());

    //update this document
    videoDto.getVendors().clear();
    videoDto.setName("");
    Document document = new Document();
    mongoTemplate.getConverter().write(videoDto, document);
    query = new Query(where("dbId").is(videoDto.getDbId()));
    var videoUpdated = mongoTemplate.findAndReplace(query, document, VIDEO_COLLECTION);

    var newList = getVideo(videoDto.getDbId());
    assertEquals(list.size(), 1);
    assertNotEquals(oldName, newList.get(0).getName());

    //delete
    query = new Query(where("dbId").is(videoDto.getDbId()));
    DeleteResult result = mongoTemplate.remove(query, VIDEO_COLLECTION);
    assertEquals(result.getDeletedCount(), 1l);

    newList = getVideo(videoDto.getDbId());
    assertEquals(newList.size(), 0);
  }

  private List<VideoDto> getVideo(String dbId) {
    var query = new Query(where("dbId").is(dbId));
    return mongoTemplate.find(query, VideoDto.class, VIDEO_COLLECTION);
  }

}
