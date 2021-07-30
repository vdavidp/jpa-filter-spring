package io.dvp.jpa.filter.db.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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

  private Boolean booleanValue;
  
  @Temporal(TemporalType.DATE)
  private Date utilDate;
}
