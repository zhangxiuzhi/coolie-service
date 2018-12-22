package com.zjtech.coolie.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class VideoDocument {
  @Id
  private String id;

}
