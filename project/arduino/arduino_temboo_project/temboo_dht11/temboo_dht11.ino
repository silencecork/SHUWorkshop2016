#include <Console.h>
#include <Process.h>
#include <DHT.h>
#include <Temboo.h>
#include "TembooAccount.h"

Process date;
DHT dht(8, DHT11);

int numRuns = 1;   // Execution count, so this doesn't run forever
int maxRuns = 10;   // Maximum number of times the Choreo should be executed

void setup() {
  Bridge.begin();
  Console.begin();
  while (!Console) {
    ; // wait for Console port to connect.
  }
  dht.begin();
  delay(4000);
  Console.println("You're connected to the Console!!!!");
}

void loop() {
  float h = dht.readHumidity();
  float t = dht.readTemperature();
  int l = analogRead(A0);

  if (!date.running()) {
    date.begin("date");        //Arduino對Linux下date指令
    date.addParameter("+%Y/%m/%d-%T");//指定顯示的格式為「年/月/日-小時：分：秒」
    date.run();
  }
  String timeString = date.readString();
  timeString.replace('\n', ' ');
  timeString.trim();
  String data = timeString + "," + h + "," + t + "," + l;
  Console.println(data);
  temboo(data);
}

void temboo(String data) {
  if (numRuns <= maxRuns) {
    Serial.println("Running AppendRow - Run #" + String(numRuns++));

    TembooChoreo AppendRowChoreo;

    // Invoke the Temboo client
    AppendRowChoreo.begin();

    // Set Temboo account credentials
    AppendRowChoreo.setAccountName(TEMBOO_ACCOUNT);
    AppendRowChoreo.setAppKeyName(TEMBOO_APP_KEY_NAME);
    AppendRowChoreo.setAppKey(TEMBOO_APP_KEY);

    // Set Choreo inputs
    AppendRowChoreo.addInput("RowData", data);
    AppendRowChoreo.addInput("SpreadsheetTitle", "surrounding detect");
    AppendRowChoreo.addInput("RefreshToken", "1/jMbHz9LYRNRamCNV1PFyJxiNmrpWo94-wMphy0VrThcMEudVrK5jSpoR30zcRFq6");
    AppendRowChoreo.addInput("ClientSecret", "tAIBipRSCgj5QsioLhMPUZhE");
    AppendRowChoreo.addInput("ClientID", "211822648353-ssq3rvtm3tthh8o0ks6ekn96lfv07c4d.apps.googleusercontent.com");

    // Identify the Choreo to run
    AppendRowChoreo.setChoreo("/Library/Google/Spreadsheets/AppendRow");

    // Run the Choreo; when results are available, print them to serial
    AppendRowChoreo.run();

    while (AppendRowChoreo.available()) {
      char c = AppendRowChoreo.read();
      Serial.print(c);
    }
    AppendRowChoreo.close();
  }

  Serial.println("Waiting...");
  delay(30000); // wait 30 seconds between AppendRow calls
}
