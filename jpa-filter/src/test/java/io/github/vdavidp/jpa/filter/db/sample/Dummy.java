package io.github.vdavidp.jpa.filter.db.sample;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.Type;

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
  
  @Type(type = "uuid-char")
  private UUID uuid;
}
