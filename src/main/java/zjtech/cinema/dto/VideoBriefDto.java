package zjtech.cinema.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

@Getter
@Setter
public class VideoBriefDto implements Serializable {

  @Id
  private String id;
  private String dbId;
  private String name;
  private String actors;
  private String imageUrl = "http://url";
}
