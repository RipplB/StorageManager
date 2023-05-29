#include "stockservice.h"

StockService::StockService(QObject *parent)
    : AbstractResource{parent}
{

}

void StockService::update()
{

}

void StockService::receive(const QVariantMap& data)
{
    m_manager->post("/stocks/receive", data, [this](QNetworkReply* reply, bool success) {
        if (success)
            this->update();
    });
}
