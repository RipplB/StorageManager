// Copyright (C) 2023 The Qt Company Ltd.
// SPDX-License-Identifier: LicenseRef-Qt-Commercial OR BSD-3-Clause

#include "restaccessmanager.h"

using namespace Qt::StringLiterals;

static constexpr auto contentTypeJson = "application/json"_L1;
// A custom authorization scheme implemented by the Qt example server
static const auto authorizationHeader = "Authorization"_ba;
static const auto authorizationType = "Bearer"_ba;
static const auto correlationIdHeader = "X-Correlation-ID"_ba;

static bool httpResponseSuccess(QNetworkReply* reply)
{
    const int httpStatusCode = reply->attribute(QNetworkRequest::HttpStatusCodeAttribute).toInt();
    const bool isReplyError = (reply->error() != QNetworkReply::NoError);
    
    // Extract correlation ID from request for logging
    QString correlationId = QString::fromUtf8(reply->request().rawHeader(correlationIdHeader));

    qDebug() << "Request to path" << reply->request().url().path() << "finished [CorrelationID:" << correlationId << "]";
    if (isReplyError)
        qDebug() << "Error" << reply->error() << "[CorrelationID:" << correlationId << "]";
    else
        qDebug() << "HTTP:" <<  httpStatusCode << "[CorrelationID:" << correlationId << "]";

    return (!isReplyError && (httpStatusCode >= 200 && httpStatusCode < 300));
}

RestAccessManager::RestAccessManager(QObject *parent)
    : QNetworkAccessManager{parent}
{
}

void RestAccessManager::setUrl(const QUrl& url)
{
    m_url = url;
}

bool RestAccessManager::sslSupported() const
{
#if QT_CONFIG(ssl)
    return QSslSocket::supportsSsl();
#else
    return false;
#endif
}

QByteArray authHeader(QByteArray token)
{
    std::string header = "Bearer " + token.toStdString();
    return QByteArray(header.c_str());
}

QByteArray generateCorrelationId()
{
    return QUuid::createUuid().toString(QUuid::WithoutBraces).toUtf8();
}

void RestAccessManager::setAuthorizationToken(const QByteArray& token)
{
    m_authorizationToken = token;
}

void RestAccessManager::post(const QString& api, const QVariantMap& value,
                             ResponseCallback callback)
{
    m_url.setPath(api);
    auto request = QNetworkRequest(m_url);
    
    QByteArray correlationId = generateCorrelationId();
    
    request.setHeader(QNetworkRequest::KnownHeaders::ContentTypeHeader, contentTypeJson);
    request.setRawHeader(authorizationHeader, authHeader(m_authorizationToken));
    request.setRawHeader(correlationIdHeader, correlationId);
    
    qDebug() << "POST request to" << api << "[CorrelationID:" << correlationId << "]";
    
    QNetworkReply* reply = QNetworkAccessManager::post(request,
                               QJsonDocument::fromVariant(value).toJson(QJsonDocument::Compact));
    QObject::connect(reply, &QNetworkReply::finished, reply, [reply, callback](){
        callback(reply, httpResponseSuccess(reply));
    });
}

void RestAccessManager::get(const QString& api, const QUrlQuery& parameters,
                            ResponseCallback callback)
{
    m_url.setPath(api);
    m_url.setQuery(parameters);
    auto request = QNetworkRequest(m_url);
    
    QByteArray correlationId = generateCorrelationId();
    
    request.setRawHeader(authorizationHeader, authHeader(m_authorizationToken));
    request.setRawHeader(correlationIdHeader, correlationId);
    
    qDebug() << "GET request to" << api << "[CorrelationID:" << correlationId << "]";
    
    QNetworkReply* reply = QNetworkAccessManager::get(request);
    QObject::connect(reply, &QNetworkReply::finished, reply, [reply, callback](){
        callback(reply, httpResponseSuccess(reply));
    });
}

void RestAccessManager::put(const QString& api, const QVariantMap& value,
                            ResponseCallback callback)
{
    m_url.setPath(api);
    auto request = QNetworkRequest(m_url);
    
    QByteArray correlationId = generateCorrelationId();
    
    request.setHeader(QNetworkRequest::KnownHeaders::ContentTypeHeader, contentTypeJson);
    request.setRawHeader(authorizationHeader, authHeader(m_authorizationToken));
    request.setRawHeader(correlationIdHeader, correlationId);
    
    qDebug() << "PUT request to" << api << "[CorrelationID:" << correlationId << "]";
    
    QNetworkReply* reply = QNetworkAccessManager::put(request,
                             QJsonDocument::fromVariant(value).toJson(QJsonDocument::Compact));
    QObject::connect(reply, &QNetworkReply::finished, reply, [reply, callback](){
        callback(reply, httpResponseSuccess(reply));
    });
}

void RestAccessManager::deleteResource(const QString& api, ResponseCallback callback)
{
    m_url.setPath(api);
    auto request = QNetworkRequest(m_url);
    
    QByteArray correlationId = generateCorrelationId();
    
    request.setRawHeader(authorizationHeader, authHeader(m_authorizationToken));
    request.setRawHeader(correlationIdHeader, correlationId);
    
    qDebug() << "DELETE request to" << api << "[CorrelationID:" << correlationId << "]";
    
    QNetworkReply* reply = QNetworkAccessManager::deleteResource(request);
    QObject::connect(reply, &QNetworkReply::finished, reply, [reply, callback](){
       callback(reply, httpResponseSuccess(reply));
    });
}
