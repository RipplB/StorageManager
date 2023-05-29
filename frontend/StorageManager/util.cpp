// Copyright (C) 2023 The Qt Company Ltd.
// SPDX-License-Identifier: LicenseRef-Qt-Commercial OR BSD-3-Clause

#include "util.h"
#include <QtCore/qjsondocument.h>
#include <QtCore/qjsonarray.h>

std::optional<QJsonObject> byteArrayToJsonObject(const QByteArray& data)
{
    QJsonParseError parseError;
    const auto json = QJsonDocument::fromJson(data, &parseError);

    if (parseError.error) {
        qDebug() << "Response data not JSON:" << parseError.errorString()
                 << "at" << parseError.offset << data;
    }
    return json.isObject() ? json.object() : std::optional<QJsonObject>(std::nullopt);
}
std::optional<QJsonArray> byteArrayToJsonArray(const QByteArray& data)
{
    QJsonParseError parseError;
    const auto json = QJsonDocument::fromJson(data, &parseError);

    if (parseError.error) {
        qDebug() << "Response data not JSON:" << parseError.errorString()
                 << "at" << parseError.offset << data;
    }
    return json.isArray() ? json.array() : std::optional<QJsonArray>(std::nullopt);
}
