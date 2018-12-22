package com.zjtech.dto.cinema;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VideoBriefDto implements Serializable {

  private String id;
  private String name;
  private String actors;
  private String imageUrl = "http://url";
}
