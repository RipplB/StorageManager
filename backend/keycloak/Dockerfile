FROM quay.io/keycloak/keycloak:latest
ENV KEYCLOAK_ADMIN=admin
ENV KEYCLOAK_ADMIN_PASSWORD=admin
ENV KC_DB=dev-mem
ENV KC_HTTP_RELATIVE_PATH=/auth
RUN /opt/keycloak/bin/kc.sh build
RUN mkdir -p /opt/keycloak/data/import
COPY realm-export.json /opt/keycloak/data/import
ENTRYPOINT [ "/opt/keycloak/bin/kc.sh" , "start-dev", "--import-realm" ]