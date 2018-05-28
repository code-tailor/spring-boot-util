package com.codetailor.springbootutil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.net.util.SubnetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
@ConditionalOnProperty(prefix = "ipSecurity", name = "enabled", havingValue = "true",
    matchIfMissing = false)
public class IpSecurityFilter implements Filter {

  private static final Logger logger = LoggerFactory.getLogger(IpSecurityFilter.class);

  @Value("${ipSecurity.allowedNetworks}")
  private String[] allowedNetworks;

  private List<SubnetUtils> subnetUtils = new ArrayList<>();

  /**
   * Initialize {@link SubnetUtils}.
   */
  @PostConstruct
  public void initialization() {
    logger.info("initialization():: allowed Networks:{}.", Arrays.toString(allowedNetworks));

    for (String network : allowedNetworks) {
      try {
        SubnetUtils subnet = new SubnetUtils(network);
        subnetUtils.add(subnet);
      } catch (Exception ex) {
        logger.warn("initialization():: fail to add allowed network:{} as it's invalid.", network,
            ex);
      }
    }
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    logger.debug("init():: IpsecurityFilter initialization.");
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    logger.debug("doFilter:: Ip security applying...");

    HttpServletRequest httpRequest = (HttpServletRequest) request;
    HttpServletResponse httpResponse = (HttpServletResponse) response;

    String remoteAddr = httpRequest.getRemoteAddr();

    if (!isFromAllowedNetworks(remoteAddr)) {
      httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
      String uri = httpRequest.getRequestURI();
      logger.info("doFilter():: Restricted access to '{}' from '{}'.", uri, remoteAddr);
      return;
    }

    chain.doFilter(httpRequest, httpResponse);
  }

  /**
   * Use to check given remote address is in range of allowed networks.
   * 
   * @param remoteAddr requested remote address
   * @return true if remoteAddr is in range of allowed network, otherwise return false.
   */
  private boolean isFromAllowedNetworks(String remoteAddr) {
    logger.debug("isFromAllowNetwork():: remoteAddr:{}", remoteAddr);

    for (SubnetUtils subnet : subnetUtils) {
      if (subnet.getInfo().isInRange(remoteAddr)) {
        return true;
      }
    }

    return false;
  }

  @Override
  public void destroy() {
    logger.debug("destroy():: invoke");
  }

}
