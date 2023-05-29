#ifndef LOCATIONSERVICE_H
#define LOCATIONSERVICE_H

#include "abstractresource.h"
#include "qjsonarray.h"
#include <QQmlEngine>

class LocationService : public AbstractResource
{
    Q_OBJECT
    QML_ELEMENT
public:
    explicit LocationService(QObject *parent = nullptr);
    Q_INVOKABLE void update();
signals:
    void locationsChanged(QJsonArray locations);
};

#endif // LOCATIONSERVICE_H
