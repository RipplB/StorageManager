#include "userservice.h"

UserService::UserService(QObject *parent)
    : AbstractResource{parent}
{

}
void UserService::create(const QVariantMap& data)
{
    m_manager->post("/user", data, [this](QNetworkReply* reply, bool success) {

    });
}
