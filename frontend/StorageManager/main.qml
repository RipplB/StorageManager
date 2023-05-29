import QtQuick
import QtQuick.Window
import QtQuick.Controls
import QtQuick.Layouts
import Qt.labs.qmlmodels
import QtCharts

import StorageManager

ApplicationWindow {
    id: window
    width: 1920
    height: 1080
    visible: true
    title: qsTr("Storage manager client")

    enum Pages {
        LANDING = 0,
        PRODUCT = 1,
        LOCATION = 2,
        RECEIVE = 3,
        RELEASE = 4,
        INTERNAL = 5,
        STOCK = 6,
        REPORT = 7,
        USERS = 8
    }

    Component.onCompleted: loginPopup.open()

    RestService {
        id: restService

        LoginService {
            id: loginService
        }
        ProductService {
            id: productService
        }
        LocationService {
            id: locationService
        }
        StockService {
            id: stockService
        }
        UserService {
            id: userService
        }
        ReportingService {
            id: reportingService
        }
    }
    Connections {
        target: loginService
        function onLoginSuccess() {
            loginPopup.close()
            productService.update()
            locationService.update()
            stockService.update()
            refreshTimer.start()
        }
        function onRolesChanged(list) {
            toolBtnProduct.visible = list.includes("OFFICE")
            toolBtnLocation.visible = list.includes("STORAGE")
            toolBtnReceive.visible = list.includes("STORAGE")
            toolBtnRelease.visible = list.includes("STORAGE")
            toolBtnMove.visible = list.includes("STORAGE")
            toolBtnReport.visible = list.includes("OFFICE")
            toolBtnUsers.visible = list.includes("MANAGER")
        }
        function onRefreshSuccess() {
            refreshTimer.start()
        }
    }
    Connections {
        target: productService
        function onProductsChanged(array) {
            productList.clear();
            productTable.rows = array;
            for (const product of array) {
                productList.append(product)
            }
        }
    }
    Connections {
        target: locationService
        function onLocationsChanged(array) {
            locationList.clear();
            locationTable.rows = array;
            for (const location of array) {
                locationList.append(location)
            }
        }
    }
    Connections {
        target: stockService
        function onStocksChanged(array) {
            stockTable.clear();
            for (const stock of array) {
                stockTable.appendRow({"product" : stock.product.name, "location": stock.location.name, "amount": stock.amount, "id": stock.id})
            }
        }
    }

    Timer {
        id: refreshTimer
        interval: 10000
        repeat: true
        onTriggered: loginService.refresh()
    }

    ListModel {
        id: productList
    }
    TableModel {
        id: productTable
        TableModelColumn {
            display: "name"
        }
        TableModelColumn {
            display: "description"
        }
        TableModelColumn {
            display: "unit"
        }
        TableModelColumn {
            display: "id"
        }
    }

    ListModel {
        id: locationList
    }
    TableModel {
        id: locationTable
        TableModelColumn {
            display: "name"
        }
        TableModelColumn {
            display: "description"
        }
        TableModelColumn {
            display: "id"
        }
    }

    TableModel {
        id: stockTable
        TableModelColumn {
            display: "product"
        }
        TableModelColumn {
            display: "location"
        }
        TableModelColumn {
            display: "amount"
        }
        TableModelColumn {
            display: "id"
        }
    }

    Popup {
        id: loginPopup
        anchors.centerIn: parent
        padding: 10
        modal: true
        focus: true
        closePolicy: Popup.NoAutoClose

        ColumnLayout {
            TextArea {
                Layout.alignment: Qt.AlignHCenter
                id: serverUrlInput
                text: "http://ripplb.uk.to:8081"
            }
            GridLayout {
                columns: 2

                Label {
                    text: "Username:"
                }
                TextArea {
                    id: usernameInput
                    placeholderText: "Enter your username here"
                }
                Label {
                    text: "Password:"
                }
                TextArea {
                    id: passwordInput
                    placeholderText: "Enter your password here"
                }
            }
            Label {
                visible: false
                color: "#FF0000"
            }
            Button {
                Layout.alignment: Qt.AlignHCenter
                id: loginButton
                text: "Login"
                onClicked: loginService.login(serverUrlInput.text, {"username": usernameInput.text, "password": passwordInput.text})
            }
        }

    }

    Popup {
        id: editProductPopup
        anchors.centerIn: parent
        padding: 10
        modal: true
        focus: true
        closePolicy: Popup.CloseOnPressOutside

        ColumnLayout {
            GridLayout {
                columns: 2
                Label {
                    text: "Id:"
                }
                Label {
                    id: productPopupIdInput
                    text: "IDENTIFIER"
                }
                Label {
                    text: "Name:"
                }
                TextArea {
                    id: productPopupNameInput
                    placeholderText: "Enter the new name here"
                }
                Label {
                    text: "Description:"
                }
                TextArea {
                    id: productPopupDescriptionInput
                    placeholderText: "Enter the new description here"
                }
                Label {
                    text: "Unit:"
                }
                TextArea {
                    id: productPopupUnitInput
                    placeholderText: "Enter the new unit here"
                }
            }
            Button {
                Layout.alignment: Qt.AlignHCenter
                id: editProductButton
                text: "Edit"
                onClicked: productService.edit(Number(productPopupIdInput.text), {
                                                   "name": productPopupNameInput.text,
                                                   "description": productPopupDescriptionInput.text,
                                                   "unit": productPopupUnitInput.text
                                               })
            }
        }

    }

    Popup {
        id: editLocationPopup
        anchors.centerIn: parent
        padding: 10
        modal: true
        focus: true
        closePolicy: Popup.CloseOnPressOutside

        ColumnLayout {
            GridLayout {
                columns: 2
                Label {
                    text: "Id:"
                }
                Label {
                    id: locationPopupIdInput
                    text: "IDENTIFIER"
                }
                Label {
                    text: "Name:"
                }
                TextArea {
                    id: locationPopupNameInput
                    placeholderText: "Enter the new name here"
                }
                Label {
                    text: "Description:"
                }
                TextArea {
                    id: locationPopupDescriptionInput
                    placeholderText: "Enter the new description here"
                }
            }
            Button {
                Layout.alignment: Qt.AlignHCenter
                id: editLocationButton
                text: "Edit"
                onClicked: locationService.edit(Number(locationPopupIdInput.text), {
                                                   "name": locationPopupNameInput.text,
                                                   "description": locationPopupDescriptionInput.text
                                               })
            }
        }

    }

    Popup {
        id: chartPopup
        anchors.centerIn: parent
        padding: 10
        modal: true
        focus: true
        closePolicy: Popup.CloseOnPressOutside
        ChartView {
            title: "Hal"
            implicitWidth: 800
            implicitHeight: 600
            anchors.fill: parent
            antialiasing: true
            theme: ChartView.ChartThemeDark
            LineSeries {
                name: "LineSeries"
                XYPoint { x: 0; y: 0 }
                XYPoint { x: 1.1; y: 2.1 }
                XYPoint { x: 1.9; y: 3.3 }
                XYPoint { x: 2.1; y: 2.1 }
                XYPoint { x: 2.9; y: 4.9 }
                XYPoint { x: 3.4; y: 3.0 }
                XYPoint { x: 4.1; y: 3.3 }
            }
        }
    }

    ColumnLayout {
        anchors.fill: parent
        ToolBar {
            Layout.fillWidth: true
            RowLayout {
                anchors.fill: parent
                ToolButton {
                    id: toolBtnProduct
                    text: qsTr("Product")
                    font.bold: dataView.currentIndex === Main.PRODUCT
                    onClicked: dataView.currentIndex = Main.PRODUCT
                }
                ToolButton {
                    id: toolBtnLocation
                    text: qsTr("Location")
                    font.bold: dataView.currentIndex === Main.LOCATION
                    onClicked: dataView.currentIndex = Main.LOCATION
                }
                ToolButton {
                    id: toolBtnReceive
                    text: qsTr("Receive goods")
                    font.bold: dataView.currentIndex === Main.RECEIVE
                    onClicked: dataView.currentIndex = Main.RECEIVE
                }
                ToolButton {
                    id: toolBtnRelease
                    text: qsTr("Release goods")
                    font.bold: dataView.currentIndex === Main.RELEASE
                    onClicked: dataView.currentIndex = Main.RELEASE
                }
                ToolButton {
                    id: toolBtnMove
                    text: qsTr("Move goods")
                    font.bold: dataView.currentIndex === Main.INTERNAL
                    onClicked: dataView.currentIndex = Main.INTERNAL
                }
                ToolButton {
                    id: toolBtnStock
                    text: qsTr("Stock")
                    font.bold: dataView.currentIndex === Main.STOCK
                    onClicked: dataView.currentIndex = Main.STOCK
                }
                ToolButton {
                    id: toolBtnReport
                    text: qsTr("Reporting")
                    font.bold: dataView.currentIndex === Main.REPORT
                    onClicked: dataView.currentIndex = Main.REPORT
                }
                ToolButton {
                    id: toolBtnUsers
                    text: qsTr("Users")
                    font.bold: dataView.currentIndex === Main.USERS
                    onClicked: dataView.currentIndex = Main.USERS
                }
            }
        }
        StackLayout {
            id: dataView
            currentIndex: Main.LANDING
            Layout.fillWidth: true
            Layout.fillHeight: true

            Pane {
                Layout.fillWidth: true
                Layout.fillHeight: true
                Item {
                    Text {
                        text: "Choose an option from the menu above!"
                    }
                }
            }
            Pane {
                Layout.fillWidth: true
                Layout.fillHeight: true
                RowLayout {
                    Label {
                        text: "Name:"
                    }
                    TextInput {
                        Layout.preferredWidth: 140
                        id: productNameInput
                        validator: RegularExpressionValidator {
                            regularExpression: /^.{1,20}$/
                        }
                    }
                    Label {
                        text: "Description:"
                    }
                    TextInput {
                        Layout.preferredWidth: 1320
                        id: productDescriptionInput
                        validator: RegularExpressionValidator {
                            regularExpression: /^.{1,255}$/
                        }
                    }
                    Label {
                        text: "Unit:"
                    }
                    TextInput {
                        Layout.preferredWidth: 70
                        id: productUnitInput
                        validator: RegularExpressionValidator {
                            regularExpression: /^.{1,10}$/
                        }
                    }
                    Button {
                        text: "Save product"
                        onClicked: {
                            productService.create({
                                                      "name": productNameInput.text,
                                                      "description": productDescriptionInput.text,
                                                      "unit": productUnitInput.text
                                                  })
                        }
                    }
                }

                TableView {
                    anchors.fill: parent
                    anchors.centerIn: parent
                    anchors.margins: 40
                    animate: false
                    model: productTable
                    id: productTableView
                    delegate: DelegateChooser {
                        DelegateChoice {
                            column: 3
                            delegate: Rectangle {
                                implicitWidth: 150
                                implicitHeight: 50
                                border.width: 1
                                Button {
                                    anchors.centerIn: parent
                                    text: "Edit"
                                    onClicked: {
                                        productPopupIdInput.text = model.display
                                        editProductPopup.open()
                                    }
                                }
                            }
                        }
                        DelegateChoice {
                            delegate: Rectangle {
                                implicitWidth: 550
                                implicitHeight: 50
                                border.width: 1
                                Text {
                                    text: display
                                    anchors.centerIn: parent
                                }
                            }
                        }
                    }
                }
            }
            Pane {
                Layout.fillWidth: true
                Layout.fillHeight: true
                RowLayout {
                    Label {
                        text: "Name:"
                    }
                    TextInput {
                        Layout.preferredWidth: 140
                        id: locationNameInput
                        validator: RegularExpressionValidator {
                            regularExpression: /^.{1,20}$/
                        }
                    }
                    Label {
                        text: "Description:"
                    }
                    TextInput {
                        Layout.preferredWidth: 1320
                        id: locationDescriptionInput
                        validator: RegularExpressionValidator {
                            regularExpression: /^.{1,255}$/
                        }
                    }
                    Button {
                        text: "Save location"
                        onClicked: {
                            locationService.create({
                                                      "name": locationNameInput.text,
                                                      "description": locationDescriptionInput.text
                                                  })
                        }
                    }
                }

                TableView {
                    anchors.fill: parent
                    anchors.centerIn: parent
                    anchors.margins: 40
                    animate: false
                    model: locationTable
                    id: locationTableView
                    delegate: DelegateChooser {
                        DelegateChoice {
                            column: 2
                            delegate: Rectangle {
                                implicitWidth: 150
                                implicitHeight: 50
                                border.width: 1
                                Button {
                                    anchors.centerIn: parent
                                    text: "Edit"
                                    onClicked: {
                                        locationPopupIdInput.text = model.display
                                        editLocationPopup.open()
                                    }
                                }
                            }
                        }
                        DelegateChoice {
                            delegate: Rectangle {
                                implicitWidth: 550
                                implicitHeight: 50
                                border.width: 1
                                Text {
                                    text: display
                                    anchors.centerIn: parent
                                }
                            }
                        }
                    }
                }
            }
            Pane {
                Layout.fillWidth: true
                Layout.fillHeight: true
                ColumnLayout {
                    anchors.centerIn: parent
                    GridLayout {
                        columns: 2

                        Label {
                            text: "Product"
                        }
                        ComboBox {
                            id: receiveProductSelection
                            model: productList
                            textRole: "name"
                            valueRole: "id"
                        }
                        Label {
                            text: "Location"
                        }
                        ComboBox {
                            id: receiveLocationSelection
                            model: locationList
                            textRole: "name"
                            valueRole: "id"
                        }
                        Label {
                            text: "Amount"
                        }
                        TextInput {
                            Layout.fillWidth: true
                            id: receiveAmountInput
                            validator: IntValidator {
                                bottom: 1
                            }
                        }
                    }
                    Button {
                        text: "Receive"
                        onClicked: {
                            stockService.receive({
                                                     "product": receiveProductSelection.currentValue,
                                                     "amount": Number(receiveAmountInput.text),
                                                     "targetLocation": receiveLocationSelection.currentValue
                                                 })
                        }
                    }
                }
            }
            Pane {
                Layout.fillWidth: true
                Layout.fillHeight: true
                ColumnLayout {
                    anchors.centerIn: parent
                    GridLayout {
                        columns: 2

                        Label {
                            text: "Product"
                        }
                        ComboBox {
                            id: releaseProductSelection
                            model: productList
                            textRole: "name"
                            valueRole: "id"
                        }
                        Label {
                            text: "Location"
                        }
                        ComboBox {
                            id: releaseLocationSelection
                            model: locationList
                            textRole: "name"
                            valueRole: "id"
                        }
                        Label {
                            text: "Amount"
                        }
                        TextInput {
                            Layout.fillWidth: true
                            id: releaseAmountInput
                            validator: IntValidator {
                                bottom: 1
                            }
                        }
                    }
                    Button {
                        text: "Release"
                        onClicked: {
                            stockService.release({
                                                     "product": releaseProductSelection.currentValue,
                                                     "amount": Number(releaseAmountInput.text),
                                                     "sourceLocation": releaseLocationSelection.currentValue
                                                 })
                        }
                    }
                }
            }
            Pane {
                Layout.fillWidth: true
                Layout.fillHeight: true
                ColumnLayout {
                    anchors.centerIn: parent
                    GridLayout {
                        columns: 2

                        Label {
                            text: "Product"
                        }
                        ComboBox {
                            id: moveProductSelection
                            model: productList
                            textRole: "name"
                            valueRole: "id"
                        }
                        Label {
                            text: "Source location"
                        }
                        ComboBox {
                            id: moveSourceLocationSelection
                            model: locationList
                            textRole: "name"
                            valueRole: "id"
                        }
                        Label {
                            text: "Target location"
                        }
                        ComboBox {
                            id: moveTargetLocationSelection
                            model: locationList
                            textRole: "name"
                            valueRole: "id"
                        }
                        Label {
                            text: "Amount"
                        }
                        TextInput {
                            Layout.fillWidth: true
                            id: moveAmountInput
                            validator: IntValidator {
                                bottom: 1
                            }
                        }
                    }
                    Button {
                        text: "Move"
                        onClicked: {
                            stockService.move({
                                                     "product": moveProductSelection.currentValue,
                                                     "amount": Number(moveAmountInput.text),
                                                     "sourceLocation": moveSourceLocationSelection.currentValue,
                                                     "targetLocation": moveTargetLocationSelection.currentValue
                                                 })
                        }
                    }
                }
            }
            Pane {
                Layout.fillWidth: true
                Layout.fillHeight: true

                TableView {
                    anchors.fill: parent
                    anchors.centerIn: parent
                    anchors.margins: 40
                    animate: false
                    model: stockTable
                    id: stockTableView
                    delegate: DelegateChooser {
                        DelegateChoice {
                            column: 3
                            delegate: Rectangle {
                                implicitWidth: 150
                                implicitHeight: 50
                                border.width: 1
                                Button {
                                    anchors.centerIn: parent
                                    text: "Show graph"
                                    onClicked: {
                                        chartPopup.open()
                                    }
                                }
                            }
                        }
                        DelegateChoice {
                            delegate: Rectangle {
                                implicitWidth: 550
                                implicitHeight: 50
                                border.width: 1
                                Text {
                                    text: display
                                    anchors.centerIn: parent
                                }
                            }
                        }
                    }
                }
            }
            Pane {
                Layout.fillWidth: true
                Layout.fillHeight: true
                ColumnLayout {
                    anchors.centerIn: parent
                    GridLayout {
                        columns: 2

                        Label {
                            text: "Type"
                        }
                        ComboBox {
                            id: reportTypeSelection
                            model: ["Daily Report", "Report By Name", "Report By Location"]
                        }
                        Label {
                            text: "Product parameter"
                        }
                        ComboBox {
                            id: reportProductSelection
                            model: productList
                            textRole: "name"
                            valueRole: "id"
                        }
                        Label {
                            text: "Location parameter"
                        }
                        ComboBox {
                            id:reportLocationSelection
                            model: locationList
                            textRole: "name"
                            valueRole: "id"
                        }
                        Label {
                            text: "Recipients"
                        }
                        TextArea {
                            id: reportRecipientInput
                            placeholderText: "Enter the email addresses\nline by line"
                        }
                    }
                    Button {
                        text: "Report"
                        onClicked: {
                            let rec = Array();
                            reportRecipientInput.text.split("\n").forEach(line => rec.push(line.trim()));
                            if (reportTypeSelection.currentText === "Daily Report")
                                reportingService.send({"type": "Daily Report", "receivers": rec})
                            if (reportTypeSelection.currentText === "Report By Name")
                                reportingService.send({"type": "Report By Name", "param": reportProductSelection.currentText, "receivers": rec})
                            if (reportTypeSelection.currentText === "Report By Location")
                                reportingService.send({"type": "Report By Location", "param": reportLocationSelection.currentText, "receivers": rec})
                        }
                    }
                }
            }
            Pane {
                Layout.fillWidth: true
                Layout.fillHeight: true
                ColumnLayout {
                    anchors.centerIn: parent
                    GridLayout {
                        columns: 2

                        Label {
                            text: "Login name"
                        }
                        TextInput {
                            Layout.preferredWidth: 200
                            id: newUserNameInput
                            validator: RegularExpressionValidator {
                                regularExpression: /^.{1,20}$/
                            }
                        }
                        Label {
                            text: "Password"
                        }
                        TextInput {
                            Layout.preferredWidth: 200
                            id: newUserPasswordInput
                            validator: RegularExpressionValidator {
                                regularExpression: /^.{6,64}$/
                            }
                        }
                        Label {
                            text: "Full name"
                        }
                        TextInput {
                            Layout.preferredWidth: 200
                            id: newUserFullNameInput
                            validator: RegularExpressionValidator {
                                regularExpression: /^.{1,255}$/
                            }
                        }
                        Label {
                            text: "Email address"
                        }
                        TextInput {
                            Layout.preferredWidth: 200
                            id: newUserEmailInput
                        }
                    }
                    RowLayout {
                        CheckBox {
                            id: managerRole
                            text: "MANAGER"
                        }
                        CheckBox {
                            id: storageRole
                            text: "STORAGE"
                        }
                        CheckBox {
                            id: officeRole
                            text: "OFFICE"
                        }
                    }

                    Button {
                        text: "Create user"
                        onClicked: {
                            if (!/^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/.test(newUserEmailInput.text))
                                return
                            let roles = Array()
                            if (managerRole.checked)
                                roles.push(managerRole.text)
                            if (storageRole.checked)
                                roles.push(storageRole.text)
                            if (officeRole.checked)
                                roles.push(officeRole.text)
                            userService.create({
                                                   "loginName": newUserNameInput.text,
                                                   "password": newUserPasswordInput.text,
                                                   "fullName": newUserFullNameInput.text,
                                                   "roleNames": roles,
                                                   "email": newUserEmailInput.text
                                               })
                        }
                    }
                }
            }
        }
    }

}
