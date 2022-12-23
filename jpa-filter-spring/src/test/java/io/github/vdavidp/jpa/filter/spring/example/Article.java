package io.github.vdavidp.jpa.filter.spring.example;

import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
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