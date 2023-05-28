import QtQuick
import QtQuick.Window
import QtQuick.Controls
import QtQuick.Layouts

import StorageManager

Window {
    width: 1920
    height: 1080
    visible: true
    title: qsTr("Storage manager client")

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

}
