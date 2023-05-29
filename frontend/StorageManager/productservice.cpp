#include "productservice.h"
#include "util.h"

ProductService::ProductService(QObject *parent)
    : AbstractResource{parent}
{

}

void ProductService::update()
{
    RestAccessManager::ResponseCallback callback =
        [this](QNetworkReply* reply, bool success) {
            if (!success)
                return;
            std::optional<QJsonArray> json = byteArrayToJsonArray(reply->readAll());
            if(!json)
                return;
            emit productsChanged(json.value());
        };
    m_manager->get("/product", QUrlQuery(), callback);
}
void ProductService::create(const QVariantMap& data)
{
    m_manager->post("/product", data, [this](QNetworkReply* reply, bool success) {
        if (success)
            this->update();
    });
}
void ProductService::edit(const int& id, const QVariantMap& data)
{
    m_manager->put(("/product/" + std::to_string(id)).c_str(), data, [this](QNetworkReply* reply, bool success) {
        if (success)
            this->update();
    });
}
