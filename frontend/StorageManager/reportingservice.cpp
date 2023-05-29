#include "reportingservice.h"

ReportingService::ReportingService(QObject *parent)
    : AbstractResource{parent}
{

}
void ReportingService::send(const QVariantMap& data)
{
    m_manager->post("/reporting/report", data, [this](QNetworkReply* reply, bool success) {

    });
}
