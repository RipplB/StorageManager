FROM icr.io/ibm-messaging/mq AS manuelmq
ENV LICENSE=accept
ENV MQ_QMGR_NAME=MANUEL
COPY config.mqsc /etc/mqm/
ENTRYPOINT [ "setmqweb",  "properties", "-k", "httpPort", "-v", "80"]