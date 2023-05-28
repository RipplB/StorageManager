#ifndef LOGINSERVICE_H
#define LOGINSERVICE_H

#include "abstractresource.h"
#include <QQmlEngine>

class LoginService : public AbstractResource
{
    Q_OBJECT
    Q_PROPERTY(QList<QString> currentRoles READ getCurrentRoles NOTIFY rolesChanged)
    QML_ELEMENT
public:
    explicit LoginService(QObject *parent = nullptr);
    Q_INVOKABLE void login(const QString& url, const QVariantMap& data);
    Q_INVOKABLE QList<QString> getCurrentRoles() const;
private:
    void loginRequestFinished(QNetworkReply* reply, const QVariantMap& data);
    QList<QString> currentRoles;
signals:
    void loginSuccess();
    void rolesChanged(QList<QString> newRoles);
};

#endif // LOGINSERVICE_H
