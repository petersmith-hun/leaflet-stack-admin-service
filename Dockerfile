FROM eclipse-temurin:21-jre-alpine

ARG APP_USER=leaflet
ARG APP_HOME=/opt/lsas
ARG APP_EXECUTABLE=leaflet-sas-exec.jar
ENV ENV_APP_EXECUTABLE=$APP_EXECUTABLE
ENV ENV_APP_USER=$APP_USER

RUN addgroup --system --gid 1000 $APP_USER
RUN adduser --system --no-create-home --ingroup $APP_USER --uid 1000 $APP_USER
RUN mkdir -p $APP_HOME
ADD web/target/$APP_EXECUTABLE $APP_HOME
ADD config/leaflet-sas-exec.conf $APP_HOME

WORKDIR $APP_HOME
RUN chmod 744 $APP_HOME
RUN chmod 744 $APP_EXECUTABLE
RUN chown -R $APP_USER:$APP_USER $APP_HOME

ENTRYPOINT ./$ENV_APP_EXECUTABLE ${APP_ARGS}
