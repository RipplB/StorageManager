#ifndef PRODUCTSERVICE_H
#define PRODUCTSERVICE_H

#include "abstractresource.h"
#include "qjsonarray.h"
#include <QQmlEngine>

class ProductService : public AbstractResource
{
    Q_OBJECT
    QML_ELEMENT
public:
    explicit ProductService(QObject *parent = nullptr);
    Q_INVOKABLE void update();
    Q_INVOKABLE void create(const QVariantMap& data);
signals:
    void productsChanged(QJsonArray products);
};

#endif // PRODUCTSERVICE_H
