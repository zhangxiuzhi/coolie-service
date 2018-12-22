package com.zjtech.dto.cinema;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 * Video DTO
 */
@Getter
@Setter
public class VideoDto extends VideoBriefDto {
private String language;
  private String score;
  private String region;
  private String videoType;
  private String director;
  private String year;
  private String introduction;

  private List<VideoVendorDto> vendors = new ArrayList<>();
}
