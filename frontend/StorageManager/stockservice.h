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
    Q_INVOKABLE void release(const QVariantMap& data);
    Q_INVOKABLE void move(const QVariantMap& data);
    Q_INVOKABLE void update();
signals:
    void stocksChanged(QJsonArray locations);
};

#endif // STOCKSERVICE_H
