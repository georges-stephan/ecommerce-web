FROM tomcat:9.0-alpine

ADD target/ecommerce-web-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ecommerce-web.war

EXPOSE 8080

CMD ["catalina.sh", "run"]