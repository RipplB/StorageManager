import QtQuick
import QtQuick.Window
import QtQuick.Controls
import QtQuick.Layouts

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
    }
    Connections {
        target: loginService
        function onLoginSuccess() {
            loginPopup.close();
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

    ColumnLayout {
        anchors.fill: parent
        ToolBar {
            Layout.fillWidth: true
            RowLayout {
                anchors.fill: parent
                ToolButton {
                    text: qsTr("Product")
                    font.bold: dataView.currentIndex === Main.PRODUCT
                    onClicked: dataView.currentIndex = Main.PRODUCT
                }
                ToolButton {
                    text: qsTr("Location")
                    font.bold: dataView.currentIndex === Main.LOCATION
                    onClicked: dataView.currentIndex = Main.LOCATION
                }
                ToolButton {
                    text: qsTr("Receive goods")
                    font.bold: dataView.currentIndex === Main.RECEIVE
                    onClicked: dataView.currentIndex = Main.RECEIVE
                }
                ToolButton {
                    text: qsTr("Release goods")
                    font.bold: dataView.currentIndex === Main.RELEASE
                    onClicked: dataView.currentIndex = Main.RELEASE
                }
                ToolButton {
                    text: qsTr("Move goods")
                    font.bold: dataView.currentIndex === Main.INTERNAL
                    onClicked: dataView.currentIndex = Main.INTERNAL
                }
                ToolButton {
                    text: qsTr("Stock")
                    font.bold: dataView.currentIndex === Main.STOCK
                    onClicked: dataView.currentIndex = Main.STOCK
                }
                ToolButton {
                    text: qsTr("Reporting")
                    font.bold: dataView.currentIndex === Main.REPORT
                    onClicked: dataView.currentIndex = Main.REPORT
                }
                ToolButton {
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
                Item {
                    Text {
                        text: "Product"
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
                Item {
                    Text {
                        text: "Receive"
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
