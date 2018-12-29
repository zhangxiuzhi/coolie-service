package zjtech.coolie.transfer;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "mv1_vod")
@Getter
@Setter
public class Video {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "vod_id")
  private Long vodId;

  @Column(name = "type_id")
  private Long typeId;

  @Column(name = "type_id_1")
  private Long typeId1;

  @Column(name = "group_id")
  private Long groupId;

  @Column(name = "vod_name")
  private String vodName;

  @Column(name = "vod_sub")
  private String vodSub;

  @Column(name = "vod_en")
  private String vodEn;

  @Column(name = "vod_status")
  private long vodStatus;

  @Column(name = "vod_letter")
  private String vodLetter;

  @Column(name = "vod_color")
  private String vodColor;

  @Column(name = "vod_tag")
  private String vodTag;

  @Column(name = "vod_class")
  private String vodClass;

  @Column(name = "vod_pic")
  private String vodPic;

  @Column(name = "vod_pic_thumb")
  private String vodPicThumb;

  @Column(name = "vod_pic_slide")
  private String vodPicSlide;

  @Column(name = "vod_actor")
  private String vodActor;

  @Column(name = "vod_director")
  private String vodDirector;

  @Column(name = "vod_writer")
  private String vodWriter;

  @Column(name = "vod_blurb")
  private String vodBlurb;

  @Column(name = "vod_remarks")
  private String vodRemarks;

  @Column(name = "vod_pubdate")
  private String vodPubdate;

  @Column(name = "vod_total")
  private long vodTotal;

  @Column(name = "vod_serial")
  private String vodSerial;

  @Column(name = "vod_tv")
  private String vodTv;

  @Column(name = "vod_weekday")
  private String vodWeekday;

  @Column(name = "vod_area")
  private String vodArea;

  @Column(name = "vod_lang")
  private String vodLang;

  @Column(name = "vod_year")
  private String vodYear;

  @Column(name = "vod_version")
  private String vodVersion;

  @Column(name = "vod_state")
  private String vodState;

  @Column(name = "vod_author")
  private String vodAuthor;

  @Column(name = "vod_jumpurl")
  private String vodJumpurl;

  @Column(name = "vod_tpl")
  private String vodTpl;

  @Column(name = "vod_tpl_Play")
  private String vodTplPlay;
  @Column(name = "vod_tpl_down")
  private String vodTplDown;

  @Column(name = "vod_isend")
  private long vodIsend;

  @Column(name = "vod_lock")
  private long vodLock;

  @Column(name = "vod_level")
  private long vodLevel;


  @Column(name = "vod_points_play")
  private long vodPointsPlay;

  @Column(name = "vod_points_down")
  private long vodPointsDown;

  @Column(name = "vod_hits")
  private long vodHits;

  @Column(name = "vod_hits_day")
  private long vodHitsDay;

  @Column(name = "vod_hits_week")
  private long vodHitsWeek;

  @Column(name = "vod_hits_month")
  private long vodHitsMonth;

  @Column(name = "vod_duration")
  private String vodDuration;

  @Column(name = "vod_up")
  private long vodUp;

  @Column(name = "vod_down")
  private long vodDown;

  @Column(name = "vod_score")
  private double vodScore;

  @Column(name = "vod_score_all")
  private long vodScoreAll;

  @Column(name = "vod_score_num")
  private long vodScoreNum;

  @Column(name = "vod_time")
  private long vodTime;

  @Column(name = "vod_time_add")
  private long vodTimeAdd;
  @Column(name = "vod_time_hits")
  private long vodTimeHits;

  @Column(name = "vod_time_make")
  private long vodTimeMake;

  @Column(name = "vod_trysee")
  private long vodTrysee;

  @Column(name = "vod_douban_id")
  private long vodDoubanId;

  @Column(name = "vod_douban_score")
  private double vodDoubanScore;

  @Column(name = "vod_reurl")
  private String vodReurl;

  @Column(name = "vod_rel_vod")
  private String vodRelVod;

  @Column(name = "vod_rel_art")
  private String vodRelArt;

  @Column(name = "vod_content")
  private String vodContent;

  @Column(name = "vod_play_from")
  private String vodPlayFrom;

  @Column(name = "vod_play_server")
  private String vodPlayServer;

  @Column(name = "vod_play_note")
  private String vodPlayNote;

  @Column(name = "vod_play_url")
  private String vodPlayUrl;

  @Column(name = "vod_down_from")
  private String vodDownFrom;

  @Column(name = "vod_down_server")
  private String vodDownServer;

  @Column(name = "vod_down_note")
  private String vodDownNote;

  @Column(name = "vod_down_url")
  private String vodDownUrl;

}
