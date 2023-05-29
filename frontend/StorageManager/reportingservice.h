#ifndef REPORTINGSERVICE_H
#define REPORTINGSERVICE_H

#include "abstractresource.h"
#include <QQmlEngine>

class ReportingService : public AbstractResource
{
    Q_OBJECT
    QML_ELEMENT
public:
    explicit ReportingService(QObject *parent = nullptr);
    Q_INVOKABLE void send(const QVariantMap& data);
};

#endif // REPORTINGSERVICE_H
