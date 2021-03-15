package io.dvp.jpa.filter.db.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Dummy {

  @Id
  private Long id;

  private Integer integerValue;

  private Long longValue;

  private BigInteger bigIntegerValue;

  private Float floatValue;

  private Float doubleValue;

  private BigDecimal bigDecimalValue;
}
