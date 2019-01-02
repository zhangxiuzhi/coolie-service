package zjtech.cinema.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class VideoTypeDto {
  public static final String COLLECTION_NAME = "videoType";

  @Id
  private String id;

  private String name;

  private String parentId;

  private String dbId;
}
