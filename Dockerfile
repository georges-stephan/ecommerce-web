FROM tomcat:9.0-alpine

ADD target/ecommerce-web.war /usr/local/tomcat/webapps/

EXPOSE 8080

CMD ["catalina.sh", "run"]