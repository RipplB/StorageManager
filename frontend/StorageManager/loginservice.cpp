#include "loginservice.h"
#include "util.h"
#include <QDebug>

LoginService::LoginService(QObject *parent)
    : AbstractResource{parent}
{

}

void LoginService::login(const QString& url, const QVariantMap& data)
{
    m_manager->setUrl(url);
    RestAccessManager::ResponseCallback callback =
        [this,  data](QNetworkReply* reply, bool success) {
            if (success)
                loginRequestFinished(reply, data);
        };
    m_manager->post("/auth/login", data, callback);
}

void LoginService::loginRequestFinished(QNetworkReply* reply, const QVariantMap& data)
{
    std::optional<QJsonObject> json = byteArrayToJsonObject(reply->readAll());
    if (json && json->contains("value")) {
        qDebug() << json->value("value").toString().toStdString();
        m_manager->setAuthorizationToken(json->value("value").toVariant().toByteArray());
        emit loginSuccess();
    }
}
