from http.server import BaseHTTPRequestHandler, HTTPServer
import json

class Handler(BaseHTTPRequestHandler):
    def do_POST(self):
        if self.path == "/update":
            content_length = int(self.headers.get('Content-Length', 0))
            body = self.rfile.read(content_length).decode('utf-8')

            try:
                data = json.loads(body)
                print("body:", data)
            except:
                data = None
                print("body is not valid JSON")

            self.send_response(200)
            self.send_header("Content-Type", "application/json")
            self.end_headers()
            response = {"ok": True}
            self.wfile.write(json.dumps(response).encode())

        else:
            self.send_error(404, "Not found")

def run(server_class=HTTPServer, handler_class=Handler):
    server = server_class(("0.0.0.0", 3000), handler_class)
    print("server running on port 3000...")
    server.serve_forever()

if __name__ == "__main__":
    run()
