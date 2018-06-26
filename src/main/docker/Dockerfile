FROM openjdk:8-jdk

LABEL maintainer="citrusframework.org <citrus-dev-l@consol.de>"

ARG APP_VERSION
ARG APP_ARTIFACT

ENV TODO_PERSISTENCE_TYPE in_memory
ENV TODO_PERSISTENCE_SERVER enabled
ENV TODO_PERSISTENCE_TRANSACTIONAL false
ENV TODO_JMS_ENABLED false
ENV TODO_JMS_BROKER disabled
ENV TODO_JDBC_AUTO_CREATE_TABLES true
ENV TODO_JDBC_DRIVER org.hsqldb.jdbcDriver
ENV TODO_JDBC_HOST localhost
ENV TODO_JDBC_PORT 9099
ENV TODO_JDBC_FILE /tmp/testdb

ENV DEPLOY_DIR /app

EXPOSE 8080
EXPOSE 9099
EXPOSE 61616

USER root

VOLUME /tmp

RUN mkdir ${DEPLOY_DIR}

RUN curl https://labs.consol.de/maven/repository/com/consol/citrus/samples/$APP_ARTIFACT/$APP_VERSION/$APP_ARTIFACT-$APP_VERSION-executable.jar -o $DEPLOY_DIR/app.jar

RUN find $DEPLOY_DIR -name '*.jar' -exec chmod a+x {} +

RUN useradd -U citrus

CMD java -Dtodo.persistence.server=${TODO_PERSISTENCE_SERVER} -Dtodo.persistence.type=${TODO_PERSISTENCE_TYPE} -Dtodo.persistence.transactional=${TODO_PERSISTENCE_TRANSACTIONAL} -Dtodo.jms.broker=${TODO_JMS_BROKER} -Dtodo.jms.enabled=${TODO_JMS_ENABLED} -Dtodo.jdbc.driverClassname=${TODO_JDBC_DRIVER} -Dtodo.jdbc.url=${TODO_JDBC_URL} -Dtodo.jdbc.host=${TODO_JDBC_HOST} -Dtodo.jdbc.port=${TODO_JDBC_PORT} -Dtodo.jdbc.file=${TODO_JDBC_FILE}  -Dtodo.jdbc.autoCreateTables=${TODO_JDBC_AUTO_CREATE_TABLES} -Djava.security.egd=file:/dev/./urandom -jar ${DEPLOY_DIR}/app.jar

USER citrus