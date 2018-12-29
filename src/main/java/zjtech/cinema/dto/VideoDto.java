package zjtech.cinema.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * Video DTO
 */
@Document
@Getter
@Setter
public class VideoDto extends VideoBriefDto {
  private String language;
  private String score;
  private String region;
  private String videoType;
  private String videoCatalog;
  private String director;
  private String year;
  private String introduction;
  private long updateTime;
  private long createTime;

  private List<VideoVendorDto> vendors = new ArrayList<>();
}
