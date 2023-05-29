#include "stockservice.h"

StockService::StockService(QObject *parent)
    : AbstractResource{parent}
{

}

void StockService::receive(const QVariantMap& data)
{
    m_manager->post("/stocks/receive", data, nullptr);
}
