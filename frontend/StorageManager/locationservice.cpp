#include "locationservice.h"
#include "qjsonarray.h"
#include "util.h"

LocationService::LocationService(QObject *parent)
    : AbstractResource{parent}
{

}

void LocationService::update()
{
    RestAccessManager::ResponseCallback callback =
        [this](QNetworkReply* reply, bool success) {
            if (!success)
                return;
            std::optional<QJsonArray> json = byteArrayToJsonArray(reply->readAll());
            if(!json)
                return;
            emit locationsChanged(json.value());
        };
    m_manager->get("/location", QUrlQuery(), callback);
}
void LocationService::create(const QVariantMap& data)
{
    m_manager->post("/location", data, [this](QNetworkReply* reply, bool success) {
        if (success)
            this->update();
    });
}
void LocationService::edit(const int& id, const QVariantMap& data)
{
    m_manager->put(("/location/" + std::to_string(id)).c_str(), data, [this](QNetworkReply* reply, bool success) {
        if (success)
            this->update();
    });
}
