import QtQuick
import QtQuick.Window
import QtQuick.Controls
import QtQuick.Layouts
import Qt.labs.qmlmodels

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
    }
    Connections {
        target: loginService
        function onLoginSuccess() {
            loginPopup.close()
            productService.update()
            locationService.update()
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
            for (const location of array) {
                locationList.append({"name": location.name, "id": location.id})
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
    }

    ListModel {
        id: locationList
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
                    delegate: Rectangle {
                        implicitWidth: 600
                        implicitHeight: 50
                        border.width: 1
                        Text {
                            text: display
                            anchors.centerIn: parent
                        }
                    }
                }
            }
            Pane {
                Layout.fillWidth: true
                Layout.fillHeight: true
                Item {
                    Text {
                        text: "Location"
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
                Item {
                    Text {
                        text: "Release"
                    }
                }
            }
            Pane {
                Layout.fillWidth: true
                Layout.fillHeight: true
                Item {
                    Text {
                        text: "Internal"
                    }
                }
            }
            Pane {
                Layout.fillWidth: true
                Layout.fillHeight: true
                Item {
                    Text {
                        text: "Stock"
                    }
                }
            }
            Pane {
                Layout.fillWidth: true
                Layout.fillHeight: true
                Item {
                    Text {
                        text: "Report"
                    }
                }
            }
            Pane {
                Layout.fillWidth: true
                Layout.fillHeight: true
                Item {
                    Text {
                        text: "Users"
                    }
                }
            }
        }
    }

}
