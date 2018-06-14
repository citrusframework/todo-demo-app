FROM openjdk:8-jdk

LABEL maintainer="citrusframework.org <citrus-dev-l@consol.de>"

ARG APP_VERSION
ARG APP_ARTIFACT

ENV DEPLOY_DIR /app

EXPOSE 8080

USER root

VOLUME /tmp

RUN mkdir ${DEPLOY_DIR}

RUN curl https://labs.consol.de/maven/repository/com/consol/citrus/samples/$APP_ARTIFACT/$APP_VERSION/$APP_ARTIFACT-$APP_VERSION-executable.jar -o $DEPLOY_DIR/app.jar

RUN find $DEPLOY_DIR -name '*.jar' -exec chmod a+x {} +

RUN useradd -U citrus

CMD java -Djava.security.egd=file:/dev/./urandom -jar ${DEPLOY_DIR}/app.jar

USER citrus