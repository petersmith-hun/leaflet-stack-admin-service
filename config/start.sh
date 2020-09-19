#!/usr/bin/env sh

socat TCP-LISTEN:2375,reuseaddr,fork UNIX-CONNECT:/var/run/docker.sock &
su $ENV_APP_USER -s /bin/sh -c "./$ENV_APP_EXECUTABLE ${APP_ARGS}"
