#ifndef USERSERVICE_H
#define USERSERVICE_H

#include "abstractresource.h"
#include <QObject>
#include <QQmlEngine>

class UserService : public AbstractResource
{
    Q_OBJECT
    QML_ELEMENT
public:
    explicit UserService(QObject *parent = nullptr);
    Q_INVOKABLE void create(const QVariantMap& data);
};

#endif // USERSERVICE_H
