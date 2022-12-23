package io.github.vdavidp.jpa.filter.db.sample;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
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

  @JdbcTypeCode(java.sql.Types.VARCHAR)
  private UUID uuid;
}
