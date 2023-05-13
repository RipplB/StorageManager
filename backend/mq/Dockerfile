FROM icr.io/ibm-messaging/mq AS manuelmq
ENV LICENSE=accept
ENV MQ_QMGR_NAME=MANUEL
EXPOSE 80
COPY config.mqsc /etc/mqm/
ENTRYPOINT [ "setmqweb",  "properties", "-k", "httpPort", "-v", "80"]