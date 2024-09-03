import * as React from "react";
import { createRoot } from "react-dom/client";
import { useEffect, useState } from "react";

const root = createRoot(document.getElementById("root")!);

function Application() {
  const [webSocket, setWebSocket] = useState<WebSocket>();
  useEffect(() => {
    setWebSocket(new WebSocket("ws://" + window.location.host + "/ws"));
  }, []);
  useEffect(() => {
    webSocket?.addEventListener("message", (event) => {
      setIncidents(JSON.parse(event.data).list);
    });
  }, [webSocket]);
  const [incidents, setIncidents] = useState<any[]>([]);

  return (
    <>
      <h1>Incident Response</h1>
      <h2>Incidents</h2>
      {incidents.map((i) => (
        <div>
          {i.description} ({i.incidentType})
        </div>
      ))}
    </>
  );
}

root.render(<Application />);
