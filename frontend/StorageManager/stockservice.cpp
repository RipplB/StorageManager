#include "stockservice.h"
#include "qjsonarray.h"
#include "util.h"
#include <QDebug>

StockService::StockService(QObject *parent)
    : AbstractResource{parent}
{

}

void StockService::update()
{
    RestAccessManager::ResponseCallback callback =
        [this](QNetworkReply* reply, bool success) {
            if (!success)
                return;
            std::optional<QJsonArray> json = byteArrayToJsonArray(reply->readAll());
            if(!json)
                return;
            emit stocksChanged(json.value());
        };
    m_manager->get("/stocks", QUrlQuery(), callback);
}

void StockService::graphData(const QString& query)
{
    RestAccessManager::ResponseCallback callback =
        [this](QNetworkReply* reply, bool success) {
            if (!success)
                return;
            std::optional<QJsonArray> json = byteArrayToJsonArray(reply->readAll());
            if(!json)
                return;
            emit graphReady(json.value());
        };
    qDebug() << query;
    m_manager->get("/stocks", QUrlQuery(query), callback);
}

void StockService::receive(const QVariantMap& data)
{
    m_manager->post("/stocks/receive", data, [this](QNetworkReply* reply, bool success) {
        if (success)
            this->update();
    });
}

void StockService::release(const QVariantMap& data)
{
    m_manager->post("/stocks/release", data, [this](QNetworkReply* reply, bool success) {
        if (success)
            this->update();
    });
}

void StockService::move(const QVariantMap& data)
{
    m_manager->post("/stocks/internal", data, [this](QNetworkReply* reply, bool success) {
        if (success)
            this->update();
    });
}
