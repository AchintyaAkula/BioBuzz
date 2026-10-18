import http.server
import socketserver
import os
import json
import math
from urllib.parse import urlparse, parse_qs

PORT = 8080
LOG_DIR = os.path.join("FIRST", "turtle_logger")
os.makedirs(LOG_DIR, exist_ok=True)

# Create a 2.5-minute match log file (7,500 ticks at 20ms intervals starting at t=0)
sample_log = os.path.join(LOG_DIR, "session_match_2.5min.json")
if not os.path.exists(sample_log):
    mock_data = {}
    total_duration_ms = int(2.5 * 60 * 1000)  # 150,000 ms
    interval_ms = 20
    for i in range(int(total_duration_ms / interval_ms)):
        ts = i * interval_ms
        mock_data[str(ts)] = {
            "robot_x": round(10.0 + (i * 0.05) % 120.0, 2),
            "robot_y": round(20.0 + (i * 0.03) % 140.0, 2),
            "robot_heading": round((i * 0.01) % 6.28, 3),
            "left_front_motor": round(math.sin(i * 0.05) * 100, 1),
            "right_front_motor": round(math.cos(i * 0.05) * 100, 1),
            "left_rear_motor": round(math.sin(i * 0.05) * 100, 1),
            "right_rear_motor": round(math.cos(i * 0.05) * 100, 1),
            "arm_position": int(500 + (i * 2) % 3000),
            "arm_target": 3000,
            "intake_state": "ACTIVE" if (i // 50) % 2 == 0 else "REVERSE",
            "claw_open": (i // 25) % 2 == 0,
            "limelight_target_found": True,
            "limelight_tx": round(math.sin(i * 0.03) * 15.0, 2),
            "limelight_ty": round(math.cos(i * 0.03) * 10.0, 2),
            "battery_voltage": round(13.2 - (i * 0.00001), 2),
            "gamepad1_left_stick_x": round(math.sin(i * 0.02), 2),
            "gamepad1_left_stick_y": round(math.cos(i * 0.02), 2),
            "system_cpu_load": f"{round(15.0 + (i % 5), 1)}%"
        }
    with open(sample_log, "w") as f:
        json.dump(mock_data, f)

class LoggerMockHandler(http.server.SimpleHTTPRequestHandler):
    def do_GET(self):
        if self.path.startswith("/logger/api/list"):
            self.send_response(200)
            self.send_header("Content-Type", "application/json")
            self.end_headers()
            files = [f for f in os.listdir(LOG_DIR) if f.endswith(".json")]
            self.wfile.write(json.dumps(files).encode())
        elif self.path.startswith("/logger/api/get"):
            query = parse_qs(urlparse(self.path).query)
            filename = query.get("file", [""])[0]
            filepath = os.path.join(LOG_DIR, filename)
            if os.path.exists(filepath):
                self.send_response(200)
                self.send_header("Content-Type", "application/json")
                self.end_headers()
                with open(filepath, "r") as f:
                    self.wfile.write(f.read().encode())
            else:
                self.send_response(404)
                self.end_headers()
        elif self.path.startswith("/graph"):
            self.path = "/src/main/assets/turtle_logger/graph.html"
            super().do_GET()
        elif self.path.startswith("/logger") or self.path == "/":
            self.path = "/src/main/assets/turtle_logger/index.html"
            super().do_GET()
        else:
            super().do_GET()

    def do_POST(self):
        if self.path.startswith("/logger/api/delete"):
            query = parse_qs(urlparse(self.path).query)
            filename = query.get("file", [""])[0]
            filepath = os.path.join(LOG_DIR, filename)
            success = False
            if os.path.exists(filepath):
                os.remove(filepath)
                success = True
            self.send_response(200)
            self.send_header("Content-Type", "application/json")
            self.end_headers()
            self.wfile.write(json.dumps({"success": success}).encode())

print(f"Starting mock FTC logger server:")
print(f"  - Table Viewer: http://localhost:{PORT}/logger")
print(f"  - Graph Viewer: http://localhost:{PORT}/graph")
print("Press Ctrl+C to stop.")
with socketserver.TCPServer(("", PORT), LoggerMockHandler) as httpd:
    httpd.serve_forever()
