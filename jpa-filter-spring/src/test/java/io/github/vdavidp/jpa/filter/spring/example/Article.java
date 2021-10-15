package io.github.vdavidp.jpa.filter.spring.example;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Article extends BaseEntity {

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private Boolean active;
  
  @OneToMany(mappedBy = "article")
  private List<Comment> comments;
  
  @OneToOne
  private Language language;
}