# spring-boot-util
Utility library for spring-boot project.

## Introduction
These project provide IP security using configure allowed IP addresses, Network addresses through CIDR notation. It is validate CIDR notation using `org.apache.commons.net.util.SubnetUtils`.

## Integration steps

### 1. Add 'spring-boot-util` as dependency in pom.xml

```
<dependency>
  <groupId>com.codetailor</groupId>
  <artifactId>spring-boot-util</artifactId>
  <<version>1.0.0-SNAPSHOT</version>
</dependency>
```

#### 2. Configure properties for IP security

##### Enable IP security

```
ipSecurity.enabled=true
```
> **Notes:** It defaults to `ipSecurity.enabled=false`.

##### Configure allowed IP addresses, Network addresses through CIDR notation

```
ipSecurity.allowedNetworks=192.168.0.0/24
```
