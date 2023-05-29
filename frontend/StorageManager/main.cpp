#include <QApplication>
#include <QQmlApplicationEngine>


int main(int argc, char *argv[])
{
    QApplication app(argc, argv);

    QQmlApplicationEngine engine;
    const QUrl url(u"qrc:/StorageManager/Main.qml"_qs);
    engine.load(url);

    return app.exec();
}
