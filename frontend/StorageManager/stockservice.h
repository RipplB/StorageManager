#ifndef STOCKSERVICE_H
#define STOCKSERVICE_H

#include "abstractresource.h"
#include <QQmlEngine>

class StockService : public AbstractResource
{
    Q_OBJECT
    QML_ELEMENT
public:
    explicit StockService(QObject *parent = nullptr);
    Q_INVOKABLE void receive(const QVariantMap& data);
};

#endif // STOCKSERVICE_H
