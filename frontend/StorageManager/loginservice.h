#ifndef LOGINSERVICE_H
#define LOGINSERVICE_H

#include "abstractresource.h"
#include <QQmlEngine>

class LoginService : public AbstractResource
{
    Q_OBJECT
    QML_ELEMENT
public:
    explicit LoginService(QObject *parent = nullptr);
    Q_INVOKABLE void login(const QString& url, const QVariantMap& data);
private:
    void loginRequestFinished(QNetworkReply* reply, const QVariantMap& data);
signals:
    void loginSuccess();
};

#endif // LOGINSERVICE_H
