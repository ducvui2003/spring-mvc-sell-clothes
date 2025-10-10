package com.spring.websellspringmvc.models;

import java.sql.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
public class Log {
  private int id;
  private String ip;
  private String level;
  private String resource;
  private Date dateCreated;
  private String previous; // json
  private String current; // json
}
