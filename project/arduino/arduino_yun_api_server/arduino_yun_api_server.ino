#include <Bridge.h>
#include <Console.h>
#include <BridgeServer.h>
#include <BridgeClient.h>

BridgeServer server;

void setup() {
  pinMode(13, OUTPUT);
  digitalWrite(13, LOW);
  Bridge.begin();
  Console.begin();
  digitalWrite(13, HIGH);

  server.listenOnLocalhost();
  server.begin();

  delay(4000);
  Console.println("You're connected to the Console!!!!");

}

void loop() {
  BridgeClient client = server.accept();

  if (client) {
    String command = client.readStringUntil('/');
    Console.println("got command " + command);
    if (command == "digital") {
      int onoff = client.parseInt();
      onoff = (onoff > 0) ? 1 : 0;
      digitalWrite(13, onoff);
      client.print("OK");
    } else {
      client.print("FAIL");
    }
    client.stop();
  }
}
