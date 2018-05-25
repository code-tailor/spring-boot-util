package com.codetailor.springbootutil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
public class SpringBootUtilAutoConfig {

  private static final Logger logger = LoggerFactory.getLogger(SpringBootUtilAutoConfig.class);

  public SpringBootUtilAutoConfig() {
    logger.debug("SpringBootUtilAutoConfig() :: Initialized.");
  }

}
