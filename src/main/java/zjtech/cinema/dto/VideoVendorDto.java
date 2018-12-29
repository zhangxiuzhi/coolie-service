package zjtech.cinema.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoVendorDto implements Serializable {

  private String name;
  private String displayName;
  private List<PlayInfo> playList = new ArrayList<>();
}
