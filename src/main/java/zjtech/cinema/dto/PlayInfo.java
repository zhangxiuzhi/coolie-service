package zjtech.cinema.dto;

import java.io.Serializable;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayInfo implements Serializable {

  private String name;
  private String playUrl;

}
