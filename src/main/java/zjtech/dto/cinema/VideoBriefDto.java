package zjtech.dto.cinema;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class VideoBriefDto implements Serializable {

  private String id;
  private String dbId;
  private String name;
  private String actors;
  private String imageUrl = "http://url";
}
