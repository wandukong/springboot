# DAO

## 🎁Add starters
<img src="https://user-images.githubusercontent.com/47289479/137699464-07c08c29-b256-4fb2-acbd-281708018300.png">

## 🎪application.properties 설정 추가
```
#Orcle DataSource(Conntection pool)
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver
spring.datasource.url=jdbc:oracle:thin:@호스트:포트번호:sid
spring.datasource.username=사용자
spring.datasource.password=패스워드
spring.datasource.hikari.maximum-pool-size=2 // Connection pool의 최대 연결 객체 생성 수

#MyBatis 
mybatis.config-location=classpath:mybatis/mapper-config.xml // mybatis congig 경로
mybatis.mapper-locations=classpath:mybatis/mapper/*.xml     // mapper 경로
```