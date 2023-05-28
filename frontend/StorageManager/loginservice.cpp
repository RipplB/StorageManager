#include "loginservice.h"
#include "util.h"
#include <QDebug>
#include <QVariant>
#include <QJsonArray>

LoginService::LoginService(QObject *parent)
    : AbstractResource{parent}
{
    currentRoles = QList<QString>();
}

void LoginService::login(const QString& url, const QVariantMap& data)
{
    m_manager->setUrl(url);
    RestAccessManager::ResponseCallback callback =
        [this,  data](QNetworkReply* reply, bool success) {
            if (success)
                loginRequestFinished(reply);
        };
    m_manager->post("/auth/login", data, callback);
}

void LoginService::loginRequestFinished(QNetworkReply* reply)
{
    std::optional<QJsonObject> json = byteArrayToJsonObject(reply->readAll());
    if (json && json->contains("value")) {
        currentRoles = QList<QString>();
        QList<QVariant> roleList = byteArrayToJsonObject(QByteArray::fromBase64(json->value("value").toString().split(".")[1].toUtf8()))->value("roles").toArray().toVariantList().toList();
        for (auto iter = roleList.begin(), end = roleList.end(); iter != end; iter++)
            currentRoles.append(iter->toString());
        emit rolesChanged(currentRoles);
        m_manager->setAuthorizationToken(json->value("value").toVariant().toByteArray());
        emit loginSuccess();
    }
}

QList<QString> LoginService::getCurrentRoles() const {
    return currentRoles;
}

void LoginService::refresh()
{
    RestAccessManager::ResponseCallback callback =
        [this](QNetworkReply* reply, bool success) {
            if (success)
                loginRequestFinished(reply);
        };
    m_manager->post("/auth/refresh", QVariantMap(), callback);
}
