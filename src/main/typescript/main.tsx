import * as React from "react";
import { createRoot } from "react-dom/client";
import { useEffect, useState } from "react";
import {
  IncidentSnapshotDto,
  IncidentSnapshotListDto,
} from "../../../target/generated-sources/openapi-typescript";

const root = createRoot(document.getElementById("root")!);

function Application() {
  const [webSocket, setWebSocket] = useState<WebSocket>();
  useEffect(() => {
    setWebSocket(new WebSocket("ws://" + window.location.host + "/ws"));
  }, []);
  useEffect(() => {
    webSocket?.addEventListener("message", (event) => {
      const message = JSON.parse(event.data) as IncidentSnapshotListDto;
      setIncidents(message.list);
    });
  }, [webSocket]);
  const [incidents, setIncidents] = useState<IncidentSnapshotDto[]>([]);

  return (
    <>
      <h1>Incident Response</h1>
      <h2>Incidents</h2>
      {incidents.map(
        ({ incidentId, info: { description, incidentType, location } }) => (
          <div key={incidentId}>
            {description} ({incidentType}) at {location}
          </div>
        ),
      )}
    </>
  );
}

root.render(<Application />);
